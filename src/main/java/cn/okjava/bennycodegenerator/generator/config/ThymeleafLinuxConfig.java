package cn.okjava.bennycodegenerator.generator.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

/**
 * @author benny
 * @version V1.0.0
 * description 模板引擎配置
 * @date 2019/9/16 10:02
 */
public enum ThymeleafLinuxConfig {

    INSTANCE;

    private SpringTemplateEngine templateEngine;

    ThymeleafLinuxConfig() {
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode("TEXT");
        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public static TemplateEngine getTemplateEngine() {
        return INSTANCE.templateEngine;
    }
}