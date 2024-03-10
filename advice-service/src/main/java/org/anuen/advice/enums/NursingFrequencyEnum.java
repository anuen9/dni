package org.anuen.advice.enums;

import lombok.Getter;

@Getter
public enum NursingFrequencyEnum {
    ONCE_A_DAY(1, "once a day"),
    TWICE_A_DAY(2, "twice a day"),
    THREE_TIMES_A_DAY(3, "three times a day"),

    ;

    private final Integer value;
    private final String meaning;

    NursingFrequencyEnum(Integer value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    public static String getMeaningByValue(Integer value) {
        for (NursingFrequencyEnum anEnum : NursingFrequencyEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum.getMeaning();
            }
        }
        return null;
    }
}
