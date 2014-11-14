package hu.karsany.plsqlfromjava.entity;

import hu.karsany.plsqlfromjava.util.StringHelper;

import java.util.List;

/**
 * User: fkarsany
 * Date: 2013.11.18.
 */
public class Procedure {

    private String objectName;
    private String procedureName;
    private String overload;
    private String methodType;
    private List<ProcedureArgument> argumentList;
    private String pldoc;

    public Procedure(String objectName, String procedureName, String overload, String methodType, List<ProcedureArgument> argumentList, String pldoc) {
        this.objectName = objectName;
        this.procedureName = procedureName;
        this.overload = overload;
        this.methodType = methodType;
        this.argumentList = argumentList;
        this.pldoc = pldoc;
    }

    public Procedure(String objectName, String procedureName, String overload, String methodType, List<ProcedureArgument> argumentList) {
        this.objectName = objectName;
        this.procedureName = procedureName;
        this.overload = overload;
        this.methodType = methodType;
        this.argumentList = argumentList;
    }

    public Procedure(String objectName, String procedureName, String overload, String methodType) {
        this.objectName = objectName;
        this.procedureName = procedureName;
        this.overload = overload;
        this.methodType = methodType;
    }

    public List<ProcedureArgument> getArgumentList() {

        return argumentList;
    }

    public void setArgumentList(List<ProcedureArgument> argumentList) {
        this.argumentList = argumentList;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getObjectName() {

        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getOverload() {
        return overload;
    }

    public void setOverload(String overload) {
        this.overload = overload;
    }

    public String getJavaProcedureName() {
        return StringHelper.toCamelCaseSmallBegin(this.procedureName + "_" + this.overload);
    }

    public String getStoredProcedureClassName() {
        return StringHelper.toCamelCase(this.getObjectName() + "_" + this.getProcedureName() + "_" + this.getOverload());
    }

    public void getReturnJavaType() {
        if (!methodType.equals("FUNCTION")) {
            throw new RuntimeException("Csak fuggvenyek eseteben ertelmezett.");
        }
        for (ProcedureArgument argument : argumentList) {
            argumentList.get(0).getJavaDataType();
        }
    }

    public String getPldoc() {
        return pldoc;
    }

    public void setPldoc(String pldoc) {
        this.pldoc = pldoc;
    }
}
