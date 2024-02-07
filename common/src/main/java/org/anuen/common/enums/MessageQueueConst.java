package org.anuen.common.enums;

import lombok.Getter;

@Getter
public enum MessageQueueConst {
    MQ_MODIFY_PASS(
            "patient.topic",
            "modify.user.pass.queue",
            "modify.pass"),


    ;

    /**
     * exchange name
     */
    private final String exchange;

    /**
     * queue name
     */
    private final String queue;

    /**
     * routing key
     */
    private final String routingKey;

    MessageQueueConst(String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }


}
