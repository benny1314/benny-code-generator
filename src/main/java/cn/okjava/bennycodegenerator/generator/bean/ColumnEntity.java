package cn.okjava.bennycodegenerator.generator.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * @author benny
 * @version 1.0
 * description: 表字段信息
 */
@Table(name = "information_schema.COLUMNS")
@Entity
public class ColumnEntity implements Serializable {

    private static final long serialVersionUID = -4814718660521826935L;
    /**
     * 字段在表中的顺序数字
     */
    @Id
    @Column(name = "ORDINAL_POSITION")
    private Integer ordinalPosition;
    /**
     * 表名
     */
    @Column(name = "TABLE_NAME")
    private String tableName;

    /**
     * 数据库名字
     */
    @Column(name = "TABLE_SCHEMA")
    private String tableSchema;

    /**
     * 列名
     */
    @Column(name = "COLUMN_NAME")
    private String columnName;

    /**
     * 列名
     */
    @Transient
    private String columnProperty;

    /**
     * 列的数据类型
     */
    @Transient
    private String columnType;

    /**
     * 数据类型
     */
    @Column(name = "DATA_TYPE")
    private String dataType;

    /**
     * 属性注释
     */
    @Column(name = "COLUMN_COMMENT")
    private String columnComment;

    /**
     * 属性键类型（主键 PRI）
     */
    @Column(name = "COLUMN_KEY")
    private String columnKey;

    /**
     * 是否允许为空
     */
    @Column(name = "IS_NULLABLE")
    private String isNullable;

    /**
     * 默认值
     */
    @Column(name = "COLUMN_DEFAULT")
    private String columnDefault;

    /**
     * 列长度
     */
    @Column(name = "CHARACTER_MAXIMUM_LENGTH")
    private Integer length;

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
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

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getColumnProperty() {
        return columnProperty;
    }

    public void setColumnProperty(String columnProperty) {
        this.columnProperty = columnProperty;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnType() {
        switch (this.dataType) {
            case "varchar":
            case "char":
            case "blob":
                return "String";
            case "int":
            case "smallint":
            case "tinyint":
                return "Integer";
            case "bigint":
                return "Long";
            case "datetime":
            case "timestamp":
                return "Date";
            default:
                throw new NoSuchElementException("未找到栏目类型");
        }
    }
}

