package cn.okjava.bennycodegenerator.generator.service.impl;

import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
import cn.okjava.bennycodegenerator.generator.bean.TableEntity;
import cn.okjava.bennycodegenerator.generator.config.GenerateConfig;
import cn.okjava.bennycodegenerator.generator.repository.ColumnRepository;
import cn.okjava.bennycodegenerator.generator.repository.TableRepository;
import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@Service(value = "generateServiceImpl")
public class GenerateServiceImpl implements GenerateService {

    @Resource
    private TableRepository tableRepository;

    @Resource
    private ColumnRepository columnRepository;

    @Override
    public List<TableEntity> queryAllTables() {
        return tableRepository.findByTableSchemaEquals(GenerateConfig.schema);
    }

    @Override
    public List<ColumnEntity> queryTableColumns(String tableName) {
        return columnRepository.findByTableSchemaAndTableNameOrderByOrdinalPositionAsc(GenerateConfig.schema, tableName);
    }
}
