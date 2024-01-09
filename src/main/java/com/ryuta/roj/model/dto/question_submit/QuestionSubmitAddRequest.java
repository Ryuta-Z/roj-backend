package com.ryuta.roj.model.dto.question_submit;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSubmitAddRequest implements Serializable {
    /**
     * 语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

    /**
     * 题目Id
     */
    private Long questionId;


    private static final long serialVersionUID = 1L;
}
