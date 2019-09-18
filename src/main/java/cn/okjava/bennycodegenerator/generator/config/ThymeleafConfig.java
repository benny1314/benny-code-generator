package cn.okjava.bennycodegenerator.generator.config;

import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author benny
 * @version V1.0.0
 * description 模板引擎配置
 * @date 2019/9/16 10:02
 */
public enum ThymeleafConfig {

    INSTANCE;
    /**
     * 文件模板引擎
     */
    private TemplateEngine templateEngine;

    ThymeleafConfig() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            FileTemplateResolver templateResolver = new FileTemplateResolver();
            templateResolver.setPrefix(getTemplatePath());
            templateResolver.setTemplateMode("TEXT");
            templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);
        } else {
            StringTemplateResolver templateResolver = new StringTemplateResolver();
            templateResolver.setTemplateMode("TEXT");
            templateEngine = new SpringTemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);
        }
    }

    private String getTemplatePath() {
        try {
            // windows 平台下
            return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "templates" + File.separator + "tmpl").getPath() + File.separator;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("can't find the tempalte.");
        }
    }

    public static TemplateEngine getTemplateEngine() {
        return INSTANCE.templateEngine;
    }
}
