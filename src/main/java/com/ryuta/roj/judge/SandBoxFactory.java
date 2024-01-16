package com.ryuta.roj.judge;

import com.ryuta.roj.judge.sandbox.ExampleSandBox;
import com.ryuta.roj.judge.sandbox.RemoteSandBox;
import com.ryuta.roj.judge.sandbox.SandBox;

public class SandBoxFactory {
    public  static SandBox getSandBox(String type) {
        switch (type){
            case "example": return new ExampleSandBox();
            case "remote": return new RemoteSandBox();
            default:throw new IllegalArgumentException("不支持类型");
        }
    }
}
