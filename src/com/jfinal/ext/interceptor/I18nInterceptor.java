package com.jfinal.ext.interceptor;

import java.util.Locale;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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
    private String excludeViewRegex;
//    private String exps = "language";
//    private Pattern pattern;

    @Override
    public void intercept(ActionInvocation ai) {
        //TODO 如何过滤不需要国际化的action?
//        if (StringKit.notBlank(excludeViewRegex)) {
//            pattern = Pattern.compile(excludeViewRegex);
//        }
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
        Render render = controller.getRender();
        if (render == null){
            render = RenderFactory.me().getDefaultRender(ai.getMethodName());
        }
        String view = render.getView();
        if (StringKit.isBlank(view) 
//                || (pattern != null && pattern.matcher(view).matches())
           ) {
            return;
        }
        String prefix = getPrefix(country,language);
        if(StringKit.isBlank(prefix)){
        	prefix = language;
        }
        if (view.startsWith("/")) {
            view = view.substring(1, view.length());
        }
        controller.render("/" + prefix + ai.getViewPath() + view);
//        System.out.println("/" + prefix + ai.getViewPath() + view);
    }

    private String getPrefix(String country,String language) {
        String prefix = language;
        if(StringKit.notBlank(country)){
            prefix = language+"_"+country;
        }
//        ScriptEngineManager sem = new ScriptEngineManager();   
//        ScriptEngine se = sem.getEngineByName("javascript");   
//        try {
//        	se.put("country",country);   
//        	se.put("language", language);   
//			prefix = (String) se.eval(exps);
//		} catch (ScriptException e) {
//			Throwables.propagate(e);
//		}
		return prefix;
	}

	public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;

    }

    public String getDefaultCountry() {
        return defaultCountry;
    }

    public void setDefaultCountry(String defaultCountry) {
        this.defaultCountry = defaultCountry;
    }

    public String getLanguagePara() {
        return languagePara;
    }

    public void setLanguagePara(String languagePara) {
        this.languagePara = languagePara;
    }

    public String getCountryPara() {
        return countryPara;
    }

    public void setCountryPara(String countryPara) {
        this.countryPara = countryPara;
    }

    public String getLocalePara() {
        return localePara;
    }

    public void setLocalePara(String localePara) {
        this.localePara = localePara;
    }

    public String getExcludeViewRegex() {
        return excludeViewRegex;
    }

    public void setExcludeViewRegex(String excludeViewRegex) {
        this.excludeViewRegex = excludeViewRegex;
    }
    public static void main(String[] args) throws ScriptException {
    	ScriptEngineManager sem = new ScriptEngineManager();   
        ScriptEngine se = sem.getEngineByName("javascript");   
        String exps = "country.toUpperCase()+'_'+language;";
        se.put("country","en");   
        se.put("language", "us");   
		System.out.println(se.eval(exps));   
	}
    
}
