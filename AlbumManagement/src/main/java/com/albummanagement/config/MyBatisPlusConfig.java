package com.albummanagement.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本类的作用是配置MybatisPlus的各种配置
 * @Author:gj
 * @DateTime:2023/4/3 20:40
 **/
@Configuration
@MapperScan("com/albummanagement/dao/mapper")//开启包扫描
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //创建 MybatisPlusInterceptor 实例，用于存储各种 Mybatis-Plus 插件。
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //向 MybatisPlusInterceptor 中添加一个 PaginationInnerInterceptor 实例
        // 指定数据库类型为 MySQL，即启用 Mybatis-Plus 分页插件。
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
