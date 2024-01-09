package com.ryuta.roj.service;

import com.ryuta.roj.model.dto.question_submit.QuestionSubmitAddRequest;
import com.ryuta.roj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuta.roj.model.entity.User;

/**
* @author ryuta
* @description 针对表【question_submit(用户提交)】的数据库操作Service
* @createDate 2024-01-05 17:43:12
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    Long doSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
    void validSubmit(QuestionSubmit questionSubmit);
}
