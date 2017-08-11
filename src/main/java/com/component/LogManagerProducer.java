package com.component;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.json.model.MessageType;
import com.message.syslog.SyslogMessageTransmitter;

/**
 * The logmanager producer.
 */
public class LogManagerProducer extends DefaultProducer {

	private static final Logger log = LoggerFactory.getLogger(LogManagerProducer.class);

	private LogManagerEndPoint endpoint;

	public LogManagerProducer(LogManagerEndPoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
	}

	public void process(Exchange exchange) throws Exception {
		
		log.debug("RequestId : " + getRequestId(exchange));
		log.debug("ProcCode: " + exchange.getProperty("ProcCode", String.class));
		log.debug("Service Name : " + endpoint.getServiceName());
		log.debug("Message Type : " + endpoint.getMessageType());
		log.debug("Log Time: " + endpoint.getLogTime());
		log.debug("Channel : " + getDeliveryChannel(exchange));
		log.debug("Message : " + exchange.getIn().getBody());
		log.debug("Client IP: " + exchange.getProperty("RemoteIP", String.class));
		MessageType messageType = new MessageType();
		
		messageType.setRequestId(getRequestId(exchange));
		messageType.setProcCode(exchange.getProperty("ProcCode", String.class));
		messageType.setMessageType(endpoint.getMessageType());
		messageType.setDeliveryChannel(getDeliveryChannel(exchange));
		messageType.setServiceName(endpoint.getServiceName());
		messageType.setLoggingTime(endpoint.getLogTime());
		messageType.setData(exchange.getIn().getBody(String.class));
		messageType.setClientIp(exchange.getProperty("RemoteIP", String.class));
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		log.debug("LogManager::JsonData" + gson.toJson(messageType));
		
		SyslogMessageTransmitter transmitter = new SyslogMessageTransmitter();
		transmitter.send(gson.toJson(messageType));
	}
	
	
	private String getDeliveryChannel(Exchange exchange) {
		if(exchange.getProperty("channelId", String.class) != null){
			return exchange.getProperty("channelId", String.class); 
		} else {
			return "NOTDEFIND";
		}
	}

	private String getRequestId(Exchange exchange) {
		if(exchange.getProperty("RequestID", String.class) != null) {
			return exchange.getProperty("RequestID", String.class);
		} else if(exchange.getProperty("RqUID", String.class) != null) {
			return exchange.getProperty("requestId", String.class);
		} else if(exchange.getProperty("RqUIDCardTrnLimit", String.class) != null) {
			return exchange.getProperty("requestId", String.class);
		} else {
			return exchange.getExchangeId();
		}
	}
}