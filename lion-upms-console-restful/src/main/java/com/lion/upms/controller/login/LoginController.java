package com.lion.upms.controller.login;

import com.lion.config.RestTemplateConfiguration;
import com.lion.core.IResultData;
import com.lion.core.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Map;

/**
 * @author mr.liu
 * @Description: 后台登陆
 * @date 2020/9/9下午3:50
 */
@RestController
@RequestMapping("/upms/console")
public class LoginController  {

    @Autowired
    @Qualifier(RestTemplateConfiguration.REST_TAMPLATE_LOAD_BALANCED_BEAN_NAME)
    private RestTemplate restTemplate;

    @PostMapping("/login")
    public IResultData login(Principal principal, Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        ResultData resultData = ResultData.instance();

        return resultData;
    }
}
