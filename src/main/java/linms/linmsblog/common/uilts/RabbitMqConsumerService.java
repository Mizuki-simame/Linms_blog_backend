package linms.linmsblog.common.uilts;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.Channel;
import linms.linmsblog.model.dto.MailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class RabbitMqConsumerService {

    @Autowired
    private JavaMailSender mailSender;


    @RabbitListener(queues = "${rabbitmq.queue.mail-queue}",ackMode = "MANUAL")
    public void receivedMessage(@Payload MailDTO mailDTO, Channel channel, @Header("amqp_deliveryTag") long tag) throws IOException {
        try {
            String mailFrom = "javamail_lin@163.com";
            String subject = "有新的回复";
            String sendText = "有人评论：" + mailDTO.getCommit();
                String mailAddress = "lin_19382116831@163.com";
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setSubject(subject);
                mail.setText(sendText);
                mail.setFrom(mailFrom);
                mail.setTo(mailAddress);
                mailSender.send(mail);

            channel.basicAck(tag, false); // 确认处理成功
            log.info("received message with text: {}", mailDTO.getCommit());
        } catch (Exception e) {
            channel.basicNack(tag, false, true); // 重新入队
        }

    }

}
