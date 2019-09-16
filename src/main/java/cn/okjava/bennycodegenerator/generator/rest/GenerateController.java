package cn.okjava.bennycodegenerator.generator.rest;

import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
import cn.okjava.bennycodegenerator.generator.bean.TableEntity;
import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@Controller
@RequestMapping("/")
public class GenerateController {

    @Resource
    private GenerateService generateServiceImpl;

    /**
     * 查看数据库对应的表结构
     *
     * @param model
     * @return
     */
    @GetMapping("/table")
    public String toTablePage(Model model) {
        List<TableEntity> tables = generateServiceImpl.queryAllTables();
        model.addAttribute("tables", tables);
        return "table";
    }

    /**
     * 查询表中国的字段
     *
     * @param model
     * @param tableName
     * @return
     */
    @GetMapping("/detail")
    public String toTableDetail(Model model, String tableName) {
        List<ColumnEntity> columns = generateServiceImpl.queryTableColumns(tableName);
        model.addAttribute("columns", columns);
        model.addAttribute("tableName", tableName);
        return "column";
    }

    /**
     * 查询表中国的字段
     *
     * @param tableName
     * @return
     */
    @ResponseBody
    @PostMapping("/generate")
    public String genrerateCode(String tableName) {
        String generate = generateServiceImpl.generate(tableName);
        return generate;
    }
}
