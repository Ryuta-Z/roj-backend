package com.ryuta.roj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuta.roj.common.ErrorCode;
import com.ryuta.roj.constant.CommonConstant;
import com.ryuta.roj.exception.BusinessException;
import com.ryuta.roj.exception.ThrowUtils;
import com.ryuta.roj.judge.JudgeService;
import com.ryuta.roj.model.dto.question_submit.QuestionSubmitAddRequest;
import com.ryuta.roj.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.ryuta.roj.model.entity.Question;
import com.ryuta.roj.model.entity.QuestionSubmit;
import com.ryuta.roj.model.entity.User;
import com.ryuta.roj.model.enums.JudgeStatusEnum;
import com.ryuta.roj.model.enums.LanguageEnum;
import com.ryuta.roj.model.vo.QuestionSubmitVO;
import com.ryuta.roj.service.QuestionService;
import com.ryuta.roj.service.QuestionSubmitService;
import com.ryuta.roj.mapper.QuestionSubmitMapper;
import com.ryuta.roj.service.UserService;
import com.ryuta.roj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author ryuta
* @description 针对表【question_submit(用户提交)】的数据库操作Service实现
* @createDate 2024-01-05 17:43:12
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    QuestionService questionService;

    @Resource
    JudgeService judgeService;

    @Resource
    UserService userService;
    @Override
    public void validSubmit(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(language,code), ErrorCode.PARAMS_ERROR);
        // 有参数则校验
        if (StringUtils.isNotBlank(language)&&LanguageEnum.getEnumByValue(questionSubmit.getLanguage())==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "语言选择错误");
        }
        if (StringUtils.isNotBlank(code) && code.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "代码过长");
        }
    }
    @Override
    public Long doSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        Question question = questionService.getById(questionSubmitAddRequest.getQuestionId());
        if(ObjectUtils.isEmpty(question)){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR,"不存在的题目id");
        }
        //保存提交
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitAddRequest,questionSubmit);
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setStatus(JudgeStatusEnum.WAITING.getValue());
        this.validSubmit(questionSubmit);
        this.save(questionSubmit);
        //异步调用判题
        CompletableFuture.runAsync(()->{
            judgeService.doJudge(question,questionSubmit);
        });
        return questionSubmit.getId();
    }
    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        if(StringUtils.isBlank(sortField)){
            sortField = "createTime";
            sortOrder = CommonConstant.SORT_ORDER_DESC;
        }
        // 拼接查询条件
        queryWrapper.or().like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.or().eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.or().eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(status!=null&&JudgeStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




