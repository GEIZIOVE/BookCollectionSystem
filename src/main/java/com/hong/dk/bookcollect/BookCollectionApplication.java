package com.hong.dk.bookcollect;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;


@SpringBootApplication
@EnableTransactionManagement //启动事务
@EnableAspectJAutoProxy(exposeProxy = true)
public class BookCollectionApplication {
    @Autowired
    private static DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(BookCollectionApplication.class, args);
    }

}
