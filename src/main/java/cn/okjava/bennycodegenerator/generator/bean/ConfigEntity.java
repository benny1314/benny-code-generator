package cn.okjava.bennycodegenerator.generator.bean;

import org.springframework.stereotype.Component;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@Component
public class ConfigEntity {

    private String packageName;

    private String outputDir;

    private String moduleName;

    private String author;

    private String version;

    private String tablePrefix;

    private String entitySuffix;

    private boolean lombokFlag;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public boolean isLombokFlag() {
        return lombokFlag;
    }

    public String getEntitySuffix() {
        return entitySuffix;
    }

    public void setEntitySuffix(String entitySuffix) {
        this.entitySuffix = entitySuffix;
    }

    public void setLombokFlag(boolean lombokFlag) {
        this.lombokFlag = lombokFlag;
    }
}
