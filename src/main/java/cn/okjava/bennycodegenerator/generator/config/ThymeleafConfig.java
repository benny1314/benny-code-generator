package cn.okjava.bennycodegenerator.generator.config;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;

/**
 * @author benny
 * @version V1.0.0
 * description 模板引擎配置
 * @date 2019/9/16 10:02
 */
@Component
public class ThymeleafConfig {

    private static TemplateEngine templateEngine;

    {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
//        templateResolver.setPrefix("templates" + File.separator + "tmpl" + File.separator);
        templateResolver.setTemplateMode("TEXT");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    private String getTemplatePath() {
        // 获取静态资源文件夹目录 此种方式 linux 不生效
//        try {

        String templates = ClassLoader.getSystemResource("templates" + File.separator + "tmpl").getPath() + File.separator;
        System.out.println("---------------》"+templates);
        return templates;
//            System.out.println("===============" + templates);
//            return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "templates" + File.separator + "tmpl").getPath() + File.separator;
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException();
//        }
    }

    public static TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
