package org.anuen.email;

import org.anuen.common.enums.EmailSubjects;
import org.anuen.email.config.MailProperties;
import org.anuen.email.entity.EmailSettings;
import org.anuen.email.service.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties(MailProperties.class)
public class SpringbootTest {

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private EmailSender emailSender;


    @Test
    public void testSend() {
        EmailSettings settings = EmailSettings
                .newSettings()
                .subject(mailProperties.getSubject().get(EmailSubjects.VERIFY_CODE.toString()))
                .destination("yuchen.f1001@outlook.com");

        emailSender.sendVerifyCode(settings);
    }

    /*@Autowired
    private EmailSenderImpl emailSender;

    @Test
    public void testHtml() {
        String s = emailSender.buildContent("123456");
        System.out.println("s = " + s);
    }*/
}
