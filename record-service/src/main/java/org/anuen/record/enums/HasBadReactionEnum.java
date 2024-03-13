package org.anuen.record.enums;

import lombok.Getter;

@Getter
public enum HasBadReactionEnum {
    HAS(1, Boolean.TRUE),
    DO_NOT_HAS(0, Boolean.FALSE)
    ;

    private final Integer value;
    private final Boolean judgement;


    HasBadReactionEnum(Integer value, Boolean has) {
        this.value = value;
        this.judgement = has;
    }
}
