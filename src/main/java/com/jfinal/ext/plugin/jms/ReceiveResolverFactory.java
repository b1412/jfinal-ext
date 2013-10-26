/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.plugin.jms;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.log.Logger;

public class ReceiveResolverFactory {

    public static final String RESOLVER_SUFFIX = ".resolver";

    protected final Logger logger = Logger.getLogger(getClass());

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
        if (messageTypeMap == null) {
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
            logger.error("load ReceiveResolver error, it is defined in file" + resoruceLocation,e);
        }
        logger.debug("resolvers in  " + typeFilter + " :" + receiveResolverMap);
    }

    private void loadReceiveResolver() {
        for (String key : JmsConfig.keys()) {
            if (key.startsWith(typeFilter) && !key.endsWith(RESOLVER_SUFFIX)) {
                Integer messageType = new Integer(JmsConfig.getStr(key));
                String messageResolver = JmsConfig.getStr(key + RESOLVER_SUFFIX);
                messageTypeMap.put(key, messageType);
                try {
                    receiveResolverMap.put(messageType, (ReceiveResolver) Class.forName(messageResolver).newInstance());
                } catch (Exception e) {
                    logger.error("cant create " + messageResolver,e);
                }
            }
        }
    }
}
