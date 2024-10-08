package org.anuen.common.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    // fail response: code are start with 100
    FAIL(1000, "fail"),
    USER_EXISTS(1001, "user exists"),
    LOGIN_FORM_EMPTY(1002, "account or password can't be empty"),
    PERMISSION_DENY(1003, "illegal access credentials (invalid password)"),
    PARAM_LOSE(1004, "the necessary parameters are missing"),
    REMOTE_PROCEDURE_CALL_ERROR(1005, "the service invoked remotely failed"),
    USER_NOT_FOUND(1006, "user not found"),
    EMAIL_NOT_VERIFIED(1007, "e-mailbox not verified, verify your e-mailbox to continue"),
    IS_NOT_EMAIL_ADDRESS(1008, ExceptionMessage.IS_NOT_EMAIL_ADDRESS.getMessage()),
    NECESSARY_PARAM_MISSING(1009, "necessary parameter missing"),
    DATABASE_ERROR(1010, "database may be abnormal, please wait"),
    DATABASE_NO_RECORD(1011, "database has no such record"),
    DOCTOR_OPERATE_DENY(1012, "you do not have permission to operate"),
    PARAM_LOSS_LOGIC(1013, "parameter of form loss logic, please check again"),
    DATABASE_INCONSISTENCY(1014, "database consistency check fails"),
    TYPE_CONVERSION_ERROR(1015, "server error: type conversion error, please wait"),
    ADVICE_HAS_BEEN_COMPLETED(1016, "the medical advice basis for this care has been closed"),
    NURSE_OPERATE_DENY(1017, "you do not have permission to operate"),
    NURSE_NUMBER_CONFLICT(1018, "nurse shortage"),
    SCHEDULE_EXIST(1019, "the schedule of tomorrow has been exist"),
    PATIENT_PERMISSION_DENY(1020, "you do not have permission to look user info"),

    // failure in service details
    APPT_NOT_EXIST(2001, "appointment not exist"),

    // normal response
    UNAUTHORIZED(401, "no authorized"),
    SUCCESS(200, "success"),
    ;
    private final Integer code;
    private final String message;

    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
