package com.lion.upms.controller.test;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
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
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/sentinel")
    @SentinelResource(value = "testSentinel",blockHandler = "blockHandler",defaultFallback = "defaultFallback")
    @AuthorizationIgnore
    public IResultData testSentinel(){
        return ResultData.instance();
    }

    {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule = new FlowRule();
        rule.setResource("testSentinel");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(1);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    public IResultData blockHandler(){
        ResultData resultData = ResultData.instance();
        resultData.setMessage("服务被降级/限流");
        return resultData;
    }

    public IResultData fallback(){
        ResultData resultData = ResultData.instance();
        resultData.setMessage("服务被熔断");
        return resultData;
    }

    public IResultData defaultFallback(){
        ResultData resultData = ResultData.instance();
        resultData.setMessage("服务被熔断/降级/限流");
        return resultData;
    }

}
