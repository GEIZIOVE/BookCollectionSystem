package com.hong.dk.bookcollect.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.hong.dk.bookcollect.constant.MQPrefixConst.*;


/**
 * Rabbitmq配置类
 *
 * @author yezhiqiu
 * @date 2021/07/29
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange canalExchange() { // 创建一个广播交换器
        return new TopicExchange(CANAL_EXCHANGE, true, false);
    }

    @Bean
    public Queue canalBookQueue() { // 创建一个队列
        return new Queue(CANAL_BOOK_QUEUE, true);
    }
    @Bean
    public Queue canalAppealQueue() { // 创建一个队列
        return new Queue(CANAL_APPEAL_QUEUE, true);
    }
    @Bean
    public Queue canalOrderQueue() { // 创建一个队列
        return new Queue(CANAL_ORDER_QUEUE, true);
    }


    @Bean
    public Binding bindingBookDirect() { // 绑定广播交换器和队列
        return BindingBuilder.bind(canalBookQueue()).to(canalExchange()).with(CANAL_BOOK_KEY);
    }

    @Bean
    public Binding bindingAppealDirect() { // 绑定广播交换器和队列
        return BindingBuilder.bind(canalAppealQueue()).to(canalExchange()).with(CANAL_APPEAL_KEY);
    }

    @Bean
    public Binding bindingOrderDirect() { // 绑定广播交换器和队列
        return BindingBuilder.bind(canalOrderQueue()).to(canalExchange()).with(CANAL_ORDER_KEY);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

}
