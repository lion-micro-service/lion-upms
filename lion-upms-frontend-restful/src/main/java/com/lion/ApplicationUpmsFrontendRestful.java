package com.lion;

import com.lion.core.persistence.BaseDaoFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication ( scanBasePackages = {"com.lion.**"} )
@EnableJpaRepositories(basePackages = {"com.lion.upms.dao.**"}, repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
@EntityScan({"com.lion.upms.entity.**"})
@EnableDiscoveryClient
@EnableJpaAuditing
public class ApplicationUpmsFrontendRestful {

    public static void main ( String args[] ) throws Exception {
        /*
         * new SpringApplicationBuilder(Application.class)
         * .web(WebApplicationType.NONE) .run(args);
         */
        SpringApplication.run(ApplicationUpmsFrontendRestful.class, args);
    }
}