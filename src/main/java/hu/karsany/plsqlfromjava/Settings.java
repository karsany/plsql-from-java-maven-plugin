package hu.karsany.plsqlfromjava;

/**
 * Created by fkarsany on 2014.11.14..
 */
public class Settings {
    private String jdbcUrl;
    private String basePath;

    private String entityPackageName;
    private String converterPackageName;
    private String utilPackageName;
    private String procedurePackageName;
    private String servicePackageName;

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setConverterPackageName(String converterPackageName) {
        this.converterPackageName = converterPackageName;
    }

    public String getConverterPackageName() {
        return converterPackageName;
    }

    public void setUtilPackageName(String utilPackageName) {
        this.utilPackageName = utilPackageName;
    }

    public String getUtilPackageName() {
        return utilPackageName;
    }

    public void setProcedurePackageName(String procedurePackageName) {
        this.procedurePackageName = procedurePackageName;
    }

    public String getProcedurePackageName() {
        return procedurePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }
}
