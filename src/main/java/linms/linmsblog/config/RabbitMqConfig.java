package linms.linmsblog.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange.mail-exchange}")
    private String mailExchange;

    @Getter
    @Value("${rabbitmq.queue.mail-queue}")
    private String mailQueue;


    private final ObjectMapper objectMapper;

    @Bean
    protected Queue mailQueue() {
        return new Queue(mailQueue, false);
    }

    @Bean
    protected Exchange mailExchange() {
        return new DirectExchange(mailExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    protected RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    protected RabbitTemplate RabbitTemplate(ConnectionFactory connectionFactory,RabbitAdmin rabbitAdmin) {
        var messagingBinding = prepareBinding(
                mailQueue(), mailExchange(), mailQueue);
        rabbitAdmin.declareBinding(messagingBinding);
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(mailExchange);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    private Binding prepareBinding(Queue queue, Exchange exchange, String queueRoutingKey) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(queueRoutingKey)
                .noargs();
    }

}
