package com.ryuta.roj.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
@Getter
public enum JudgeResultEnum {
    ACCEPTED("Accepted","accepted"),
    WRONG_ANSWER("Wrong Answer","wrong_answer"),
    COMPILE_ERROR("Compile Error","compile_error"),
    MEMORY_LIMIT_EXCEED("Memory Limit Exceed","memory_limit_exceed"),
    TIME_LIMIT_EXCEED("Time Limit Exceed","time_limit_exceed"),
    RUNTIME_ERROR("Runtime Error","runtime_error"),
    SYSTEM_ERROR("System Error","system_error")
    ;
    private final String text;
    private final String value;

    JudgeResultEnum(String text, String value) {
        this.text = text;
        this.value = value;

    }
    public static JudgeResultEnum getEnumByValue(String value){
        if(StringUtils.isBlank(value)) return null;
        for(JudgeResultEnum eNum:JudgeResultEnum.values()){
            if(eNum.value.equals(value))return eNum;
        }
        return null;
    }


}
