package com.ryuta.roj.judge.sandbox.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class JudgeResponse implements Serializable {

    private List<String> outputList;
    private String message;
    private Integer status;
    private JudgeInfo judgeInfo;
}
