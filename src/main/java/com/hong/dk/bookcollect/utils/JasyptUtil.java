package com.hong.dk.bookcollect.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;


public class JasyptUtil {

    public static void main(String[] args) {
        JasyptUtil test = new JasyptUtil();
        String s = test.testEncrypt();
        test.testDe(s);
    }

    public String testEncrypt() {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");          // 加密的算法，这个算法是默认的
        config.setPassword("wqh");                        // 加密的密钥
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "tlxamrtiwmvbiijd";         //自己的密码
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
        return encryptedText;
    }


    public void testDe(String s) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("wqh");
        standardPBEStringEncryptor.setConfig(config);
//        String encryptedText = s ;   //加密后的密码
        String plainText = standardPBEStringEncryptor.decrypt(s);
        System.out.println(plainText);
    }
}
