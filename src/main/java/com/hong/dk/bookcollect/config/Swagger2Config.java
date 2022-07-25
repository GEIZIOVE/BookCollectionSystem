package com.hong.dk.bookcollect.config;

import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 当空
 * Swagger2配置信息
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "dev")
public class Swagger2Config {

    @Bean
    public Docket productApiConfig(){

        //添加head参数配置
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2) //
                .groupName("郭浩")
                .apiInfo(productApiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any()) // 对所有api进行监控
                .paths(Predicates.not(PathSelectors.regex("/error.*")))// 错误路径不监控
                .paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
                .build()
                .globalOperationParameters(pars) ;// 增加全局参数

    }
    private ApiInfo productApiInfo(){ //创建API的基本信息，这些信息会在Swagger UI中进行显示。
        return new ApiInfoBuilder()
                .title("接口---API文档")
                .description("本文档描述了书籍取件项目后端服务接口定义")
                .version("测试版1.0")
                .contact(new Contact("wqh", "无", "1601709391@qq.com"))
                .extensions(null) // 在线文档的扩展信息
                .license("Apache License Version 2.0wuhu ")
                .build();
    }


}
