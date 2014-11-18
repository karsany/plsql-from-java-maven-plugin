package hu.karsany.plsqlfromjava.dao;

import hu.karsany.plsqlfromjava.entity.Procedure;
import hu.karsany.plsqlfromjava.entity.ProcedureArgument;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * User: fkarsany
 * Date: 2013.11.18.
 */
public class ProcedureCatalogDAO {

    private JdbcTemplate jdbcTemplate;

    public ProcedureCatalogDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String getProcedureComment(String packageName, String procedureName) throws SQLException {
        final String getProcComment = "" +
                "DECLARE\n" +
                "\n" +
                "  p_package_name VARCHAR2(30) := upper(:package_name);\n" +
                "  p_proc_name    VARCHAR2(30) := upper(:procedure_name);\n" +
                "\n" +
                "  v_min_line PLS_INTEGER;\n" +
                "  v_max_line PLS_INTEGER;\n" +
                "\n" +
                "  v_comment CLOB;\n" +
                "BEGIN\n" +
                "  SELECT line - 1\n" +
                "  INTO   v_max_line\n" +
                "  FROM   all_source t\n" +
                "  WHERE  t.name = p_package_name\n" +
                "  AND    ((upper(t.text) LIKE '%PROCEDURE%' || p_proc_name || '%') OR\n" +
                "        (upper(t.text) LIKE '%FUNCTION%' || p_proc_name || '%'))\n" +
                "  AND    t.type = 'PACKAGE';\n" +
                "\n" +
                "  SELECT MAX(line)\n" +
                "  INTO   v_min_line\n" +
                "  FROM   all_source t\n" +
                "  WHERE  t.name = p_package_name\n" +
                "  AND    t.type = 'PACKAGE'\n" +
                "  AND    t.line <= v_max_line\n" +
                "  AND    TRIM(t.text) LIKE '/**%';\n" +
                "\n" +
                "  SELECT listagg(t.text) within GROUP(ORDER BY t.line)\n" +
                "  INTO   v_comment\n" +
                "  FROM   all_source t\n" +
                "  WHERE  t.name = p_package_name\n" +
                "  AND    t.type = 'PACKAGE'\n" +
                "  AND    t.line BETWEEN v_min_line AND v_max_line;\n" +
                "\n" +
                "  :proc_comment := v_comment;\n" +
                "\n" +
                "END;";

        Connection connection = jdbcTemplate.getDataSource().getConnection();

        CallableStatement callableStatement = connection.prepareCall(getProcComment);

        callableStatement.setString(1, packageName);
        callableStatement.setString(2, procedureName);
        callableStatement.registerOutParameter(3, OracleTypes.CLOB);
        callableStatement.execute();
        String ret = null;
        ret = callableStatement.getString(3);
        connection.close();

        return ret;
    }

    public List<Procedure> getAllProcedures(String packagePostfix) {

        String locPackagePostfix = packagePostfix;

        if (locPackagePostfix == null || locPackagePostfix.length() == 0) {
            locPackagePostfix = "%";
        } else {
            locPackagePostfix = "%" + locPackagePostfix.toUpperCase();
        }

        List<Procedure> procedures = jdbcTemplate.query(
                "Select object_name\n" +
                        "      ,procedure_name\n" +
                        "      ,overload\n" +
                        "      ,(Select Count(*)\n" +
                        "         From user_arguments a\n" +
                        "        Where a.object_name = t.procedure_name\n" +
                        "          And a.package_name = t.object_name\n" +
                        "          And nvl(a.overload,'##NVL##') = nvl(t.overload,'##NVL##')\n" +
                        "          And a.argument_name Is Null\n" +
                        "          And a.data_level = 0" +
                        "          And a.data_type is not null) proc_or_func\n" +
                        "  From user_procedures t\n" +
                        " Where procedure_name Is Not Null\n" +
                        "   And object_type = 'PACKAGE'" +
                        "   And object_name LIKE ?",
                new RowMapper<Procedure>() {
                    @Override
                    public Procedure mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Procedure(
                                resultSet.getString("object_name"),
                                resultSet.getString("procedure_name"),
                                resultSet.getString("overload") == null ? "" : resultSet.getString("overload"),
                                resultSet.getInt("proc_or_func") == 0 ? "PROCEDURE" : "FUNCTION"
                        );
                    }
                }, locPackagePostfix
        );

        return procedures;
    }

    public List<ProcedureArgument> getProcedureArguments(String packageName, String procedureName, String overLoadNo) {
        List<ProcedureArgument> procedureArguments = jdbcTemplate.query(
                "          Select argument_name, data_type, type_name, defaulted, in_out, rownum sequen\n" +
                        "        From user_arguments t\n" +
                        "       Where nvl(t.package_name, '###') = nvl(upper(?), '###')\n" +
                        "         And t.object_name = upper(?)\n" +
                        "         And nvl(t.overload, '###') = nvl(?, '###')\n" +
                        "         And t.data_level = 0\n" +
                        "         And not(pls_type is null and argument_name is null and data_type is null)" +
                        "       Order By t.sequence\n",
                new Object[]{packageName, procedureName, overLoadNo}, new RowMapper<ProcedureArgument>() {
                    @Override
                    public ProcedureArgument mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new ProcedureArgument(
                                resultSet.getString("argument_name"),
                                resultSet.getString("data_type"),
                                resultSet.getString("type_name"),
                                resultSet.getString("defaulted"),
                                resultSet.getString("in_out").contains("IN") ? true : false,
                                resultSet.getString("in_out").contains("OUT") ? true : false,
                                resultSet.getInt("sequen")
                        );
                    }
                }
        );
        return procedureArguments;
    }

}
