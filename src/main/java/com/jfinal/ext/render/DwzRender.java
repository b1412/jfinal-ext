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
package com.jfinal.ext.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class DwzRender extends Render {
    private static final String CONTENT_TYPE = "text/html;charset=" + getEncoding();
    public static DwzRender closeCurrentAndRefresh(String refreshNavTabId) {
        DwzRender dwzRender = new DwzRender();
        dwzRender.navTabId = refreshNavTabId;
        dwzRender.callbackType = "closeCurrent";
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
    public static DwzRender success() {
        DwzRender dwzRender = new DwzRender();
        dwzRender.message("操作成功");
        return dwzRender;
    }
    public static DwzRender success(String successMsg) {
        DwzRender dwzRender = new DwzRender();
        dwzRender.message(successMsg);
        return dwzRender;
    }
    private String callbackType = "";

    private String confirmMsg = "";

    private String forwardUrl = "";

    private String message = "";

    private String navTabId = "";

    private String rel = "";

    private String statusCode = "200";

    public DwzRender() {
    }

    public DwzRender(String message, String navTabId, String callbackType) {
        this.message = message;
        this.navTabId = navTabId;
        this.callbackType = callbackType;
    }

    public DwzRender callbackType(String callbackType) {
        this.callbackType = callbackType;
        return this;
    }

    public DwzRender confirmMsg(String confirmMsg) {
        this.confirmMsg = confirmMsg;
        return this;
    }

    public DwzRender forwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
        return this;
    }

    public DwzRender message(String message) {
        this.message = message;
        return this;
    }

    public DwzRender navTabId(String navTabId) {
        this.navTabId = navTabId;
        return this;
    }

    public DwzRender rel(String rel) {
        this.rel = rel;
        return this;
    }

    @Override
    public void render() {
        PrintWriter writer = null;
        String dwz = "\"statusCode\":\"{0}\",\"message\":\"{1}\",\"navTabId\":\"{2}\",\"rel\":\"{3}\","
                + "\"callbackType\":\"{4}\",\"forwardUrl\":\"{5}\",\"confirmMsg\":\"{6}\"";
        dwz = "{\n"
                + MessageFormat.format(dwz, statusCode, message, navTabId, rel, callbackType, forwardUrl, confirmMsg)
                + "\n}";
        try {
            response.setHeader("Pragma", "no-cache"); // HTTP/1.0 caches might not implement Cache-Control and might
                                                      // only implement Pragma:
                                                      // no-cache
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType(CONTENT_TYPE);
            writer = response.getWriter();
            writer.write(dwz);
            writer.flush();
        } catch (IOException e) {
            throw new RenderException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public DwzRender statusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

}
