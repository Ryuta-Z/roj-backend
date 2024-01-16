package com.ryuta.roj.judge.sandbox.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JudgeRequest implements Serializable {
    private List<String> inputList;
    private String language;
    private String code;
}
