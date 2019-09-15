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

    public static String schema;

    @Value("${benny.generator.schema}")
    public void setSchema(String schema) {
        GenerateConfig.schema = schema;
    }
}
