package hu.karsany.plsqlfromjava.dao;

import hu.karsany.plsqlfromjava.entity.TabColumn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TableCatalogDAO {

    private JdbcTemplate jdbcTemplate;

    public TableCatalogDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<TabColumn> getColumnList(String tableName) {
        List<TabColumn> strings = jdbcTemplate.query(
                "SELECT column_name, data_type, column_id, nvl(data_scale,-1) data_scale " +
                        "FROM user_tab_columns " +
                        "WHERE UPPER(table_name) = ? ORDER BY column_id ASC",
                new Object[]{tableName.toUpperCase()}, new RowMapper<TabColumn>() {
                    @Override
                    public TabColumn mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new TabColumn(
                                resultSet.getString("column_name"),
                                resultSet.getString("data_type"),
                                resultSet.getInt("column_id"),
                                resultSet.getInt("data_scale")
                        );
                    }
                }
        );
        return strings;
    }

    public List<String> getTableList() {
        return jdbcTemplate.queryForList("SELECT table_name FROM user_tables", String.class);
    }


}
