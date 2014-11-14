package hu.karsany.plsqlfromjava.entity;

import hu.karsany.plsqlfromjava.util.StringHelper;

/**
 * User: fkarsany
 * Date: 2013.11.18.
 */
public class ProcedureArgument {

    private String argumentName;
    private String dataType;
    private String typeName;
    private String defaulted;
    private boolean inParam;
    private boolean outParam;
    private int sequence;

    public ProcedureArgument(String argumentName, String dataType, String typeName, String defaulted, boolean inParam, boolean outParam, int sequence) {

        this.argumentName = argumentName;
        this.dataType = dataType;
        this.typeName = typeName;
        this.defaulted = defaulted;
        this.inParam = inParam;
        this.outParam = outParam;
        this.sequence = sequence;


    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isInParam() {
        return inParam;
    }

    public void setInParam(boolean inParam) {
        this.inParam = inParam;
    }

    public boolean isOutParam() {
        return outParam;
    }

    public void setOutParam(boolean outParam) {
        this.outParam = outParam;
    }

    public String getDefaulted() {
        return defaulted;
    }

    public void setDefaulted(String defaulted) {
        this.defaulted = defaulted;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getJavaPropertyName() {
        if (this.argumentName == null) {
            return "__return";
        } else if (this.argumentName.startsWith("P_")) {
            return StringHelper.toCamelCaseSmallBegin(this.argumentName.substring(2));
        } else {
            return StringHelper.toCamelCaseSmallBegin(this.argumentName);
        }
    }

    public String getJavaPropertyNameBig() {
        String javaPropName = this.getJavaPropertyName();
        return javaPropName.substring(0, 1).toUpperCase() + javaPropName.substring(1);
    }

    public String getJavaTypeName() {
        if (typeName.endsWith("_LIST")) {
            return "List<" + StringHelper.toCamelCase(typeName.substring(0, typeName.length() - 5)) + ">";
        } else {
            return StringHelper.toCamelCase(typeName);
        }
    }

    public boolean isList() {
        return (typeName.endsWith("_LIST"));
    }

    public String getUnderlyingTypeName() {
        if (typeName.endsWith("_LIST")) {
            return StringHelper.toCamelCase(typeName.substring(0, typeName.length() - 5));
        } else {
            return StringHelper.toCamelCase(typeName);
        }
    }

    public String getJavaDataType() {
        if (this.typeName != null) {
            return getJavaTypeName();
        } else {
            if (dataType.equals("CHAR") || dataType.equals("VARCHAR2") || dataType.equals("CLOB")) {
                return "String";
            } else if (dataType.equals("NUMBER") || dataType.equals("BINARY_INTEGER")) {
                return "Integer";
            } else if (dataType.equals("REF CURSOR")) {
                return "ResultSet";
            } else if (dataType.equals("DATE")) {
                return "Date";
            } else if (dataType.equals("TIMESTAMP")) {
                return "Timestamp";
            } else if (dataType.equals("PL/SQL BOOLEAN")) {
                return "Boolean";
            } else {
                return "Object";
            }
        }
    }

    public String getOracleType() {
        if (dataType.equals("VARCHAR2")) {
            return "VARCHAR";
        } else if (dataType.equals("REF CURSOR")) {
            return "CURSOR";
        } else if (dataType.equals("BINARY_INTEGER")) {
            return "INTEGER";
        } else if (dataType.equals("OBJECT")) {
            return "STRUCT";
        } else if (dataType.equals("TABLE")) {
            return "ARRAY";
        } else if (dataType.equals("PL/SQL BOOLEAN")) {
            return "BOOLEAN";
        } else {
            return dataType;
        }
    }

    public String getInoutType() {
        return (inParam ? "IN" : "") + (outParam ? "OUT" : "");
    }

}
