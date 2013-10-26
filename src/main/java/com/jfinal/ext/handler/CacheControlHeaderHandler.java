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
package com.jfinal.ext.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class CacheControlHeaderHandler extends Handler {
	private static final int invalidExpirationTime = -1;
	/**缓存分钟数*/
	private final int expirationMinutes;
	public final int expires_hour = 60;
	public final int expires_day = expires_hour * 24;
	
	public final int expires_nocache = -1;
	public final int expires_week = expires_day * 7;

	public CacheControlHeaderHandler(int expirationMinutes) {
		this.expirationMinutes = expirationMinutes;
	}

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		if (!response.isCommitted()) {
			if (expirationMinutes == expires_nocache) {
				response.setHeader("Cache-Control", "no-cache");
			} else if (expirationMinutes > invalidExpirationTime) {
				response.setDateHeader("Expires", System.currentTimeMillis() + (expirationMinutes * 60 * 1000));
				response.setHeader("Cache-Control", "private");
			}
		}
		nextHandler.handle(target, request, response, isHandled);
	}
}
