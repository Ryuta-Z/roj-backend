package com.ryuta.roj.judge;

import com.ryuta.roj.judge.sandbox.model.JudgeRequest;
import com.ryuta.roj.judge.sandbox.model.JudgeResponse;
import com.ryuta.roj.model.entity.Question;
import com.ryuta.roj.model.entity.QuestionSubmit;

public interface JudgeService {
 void doJudge(Question question, QuestionSubmit questionSubmit);
}
