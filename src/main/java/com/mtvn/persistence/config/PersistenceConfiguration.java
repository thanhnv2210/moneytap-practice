package com.mtvn.persistence.config;

import com.mtvn.common.string.StringUtils;
import com.mtvn.persistence.typemapping.JsonPostgreSQLDialect;
import com.netflix.config.DynamicPropertyFactory;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

/**
 * All configuration and bean declarations will go here
 */

@Slf4j
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories({"com.moneytap.entity.repository", "com.mwyn.priority.dao","com.mwyn.persistence.dao","com.moneytap.common.persistence"})
@EnableTransactionManagement
public class PersistenceConfiguration {

	@Value("${spring.datasource.username:#{null}}") private String username;
	@Value("${spring.datasource.password:#{null}}") private String password;
	@Value("${spring.datasource.database:#{null}}") private String database;
	@Value("${spring.datasource.server:#{null}}") private String server;
	@Value("${spring.datasource.port:#{null}}") private Integer port;
	@Value("${spring.datasource.hikari.maximum-pool-size:#{null}}") private Integer maxPoolSize;
	@Value("${spring.datasource.hikari.connection-timeout:#{null}}") private Long connectionTimeout;
	
	@Value("${mt.hibernate.cache.enabled:false}") private boolean hibernateCacheEnabled;
	@Value("${mt.hibernate.cache.provider.config:hibernate-redis.properties}") private String cacheProviderConfig;


	private static final String SHOW_SQL = "hibernate.show_sql";
	private static final String FORMAT_SQL = "hibernate.format_sql";

	private int getMaxPoolSize() {
		if(this.maxPoolSize == null)
			this.maxPoolSize = DynamicPropertyFactory.getInstance().getIntProperty("persistence.maximumPoolSize", 5).getValue();
		log.debug("maxPoolSize: {}", maxPoolSize);
		return maxPoolSize;
	}

	private String getUsername() {
		String username = this.username;
		if(!StringUtils.hasText(username))
			username = DynamicPropertyFactory.getInstance().getStringProperty("persistence.user", "mwyn").getValue();
		log.debug("username: {}", username);
		return username;
	}

	private String getPassword() {
		String password = this.password;
		if(!StringUtils.hasText(password))
			password = DynamicPropertyFactory.getInstance().getStringProperty("persistence.password", "nywm").getValue();
		return password;
	}

	private long getConnectionTimeout() {
		if(this.connectionTimeout == null)
			this.connectionTimeout = DynamicPropertyFactory.getInstance().getLongProperty("persistence.connectionTimeout", 5000).getValue();
		log.debug("connectionTimeout: {}", connectionTimeout);
		return connectionTimeout;
	}

	private String getDatabaseName() {
		if(!StringUtils.hasText(this.database))
			this.database = DynamicPropertyFactory.getInstance().getStringProperty("persistence.databaseName", "cashindb").getValue();
		return this.database;
	}

	private String getServerName() {
		if(!StringUtils.hasText(this.server))
			this.server = DynamicPropertyFactory.getInstance().getStringProperty("persistence.serverName", "localhost").getValue();
		return this.server;
	}

	private int getPortNumber() {
		if(this.port == null)
			this.port = DynamicPropertyFactory.getInstance().getIntProperty("persistence.portNumber", 15432).getValue();
		return this.port;
	}

	private Boolean getShowSql() {
		return DynamicPropertyFactory.getInstance().getBooleanProperty(SHOW_SQL, false).getValue();
	}

	private Boolean getFormatSql() {
		return DynamicPropertyFactory.getInstance().getBooleanProperty(FORMAT_SQL, false).getValue();
	}
	private Boolean getNullabilityCheck(){
		return DynamicPropertyFactory.getInstance().getBooleanProperty("spring.jpa.properties.hibernate.check_nullability",false).getValue();
	}

