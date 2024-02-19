package org.anuen.email.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anuen.common.entity.EmailSettings;
import org.anuen.email.service.EmailSender;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendVerCodeListener {

    private final EmailSender emailSender;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "send.verify.code.queue"),
                    exchange = @Exchange(name = "patient.topic", type = ExchangeTypes.TOPIC),
                    key = "send.code"))
    public void listenSendVerifyCode(EmailSettings emailSettings) {
        emailSender.sendVerifyCode(emailSettings);
    }
}
