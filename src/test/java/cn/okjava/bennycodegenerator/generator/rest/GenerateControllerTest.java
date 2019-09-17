package cn.okjava.bennycodegenerator.generator.rest;

import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GenerateControllerTest {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private GenerateService generateServiceImpl;

    @Test
    public void toTablePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/table")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

    }

    @Test
    public void toTableDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/table")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    public void should_return_json_when_call_generate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/generate").contentType(MediaType.APPLICATION_JSON).param("tableName", "t_answer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(System.out::print)
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(""));
    }
}