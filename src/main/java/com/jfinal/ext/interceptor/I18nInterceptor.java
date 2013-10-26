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
package com.jfinal.ext.interceptor;

import java.util.Locale;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StringKit;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;

public class I18nInterceptor implements Interceptor {
    private String defaultLanguage = "zh";
    private String defaultCountry = "CN";
    private String languagePara = "language";
    private String countryPara = "country";
    private String localePara = "locale";
    private String skipFlagPara = "skipi18n";

    @Override
    public void intercept(ActionInvocation ai) {
        Controller controller = ai.getController();
        String language = controller.getAttr(languagePara);
        if (StringKit.isBlank(language)) {
            language = controller.getPara(languagePara, defaultLanguage);
        }
        String country = controller.getAttr(countryPara);
        if (StringKit.isBlank(country)) {
            country = controller.getPara(countryPara, defaultCountry);
        }
        Locale locale = new Locale(language, country);
        controller.setLocaleToCookie(locale);
        controller.setAttr(localePara, locale);
        ai.invoke();
        if (Boolean.TRUE.equals(ai.getController().getAttr(skipFlagPara))) {
            return;
        }
        Render render = controller.getRender();
        if (render == null) {
            render = RenderFactory.me().getDefaultRender(ai.getMethodName());
        }
        String view = render.getView();

        String prefix = getPrefix(country, language);

        if (view.startsWith("/")) {
            view = "/" + prefix + "/" + view.substring(1, view.length());
        } else {
            view = "/" + prefix + ai.getViewPath() + view;
        }
    }

    private String getPrefix(String country, String language) {
        String prefix = language;
        if (StringKit.notBlank(country)) {
            prefix = language + "_" + country;
        }
        //TODO
        // ScriptEngineManager sem = new ScriptEngineManager();
        // ScriptEngine se = sem.getEngineByName("javascript");
        // try {
        // se.put("country",country);
        // se.put("language", language);
        // prefix = (String) se.eval(exps);
        // } catch (ScriptException e) {
        // Throwables.propagate(e);
        // }
        return prefix;
    }

    public I18nInterceptor defaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
        return this;
    }

    public I18nInterceptor defaultCountry(String defaultCountry) {
        this.defaultCountry = defaultCountry;
        return this;
    }

    public I18nInterceptor languagePara(String languagePara) {
        this.languagePara = languagePara;
        return this;
    }

    public I18nInterceptor countryPara(String countryPara) {
        this.countryPara = countryPara;
        return this;
    }

    public I18nInterceptor localePara(String localePara) {
        this.localePara = localePara;
        return this;
    }

    public I18nInterceptor skipFlagPara(String skipFlagPara) {
        this.skipFlagPara = skipFlagPara;
        return this;
    }

}
