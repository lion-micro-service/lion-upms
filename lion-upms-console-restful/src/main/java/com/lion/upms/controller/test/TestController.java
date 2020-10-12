package com.lion.upms.controller.test;

import com.lion.annotation.AuthorizationIgnore;
import com.lion.common.expose.parameter.ParameterExposeService;
import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.exception.BusinessException;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-12 16:46
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference
    private ParameterExposeService parameterExposeService;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/xa")
//    @AuthorizationIgnore
    //在Seata Xa 模式下不用全局事物注解会报错
//    @GlobalTransactional()
    public IResultData testXa(){
        //调用远程dubbo(rpc)服务
        parameterExposeService.testSeataTransactional("test","test111");
//        new BusinessException("测试(XA)分布式事物回滚");
        return ResultData.instance();
    }
}
