package org.anuen.advice.entity.vo;

import lombok.Data;

@Data
public class BindAdviceVo {
    private Integer adviceId;
    private String content;
    private String needNursing;
    private Integer requiredNursingNumber;
    private String nursingFrequency;

    public static BindAdviceVo newInstance() {
        return new BindAdviceVo();
    }
}
