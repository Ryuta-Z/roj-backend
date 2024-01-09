package com.ryuta.roj.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum LanguageEnum {

    JAVA("Java","java"),
    CPP("C++","cpp"),
    C("C","c"),
    Python("Python","python")
    ;
    private final String text;
    private final String value;

    public static LanguageEnum getEnumByValue(String value){
        if(StringUtils.isBlank(value)){
            return null;
        }
        for(LanguageEnum eNum:LanguageEnum.values()){
            if(eNum.getValue().equals(value)){
                return eNum;
            }
        }
        return null;
    }
}
