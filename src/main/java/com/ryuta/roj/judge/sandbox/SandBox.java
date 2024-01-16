package com.ryuta.roj.judge.sandbox;

import com.ryuta.roj.judge.sandbox.model.JudgeRequest;
import com.ryuta.roj.judge.sandbox.model.JudgeResponse;

public interface SandBox {
    JudgeResponse exec(JudgeRequest judgeRequest);

}
