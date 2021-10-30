package com.mtvn;

import com.mtvn.rest.config.RestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.AsyncNCSARequestLog;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.io.File;

@SpringBootApplication(exclude= {LiquibaseAutoConfiguration.class})
@Slf4j
@EnableWebMvc
@ComponentScan(basePackages = {"com.mtvn"})
@ImportResource(value = {"classpath:springmvc-resteasy.xml"})
public class Application {

    @PostConstruct
    private void post(){
        log.info("Hello Application!");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

    @Bean
    public ResteasyBootstrap resteasyBootstrap() {
        return new ResteasyBootstrap();
    }

    @Bean
    public ServletRegistrationBean initServlet(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, false, "/*");
        servletRegistrationBean.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        return servletRegistrationBean;
    }

    @Bean
    public JettyServletWebServerFactory jettyEmbeddedServletContainerFactory(
            @Value("${server.port:0}") final String port,
            @Value("${mt.server.threadPool.maxThreads:256}") final String maxThreads,
            @Value("${mt.server.threadPool.minThreads:64}") final String minThreads,
            @Value("${mt.server.threadPool.idleTimeout:60000}") final String idleTimeout,
            @Value("${mt.server.accesslog.dir:/opt/apps/moneytap/logs/first-practice/}") final String logDir,
            @Value("${mt.server.accesslog.enabled:true}") final boolean accessLogEnabled) {
        log.info("Init JettyServletWebServer Start!");
        final JettyServletWebServerFactory factory =  new JettyServletWebServerFactory(RestConfiguration.getPortToRun());
        factory.addServerCustomizers(new JettyServerCustomizer() {
            @Override
            public void customize(final Server server) {
                if(accessLogEnabled){
                    HandlerCollection handlers = new HandlerCollection();
                    for (Handler handler : server.getHandlers()) {
                        handlers.addHandler(handler);
                    }

                    handlers.addHandler(createRequestLogHandler(logDir));
                    server.setHandler(handlers);
                }

                // Tweak the connection pool used by Jetty to handle incoming HTTP connections
                final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
                threadPool.setMaxThreads(RestConfiguration.getMaximumJettyThreads());
                threadPool.setMinThreads(RestConfiguration.getMinimumJettyThreads());
                threadPool.setIdleTimeout(RestConfiguration.getIdleTimeoutJettyThreadsInMillis());
            }
        });
        log.info("Init JettyServletWebServer End!");
        return factory;
    }

    private RequestLogHandler createRequestLogHandler(String logDir){
        AsyncNCSARequestLog log = new AsyncNCSARequestLog();
        log.setFilename(logDir + File.separator + "yyyy_mm_dd.request.log");
        log.setFilenameDateFormat("yyyy_MM_dd");
        log.setExtended(true);
        log.setLogServer(true);
        log.setLogLatency(true);
        log.setPreferProxiedForAddress(true);
        log.setRetainDays(90);
        log.setAppend(true);
        log.setLogCookies(false);
        log.setLogTimeZone("UTC");
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(log);
        return requestLogHandler;
    }
}
