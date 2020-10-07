package com.lion;

import com.lion.aop.exception.RestulException;
import com.lion.config.EntityAuditorConfiguration;
import com.lion.core.persistence.BaseDaoFactoryBean;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication ()
@ComponentScan(basePackages = "com.lion.**")
@EnableDiscoveryClient
@DubboComponentScan(basePackages = {"com.lion.**"})
@EnableJpaRepositories(basePackages = {"com.lion.upms.dao.**"}, repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
@EntityScan({"com.lion.upms.entity.**"})
@EnableJpaAuditing
@EnableOpenApi
//@OpenAPIDefinition(info = @Info(title = "upms",description = "upms",version = "${spring.cloud.nacos.group}"),tags = @Tag(name = "upms",description = "upms"))
public class ApplicationUpmsConsoleRestful {

    public static void main ( String args[] ) throws Exception {
        /*
         * new SpringApplicationBuilder(Application.class)
         * .web(WebApplicationType.NONE) .run(args);
         */
        SpringApplication.run(ApplicationUpmsConsoleRestful.class, args);
    }
}