package com.ryuta.roj.controller;

import com.ryuta.roj.common.BaseResponse;
import com.ryuta.roj.common.ErrorCode;
import com.ryuta.roj.common.ResultUtils;
import com.ryuta.roj.exception.BusinessException;
import com.ryuta.roj.model.dto.question_submit.QuestionSubmitAddRequest;
import com.ryuta.roj.model.entity.User;
import com.ryuta.roj.service.QuestionSubmitService;
import com.ryuta.roj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    @PostMapping("/")
    public BaseResponse<Long> doSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        Long result = questionSubmitService.doSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

}
