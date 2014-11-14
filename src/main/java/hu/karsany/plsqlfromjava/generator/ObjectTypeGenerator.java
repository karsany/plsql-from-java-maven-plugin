package hu.karsany.plsqlfromjava.generator;

import hu.karsany.plsqlfromjava.Settings;
import hu.karsany.plsqlfromjava.dao.TypeCatalogDAO;
import hu.karsany.plsqlfromjava.entity.TypeAttribute;
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
public class ObjectTypeGenerator {

    public void generate(Settings settings) {
        try {
            OracleDataSource oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(settings.getJdbcUrl());
            TypeCatalogDAO typeCatalogDAO = new TypeCatalogDAO(oracleDataSource);

            List<String> types = typeCatalogDAO.getTypeList();

            for (String typeName : types) {
                List<TypeAttribute> typeAttributes = typeCatalogDAO.getTypeAttributes(typeName);

                String className = StringHelper.toCamelCase(typeName);
                VelocityRenderer velocityRenderer = new VelocityRenderer("typepojo.vm");

                velocityRenderer.put("className", className);
                velocityRenderer.put("targetPackage", settings.getEntityPackageName());
                velocityRenderer.put("attrList", typeAttributes);

                FileUtils.writeStringToFile(new File(settings.getBasePath() + "/" + settings.getEntityPackageName().replace(".", "/") + "/" + className + ".java"), velocityRenderer.render());

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
