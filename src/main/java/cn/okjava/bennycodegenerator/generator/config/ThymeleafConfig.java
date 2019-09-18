package cn.okjava.bennycodegenerator.generator.config;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

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
        String fileName = "classpath:/templates/tmpl/";
        try {
            System.out.println(new DefaultResourceLoader().getResource(fileName).getURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取静态资源文件夹目录 此种方式 linux 不生效
        return getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "templates" + File.separator + "tmpl" + File.separator;
        // windows 平台下
        // return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "templates" + File.separator + "tmpl").getPath() + File.separator;
    }

    public static TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
