package com.ateam.calmate.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.ateam.calmate",annotationClass = Mapper.class)
public class MybatisConfig {

}
