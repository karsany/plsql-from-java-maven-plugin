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
 * Created by fkarsany on 2014.03.05..
 */
public class ObjectTypeConverterGenerator {

    public void generate(Settings settings) {
        try {
            OracleDataSource oracleDataSource = new OracleDataSource();
            oracleDataSource.setURL(settings.getJdbcUrl());
            TypeCatalogDAO typeCatalogDAO = new TypeCatalogDAO(oracleDataSource);

            List<String> types = typeCatalogDAO.getTypeList();

            for (String typeName : types) {
                List<TypeAttribute> typeAttributes = typeCatalogDAO.getTypeAttributes(typeName);

                String className = StringHelper.toCamelCase(typeName);
                String smallClassName = StringHelper.toCamelCaseSmallBegin(typeName);
                VelocityRenderer velocityRenderer = new VelocityRenderer("typegenerator.vm");

                velocityRenderer.put("className", className);
                velocityRenderer.put("smallClassName", smallClassName);
                velocityRenderer.put("typeName", typeName);
                velocityRenderer.put("targetPackage", settings.getConverterPackageName());
                velocityRenderer.put("attrList", typeAttributes);
                velocityRenderer.put("oracleTypesPackage", settings.getEntityPackageName());

                FileUtils.writeStringToFile(new File(settings.getBasePath() + "/" + settings.getConverterPackageName().replace(".", "/") + "/" + className + "Converter.java"), velocityRenderer.render());


            }

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}
