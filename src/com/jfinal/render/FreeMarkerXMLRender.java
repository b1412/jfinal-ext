package com.jfinal.render;


import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
@SuppressWarnings("serial")
/**
 *  <http://my.oschina.net/alvinte/blog/69030>
 * @author alvinte
 *
 */
public class FreeMarkerXMLRender extends FreeMarkerRender {
	private static final String contentType = "text/xml; charset=" + getEncoding();
	
	public FreeMarkerXMLRender(String view) {
		super(view);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void render() {
		response.setContentType(contentType);
		
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
			template.process(root, writer);		// Merge the data-model and the template
		} catch (Exception e) {
			throw new RenderException(e);
		}
		finally {
			try {writer.close();} catch (IOException e) {e.printStackTrace();}
		}
	}
}