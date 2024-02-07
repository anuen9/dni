package org.anuen.email.service;


import org.anuen.email.entity.EmailSettings;

public interface EmailSender {

    void sendVerifyCode(EmailSettings settings);
}
