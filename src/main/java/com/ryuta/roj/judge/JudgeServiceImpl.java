package com.ryuta.roj.judge;

import cn.hutool.json.JSONUtil;
import com.ryuta.roj.common.ErrorCode;
import com.ryuta.roj.exception.BusinessException;
import com.ryuta.roj.judge.sandbox.SandBox;
import com.ryuta.roj.judge.sandbox.model.JudgeInfo;
import com.ryuta.roj.judge.sandbox.model.JudgeRequest;
import com.ryuta.roj.judge.sandbox.model.JudgeResponse;
import com.ryuta.roj.model.dto.question.JudeCase;
import com.ryuta.roj.model.dto.question.JudgeConfig;
import com.ryuta.roj.model.entity.Question;
import com.ryuta.roj.model.entity.QuestionSubmit;
import com.ryuta.roj.model.enums.JudgeResultEnum;
import com.ryuta.roj.model.enums.JudgeStatusEnum;
import com.ryuta.roj.service.QuestionService;
import com.ryuta.roj.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Value("${SandBox.type:example}")
    private String sandBoxType;

    @Resource
    private QuestionService questionService;
    @Resource
    @Lazy
    private QuestionSubmitService questionSubmitService;
    public void doJudge(Question question, QuestionSubmit questionSubmit){

        SandBox sandBox = SandBoxFactory.getSandBox(sandBoxType);
        //请求判题
        JudgeRequest judgeRequest = new JudgeRequest();
        List<JudeCase> judeCases = JSONUtil.toList(question.getJudgeCase(), JudeCase.class);
        List<String> inputs = judeCases.stream().map(JudeCase::getInput).collect(Collectors.toList());
        List<String> outputs = judeCases.stream().map(JudeCase::getOutput).collect(Collectors.toList());
        judgeRequest.setInputList(inputs);
        judgeRequest.setCode(questionSubmit.getCode());
        judgeRequest.setLanguage(questionSubmit.getLanguage());

        // 调用判题服务
        JudgeResponse judgeResponse =  sandBox.exec(judgeRequest);
        System.out.println("判题结果:"+judgeResponse.toString());
        questionSubmit.setStatus(JudgeStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = judgeResponse.getJudgeInfo();
        List<String> outputList = judgeResponse.getOutputList();
        question.setSubmitNum(question.getSubmitNum()+1);
        boolean flag = true;
        if(judgeResponse.getStatus().equals(JudgeStatusEnum.SUCCESS.getValue())){
            if(outputList.size()!=outputs.size()){
                judgeInfo.setMessage(JudgeResultEnum.WRONG_ANSWER.getText());
                flag = false;
            }else{
                for(int i = 0; i < outputList.size(); i++){
                    if(!outputList.get(i).equals(outputs.get(i))){
                        judgeInfo.setMessage(JudgeResultEnum.WRONG_ANSWER.getText());
                        flag = false;
                        break;
                    }
                }
            }
            if(flag){
                JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(),JudgeConfig.class);
                if(judgeConfig.getTimeLimit() < judgeInfo.getTime()){
                    judgeInfo.setMessage(JudgeResultEnum.TIME_LIMIT_EXCEED.getText());
                    flag = false;
                }
            }
        }else if(judgeResponse.getStatus().equals(JudgeStatusEnum.FILED.getValue())){
            flag = false;
            judgeInfo = new JudgeInfo();
            judgeInfo.setMessage(JudgeResultEnum.COMPILE_ERROR.getText());
        }else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"代码沙箱故障");
        }

        if(flag){
            judgeInfo.setMessage(JudgeResultEnum.ACCEPTED.getText());
            question.setAcceptedNum(question.getAcceptedNum()+1);
        }
        questionService.updateById(question);
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        if(!questionSubmitService.updateById(questionSubmit)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"判题结果保存失败");
        }
    }
}
