package com.nacho.config;

import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;

@Configuration
@EnableJpaRepositories(basePackages = { "com.nacho.repository" })
@ComponentScan(basePackages = "com.nacho", excludeFilters = {
		@ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION),
		@ComponentScan.Filter(value = Configuration.class, type = FilterType.ANNOTATION) })
public class AppConfig extends RepositoryRestMvcConfiguration {

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		super.configureRepositoryRestConfiguration(config);
		try {
			config.setBaseUri(new URI("/api"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/test");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		return dataSource;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabase(Database.MYSQL);
		return adapter;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setPackagesToScan("com.nacho.model");
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.setJpaProperties(jpaProperties());

		return factoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager() throws ClassNotFoundException {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	@Bean
	public Properties jpaProperties() {
		Properties properties = new Properties();
		properties.put(HBM2DDL_AUTO, "update"); // It's better to don't have this in production
		return properties;
	}
}
