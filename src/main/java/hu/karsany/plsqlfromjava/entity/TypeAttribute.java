package hu.karsany.plsqlfromjava.entity;

import hu.karsany.plsqlfromjava.util.StringHelper;

/**
 * User: fkarsany
 * Date: 2013.11.17.
 */
public class TypeAttribute {

    private String attrName;
    private String attrTypeName;
    private int attrNo;
    private int dataScale;
    private int multiType;
    private String typeCode;
    private String collectionBaseType;

    public String getCollectionBaseType() {
        return collectionBaseType;
    }

    public void setCollectionBaseType(String collectionBaseType) {
        this.collectionBaseType = collectionBaseType;
    }

    public TypeAttribute(String attrName, String attrTypeName, int attrNo, int dataScale, int multiType, String typeCode, String collectionBaseType) {
        this.attrName = attrName;
        this.attrTypeName = attrTypeName;
        this.attrNo = attrNo;
        this.dataScale = dataScale;
        this.multiType = multiType;
        this.typeCode = typeCode;
        this.collectionBaseType = collectionBaseType;


    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public int getMultiType() {
        return multiType;
    }

    public void setMultiType(int multiType) {
        this.multiType = multiType;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrTypeName() {
        return attrTypeName;
    }

    public void setAttrTypeName(String attrTypeName) {
        this.attrTypeName = attrTypeName;
    }

    public int getAttrNo() {
        return attrNo;
    }

    public void setAttrNo(int attrNo) {
        this.attrNo = attrNo;
    }

    public int getDataScale() {
        return dataScale;
    }

    public void setDataScale(int dataScale) {
        this.dataScale = dataScale;
    }

    public String getJavaDataType() {
        if (attrTypeName.equals("VARCHAR2") ||
                attrTypeName.equals("CHAR") ||
                attrTypeName.equals("CLOB")) {
            return "String";
        } else if (attrTypeName.equals("DATE")) {
            return "Date";
        } else if (attrTypeName.startsWith("TIMESTAMP")) {
            return "Timestamp";
        } else if (attrTypeName.equals("NUMBER")) {
            if (this.dataScale == 0) {
                return "Integer";
            } else {
                return "Double";
            }
        } else if (multiType == 1) {
            if (typeCode.equals("COLLECTION")) {
                return "List<" + StringHelper.toCamelCase(attrTypeName.replace("_LIST", "")) + ">";
            } else {
                return StringHelper.toCamelCase(attrTypeName);
            }
        } else {
            return "Object";
        }

    }

    public String getJavaPropertyName() {
        return StringHelper.toCamelCaseSmallBegin(this.attrName);
    }

    public String getJavaPropertyNameBig() {
        return StringHelper.toCamelCase(this.attrName);
    }

    public String getJavaCollectionBaseTypeNameBig() {
        return StringHelper.toCamelCase(this.collectionBaseType);
    }

    public int getAttrNoIndex() {
        return this.attrNo - 1;
    }
}
