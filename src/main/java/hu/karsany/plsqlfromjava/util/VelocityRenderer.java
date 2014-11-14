package hu.karsany.plsqlfromjava.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

public class VelocityRenderer {

    private VelocityContext context = new VelocityContext();
    private Template template;

    public VelocityRenderer(String velocityTemplateName) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init();
        template = velocityEngine.getTemplate(velocityTemplateName);
    }

    public void put(String key, Object value) {
        context.put(key, value);
    }

    public String render() {
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

}
