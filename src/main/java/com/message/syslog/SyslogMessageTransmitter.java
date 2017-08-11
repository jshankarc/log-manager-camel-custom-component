package com.message.syslog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.MessageFormat;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.sender.TcpSyslogMessageSender;

public class SyslogMessageTransmitter {

	private static final Logger log = LoggerFactory.getLogger(SyslogMessageTransmitter.class);
	
	private static String ipAddress;
	private static int port;
	
	public void send(final String message) {
		
		TcpSyslogMessageSender messageSender = new TcpSyslogMessageSender();
		messageSender.setDefaultFacility(Facility.LOCAL0);
		messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
		messageSender.setSyslogServerHostname(ipAddress);
		messageSender.setSyslogServerPort(port);
		messageSender.setMessageFormat(MessageFormat.RFC_3164);
		messageSender.setSsl(false);
		try {
			messageSender.sendMessage(message);
		} catch (Exception exception) {
			log.error("LogManager::Exception while connecting to Rsyslog" + exception.getMessage());
		}
		log.debug("SyslogMessageTransmitter::MessagePushed" + messageSender.toString());
	}

	public void initializeConfigValues(){
		log.info("logmanager::bean-initalizer" + ipAddress + ":" + port);
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}