package cn.okjava.bennycodegenerator.generator.config;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenerateConfigTest {

    @Test
    public void getSchema() {
        Assertions.assertThat(GenerateConfig.schema)
                .as("静态注入数据库名字")
                .isNotBlank()
                .isEqualTo("baby-story");

    }
}