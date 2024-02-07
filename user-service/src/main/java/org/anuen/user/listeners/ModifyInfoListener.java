package org.anuen.user.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anuen.common.entity.ModifyPassForm;
import org.anuen.user.service.IUserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ModifyInfoListener {

    private final IUserService userService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "modify.user.pass.queue"),
                    exchange = @Exchange(name = "patient.topic", type = ExchangeTypes.TOPIC),
                    key = "modify.pass"))
    public void listenModifyPass(ModifyPassForm mpf) {
        userService.modifyPassword(mpf);
    }
}
