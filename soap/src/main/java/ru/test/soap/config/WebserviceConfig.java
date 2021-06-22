package ru.test.soap.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * configuration soap service
 */
@EnableWs
@Configuration
public class WebserviceConfig extends WsConfigurerAdapter {

    /**
     * servlet registration
     * @param context
     * @return
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/*");
    }

    /**
     * configure wsdl file and api
     * @param personsSchema
     * @return
     */
    @Bean(name = "clients")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema personsSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("GetPerson");
        definition.setLocationUri("/ws");
        definition.setSchema(personsSchema);
        return definition;
    }

    @Bean
    public XsdSchema personsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("clients.xsd"));
    }

}
