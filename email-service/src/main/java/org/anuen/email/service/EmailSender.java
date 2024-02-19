package org.anuen.email.service;


import org.anuen.common.entity.EmailSettings;

public interface EmailSender {

    void sendVerifyCode(EmailSettings settings);
}
