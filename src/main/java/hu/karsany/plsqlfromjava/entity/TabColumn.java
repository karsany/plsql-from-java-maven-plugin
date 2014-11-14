package hu.karsany.plsqlfromjava.entity;


import hu.karsany.plsqlfromjava.util.StringHelper;

public class TabColumn {

    private String columnName;
    private String dataType;
    private int columnId;
    private int dataScale;

    public TabColumn(String columnName, String dataType, int columnId, int dataScale) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.columnId = columnId;
        this.dataScale = dataScale;
    }

    public TabColumn(String columnName, String dataType, int columnId) {

        this.columnName = columnName;
        this.dataType = dataType;
        this.columnId = columnId;
    }

    public int getDataScale() {

        return dataScale;
    }

    public void setDataScale(int dataScale) {
        this.dataScale = dataScale;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public String getJavaPropertyName() {
        return StringHelper.toCamelCaseSmallBegin(this.columnName);
    }

    public String getJavaPropertyNameBig() {
        return StringHelper.toCamelCase(this.columnName);
    }

    public String getJavaDataType() {
        if (dataType.equals("VARCHAR2") ||
                dataType.equals("CHAR") ||
                dataType.equals("CLOB")) {
            return "String";
        } else if (dataType.equals("DATE")) {
            return "Date";
        } else if (dataType.startsWith("TIMESTAMP")) {
            return "Timestamp";
        } else if (dataType.equals("NUMBER")) {
            if (this.dataScale == 0) {
                return "int";
            } else {
                return "double";
            }
        } else {
            return "Object";
        }

    }

    public String getSqliteDataType() {

        if (dataType.equals("NUMBER")) {
            if (this.dataScale == 0) {
                return "INTEGER";
            } else {
                return "NUMBER";
            }
        } else {
            return dataType;
        }

    }

    public String getResultSetDataType() {
        return getJavaDataType().substring(0, 1).toUpperCase() + getJavaDataType().substring(1);
    }

    @Override
    public String toString() {
        return "TabColumn{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", columnId=" + columnId +
                '}';
    }
}
