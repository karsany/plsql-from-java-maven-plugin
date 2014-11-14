package hu.karsany.plsqlfromjava.generator;

import hu.karsany.plsqlfromjava.Settings;
import hu.karsany.plsqlfromjava.util.VelocityRenderer;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by fkarsany on 2014.03.07..
 */
public class UtilsGenerator {

    public void generate(Settings settings) {
        try {
            VelocityRenderer velocityRenderer = new VelocityRenderer("StoredProcedure.vm");
            velocityRenderer.put("utilPackage", settings.getUtilPackageName());
            FileUtils.writeStringToFile(new File(settings.getBasePath() + "/" + settings.getUtilPackageName().replace(".", "/") + "/StoredProcedure.java"), velocityRenderer.render());

            velocityRenderer = new VelocityRenderer("DatabaseSessionManager.vm");
            velocityRenderer.put("utilPackage", settings.getUtilPackageName());
            FileUtils.writeStringToFile(new File(settings.getBasePath() + "/" + settings.getUtilPackageName().replace(".", "/") + "/DatabaseSessionManager.java"), velocityRenderer.render());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
