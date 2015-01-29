/**
 * Copyright (c) 2010-2013, Copyright (c) 2010-2013,Trafree Travel Technology(HK) Co.,Limited  All Rights Reserved.
 */

package com.jfinal.ext.kit;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * 
 * @author kid create 2013-4-13
 */
public class TestClassSearch {
//    @Test
    public void findInClasspathAndJars() {

        List<Class<? extends Model>> models2 = ClassSearcher.of(Model.class).search();
        for (Class<? extends Model> model : models2) {
            System.out.println(model);
        }
    }

    //@Test
    public void testFindInClasspath() {
        List<Class<? extends Model>> models = ClassSearcher.of(Model.class).inJars("modelInJar.jar").search();
        for (Class<? extends Model> model : models) {
            System.out.println(model);
        }
    }
}
