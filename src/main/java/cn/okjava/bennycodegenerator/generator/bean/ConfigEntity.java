package cn.okjava.bennycodegenerator.generator.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@Data
@Component
public class ConfigEntity {
    /**
     * 数据库名字
     */
    private String databaseName;

    private String packageName;

    private String outputDir;

    private String moduleName;

    private String author;

    private String version;

    private String tablePrefix;

    private String entitySuffix;

    private boolean lombokFlag;
}
