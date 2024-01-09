package com.ryuta.roj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuta.roj.common.ErrorCode;
import com.ryuta.roj.exception.BusinessException;
import com.ryuta.roj.exception.ThrowUtils;
import com.ryuta.roj.model.dto.question_submit.QuestionSubmitAddRequest;
import com.ryuta.roj.model.entity.Post;
import com.ryuta.roj.model.entity.Question;
import com.ryuta.roj.model.entity.QuestionSubmit;
import com.ryuta.roj.model.entity.User;
import com.ryuta.roj.model.enums.JudgeStatusEnum;
import com.ryuta.roj.model.enums.LanguageEnum;
import com.ryuta.roj.service.QuestionService;
import com.ryuta.roj.service.QuestionSubmitService;
import com.ryuta.roj.mapper.QuestionSubmitMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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
        Question question = questionService.getById(loginUser.getId());
        if(ObjectUtils.isEmpty(question)){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //保存提交
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitAddRequest,questionSubmit);
        // todo
        String judgeInfo = questionSubmit.getJudgeInfo();
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setStatus(JudgeStatusEnum.WAITING.getValue());
        this.validSubmit(questionSubmit);
        this.save(questionSubmit);
        // todo 调用代码沙箱判题


        return questionSubmit.getId();
    }
}




