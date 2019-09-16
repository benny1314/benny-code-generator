package cn.okjava.bennycodegenerator.generator.service.impl;

import cn.hutool.core.builder.HashCodeBuilder;
import cn.hutool.core.util.StrUtil;
import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
import cn.okjava.bennycodegenerator.generator.bean.TableEntity;
import cn.okjava.bennycodegenerator.generator.config.GenerateConfig;
import cn.okjava.bennycodegenerator.generator.config.ThymeleafConfig;
import cn.okjava.bennycodegenerator.generator.repository.ColumnRepository;
import cn.okjava.bennycodegenerator.generator.repository.TableRepository;
import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.springframework.cglib.core.HashCodeCustomizer;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import sun.management.Sensor;
import sun.swing.StringUIClientPropertyKey;

import javax.annotation.Resource;
import java.sql.Struct;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public TableEntity queryTableByTableName(String tableName) {
        return tableRepository.findByTableSchemaAndTableName(GenerateConfig.schema, tableName);
    }

    @Override
    public List<ColumnEntity> queryTableColumns(String tableName) {
        return columnRepository.findByTableSchemaAndTableNameOrderByOrdinalPositionAsc(GenerateConfig.schema, tableName);
    }

    @Override
    public Integer generate(String tableName) {
        Context context = new Context();
        TableEntity tableEntity = queryTableByTableName(tableName);
        // 表信息
        context.setVariable("package", Optional.ofNullable(GenerateConfig.packageName).orElse(""));
        context.setVariable("author", Optional.ofNullable(GenerateConfig.author).orElse(""));
        context.setVariable("version", Optional.ofNullable(GenerateConfig.version).orElse(""));
        context.setVariable("tableName", Optional.ofNullable(tableEntity).map(TableEntity::getTableName).orElse(""));
        context.setVariable("entityName", Optional.ofNullable(tableEntity).map(TableEntity::getTableName).map(name -> {
            String entityName = StrUtil.replace(name, GenerateConfig.tablePrefix, "");
            return StrUtil.upperFirst(entityName) + StrUtil.upperFirst(GenerateConfig.entitySuffix);
        }).orElse(""));
        context.setVariable("tableComment", Optional.ofNullable(tableEntity).map(TableEntity::getTableComment).orElse(""));
        context.setVariable("serialVersionUID", generateSerialVersionUID(tableEntity));;
        // 设置实体属性
        List<ColumnEntity> columnEntities = queryTableColumns(tableName);
        columnEntities.forEach(columnEntity -> {
            columnEntity.setColumnComment(Optional.of(columnEntity).map(entity -> StrUtil.toCamelCase(entity.getColumnComment())).orElse(""));
            columnEntity.setColumnKey(Optional.of(columnEntity).map(entity -> StrUtil.toCamelCase(entity.getColumnKey())).orElse(""));
            columnEntity.setColumnName(Optional.of(columnEntity).map(ColumnEntity::getColumnName).orElse(""));
            columnEntity.setColumnProperty(Optional.of(columnEntity).map(entity -> StrUtil.toCamelCase(entity.getColumnName())).orElse(""));
            columnEntity.setColumnDefault(Optional.of(columnEntity).map(entity -> StrUtil.toString(entity.getColumnDefault())).orElse(""));
            columnEntity.setDataType(Optional.of(columnEntity).map(entity -> StrUtil.toString(entity.getDataType())).orElse(""));
            columnEntity.setIsNullable(Optional.of(columnEntity).map(entity -> StrUtil.toString(entity.getIsNullable())).orElse(""));
            columnEntity.setLength(Optional.of(columnEntity).map(ColumnEntity::getLength).orElse(0));
            columnEntity.setOrdinalPosition(Optional.of(columnEntity).map(ColumnEntity::getOrdinalPosition).orElse(0));
        });
        // 设置 表字段
        context.setVariable("columns", columnEntities);
        TemplateEngine templateEngine = ThymeleafConfig.getTemplateEngine();
        String process = templateEngine.process("ColumnEntity.benny", context);
        System.out.println(process);
        return 1;
    }

    /**
     * 生成 serialVersionUID
     * @param tableEntity
     * @return
     */
    private long generateSerialVersionUID(TableEntity tableEntity) {
        return HashCodeBuilder.reflectionHashCode(tableEntity);
    }

    public static void main(String[] args) {
        try {
            System.out.println(Class.forName("cn.okjava.bennycodegenerator.generator.bean.AnswerEntity").hashCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        System.out.println(GenerateService.class.hashCode());
    }
}
