/*
package com.sky.controller.user;

import com.sky.service.impl.BeanInitMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

*/
/**
 * @Author chengshengwen
 * @Date 2023/6/15 11:47
 * @PackageName:com.tekrally.assetManagement.controller
 * @ClassName: BeanInitController
 * @Description: TODO 测试加载bean慢的原因的api层
 * @Version 1.0
 *//*

@RestController
@RequestMapping("/beanInit")
public class BeanInitController {

    @Resource
    private BeanInitMetrics beanInitMetrics;
    @Autowired
    private com.sky.service.impl.BeanInitMetrics BeanInitMetrics;

    @GetMapping("/getMetrics")
    public List<BeanInitMetrics.Metric> getMetrics() {
        return beanInitMetrics.getMetrics();
    }
}

*/
