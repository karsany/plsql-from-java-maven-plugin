package hu.karsany.plsqlfromjava.generator;

import hu.karsany.plsqlfromjava.Settings;
import hu.karsany.plsqlfromjava.dao.ServiceCatalogDAO;
import hu.karsany.plsqlfromjava.entity.Procedure;
import hu.karsany.plsqlfromjava.util.StringHelper;
import hu.karsany.plsqlfromjava.util.VelocityRenderer;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by fkarsany on 2014.03.13..
 */
public class ServiceGenerator {

    public void generate(Settings settings) {
        try {
            OracleDataSource oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(settings.getJdbcUrl());

            ServiceCatalogDAO serviceCatalogDAO = new ServiceCatalogDAO(oracleDataSource);

            List<String> servicePackages = serviceCatalogDAO.getServicePackages();

            for (String s : servicePackages) {

                List<Procedure> packageServiceProcedures = serviceCatalogDAO.getPackageServiceProcedures(s);

                VelocityRenderer velocityRenderer = new VelocityRenderer("serviceclass.vm");

                String className = StringHelper.toCamelCase(s);

                velocityRenderer.put("className", className);
                velocityRenderer.put("service", packageServiceProcedures);
                velocityRenderer.put("servicePackage", settings.getServicePackageName());
                velocityRenderer.put("modelPackage", settings.getEntityPackageName());
                velocityRenderer.put("utilPackage", settings.getUtilPackageName());
                velocityRenderer.put("procPackage", settings.getProcedurePackageName());
                FileUtils.writeStringToFile(new File(settings.getBasePath() + "/" + settings.getServicePackageName().replace(".", "/") + "/" + className + ".java"), velocityRenderer.render());

            }

        } catch (Exception e) {
        }
    }
}
