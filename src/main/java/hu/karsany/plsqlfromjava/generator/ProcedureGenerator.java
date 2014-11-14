package hu.karsany.plsqlfromjava.generator;


import hu.karsany.plsqlfromjava.Settings;
import hu.karsany.plsqlfromjava.dao.ProcedureCatalogDAO;
import hu.karsany.plsqlfromjava.entity.Procedure;
import hu.karsany.plsqlfromjava.entity.ProcedureArgument;
import hu.karsany.plsqlfromjava.util.StringHelper;
import hu.karsany.plsqlfromjava.util.VelocityRenderer;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * User: fkarsany
 * Date: 2013.11.13.
 */
public class ProcedureGenerator {

    public void generate(Settings settings) {
        try {
            OracleDataSource oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(settings.getJdbcUrl());

            ProcedureCatalogDAO procedureCatalogDAO = new ProcedureCatalogDAO(oracleDataSource);

            List<Procedure> allProcedures = procedureCatalogDAO.getAllProcedures("");

            for (Procedure p : allProcedures) {

                List<ProcedureArgument> procedureArguments = procedureCatalogDAO.getProcedureArguments(p.getObjectName(), p.getProcedureName(), p.getOverload());

                String className = StringHelper.toCamelCase(p.getObjectName() + "_" + p.getProcedureName() + "_" + p.getOverload());
                VelocityRenderer velocityRenderer = new VelocityRenderer("sppojo.vm");

                velocityRenderer.put("className", className);
                velocityRenderer.put("procedure", p);
                velocityRenderer.put("procedureName", p.getObjectName() + "." + p.getProcedureName());
                velocityRenderer.put("targetPackage", settings.getProcedurePackageName());
                velocityRenderer.put("par", procedureArguments);
                velocityRenderer.put("modelPackage", settings.getEntityPackageName());
                velocityRenderer.put("converterPackage", settings.getConverterPackageName());
                velocityRenderer.put("utilPackage", settings.getUtilPackageName());

                FileUtils.writeStringToFile(new File(settings.getBasePath() + "/" + settings.getProcedurePackageName().replace(".", "/") + "/" + className + ".java"), velocityRenderer.render());

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