	@Bean
	public DataSource dataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
		ds.addDataSourceProperty("databaseName", getDatabaseName());
		ds.addDataSourceProperty("serverName", getServerName());
		ds.addDataSourceProperty("portNumber", getPortNumber());
		ds.setUsername(getUsername());
		ds.setPassword(getPassword());
		ds.setConnectionTimeout(getConnectionTimeout());
		ds.setMaximumPoolSize(getMaxPoolSize());
		//ds.setLeakDetectionThreshold(5000);
		return ds;
	}

	@Bean("entityManagerFactory")
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
		em.setDataSource(dataSource());
		em.setJpaDialect(new HibernateJpaDialect());
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setPackagesToScan("com.mwyn");
		em.setJpaProperties(hibernateProperties());
		em.afterPropertiesSet();
		return em.getObject();
	}

	@Bean
	public Properties hibernateProperties(){
		Properties props = new Properties();
		props.put(Environment.DIALECT, JsonPostgreSQLDialect.class.getName());
		props.put(Environment.SHOW_SQL, getShowSql());
		props.put(Environment.FORMAT_SQL, getFormatSql());
		props.put(Environment.USE_SQL_COMMENTS, false);
		props.put(Environment.GENERATE_STATISTICS, true);
		props.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, SpringSessionContext.class.getName());
		props.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);
		props.put(Environment.CONNECTION_HANDLING, PhysicalConnectionHandlingMode.DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION);
		props.put(Environment.CHECK_NULLABILITY,getNullabilityCheck());
		
		log.debug("Hibernate Cache Enabled: {}", hibernateCacheEnabled);
		if(hibernateCacheEnabled) {
			props.put(Environment.USE_QUERY_CACHE, hibernateCacheEnabled);
			props.put(Environment.USE_SECOND_LEVEL_CACHE, hibernateCacheEnabled);
			props.put(Environment.USE_STRUCTURED_CACHE, hibernateCacheEnabled);
			props.put(Environment.USE_DIRECT_REFERENCE_CACHE_ENTRIES, hibernateCacheEnabled);
			props.put(Environment.GENERATE_STATISTICS, hibernateCacheEnabled);
			
			props.put(Environment.CACHE_REGION_FACTORY, org.hibernate.cache.redis.hibernate52.SingletonRedisRegionFactory.class.getName());
			props.put(Environment.CACHE_REGION_PREFIX, "hibernate");
			props.put(Environment.CACHE_PROVIDER_CONFIG, cacheProviderConfig);
			props.put("javax.persistence.sharedCache.mode", SharedCacheMode.ENABLE_SELECTIVE);
			props.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
		}
		return props;
	}

	@Bean
	public JpaTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(emf);
		jpaTransactionManager.setGlobalRollbackOnParticipationFailure(false);
		return jpaTransactionManager;
	}

	@Bean
	public Statistics statistics(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
		return emf.unwrap(SessionFactory.class).getStatistics();
	}

//	@Primary
//	@Bean
//	public SessionFactory sessionFactory(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
//		return emf.unwrap(SessionFactory.class);
//	}
	
	/*
	 * To catch Platform specific exceptions and throw them as Spring's unified 
	 * unchecked exceptions. DAOs need to be marked as @Repository for this to take 
	 * effect. 
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean 
	public HibernateExceptionTranslator hibernateExceptionTranslator(){ 
		return new HibernateExceptionTranslator(); 
	}

	@Bean
	PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessorBean() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	@ConditionalOnProperty(name="spring.liquibase.enabled", havingValue="true", matchIfMissing=false)
	public SpringLiquibase liquibase() {
		log.info("Initializing Liquibase from classpath:db/changelog/db.changelog-master.xml");
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
		liquibase.setDataSource(dataSource());
		return liquibase;
	}
	
	
	@Bean
	@Order(Integer.MAX_VALUE)
	@ConditionalOnMissingBean
	public AuditorAware<String> auditorAware() {
		return new AuditorAware<String>() {
			@Override
			public Optional<String> getCurrentAuditor() {
				String retVal = null;
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if(auth != null && auth.isAuthenticated()) {
					if(auth.getPrincipal() instanceof UserDetails){
						UserDetails user = ((UserDetails) auth.getPrincipal());
						if(user != null && StringUtils.hasText(user.getUsername()))
							retVal = user.getUsername();
					}
				}
				retVal = StringUtils.hasText(retVal) ? retVal : "SYSTEM";
				return Optional.of(StringUtils.trimUpto(retVal, 32));
			}
		};
	}
}