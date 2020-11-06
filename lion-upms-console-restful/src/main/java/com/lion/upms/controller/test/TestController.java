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
import com.lion.upms.service.user.UserService;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-12 16:46
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private CuratorFramework client;

    @Autowired
    private UserService userService;

    @GetMapping("/xa")
    @AuthorizationIgnore
    public IResultData testXa(){
        //调用远程dubbo(rpc)服务
        userService.testXa();
        return ResultData.instance();
    }

    @GetMapping("/sentinel")
    @SentinelResource(value = "testSentinel",blockHandler = "blockHandler",defaultFallback = "defaultFallback")
    @AuthorizationIgnore
    @Transactional
    public IResultData testSentinel(){
        return ResultData.instance();
    }

    //分布式测试变量
    private Integer testI  = 1;
    @GetMapping("/lock")
    @AuthorizationIgnore
    public IResultData testLock(){
        InterProcessMutex lock = new InterProcessMutex(client, "/test-lock");
        for (int i =0; i<10; i++){
            new Thread("thread-"+i){
                @SneakyThrows
                @Override
                public void run() {
                    while (true) {
                        try {
                            lock.acquire(1, TimeUnit.SECONDS);
                            if (testI >= 100) {
                                return;
                            }
                            System.out.println(testI++);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            lock.release();
                        }
                    }
                }
            }.start();
        }
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
