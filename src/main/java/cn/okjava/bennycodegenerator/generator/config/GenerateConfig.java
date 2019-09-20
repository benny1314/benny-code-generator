package cn.okjava.bennycodegenerator.generator.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@Component
public class GenerateConfig {
    /**
     * 数据库名字
     */
    public static String schema;
    /**
     * 包名
     */
    public static String packageName;
    /**
     * 输出目录
     */
    public static String outputDir;
    /**
     * 作者
     */
    public static String author;
    /**
     * 版本
     */
    public static String version;
    /**
     * 表前缀
     */
    public static String[] tablePrefix;
    /**
     * 实体后缀
     */
    public static String entitySuffix;
    /**
     * lombok风格
     */
    public static boolean lombokFlag;

    @Value("${benny.generator.schema}")
    public void setSchema(String schema) {
        GenerateConfig.schema = schema;
    }

    @Value("${benny.generator.package}")
    public  void setPackageName(String packageName) {
        GenerateConfig.packageName = packageName;
    }

    @Value("${benny.generator.outputdir}")
    public  void setOutputDir(String outputDir) {
        GenerateConfig.outputDir = outputDir;
    }

    @Value("${benny.generator.author}")
    public void setAuthor(String author) {
        GenerateConfig.author = author;
    }

    @Value("${benny.generator.version}")
    public void setVersion(String version) {
        GenerateConfig.version = version;
    }

    @Value("${benny.generator.tableprefix}")
    public void setTablePrefix(String[] tablePrefix) {
        GenerateConfig.tablePrefix = tablePrefix;
    }

    @Value("${benny.generator.entitysuffix}")
    public  void setEntitySuffix(String entitySuffix) {
        GenerateConfig.entitySuffix = entitySuffix;
    }

    @Value("${benny.generator.lombok}")
    public void setLombokStyle(boolean lombokFlag) {
        GenerateConfig.lombokFlag = lombokFlag;
    }
}
