/**
 * Project Name:spring-boot-starter-mybatis
 * File Name:DataSourceConfiguration.java
 * Package Name:com.midai.springboot.mybatis
 * Date:2016年7月28日上午9:58:36
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.framework.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * ClassName:DataSourceConfiguration <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年7月28日 上午9:58:36 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Configuration
@EnableConfigurationProperties(JdbcProperties.class)
@AutoConfigureBefore(MybatisAutoConfiguration.class)
@ConditionalOnClass(DruidDataSource.class)
public class DataSourceConfiguration {

	private static Logger log= LoggerFactory.getLogger(DataSourceConfiguration.class);

	@Autowired
	private JdbcProperties jdbcProperties;

	/**
	 * 
	 * dataSource:(数据库连接对象). <br/>
	 * 
	 * @author 陈勋
	 * @return
	 * @since JDK 1.7
	 */
	@Bean(initMethod="init",destroyMethod="close")
	public DataSource dataSource() {

		log.info("statrt config DruidDataSource");
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(jdbcProperties.getUrl());
		dataSource.setUsername(jdbcProperties.getUsername());
		dataSource.setPassword(jdbcProperties.getPassword());
		// 连接池属性
		dataSource.setInitialSize(jdbcProperties.getInitialSize());
		dataSource.setMaxActive(jdbcProperties.getMaxActive());
		dataSource.setMinIdle(jdbcProperties.getMinIdle());
		dataSource.setMaxWait(jdbcProperties.getMaxWait());
		dataSource.setPoolPreparedStatements(jdbcProperties.isPoolPreparedStatements());
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(jdbcProperties.getMaxPoolPreparedStatementPerConnectionSize());
		// 测试属性
		dataSource.setValidationQuery(jdbcProperties.getValidationQuery());
		dataSource.setTestOnBorrow(jdbcProperties.isTestOnBorrow());
		dataSource.setTestOnReturn(jdbcProperties.isTestOnReturn());
		dataSource.setTestWhileIdle(jdbcProperties.isTestWhileIdle());
		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		dataSource.setTimeBetweenEvictionRunsMillis(jdbcProperties.getTimeBetweenEvictionRunsMillis());
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		dataSource.setMinEvictableIdleTimeMillis(jdbcProperties.getMinEvictableIdleTimeMillis());
		// 打开removeAbandoned功能
		dataSource.setRemoveAbandoned(jdbcProperties.isRemoveAbandoned());
		// 1800秒，也就是30分钟
		dataSource.setRemoveAbandonedTimeout(jdbcProperties.getRemoveAbandonedTimeout());
		// 关闭abanded连接时输出错误日志
		dataSource.setLogAbandoned(jdbcProperties.isLogAbandoned());
		// 监控数据库
//		try {
//			dataSource.setFilters("mergeStat");
//		} catch (SQLException e) {
//			log.error(e.getMessage());
//		}
		return dataSource;

	}

}
