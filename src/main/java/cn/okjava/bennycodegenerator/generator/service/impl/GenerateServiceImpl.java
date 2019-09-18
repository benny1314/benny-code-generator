package cn.okjava.bennycodegenerator.generator.service.impl;

import cn.hutool.core.builder.HashCodeBuilder;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
import cn.okjava.bennycodegenerator.generator.bean.TableEntity;
import cn.okjava.bennycodegenerator.generator.config.GenerateConfig;
import cn.okjava.bennycodegenerator.generator.config.ThymeleafConfig;
import cn.okjava.bennycodegenerator.generator.repository.ColumnRepository;
import cn.okjava.bennycodegenerator.generator.repository.TableRepository;
import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Map<String, String> generate(String tableName) {
        Context context = new Context();
        TableEntity tableEntity = queryTableByTableName(tableName);
        // 表信息
        context.setVariable("package", Optional.ofNullable(GenerateConfig.packageName).orElse(""));
        context.setVariable("author", Optional.ofNullable(GenerateConfig.author).orElse(""));
        context.setVariable("version", Optional.ofNullable(GenerateConfig.version).orElse(""));
        context.setVariable("tableName", Optional.ofNullable(tableEntity).map(TableEntity::getTableName).orElse(""));
        context.setVariable("entityType", Optional.ofNullable(GenerateConfig.entitySuffix).map(StrUtil::upperFirst).orElse(""));
        // 首字母大写的表名
        context.setVariable("entityName", Optional.ofNullable(tableEntity).map(TableEntity::getTableName).map(name -> {
            for (String prefix : GenerateConfig.tablePrefix) {
                name = StrUtil.replace(name, prefix, "");
            }
            return StrUtil.upperFirst(StrUtil.toCamelCase(name));
        }).get());
        // 小驼峰的表名
        context.setVariable("entityCamelName", Optional.ofNullable(tableEntity).map(TableEntity::getTableName).map(name -> {
            for (String prefix : GenerateConfig.tablePrefix) {
                name = StrUtil.replace(name, prefix, "");
            }
            return StrUtil.toCamelCase(name);
        }).get());

        context.setVariable("tableComment", Optional.ofNullable(tableEntity).map(TableEntity::getTableComment).orElse(""));
        context.setVariable("serialVersionUID", generateSerialVersionUID(tableEntity));
        // 设置实体属性
        List<ColumnEntity> columnEntities = queryTableColumns(tableName);
        columnEntities.forEach(columnEntity -> {
            columnEntity.setColumnComment(Optional.of(columnEntity).map(entity -> StrUtil.toCamelCase(entity.getColumnComment())).orElse(""));
            columnEntity.setColumnKey(Optional.of(columnEntity).map(entity -> StrUtil.toCamelCase(entity.getColumnKey())).orElse(""));
            columnEntity.setColumnName(Optional.of(columnEntity).map(ColumnEntity::getColumnName).orElse(""));
            columnEntity.setColumnProperty(Optional.of(columnEntity).map(entity -> StrUtil.toCamelCase(entity.getColumnName())).orElse(""));
            columnEntity.setColumnDefault(Optional.of(columnEntity).map(entity -> StrUtil.toString(entity.getColumnDefault())).orElse(""));
            columnEntity.setIsNullable(Optional.of(columnEntity).map(entity -> StrUtil.toString(entity.getIsNullable())).orElse(""));
            columnEntity.setLength(Optional.of(columnEntity).map(ColumnEntity::getLength).orElse(0));
            columnEntity.setOrdinalPosition(Optional.of(columnEntity).map(ColumnEntity::getOrdinalPosition).orElse(0));
        });
        // 设置 表字段
        context.setVariable("columns", columnEntities);
        TemplateEngine templateEngine = ThymeleafConfig.getTemplateEngine();
        return renderTemplate(context, templateEngine);
    }

    /**
     * 渲染模板
     *
     * @param context
     * @param templateEngine
     * @return
     */
    private Map<String, String> renderTemplate(Context context, TemplateEngine templateEngine) {
        try {
            InputStream inputStream = new ClassPathResource("/templates/tmpl/Bean.benny").getInputStream();
            OutputStream out = new ByteArrayOutputStream();
            long copy = IoUtil.copy(inputStream, out, IoUtil.DEFAULT_BUFFER_SIZE);
            String result = out.toString();
            System.out.println("=====");
            System.out.println(result);
            System.out.println("=====");
        } catch (IOException e) {

        }


        // 生成jpa Entity
        String jpaEntity = templateEngine.process("JpaEntity.benny", context);
        // 生成Bean
        String beanEntity = templateEngine.process("Bean.benny", context);
        // 生成jpa Repository
        String jpaRepository = templateEngine.process("Repository.benny", context);
        // 生成 Mapper
        String mapper = templateEngine.process("Mapper.benny", context);
        // 生成 Service
        String service = templateEngine.process("service.benny", context);
        // 生成 RepositoryImpl
        String repositoryImpl = templateEngine.process("JpaImpl.benny", context);
        // 生成 MapperImpl
        String mapperImpl = templateEngine.process("MapperImpl.benny", context);
        // 生成dto
        String dto = templateEngine.process("Dto.benny", context);
        // 生成controller
        String controller = templateEngine.process("Controller.benny", context);

        return MapUtil.builder("jpa", jpaEntity)
                .put("bean", beanEntity)
                .put("repository", jpaRepository)
                .put("mapper", mapper)
                .put("service", service)
                .put("repositoryImpl", repositoryImpl)
                .put("mapperImpl", mapperImpl)
                .put("controller", controller)
                .put("dto", dto)
                .build();
    }

    /**
     * 渲染模板
     *
     * @param context
     * @param templateEngine
     * @return
     */
    private Map<String, String> renderLinuxTemplate(Context context, TemplateEngine templateEngine) {
//        org.springframework.core.io.Resource resource = new DefaultResourceLoader().getResource("classpath:test-environment.yml");
//        System.out.println("文件名称: " + resource.getFilename());
        try {
            ClassPathResource resource = new ClassPathResource("/tmpl/JpaEntity.benny");
            OutputStream out = new ByteArrayOutputStream();
            long copy = IoUtil.copy(resource.getInputStream(), out, IoUtil.DEFAULT_BUFFER_SIZE);
            String result = "";
            IoUtil.write(out, true, result.getBytes());
            System.out.println("=====");
            System.out.println(result);
            System.out.println("=====");

        } catch (IOException e) {
            throw new RuntimeException("读取模板文件异常", e);
        }
        // 生成jpa Entity
        String jpaEntity = templateEngine.process("JpaEntity.benny", context);
        // 生成Bean
        String beanEntity = templateEngine.process("Bean.benny", context);
        // 生成jpa Repository
        String jpaRepository = templateEngine.process("Repository.benny", context);
        // 生成 Mapper
        String mapper = templateEngine.process("Mapper.benny", context);
        // 生成 Service
        String service = templateEngine.process("service.benny", context);
        // 生成 RepositoryImpl
        String repositoryImpl = templateEngine.process("JpaImpl.benny", context);
        // 生成 MapperImpl
        String mapperImpl = templateEngine.process("MapperImpl.benny", context);
        // 生成dto
        String dto = templateEngine.process("Dto.benny", context);
        // 生成controller
        String controller = templateEngine.process("Controller.benny", context);

        return MapUtil.builder("jpa", jpaEntity)
                .put("bean", beanEntity)
                .put("repository", jpaRepository)
                .put("mapper", mapper)
                .put("service", service)
                .put("repositoryImpl", repositoryImpl)
                .put("mapperImpl", mapperImpl)
                .put("controller", controller)
                .put("dto", dto)
                .build();
    }

    /**
     * 生成 serialVersionUID
     *
     * @param tableEntity
     * @return
     */
    private long generateSerialVersionUID(TableEntity tableEntity) {
        return HashCodeBuilder.reflectionHashCode(tableEntity);
    }
}