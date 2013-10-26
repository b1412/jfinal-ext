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

import javax.jms.Message;
import javax.jms.ObjectMessage;

import com.jfinal.log.Logger;

public class JmsReceive implements IMessageHandler {
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    private ReceiveResolverFactory receiveResolverFactory;

    public JmsReceive(ReceiveResolverFactory receiveResolverFactory) {
        this.receiveResolverFactory = receiveResolverFactory;
    }

    @Override
    public void handleMessage(Message message) {
        try {
            int messageType = message.getIntProperty(JMSConstants.JMS_MESSAGE_TYPE);
            ObjectMessage objMsg = (ObjectMessage) message;
            logger.debug("msgType " + messageType + " objMsg :" + objMsg);
            ReceiveResolver resolver = this.receiveResolverFactory.createReceiveResolver(new Integer(messageType));
            if (resolver == null) {
                logger.error("cant find  ReceiveResolver with messageType = " + messageType);
                return;
            }
            resolver.resolve(objMsg.getObject());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
}
