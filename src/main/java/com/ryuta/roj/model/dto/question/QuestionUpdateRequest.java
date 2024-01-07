package com.ryuta.roj.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/liryuta">程序员鱼皮</a>
 * @from <a href="https://ryuta.icu">编程导航知识星球</a>
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * Id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 参考答案
     */
    private String anwser;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 通过数
     */
    private Integer acceptedNum;

    /**
     * 用例
     */
    private List<JudeCase> judgeCases;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;



    private static final long serialVersionUID = 1L;
}