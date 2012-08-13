package com.jfinal.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;

/**
 * ActionReporter
 */
final class ActionReporter {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Report action before action invoking when the common request coming
	 */
	static final boolean reportCommonRequest(Controller controller, Action action) {
		String content_type = controller.getRequest().getContentType();
		if (content_type == null || content_type.toLowerCase().indexOf("multipart") == -1) {	// if (content_type == null || content_type.indexOf("multipart/form-data") == -1) {
			doReport(controller, action);
			return false;
		}
		return true;
	}
	
	/**
	 * Report action after action invoking when the multipart request coming
	 */
	static final void reportMultipartRequest(Controller controller, Action action) {
		doReport(controller, action);
	}
	
	private static final void doReport(Controller controller, Action action) {
		StringBuilder sb = new StringBuilder("\nJFinal action report -------- ").append(sdf.format(new Date())).append(" ------------------------------\n");
		Class<? extends Controller> cc = action.getControllerClass();
		sb.append("Controller  : ").append(cc.getName()).append(".(").append(cc.getSimpleName()).append(".java:")
		  .append(lineNum("publicvoid"+action.getMethodName()+"(){", fileName(cc))).append(")");
		sb.append("\nMethod      : ").append(action.getMethodName()).append("\n");
		
		String urlParas = controller.getPara();
		if (urlParas != null) {
			sb.append("UrlPara     : ").append(urlParas).append("\n");
		}
		
		Interceptor[] inters = action.getInterceptors();
		if (inters.length > 0) {
			sb.append("Interceptor : ");
			for (int i=0; i<inters.length; i++) {
				if (i > 0)
					sb.append("\n              ");
				Interceptor inter = inters[i];
				Class<? extends Interceptor> ic = inter.getClass();
				sb.append(ic.getName()).append(".(").append(ic.getSimpleName()).append(".java:")
				  .append(lineNum("publicvoidintercept", fileName(inter.getClass()))).append(")");
			}
			sb.append("\n");
		}
		
		// print all parameters
		HttpServletRequest request = controller.getRequest();
		@SuppressWarnings("unchecked")
		Enumeration<String> e = request.getParameterNames();
		if (e.hasMoreElements()) {
			sb.append("Parameter   : ");
			while (e.hasMoreElements()) {
				String name = e.nextElement();
				String[] values = request.getParameterValues(name);
				if (values.length == 1) {
					sb.append(name).append("=").append(values[0]);
				}
				else {
					sb.append(name).append("[]={");
					for (int i=0; i<values.length; i++) {
						if (i > 0)
							sb.append(",");
						sb.append(values[i]);
					}
					sb.append("}");
				}
				sb.append("  ");
			}
			sb.append("\n");
		}
		sb.append("--------------------------------------------------------------------------------\n");
		System.out.print(sb.toString());
	}

	private static String fileName(Class clazz) {
		String controllerFile = System.getProperty("user.dir")+File.separator+"src";
		for (String temp : clazz.getName().split("\\.")) {
			controllerFile = controllerFile+File.separator+temp;
		}
		return controllerFile+".java";
	}

	private static int lineNum(String codeFragment, String fileName) {
		List<String> lines = new ArrayList<>();
		int lineNum = 1;
		Path path = Paths.get(fileName);
		try {
			lines = Files.readAllLines(path, Charset.forName("utf-8"));
			for (int i = 0; i <lines.size(); i++) {
				String line = lines.get(i);
				if (codeFragment.equals(deleteWhitespace(line))) {
					lineNum=i+1;
					break;
				}
			}
		} catch(NoSuchFileException e1){
			// interceptor in jfinal.jar
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
		return lineNum;
	}
	
	 private static String deleteWhitespace(String str) {
	        if (isEmpty(str)) {
	            return str;
	        }
	        int sz = str.length();
	        char[] chs = new char[sz];
	        int count = 0;
	        for (int i = 0; i < sz; i++) {
	            if (!Character.isWhitespace(str.charAt(i))) {
	                chs[count++] = str.charAt(i);
	            }
	        }
	        if (count == sz) {
	            return str;
	        }
	        return new String(chs, 0, count);
	    }
	 
	 private static boolean isEmpty(CharSequence cs) {
	        return cs == null || cs.length() == 0;
	 }
}
