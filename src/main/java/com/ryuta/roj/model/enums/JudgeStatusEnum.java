package com.ryuta.roj.model.enums;

public enum JudgeStatusEnum {
    WAITING("等待中", 0)
    ;
    private String text;
    private int status;

    JudgeStatusEnum(String text, int status) {
        this.text = text;
        this.status = status;
    }

}
