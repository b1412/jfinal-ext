/**
 * Copyright (c) 2010-2013, Copyright (c) 2010-2013,Trafree Travel Technology(HK) Co.,Limited  All Rights Reserved.
 */

package com.jfinal.ext.kit;

import java.util.List;

import org.junit.Test;

import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * @author kid create 2013-4-13
 */
public class TestClassSearch {
    @Test
    public void testFindInClasspath() {
        List<Class<? extends Model>> models = ClassSearcher.me.search(Model.class);
        for (Class<? extends Model> model : models) {
            System.out.println(model);
        }
    }

    @Test
    public void findInClasspathAndJars() {
        List<Class<? extends Model>> models2 = ClassSearcher.me.addJar("modelInJar.jar").search(Model.class);
        for (Class<? extends Model> model : models2) {
            System.out.println(model);
        }
    }
}
