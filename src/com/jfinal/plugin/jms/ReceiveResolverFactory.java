package com.jfinal.plugin.jms;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.log.Logger;

public class ReceiveResolverFactory  {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	public static final String RESOLVER_SUFFIX = ".resolver";

	private Map<Integer, ReceiveResolver> receiveResolverMap = new HashMap<Integer, ReceiveResolver>();
	private Map<String, Integer> messageTypeMap;

	private String resoruceLocation;
	private String typeFilter;

	public ReceiveResolverFactory(String resoruceLocation, String typeFilter) {
		this.resoruceLocation = resoruceLocation;
		this.typeFilter = typeFilter;
	}

	public ReceiveResolver createReceiveResolver(Integer messageType) {
		logger.debug(" receive messageType " + messageType);
		if (messageTypeMap ==null) {
			init();
		}
		return receiveResolverMap.get(messageType);
	}

	public Integer getMessageType(String key) {
		if (messageTypeMap == null) {
			init();
		}
		return messageTypeMap.get(key);
	}

	private synchronized void init() {
		messageTypeMap = new HashMap<String, Integer>();
		try {
			loadReceiveResolver();
		} catch (Exception e) {
			logger.debug("load ReceiveResolver error, it is defined in file" + resoruceLocation);
			e.printStackTrace();
		}
		logger.debug("resolvers in  "+typeFilter+" :"+ receiveResolverMap);
	}

	private void loadReceiveResolver() {
		for (String key : JmsConfig.keys()) {
			if (key.startsWith(typeFilter)&&!key.endsWith(RESOLVER_SUFFIX)) {
				Integer messageType = new Integer(JmsConfig.getVal(key));
				String messageResolver = JmsConfig.getVal(key + RESOLVER_SUFFIX);
				messageTypeMap.put(key, messageType);
				try {
					receiveResolverMap.put(messageType,
							(ReceiveResolver) Class.forName(messageResolver).newInstance());
				} catch (Exception e) {
					logger.debug("cant create " + messageResolver);
					e.printStackTrace();
				}
			}
		}
	}
}
