package com.hong.dk.bookcollect.consumer;

import com.alibaba.fastjson.JSON;
import com.hong.dk.bookcollect.entity.pojo.dto.EmailDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.hong.dk.bookcollect.constant.MQPrefixConst.EMAIL_QUEUE;


/**
 * 通知邮箱
 *
 * @author yezhqiu
 * @date 2021/06/13
 * @since 1.0.0
 **/
@Component
@RabbitListener(queues = EMAIL_QUEUE)
public class EmailConsumer {

    /**
     * 邮箱号
     */
    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @RabbitHandler
    public void process(byte[] data) throws MessagingException { //data是消息内容
        EmailDTO emailDTO = JSON.parseObject(new String(data), EmailDTO.class);
//        SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(email); //发送者
        helper.setTo(emailDTO.getEmail()); //接收者
        helper.setSubject(emailDTO.getSubject()); //主题

        String code = emailDTO.getContent();
        Map<String, Object> valueMap = new HashMap<>();
        //如果code长度为六位，则使用code_email.html模板
        if (code.length() == 6) {
            //将code转为Arraylist
            ArrayList<String> codeList = new ArrayList<>();
            for (int i = 0; i < code.length(); i++) {
                codeList.add(code.substring(i, i + 1));
            }

            valueMap.put("code", codeList);
            Context context = new Context();
            context.setVariables(valueMap);
            String content = this.templateEngine.process("code_email", context);
            helper.setText(content, true);
        }else {
            //        valueMap.put("to", emailDTO.getEmail());
            valueMap.put("email", emailDTO.getEmail());
            valueMap.put("content", emailDTO.getContent());
            // 添加正文（使用thymeleaf模板）
            Context context = new Context();
            context.setVariables(valueMap);
            String content = this.templateEngine.process("mail", context);
            helper.setText(content, true);
        }

        javaMailSender.send(message);
    }

}
