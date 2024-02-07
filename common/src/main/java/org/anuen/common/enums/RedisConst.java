package org.anuen.common.enums;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public enum RedisConst {
    LOGIN_CODE("login:code:", 2L, TimeUnit.MINUTES),
    LOGIN_USER("login:user:", 30L, TimeUnit.MINUTES),
    MAIL_CODE("email:code:", 10L, TimeUnit.MINUTES),
    ;


    private final String prefix;
    private final Long TTL;
    private final TimeUnit timeUnit;

    RedisConst(String prefix, Long TTL, TimeUnit timeUnit) {
        this.prefix = prefix;
        this.TTL = TTL;
        this.timeUnit = timeUnit;
    }
}
