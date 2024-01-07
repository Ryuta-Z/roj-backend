package com.ryuta.roj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuta.roj.model.dto.question.QuestionQueryRequest;
import com.ryuta.roj.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuta.roj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author ryuta
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-01-05 17:42:07
*/
public interface QuestionService extends IService<Question> {
    void validQuestion(Question question, boolean add);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    Wrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
