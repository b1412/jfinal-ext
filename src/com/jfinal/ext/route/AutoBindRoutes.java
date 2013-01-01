package com.jfinal.ext.route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;

public class AutoBindRoutes extends Routes {

    protected final Logger                    logger              = Logger.getLogger(getClass());

    private List<Class<? extends Controller>> excludeClasses      = new ArrayList<Class<? extends Controller>>();

    private List<String>                      includeJars         = new ArrayList<String>();

    private boolean                           includeAllJarsInLib = false;

    private boolean                           autoScan            = true;

    private String                            suffix              = "Controller";

    public AutoBindRoutes(){
    }

    public AutoBindRoutes(boolean autoScan){
        this.autoScan = autoScan;
    }

    public boolean setIncludeAllJarsInLib() {
        return includeAllJarsInLib;
    }

    public void addJar(String jarName) {
        if (StringKit.isBlank(jarName)) {
            return;
        }
        includeJars.add(jarName);
    }

    public void addJars(String jarNames) {
        if (StringKit.isBlank(jarNames)) {
            return;
        }
        addJars(jarNames.split(","));
    }

    public void addJars(String[] jarsName) {
        includeJars.addAll(Arrays.asList(jarsName));
    }

    public void addJars(List<String> jarsName) {
        includeJars.addAll(jarsName);
    }

    public void addExcludeClass(Class<? extends Controller> clazz) {
        if (clazz == null) {
            return;
        }
        excludeClasses.add(clazz);
    }

    public void addExcludeClasses(Class<? extends Controller>[] clazzes) {
        excludeClasses.addAll(Arrays.asList(clazzes));
    }

    public void addExcludeClasses(List<Class<? extends Controller>> clazzes) {
        excludeClasses.addAll(clazzes);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void config() {
        List<Class> controllerClasses = ClassSearcher.findInClasspathAndJars(Controller.class, includeJars);
        ControllerBind controllerBind = null;
        for (Class controller : controllerClasses) {
            if (excludeClasses.contains(controller)) {
                continue;
            }
            controllerBind = (ControllerBind) controller.getAnnotation(ControllerBind.class);
            if (controllerBind == null) {
                if (autoScan == false) continue;
                this.add(controllerKey(controller), controller);
                logger.debug("routes.add(" + controllerKey(controller) + ", " + controller.getName() + ")");
            } else if (StringKit.isBlank(controllerBind.viewPath())) {
                this.add(controllerBind.controllerKey(), controller);
                logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller.getName() + ")");
            } else {
                this.add(controllerBind.controllerKey(), controller, controllerBind.viewPath());
                logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller + ","
                             + controllerBind.viewPath() + ")");
            }
        }
    }

    private String controllerKey(Class clazz) {
        if (!clazz.getSimpleName().endsWith(suffix)) {
            throw new RuntimeException(clazz
                                       + " does not has a ControllerBind annotation and it,s name is not end with "
                                       + suffix);
        }
        String controllerKey = "/" + StringKit.firstCharToLowerCase(clazz.getSimpleName());
        controllerKey = controllerKey.substring(0, controllerKey.indexOf("Controller"));
        return controllerKey;
    }

    public List<Class<? extends Controller>> getExcludeClasses() {
        return excludeClasses;
    }

    public void setExcludeClasses(List<Class<? extends Controller>> excludeClasses) {
        this.excludeClasses = excludeClasses;
    }

    public void setIncludeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
    }

    public void setAutoScan(boolean autoScan) {
        this.autoScan = autoScan;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
