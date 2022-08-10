package com.hong.dk.bookcollect.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hong.dk.bookcollect.entity.pojo.Appeal;
import com.hong.dk.bookcollect.entity.pojo.User;
import com.hong.dk.bookcollect.entity.pojo.dto.CanalDataDTO;
import com.hong.dk.bookcollect.entity.pojo.dto.EmailDTO;
import com.hong.dk.bookcollect.mapper.UserMapper;
import com.hong.dk.bookcollect.utils.UserUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hong.dk.bookcollect.constant.MQPrefixConst.*;
import static com.hong.dk.bookcollect.constant.MailInfoConst.MAIL_SUBJECT_APPROVED;


/**
 * maxwell监听数据
 *
 * @author yezhiqiu
 * @date 2021/08/02
 */
@Component
@RabbitListener(queues = CANAL_QUEUE)
public class CanalConsumer {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @RabbitHandler
    public void process(byte[] data) {
        Object obj = JSON.parse(new String(data, StandardCharsets.UTF_8)); //解析消息体
        CanalDataDTO<JSONObject> canalDataDTO = new CanalDataDTO<>();
        BeanUtil.copyProperties(obj, canalDataDTO); //拷贝数据
        List<JSONObject> newData = canalDataDTO.getData(); //获取修改后的数据
        List<JSONObject> oldData = canalDataDTO.getOld(); //获修改前的数据
        String type = canalDataDTO.getType(); //获取操作类型
        String table = canalDataDTO.getTable(); //获取表名
        switch (table) {
            case "bcs_order":{
                //获取订单id
                String orderId = newData.get(0).getString("order_id");
                //获取userId
                String userId = newData.get(0).getString("user_id");
                //更新redis中的订单信息
                if("DELETE".equals(type) || "UPDATE".equals(type)){
                    //删除redis中的订单信息
                    redisTemplate.delete(userId+":order:"+orderId);
                }
            break;
            }
            case "appeal":{
                if ("UPDATE".equals(type)) { //如果是修改操作
                    for (int i = 0; i < newData.size(); i++) { //遍历修改后的数据
                        Appeal newAppeal = JSONObject.parseObject(newData.get(i).toJSONString(), Appeal.class);
                        Appeal oldAppeal = JSONObject.parseObject(oldData.get(i).toJSONString(), Appeal.class);
                        if (newAppeal.getAuditStatus() == 1 && oldAppeal.getAuditStatus() == 0) { //审核通过
                            String userId = newAppeal.getUserId(); //获取用户id
                            //根据userId查询用户邮箱
                            User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
                            String email = user.getEmail();
                            if (StringUtils.isNotBlank(email)) {
                                // 发送消息
                                EmailDTO emailDTO = new EmailDTO(); //创建邮件DTO对象
                                emailDTO.setEmail(email);
                                emailDTO.setSubject(MAIL_SUBJECT_APPROVED);
                                emailDTO.setContent("密码申诉审核已经通过");
                                rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));//参数1：交换机名称，参数2：路由键，参数3：消息内容，参数4：消息属性
                            }
                        }
                    }
                }
                break;
            }
        }



        }
    }


