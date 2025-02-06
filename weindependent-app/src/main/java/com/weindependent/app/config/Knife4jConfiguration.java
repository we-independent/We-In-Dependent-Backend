package com.weindependent.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc // 貌似被弃用，可以替换为@EnableSwagger2
public class Knife4jConfiguration {

    @Value("${swagger.enable}")
    private Boolean enable;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(
                        new ApiInfoBuilder()
                                //.title("swagger-bootstrap-ui-demo RESTful APIs")
                                .description("# swagger-bootstrap-ui-demo RESTful APIs")
                                .termsOfServiceUrl("http://www.xx.com/")
                                .contact(new Contact("小盛", "123", "504040410@qq.com"))
                                .version("1.0")
                                .build()
                )
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定你自己的Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
