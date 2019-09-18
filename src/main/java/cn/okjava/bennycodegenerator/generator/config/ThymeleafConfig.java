package cn.okjava.bennycodegenerator.generator.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author benny
 * @version V1.0.0
 * description 模板引擎配置
 * @date 2019/9/16 10:02
 */
@Component
public class ThymeleafConfig {

    @Resource
    private ResourceLoader resourceLoader;

    private static TemplateEngine templateEngine;

    {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix(getTemplatePath());
        templateResolver.setTemplateMode("TEXT");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    private String getTemplatePath() {
        // 获取静态资源文件夹目录 此种方式 linux 不生效
        try {
            System.out.println("=====》"+new ClassPathResource("Bean.benny").getPath());
            System.out.println("=====》"+new ClassPathResource("tmpl/Bean.benny").getPath());
            System.out.println("=====》"+new ClassPathResource("templates/tmpl/Bean.benny").getPath());
            System.out.println("-------------------->"+getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "templates" + File.separator + "tmpl").getPath() + File.separator;
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }

    public static TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
