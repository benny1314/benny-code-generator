package cn.okjava.bennycodegenerator.generator.service.impl;

import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author benny
 * @version V1.0.0
 * description $
 * @date 2019/9/16 10:10
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class GenerateServiceImplTest {

    @Resource
    private GenerateService generateServiceImpl;

    @Test
    public void generate() {
        Map<String, String> resultMap = generateServiceImpl.generate("t_note");
        Assertions.assertThat(resultMap).as("生成的字符串map对象").isNotNull().hasSizeGreaterThan(0);
    }

    @Test
    public void setGeneratorCofig() {
    }
}