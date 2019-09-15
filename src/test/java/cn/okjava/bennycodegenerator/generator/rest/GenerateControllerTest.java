package cn.okjava.bennycodegenerator.generator.rest;

import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenerateControllerTest {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private GenerateService generateServiceImpl;

    @Test
    public void toTablePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/tables"));
    }

    @Test
    public void toTableDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/detail", ""));

    }
}