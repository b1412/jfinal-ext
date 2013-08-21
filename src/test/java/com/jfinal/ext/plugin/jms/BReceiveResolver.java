package com.jfinal.ext.plugin.jms;

import java.io.Serializable;

public class BReceiveResolver implements ReceiveResolver {

    @Override
    public void resolve(Serializable objectMessage) throws Exception {
        System.out.println("BReceiveResolver");
    }

}
