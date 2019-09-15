package cn.okjava.bennycodegenerator.generator.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@Entity
@Table(name = "information_schema.TABLES")
public class TableEntity implements Serializable {

    private static final long serialVersionUID = 8047729190905562776L;

    @Id
    @Column(name = "TABLE_NAME")
    private String tableName;
    /**
     * 表名字
     */
    @Column(name = "TABLE_SCHEMA")
    private String tableSchema;
    /**
     * 表注释
     */
    @Column(name = "TABLE_COMMENT")
    private String tableComment;
    /**
     * 引擎
     */
    @Column
    private String engine;

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

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }
}