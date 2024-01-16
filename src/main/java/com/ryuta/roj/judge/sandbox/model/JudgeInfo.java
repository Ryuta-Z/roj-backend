package com.ryuta.roj.judge.sandbox.model;

import lombok.Data;

@Data
public class JudgeInfo {
    /**
     * 执行信息
     */
    private String message;
    /**
     * 耗时
     */
    private Long time;
    /**
     * 消耗内存
     */
    private Long memory;

}
