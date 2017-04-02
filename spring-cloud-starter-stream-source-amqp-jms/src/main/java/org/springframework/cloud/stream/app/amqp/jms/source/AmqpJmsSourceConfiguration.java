package org.springframework.cloud.stream.app.amqp.jms.source;

import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.qpid.jms.JmsConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.app.jms.source.JmsSourceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Creates Qpid ConnectionFactory initialized with the qpid.* parameters
 */
@Configuration
@EnableConfigurationProperties(AmqpJmsSourceProperties.class)
@Import(JmsSourceConfiguration.class)
public class AmqpJmsSourceConfiguration {

	public static final String QPID_INITIAL_CONTEXT_FACTORY = "org.apache.qpid.jms.jndi.JmsInitialContextFactory";

	private static final String QPID_CONNECTION_FACTORY_KEY_PREFIX = "connectionfactory.";

	private static final String QPID_CONNECTION_FACTORY_LOOKUP_KEY = "qpidFactoryLookup";

	@Autowired
	private AmqpJmsSourceProperties qpidProperties;

	@Bean
	public ConnectionFactory connectionFactory() throws NamingException {
		Hashtable<Object, Object> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, QPID_INITIAL_CONTEXT_FACTORY);
		env.put(QPID_CONNECTION_FACTORY_KEY_PREFIX + QPID_CONNECTION_FACTORY_LOOKUP_KEY, qpidProperties.computeQpidUri());
		Context context = new InitialContext(env);
		return (ConnectionFactory) context.lookup(QPID_CONNECTION_FACTORY_LOOKUP_KEY);
	}

//	@Bean
	public ConnectionFactory connectionFactory2() {
		return new JmsConnectionFactory(qpidProperties.computeQpidUri());
	}
}
