package com.jfinal.ext.plugin.tablebind;

import com.jfinal.plugin.druid.DruidPlugin;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TestTableBind {
    private static AutoTableBindPlugin atbp;

    private static AutoTableBindPlugin atbp2;

    private static DruidPlugin druidPlugin;

    private static DruidPlugin druidPlugin2;

    @BeforeClass
    public static void init() {
        druidPlugin = new DruidPlugin("jdbc:mysql://127.0.0.1/test", "root", "root");
        druidPlugin.start();
        druidPlugin2 = new DruidPlugin("jdbc:mysql://127.0.0.1/test2", "root", "root");
        druidPlugin2.start();
    }

    @AfterClass
    public static void stop() {
        druidPlugin.stop();
    }

//    @Test
    public void testDefault() {
        atbp = new AutoTableBindPlugin(druidPlugin,SimpleNameStyles.LOWER)
                .addScanPackages("com.jfinal.ext.render")
                .addJars("modelInJar.jar").addExcludeClasses(TestModel.class);
        atbp.start();
    }

//    @Test
    public void testInJar() {
        atbp = new AutoTableBindPlugin(druidPlugin2,SimpleNameStyles.LOWER)
                .addScanPackages("com.jfinal.ext.render");
        atbp2 = new AutoTableBindPlugin("another",druidPlugin)
                .addScanPackages("test.com.jfinal","com.jfinal.ext.render");
        atbp.start();
        atbp2.start();
    }

    //@Test
    public void testMoudle() {
        atbp = new AutoTableBindPlugin(druidPlugin, ParamNameStyles.lowerModule("SNS_"));
        atbp.start();
    }

}
