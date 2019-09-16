package cn.okjava.bennycodegenerator.generator.service.impl;

import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author benny
 * @version V1.0.0
 * description $
 * @date 2019/9/16 10:10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenerateServiceImplTest {

    @Resource
    private GenerateService generateServiceImpl;

    @Test
    public void generate() {
        Integer hello = generateServiceImpl.generate("t_answer");
    }
}