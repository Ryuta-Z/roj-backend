package com.ryuta.roj.model.vo;

import cn.hutool.json.JSONUtil;
import com.ryuta.roj.model.dto.question.JudeCase;
import com.ryuta.roj.model.dto.question.JudgeConfig;
import com.ryuta.roj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionVO implements Serializable {

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
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 用例
     */
    private List<JudeCase> judgeCases;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 通过数
     */
    private Integer acceptedNum;


    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;


    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return Question
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) return null;
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        List<String> tags = questionVO.getTagList();
        question.setTags(JSONUtil.toJsonStr(tags));
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return QuestionVO
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) return null;
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        questionVO.setTagList(JSONUtil.toList(question.getTags(), String.class));
        questionVO.setJudgeCases(JSONUtil.toList(question.getJudgeCase(), JudeCase.class));
        questionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        return questionVO;
    }
}
