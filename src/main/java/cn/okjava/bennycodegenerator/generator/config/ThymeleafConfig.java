package cn.okjava.bennycodegenerator.generator.config;

import cn.hutool.core.io.IoUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.*;

/**
 * @author benny
 * @version V1.0.0
 * description 模板引擎配置
 * @date 2019/9/16 10:02
 */
public enum ThymeleafConfig {

    INSTANCE;

    private TemplateEngine templateEngine;

    ThymeleafConfig() {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix(getTemplatePath());
        templateResolver.setTemplateMode("TEXT");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    private String getTemplatePath() {
        try {
            try {
                ClassPathResource resource = new ClassPathResource("/templates/tmpl/Bean.benny");
                OutputStream out = new ByteArrayOutputStream();
                long copy = IoUtil.copy(resource.getInputStream(), out, IoUtil.DEFAULT_BUFFER_SIZE);
                System.out.println("=====");
                System.out.println(out.toString());
                System.out.println("=====");

            } catch (IOException e) {
                throw new RuntimeException("读取模板文件异常", e);
            }
            // windows 平台下
            return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "templates" + File.separator + "tmpl").getPath() + File.separator;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("获取模板路径问题");
        }
    }

    public static TemplateEngine getTemplateEngine() {
        return INSTANCE.templateEngine;
    }
}
