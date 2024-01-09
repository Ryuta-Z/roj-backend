package com.ryuta.roj.model.enums;

import lombok.Getter;

@Getter
public enum JudgeStatusEnum {
    WAITING("等待中", 0),
    RUNNING("执行中",1),
    SUCCESS("成功",2),
    FILED("失败",3)
    ;
    private final String text;
    private final int value;

    JudgeStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static JudgeStatusEnum getEnumByValue(int value){
        if(value < 0 || value > 3) return null;
        for(JudgeStatusEnum eNum:JudgeStatusEnum.values()){
            if(eNum.value == value) return eNum;
        }
        return null;
    }
}
