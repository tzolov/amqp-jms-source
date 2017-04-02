package org.springframework.cloud.stream.app.amqp.jms.source;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by tzoloc on 3/30/17.
 */
@ConfigurationProperties(prefix = "")
public class AmqpJmsSourceProperties {

	// Note the field name 'qpid' is used by Spring as property prefix!
	// All application properties starting wih 'qpid.*' will be returned by this map. The prefix is removed.
	// https://qpid.apache.org/releases/qpid-jms-0.21.0/docs/index.html
	private final Map<String, String> qpid = new HashMap<>();

	private AmqpProtocol amqpJmsProtocol = AmqpProtocol.amqp;

	private String amqpJmsHostname = "localhost";

	private int amqpJmsPort = 5672;

	public Map<String, String> getQpid() {
		return qpid;
	}

	public AmqpProtocol getAmqpJmsProtocol() {
		return amqpJmsProtocol;
	}

	public void setAmqpJmsProtocol(AmqpProtocol amqpJmsProtocol) {
		this.amqpJmsProtocol = amqpJmsProtocol;
	}

	public String getAmqpJmsHostname() {
		return amqpJmsHostname;
	}

	public void setAmqpJmsHostname(String amqpJmsHostname) {
		this.amqpJmsHostname = amqpJmsHostname;
	}

	public int getAmqpJmsPort() {
		return amqpJmsPort;
	}

	public void setAmqpJmsPort(int amqpJmsPort) {
		this.amqpJmsPort = amqpJmsPort;
	}

	public String computeQpidUri() {
		String uri = String.format("%s://%s:%s?%s",
				getAmqpJmsProtocol(),
				getAmqpJmsHostname(),
				getAmqpJmsPort(),
				toParams(getQpid()));

		System.out.println("QPID URI: " + uri);
		return uri;
	}

	/**
	 * Converst the qpid properties into APQP URI parameters.
	 * @param map Map of qpid application properties (https://qpid.apache.org/releases/qpid-jms-0.21.0/docs/index.html)
	 * @return Returns a string that converts the map into: k1=v1&k2=v2&.....&kN=vN
	 */
	private String toParams(Map<String, String> map) {

		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> e : map.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		return sb.toString();
	}

	public enum AmqpProtocol {
		amqp,
		amqps
	}
}
