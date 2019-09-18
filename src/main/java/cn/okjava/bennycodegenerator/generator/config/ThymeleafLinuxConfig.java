package cn.okjava.bennycodegenerator.generator.config;

import cn.hutool.core.io.IoUtil;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author benny
 * @version V1.0.0
 * description 模板引擎配置
 * @date 2019/9/16 10:02
 */
public enum ThymeleafLinuxConfig {

    INSTANCE;

    private TemplateEngine templateEngine;

    ThymeleafLinuxConfig() {
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode("TEXT");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public static TemplateEngine getTemplateEngine() {
        try {
            Resource resource = new DefaultResourceLoader().getResource("classpath:/templates/tmpl/Bean.benny");
            OutputStream out = new ByteArrayOutputStream();
            long copy = IoUtil.copy(resource.getInputStream(), out, IoUtil.DEFAULT_BUFFER_SIZE);
            System.out.println("=====");
            System.out.println(out.toString());
            System.out.println("=====");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("can't find the tempalte.");
        } catch (IOException e) {
            throw new RuntimeException("IO异常");
        }
        return INSTANCE.templateEngine;
    }
}