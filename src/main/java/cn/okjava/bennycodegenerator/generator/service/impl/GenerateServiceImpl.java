package cn.okjava.bennycodegenerator.generator.service.impl;

import cn.hutool.core.builder.HashCodeBuilder;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
import cn.okjava.bennycodegenerator.generator.bean.ConfigEntity;
import cn.okjava.bennycodegenerator.generator.bean.TableEntity;
import cn.okjava.bennycodegenerator.generator.config.GenerateConfig;
import cn.okjava.bennycodegenerator.generator.config.ThymeleafConfig;
import cn.okjava.bennycodegenerator.generator.config.ThymeleafLinuxConfig;
import cn.okjava.bennycodegenerator.generator.repository.ColumnRepository;
import cn.okjava.bennycodegenerator.generator.repository.TableRepository;
import cn.okjava.bennycodegenerator.generator.service.GenerateService;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public String saveGenerateConfig(ConfigEntity configEntity) {
        String result = checkGeneratorConfig(configEntity);
        if (StrUtil.isNotBlank(result)) {
            return result;
        }
        GenerateConfig.packageName = configEntity.getPackageName();
        GenerateConfig.tablePrefix = configEntity.getTablePrefix().split("\\+");
        GenerateConfig.author = configEntity.getAuthor();
        GenerateConfig.version = configEntity.getVersion();
        GenerateConfig.outputDir = configEntity.getOutputDir();
        GenerateConfig.entitySuffix = configEntity.getEntitySuffix();
        GenerateConfig.lombokFlag = configEntity.isLombokFlag();
        return StrUtil.EMPTY;
    }

    /**
     * 校验参数非空
     *
     * @param configEntity
     * @return
     */
    public String checkGeneratorConfig(ConfigEntity configEntity) {
        Field[] declaredFields = configEntity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            //设置是否允许访问，不是修改原来的访问权限修饰词。
            field.setAccessible(true);
            try {
                if (Objects.isNull(field.get(configEntity))) {
                    return field.getName() + "为空！";
                }
            } catch (IllegalAccessException e) {
            }
        }
        return StrUtil.EMPTY;
    }

    @Override
    public Map<String, String> generate(String tableName) {
        Context context = getContext(tableName);
        // 判断操作系统决定使用何种方式渲染模板
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return renderTemplate(context, ThymeleafConfig.getTemplateEngine());
        }
        return renderLinuxTemplate(context, ThymeleafLinuxConfig.getTemplateEngine());
    }

    private Context getContext(String tableName) {
        Context context = new Context();
        TableEntity tableEntity = queryTableByTableName(tableName);
        // 表信息
        context.setVariable("package", Optional.ofNullable(GenerateConfig.packageName).orElse(""));
        context.setVariable("author", Optional.ofNullable(GenerateConfig.author).orElse(""));
        context.setVariable("version", Optional.ofNullable(GenerateConfig.version).orElse(""));
        context.setVariable("tableName", Optional.ofNullable(tableEntity).map(TableEntity::getTableName).orElse(""));
        context.setVariable("entitySuffix", Optional.ofNullable(GenerateConfig.entitySuffix).map(StrUtil::upperFirst).orElse(""));
        context.setVariable("lombokFlag", BooleanUtil.isTrue(GenerateConfig.lombokFlag));
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
            columnEntity.setColumnCamelProperty(Optional.of(columnEntity).map(entity -> StrUtil.toCamelCase(entity.getColumnName())).orElse(""));
            columnEntity.setColumnProperty(Optional.of(columnEntity).map(entity -> StrUtil.upperFirst(StrUtil.toCamelCase(entity.getColumnName()))).orElse(""));
            columnEntity.setColumnDefault(Optional.of(columnEntity).map(entity -> StrUtil.toString(entity.getColumnDefault())).orElse(""));
            columnEntity.setIsNullable(Optional.of(columnEntity).map(entity -> StrUtil.toString(entity.getIsNullable())).orElse(""));
            columnEntity.setLength(Optional.of(columnEntity).map(ColumnEntity::getLength).orElse(0));
            columnEntity.setOrdinalPosition(Optional.of(columnEntity).map(ColumnEntity::getOrdinalPosition).orElse(0));
        });
        // 设置 表字段
        context.setVariable("columns", columnEntities);
        return context;
    }

    /**
     * 渲染模板
     *
     * @param context
     * @param templateEngine
     * @return
     */
    private Map<String, String> renderTemplate(Context context, TemplateEngine templateEngine) {
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
        String serviceImpl = templateEngine.process("ServiceImpl.benny", context);
        // 生成dto
        String dto = templateEngine.process("Dto.benny", context);
        // 生成controller
        String controller = templateEngine.process("Controller.benny", context);
        // 生成xml
        String mapperXml = templateEngine.process("MapperXml.benny", context);

        return MapUtil.builder("jpa", jpaEntity)
                .put("bean", beanEntity)
                .put("repository", jpaRepository)
                .put("mapper", mapper)
                .put("service", service)
                .put("repositoryImpl", repositoryImpl)
                .put("serviceImpl", serviceImpl)
                .put("controller", controller)
                .put("dto", dto)
                .put("mapperXml", mapperXml)
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
        // 生成jpa Entity
        String jpaEntity = templateEngine.process(getTemplateStr("JpaEntity.benny"), context);
        // 生成Bean
        String beanEntity = templateEngine.process(getTemplateStr("Bean.benny"), context);
        // 生成jpa Repository
        String jpaRepository = templateEngine.process(getTemplateStr("Repository.benny"), context);
        // 生成 Mapper
        String mapper = templateEngine.process(getTemplateStr("Mapper.benny"), context);
        // 生成 Service
        String service = templateEngine.process(getTemplateStr("Service.benny"), context);
        // 生成 RepositoryImpl
        String repositoryImpl = templateEngine.process(getTemplateStr("JpaImpl.benny"), context);
        // 生成 serviceImpl
        String serviceImpl = templateEngine.process(getTemplateStr("ServiceImpl.benny"), context);
        // 生成dto
        String dto = templateEngine.process(getTemplateStr("Dto.benny"), context);
        // 生成controller
        String controller = templateEngine.process(getTemplateStr("Controller.benny"), context);
        // 生成 mapperXml
        String mapperXml = templateEngine.process(getTemplateStr("MapperXml.benny"), context);

        return MapUtil.builder("jpa", jpaEntity)
                .put("bean", beanEntity)
                .put("repository", jpaRepository)
                .put("mapper", mapper)
                .put("service", service)
                .put("repositoryImpl", repositoryImpl)
                .put("serviceImpl", serviceImpl)
                .put("controller", controller)
                .put("dto", dto)
                .put("mapperXml", mapperXml)
                .build();
    }

    @Override
    public void download(String tableName) {
        Context context = getContext(tableName);
        String os = System.getProperty("os.name");
        Map<String, String> template;
        if (os.toLowerCase().startsWith("win")) {
            template = renderTemplate(context, ThymeleafConfig.getTemplateEngine());
        } else {
            template = renderLinuxTemplate(context, ThymeleafLinuxConfig.getTemplateEngine());
        }

        String outputDir = GenerateConfig.outputDir + File.separator;
        if (StrUtil.isBlank(outputDir)) {
            System.out.println("未获取到下载路径");
        }
        File directory = new File(outputDir);
        if (!directory.isDirectory()) {
            System.out.println("配置的下载路径 不是文件夹");
        }
        try {
            for (Map.Entry<String, String> entry : template.entrySet()) {
                String fileName = entry.getKey();
                String content = entry.getValue();
                String packageName = GenerateConfig.packageName;
                String packageDirectory = packageName.replaceAll("\\.", "\\\\");
                String fileDirectory = outputDir + packageDirectory + File.separator + getFileName(fileName, tableName);
                new File(fileDirectory).mkdirs();
                String filePath = "";
                if (fileName.contains("Xml")) {
                    filePath = StrUtil.toCamelCase(tableName) + StrUtil.upperFirst(fileName) + ".xml";
                } else {
                    filePath = StrUtil.toCamelCase(tableName) + StrUtil.upperFirst(fileName) + ".java";
                }
                String name = fileDirectory + filePath;
                File file = new File(name);
                FileWriter fileWriter = new FileWriter(file.getPath());
                fileWriter.write(content);
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException e) {
        }
    }

    private String getFileName(String name, String tableName) {
        for (String prefix : GenerateConfig.tablePrefix) {
            tableName = StrUtil.replace(tableName, prefix, "");
        }
        tableName = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
        switch (name) {
            case "mappers":
                return "mappers" + File.separator + StrUtil.toCamelCase(tableName) + File.separator;
            case "controller":
                return "rest" + File.separator + StrUtil.toCamelCase(tableName) + File.separator ;
            case "dto":
                return "dto" + File.separator + StrUtil.toCamelCase(tableName) + File.separator ;
            case "entity":
                return "entity" + File.separator + StrUtil.toCamelCase(tableName) + File.separator ;
            case "bean":
                return "bean" + File.separator + StrUtil.toCamelCase(tableName) + File.separator ;
            case "mapper":
                return "mapper" + File.separator + StrUtil.toCamelCase(tableName) + File.separator ;
            case "repository":
                return "repository" + File.separator + StrUtil.toCamelCase(tableName) + File.separator ;
            case "service":
                return "service" + File.separator + StrUtil.toCamelCase(tableName) + File.separator ;
            case "impl":
                return "service" + File.separator + "impl" + File.separator + StrUtil.toCamelCase(tableName) + File.separator ;
            default:
                return "";
        }
    }

    /**
     * 从资源文件下 通过io流读取文件
     *
     * @return
     * @throws IOException
     */
    private String getTemplateStr(String templateName) {
        try {
            org.springframework.core.io.Resource resource = new DefaultResourceLoader().getResource("/templates/tmpl/" + templateName);
            OutputStream out = new ByteArrayOutputStream();
            long copy = IoUtil.copy(resource.getInputStream(), out, IoUtil.DEFAULT_BUFFER_SIZE);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException("render template exception", e);
        }
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