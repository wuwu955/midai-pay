/**
 * Project Name:spring-boot-starter-mybatis
 * File Name:MybatisAutoConfiguration.java
 * Package Name:com.midai.springboot.mybatis
 * Date:2016年7月27日下午5:50:37
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.framework.config.mybatis;

import com.github.pagehelper.PageHelper;
import com.midai.framework.monitor.MidaiLogMybatisPlugn;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * ClassName:MybatisAutoConfiguration <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年7月27日 下午5:50:37 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceConfiguration.class)
@AutoConfigureBefore(MyBatisMapperScannerConfig.class)
@EnableTransactionManagement
public class MybatisAutoConfiguration {

    private static Logger log= LoggerFactory.getLogger(MybatisAutoConfiguration.class);

    @Autowired
    private MybatisProperties properties;

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @PostConstruct
    public void checkConfigFileExists() {
        if (this.properties.isCheckConfigLocation()) {
            Resource resource = this.resourceLoader
                    .getResource(this.properties.getConfig());
            Assert.state(resource.exists(),
                    "Cannot find config location: " + resource
                            + " (please add config file or check your Mybatis "
                            + "configuration)");
        }
    }

    @Bean(name = "sqlSessionFactory")
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        if (StringUtils.hasText(this.properties.getConfig())) {
            factory.setConfigLocation(
                    this.resourceLoader.getResource(this.properties.getConfig()));
        } else {
            if (this.interceptors != null && this.interceptors.length > 0) {
                factory.setPlugins(this.interceptors);
            }
            

         //   factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
            factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
            factory.setMapperLocations(this.properties.getMapperLocations());
        }
        SqlSessionFactory  ssf=factory.getObject();
        //禁用一级缓存
        ssf.getConfiguration().setLocalCacheScope(LocalCacheScope.STATEMENT);
        //禁用二级缓存
        ssf.getConfiguration().setCacheEnabled(false);
        return ssf;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory,
                this.properties.getExecutorType());
    }

    /**
     * 分页插件
     *
     * @param dataSource
     * @return
     * @author SHANHY
     * @create  2016年2月18日
     */
    @Bean
    public PageHelper pageHelper(DataSource dataSource) {
        log.info("注册MyBatis分页插件PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
    
    @Bean
    public MidaiLogMybatisPlugn midaiLogMybatisPlugn() {
        log.info("注册日志监控插件midaiLogMybatisPlugn");
        MidaiLogMybatisPlugn midaiLogMybatisPlugn=new MidaiLogMybatisPlugn();

        return midaiLogMybatisPlugn;
    }
    
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource){
        DataSourceTransactionManager transactionManager=new DataSourceTransactionManager(dataSource);
        return transactionManager;
    }

}
