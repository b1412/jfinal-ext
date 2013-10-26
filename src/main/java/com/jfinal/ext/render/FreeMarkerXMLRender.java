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
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Throwables;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.RenderException;

import freemarker.template.Template;

@SuppressWarnings("serial")
/**
 *  <http://my.oschina.net/alvinte/blog/69030>
 * @author alvinte
 *
 */
public class FreeMarkerXMLRender extends FreeMarkerRender {
    private static final String CONTENT_TYPE = "text/xml; charset=" + getEncoding();

    public FreeMarkerXMLRender(String view) {
        super(view);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void render() {
        response.setContentType(CONTENT_TYPE);

        Enumeration<String> attrs = request.getAttributeNames();
        Map root = new HashMap();
        while (attrs.hasMoreElements()) {
            String attrName = attrs.nextElement();
            root.put(attrName, request.getAttribute(attrName));
        }

        Writer writer = null;
        try {
            writer = response.getWriter();
            Template template = getConfiguration().getTemplate(view);
            template.process(root, writer); // Merge the data-model and the template
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
    }
}
