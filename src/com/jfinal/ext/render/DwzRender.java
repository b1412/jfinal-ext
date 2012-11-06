package com.jfinal.ext.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class DwzRender extends Render {
	private static final String contentType = "text/html;charset=" + getEncoding();
	private String statusCode = "200";
	private String message = "";
	private String navTabId = "";
	private String callbackType = "";
	private String forwardUrl = "";
	private String rel="";
	private String confirmMsg="";

	public DwzRender(String message, String navTabId, String callbackType) {
		this.message = message;
		this.navTabId = navTabId;
		this.callbackType = callbackType;
	}

	public DwzRender() {
	}

	public static DwzRender success() {
		DwzRender dwzRender = new DwzRender();
		dwzRender.setMessage("操作成功");
		return dwzRender;
	}

	public static DwzRender success(String successMsg) {
		DwzRender dwzRender = new DwzRender();
		dwzRender.setMessage(successMsg);
		return dwzRender;
	}

	public static DwzRender error() {
		DwzRender dwzRender = new DwzRender();
		dwzRender.statusCode = "300";
		dwzRender.message = "操作失败";
		return dwzRender;
	}

	public static DwzRender error(String errorMsg) {
		DwzRender dwzRender = new DwzRender();
		dwzRender.statusCode = "300";
		dwzRender.message = errorMsg;
		return dwzRender;
	}


	public static Render refresh(String refreshNavTabId) {
		DwzRender dwzRender = new DwzRender();
		dwzRender.navTabId = refreshNavTabId;
		return dwzRender;
	}

	public static DwzRender closeCurrentAndRefresh(String refreshNavTabId) {
		DwzRender dwzRender = new DwzRender();
		dwzRender.navTabId = refreshNavTabId;
		dwzRender.callbackType = "closeCurrent";
		return dwzRender;
	}

	@Override
	public void render() {
		PrintWriter writer = null;
		String dwz = "\"statusCode\":\"{0}\",\"message\":\"{1}\",\"navTabId\":\"{2}\",\"rel\":\"{3}\",\"callbackType\":\"{4}\",\"forwardUrl\":\"{5}\",\"confirmMsg\":\"{6}\"";
		dwz = "{\n" + MessageFormat.format(dwz, statusCode, message, navTabId, rel, callbackType, forwardUrl , confirmMsg) + "\n}";
		try {
			response.setHeader("Pragma", "no-cache");	// HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expires", 0);
			response.setContentType(contentType);
			writer = response.getWriter();
			writer.write(dwz);
			writer.flush();
		} catch (IOException e) {
			throw new RenderException(e);
		} finally {
			writer.close();
		}
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNavTabId() {
		return navTabId;
	}

	public void setNavTabId(String navTabId) {
		this.navTabId = navTabId;
	}

	public String getCallbackType() {
		return callbackType;
	}

	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getConfirmMsg() {
		return confirmMsg;
	}

	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}

}
