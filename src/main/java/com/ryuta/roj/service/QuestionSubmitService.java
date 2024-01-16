package com.ryuta.roj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuta.roj.model.dto.question_submit.QuestionSubmitAddRequest;
import com.ryuta.roj.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.ryuta.roj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuta.roj.model.entity.User;
import com.ryuta.roj.model.vo.QuestionSubmitVO;

/**
* @author ryuta
* @description 针对表【question_submit(用户提交)】的数据库操作Service
* @createDate 2024-01-05 17:43:12
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    Long doSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
    void validSubmit(QuestionSubmit questionSubmit);
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
