/*
package com.sky.service.impl;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.util.*;

*/
/**
 * @Author chengshengwen
 * @Date 2023/6/15 11:41
 * @PackageName:com.tekrally.assetManagement.util
 * @ClassName: BeanInitMetrics
 * @Description: TODO  打印bean耗时的基础工具类
 * @Version 1.0
 *//*

@Service
@Slf4j
public class BeanInitMetrics implements BeanPostProcessor {

    private final Map<String, Long> stats = new HashMap<>();

    //private Map<String,Integer> metrics = new HashMap<>();


    private final List<Metric> metrics = new ArrayList<>();

    @Data
    public static class Metric{

        public Metric() {
        }

        public Metric(String name, Integer value) {
            this.name = name;
            this.value = value;
            this.createDate = new Date();
        }

        private String name; //bean名称打印

        private Integer value; //bean耗时，单位为毫秒

        private Date createDate; //bean耗时的创建时间
    }

    //重写初始化接口
    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        stats.put(beanName, System.currentTimeMillis());
        return bean;
    }

    //后处理后初始化
    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        Long start = stats.get(beanName);
        if (start != null) {
            metrics.add(new Metric(beanName, Math.toIntExact(System.currentTimeMillis() - start)));
        }
        return bean;
    }

    //通过后处理后初始化 - 初始化的时间算出bean耗时
    public List<Metric> getMetrics() {
        metrics.sort((o1, o2) -> {
            try {
                return o2.getValue() - o1.getValue();
            }catch (Exception e){
                return 0;
            }
        });
        log.info("metrics {}", JSON.toJSONString(metrics));
        return UnmodifiableList.unmodifiableList(metrics); //只读的集合
    }

}

*/
