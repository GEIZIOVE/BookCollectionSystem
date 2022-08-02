package com.hong.dk.bookcollect.config;

import com.hong.dk.bookcollect.entity.pojo.UserToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 当空
 * Swagger2配置信息
 */
@Configuration
@EnableSwagger2WebMvc
@ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "dev")
public class Swagger2Config {

    @Bean
    public Docket productApiConfig(){

        //添加head参数配置
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("郭浩")
                .apiInfo(productApiInfo())
                .ignoredParameterTypes(UserToken.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hong.dk.bookcollect")) // 对所有api进行监控
//                .paths()// 错误路径不监控  PathSelectors.regex("/error.*"))
                .paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
                .build()
                .useDefaultResponseMessages(false)
                .globalOperationParameters(pars);

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
