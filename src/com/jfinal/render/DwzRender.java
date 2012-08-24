package com.jfinal.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

/**
 * @author zhoueli
 */
public class DwzRender extends Render {
	private static final long serialVersionUID = -6389678574264885365L;

	private String statusCode = "200";
	private String tipsMessage = "";
	private String navTabId = "";
	private String callbackType = "";
	private String forwardUrl = "";

	public DwzRender(String tipsMessage, String navTabId, String callbackType) {
		this.tipsMessage = tipsMessage;
		this.navTabId = navTabId;
		this.callbackType = callbackType;
	}

	public DwzRender() {
	}

	public static DwzRender success() {
		DwzRender dwzRender = new DwzRender();
		dwzRender.setTipsMessage("操作成功");
		return dwzRender;
	}

	public static DwzRender success(String successMsg) {
		DwzRender dwzRender = new DwzRender();
		dwzRender.setTipsMessage(successMsg);
		return dwzRender;
	}

	public static DwzRender error() {
		DwzRender dwzRender = new DwzRender();
		dwzRender.statusCode = "300";
		dwzRender.tipsMessage = "操作失败";
		return dwzRender;
	}

	public static DwzRender error(String errorMsg) {
		DwzRender dwzRender = new DwzRender();
		dwzRender.statusCode = "300";
		dwzRender.tipsMessage = errorMsg;
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
		String dwz = "\"statusCode\":\"{0}\", \"message\":\"{1}\",\"navTabId\":\"{2}\", \"callbackType\":\"{3}\",\"forwardUrl\":\"{4}\"";
		dwz = "{" + MessageFormat.format(dwz, statusCode, tipsMessage, navTabId, callbackType, forwardUrl) + "}";
		try {
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setCharacterEncoding("utf-8");
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

	public String getTipsMessage() {
		return tipsMessage;
	}

	public void setTipsMessage(String tipsMessage) {
		this.tipsMessage = tipsMessage;
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

}
