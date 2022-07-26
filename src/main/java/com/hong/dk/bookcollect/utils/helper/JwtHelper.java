package com.hong.dk.bookcollect.utils.helper;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;
import java.util.Date;

public class JwtHelper {

    //过期时间
    public static long tokenExpiration = 10*60*60*1000; //10小时
    //签名秘钥
    private static final String tokenSignKey = "123456";

    //根据参数生成token
    public static String createToken(String userId, String userName) {
        return Jwts.builder() //JWT由三部分组成：header、payload、signature
                .setSubject("USER") //主题
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) //过期时间
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey) //签名算法和签名秘钥
                .compressWith(CompressionCodecs.GZIP) //压缩方式
                .compact();
    }

    //根据token字符串得到用户id
    public static String getUserId(String token) {
        if(StringUtils.isEmpty(token)) {
            return null; //如果token为空,则返回null
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);  //解析token
        Claims claims = claimsJws.getBody(); //得到payload部分
        return (String) claims.get("userId");
    }

    //根据token字符串得到用户名称
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userName");
    }

    //判断token是否过期
    public static boolean isTokenExpired(String dubToken) {
        if(StringUtils.isEmpty(dubToken)) { //如果token为空,则返回true
            return true;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(dubToken); //解析token
        Claims claims = claimsJws.getBody();  //得到payload部分
        Date expiration = claims.getExpiration();  //得到过期时间
        //如果过期时间小于当前时间,则返回true
        return expiration.getTime() < System.currentTimeMillis();

    }
}

