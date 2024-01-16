package com.ryuta.roj.judge.sandbox;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ryuta.roj.common.ErrorCode;
import com.ryuta.roj.exception.BusinessException;
import com.ryuta.roj.judge.sandbox.model.JudgeRequest;
import com.ryuta.roj.judge.sandbox.model.JudgeResponse;
import com.ryuta.roj.model.dto.question.JudeCase;
import com.ryuta.roj.model.entity.Question;
import com.ryuta.roj.model.entity.QuestionSubmit;
import com.ryuta.roj.service.QuestionService;
import com.ryuta.roj.service.QuestionSubmitService;
import io.github.classgraph.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.http.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class RemoteSandBox implements SandBox{

    private final String AUTH_REQUEST_HEADER = "auth";
    private final String AUTH_REQUEST_SECRET = "secretKey";


    @Override
    public JudgeResponse exec(JudgeRequest judgeRequest) {
        String jsonStr = JSONUtil.toJsonStr(judgeRequest);
        String url = "http://8.130.39.39:8090/sandbox/judge";
        HttpResponse response = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(jsonStr)
                .execute();
        if(StringUtils.isBlank(response.body())){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"代码沙箱请求失败");
        }
        if(response.getStatus() != 200){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"代码沙箱故障:"+response.body());
        }
        return JSONUtil.toBean(response.body(),JudgeResponse.class);
    }
}
