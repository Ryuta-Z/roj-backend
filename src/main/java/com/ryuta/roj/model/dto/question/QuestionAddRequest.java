package com.ryuta.roj.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class QuestionAddRequest implements Serializable {

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
     * 用例
     */
    private List<JudeCase> judgeCases;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;


    private static final long serialVersionUID = 1L;
}