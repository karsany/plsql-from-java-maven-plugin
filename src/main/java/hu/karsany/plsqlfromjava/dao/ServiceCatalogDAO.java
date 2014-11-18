package hu.karsany.plsqlfromjava.dao;

import hu.karsany.plsqlfromjava.entity.Procedure;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by fkarsany on 2014.03.13..
 */
public class ServiceCatalogDAO {

    private JdbcTemplate jdbcTemplate;

    public ServiceCatalogDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<String> getServicePackages() {
        final String query = "SELECT o.object_name\n" +
                "FROM   user_objects o\n" +
                "WHERE  o.object_type = 'PACKAGE'";

        List<String> ret = jdbcTemplate.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("object_name");
            }
        });

        return ret;
    }

    public List<Procedure> getPackageServiceProcedures(String packageName) {

        final String query = "SELECT object_name,\n" +
                "       procedure_name,\n" +
                "       overload,\n" +
                "       (SELECT COUNT(*)\n" +
                "        FROM   user_arguments a\n" +
                "        WHERE  a.object_name = t.procedure_name\n" +
                "        AND    a.package_name = t.object_name\n" +
                "        AND    nvl(a.overload, 0) = nvl(t.overload, 0)\n" +
                "        AND    a.argument_name IS NULL\n" +
                "        AND    a.data_level = 0" +
                "        And a.data_type is not null) proc_or_func\n" +
                "FROM   user_procedures t\n" +
                "WHERE  procedure_name IS NOT NULL\n" +
                "AND    object_type = 'PACKAGE'\n" +
                "AND    upper(object_name) = upper(:1)\n";

        final ProcedureCatalogDAO procedureCatalogDAO = new ProcedureCatalogDAO(jdbcTemplate.getDataSource());


        return jdbcTemplate.query(query, new RowMapper<Procedure>() {
            @Override
            public Procedure mapRow(ResultSet resultSet, int i) throws SQLException {


                return new Procedure(
                        resultSet.getString("object_name"),
                        resultSet.getString("procedure_name"),
                        resultSet.getString("overload") == null ? "" : resultSet.getString("overload"),
                        resultSet.getInt("proc_or_func") == 0 ? "PROCEDURE" : "FUNCTION",
                        procedureCatalogDAO.getProcedureArguments(
                                resultSet.getString("object_name"),
                                resultSet.getString("procedure_name"),
                                resultSet.getString("overload") == null ? "" : resultSet.getString("overload"))
                        //procedureCatalogDAO.getProcedureComment(resultSet.getString("object_name"),
                        //      resultSet.getString("procedure_name"))
                );
            }
        }, packageName);
    }


}
