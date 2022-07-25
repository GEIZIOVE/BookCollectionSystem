package com.hong.dk.bookcollect.result;
import lombok.Data;

import org.springframework.stereotype.Component;

@Data
@Component
public class Consts {
    public static final String USER_DEFAULT_IMAGE = "http://106.53.124.182:9000/public/2022/07/24/969e3166-094c-445c-afba-c6c2cfa4ae23.jfif";

    public static final Long IM_DEFAULT_USER_ID = 999L;

    public final static Long IM_GROUP_ID = 999L;
    public final static String IM_GROUP_NAME = "e-group-study";

    //消息类型
    public final static String IM_MESS_TYPE_PING = "pingMessage";
    public final static String IM_MESS_TYPE_CHAT = "chatMessage";

    public static final String IM_ONLINE_MEMBERS_KEY = "online_members_key";
    public static final String IM_GROUP_HISTROY_MSG_KEY = "group_histroy_msg_key";

}
