package org.anuen.email.service.impl;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Nonnull;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anuen.common.entity.EmailSettings;
import org.anuen.common.enums.EmailSubjects;
import org.anuen.common.enums.RedisConst;
import org.anuen.email.config.MailProperties;
import org.anuen.email.service.EmailSender;
import org.anuen.utils.CacheClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(MailProperties.class)
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;

    private final MailProperties mailProperties;

    private final CacheClient cacheClient;


    @Override
    public void sendVerifyCode(@Valid EmailSettings settings) {
        String onceCode = RandomUtil.randomNumbers(6); // verify code for once
        String htmlContent = buildContentVC(onceCode); // html string

        cacheClient.set( // store code in redis
                RedisConst.MAIL_CODE,
                settings.destination(),
                onceCode);

        MimeMessage message = javaMailSender.createMimeMessage(); // send email
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(settings.subject());
            helper.setText(htmlContent, true);
            helper.setTo(settings.destination());
            helper.setFrom(mailProperties.getSourceAddr());
            javaMailSender.send(message);
            log.info("""
                    ---> send verify code: {} to {} success.
                    """, onceCode, settings.destination());
        } catch (Exception e) {
            log.error("""
                    ---> method: sendVerifyCode().
                    --->---> error: fail to send message to email!
                    """, e);
        }
    }


    /**
     * build email content(html) of verify code
     *
     * @param verifyCode random verify code
     * @return html string
     */
    private String buildContentVC(@Nonnull String verifyCode) {
        final Resource resource = new ClassPathResource( // load .html file
                mailProperties.getResourcePath()
                        + mailProperties.getTemplateName().get(EmailSubjects.VERIFY_CODE.toString()));
        log.info("""
                 ---> read file from: {}, named: {}.
                 """,
                mailProperties.getResourcePath(),
                mailProperties.getTemplateName().get(EmailSubjects.VERIFY_CODE.toString()));

        StringBuilder htmlBuilder = new StringBuilder(); // convert file to string
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlBuilder.append(line);
            }
        } catch (Exception e) {
            log.error("""
                    ---> method: buildContent().
                    --->---> error: read template of sending verify code fail!
                    """, e);
        }

        return String.format(htmlBuilder.toString(), verifyCode); // insert code into html -> return
    }
}
