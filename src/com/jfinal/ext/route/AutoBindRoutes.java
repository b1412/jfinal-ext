package com.jfinal.ext.route;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;

public class AutoBindRoutes extends Routes {

    protected final Logger logger = Logger.getLogger(getClass());

    private List<Class<? extends Controller>> excludeClasses = Lists.newArrayList();

    private List<String> includeJars = Lists.newArrayList();

    private boolean includeAllJarsInLib;

    private boolean autoScan = true;

    private String suffix = "Controller";

    public AutoBindRoutes() {
    }

    public AutoBindRoutes(boolean autoScan) {
        this.autoScan = autoScan;
    }

    public boolean setIncludeAllJarsInLib() {
        return includeAllJarsInLib;
    }

    public AutoBindRoutes addJar(String jarName) {
        if (StringKit.notBlank(jarName)) {
            includeJars.add(jarName);
        }
        return this;
    }

    public AutoBindRoutes addJars(String jarNames) {
        if (StringKit.notBlank(jarNames)) {
            addJars(jarNames.split(","));
        }
        return this;
    }

    public AutoBindRoutes addJars(String[] jarsName) {
        includeJars.addAll(Arrays.asList(jarsName));
        return this;
    }

    public AutoBindRoutes addJars(List<String> jarsName) {
        includeJars.addAll(jarsName);
        return this;
    }

    public AutoBindRoutes addExcludeClass(Class<? extends Controller> clazz) {
        if (clazz != null) {
            excludeClasses.add(clazz);
        }
        return this;
    }

    public AutoBindRoutes addExcludeClasses(Class<? extends Controller>[] clazzes) {
        excludeClasses.addAll(Arrays.asList(clazzes));
        return this;
    }

    public AutoBindRoutes addExcludeClasses(List<Class<? extends Controller>> clazzes) {
        excludeClasses.addAll(clazzes);
        return this;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void config() {
        List<Class<? extends Controller>> controllerClasses = ClassSearcher.findInClasspathAndJars(Controller.class, includeJars);
        ControllerBind controllerBind = null;
        for (Class controller : controllerClasses) {
            if (excludeClasses.contains(controller)) {
                continue;
            }
            controllerBind = (ControllerBind) controller.getAnnotation(ControllerBind.class);
            if (controllerBind == null) {
                if (!autoScan) {
                    continue;
                }
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

    private String controllerKey(Class<Controller> clazz) {
        Preconditions.checkArgument(!clazz.getSimpleName().endsWith(suffix),
                " does not has a ControllerBind annotation and it,s name is not end with " + suffix);
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

    public List<String> getIncludeJars() {
        return includeJars;
    }

    public void setIncludeJars(List<String> includeJars) {
        this.includeJars = includeJars;
    }

    public boolean isIncludeAllJarsInLib() {
        return includeAllJarsInLib;
    }

    public void setIncludeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
    }


    public void setAutoScan(boolean autoScan) {
        this.autoScan = autoScan;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


}
