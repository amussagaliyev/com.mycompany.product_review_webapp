package com.mycompany.db;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mycompany.log.LogTrace;

@Configuration
@EnableTransactionManagement
public class HibernateConf
{

	@Bean
	@Scope("singleton")
	public LocalSessionFactoryBean sessionFactory()
	{
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan("com.mycompany.model");
		sessionFactory.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));

		return sessionFactory;

	}

	@Bean
	public DataSource dataSource()
	{
		try
		{
			DataSource dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/mycompanyDS");
			return dataSource;
		} catch (NamingException e)
		{
			LogTrace.error(e, "HibernateConf.dataSource", "Could not get DtaSource from contextd", null);
			throw new RuntimeException(e);
		}
	}

	@Bean
	public PlatformTransactionManager hibernateTransactionManager()
	{
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

}