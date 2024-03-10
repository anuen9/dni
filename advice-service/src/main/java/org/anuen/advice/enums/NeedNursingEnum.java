package org.anuen.advice.enums;

import lombok.Getter;

@Getter
public enum NeedNursingEnum {
    NEED(1, "need nursing"),
    NOT_NEED(0, "don't need nursing"),

    ;


    private final Integer value;
    private final String meaning;

    NeedNursingEnum(Integer value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    public static Boolean isValueValid(Integer value) {
        for (NeedNursingEnum anEnum : NeedNursingEnum.values()) {
            if (value.equals(anEnum.getValue())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static String getMeaningByValue(Integer value) {
        for (NeedNursingEnum anEnum : NeedNursingEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum.getMeaning();
            }
        }
        return null;
    }
}
