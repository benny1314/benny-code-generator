package cn.okjava.bennycodegenerator.generator.service;

import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
import cn.okjava.bennycodegenerator.generator.bean.ConfigEntity;
import cn.okjava.bennycodegenerator.generator.bean.TableEntity;

import java.util.List;
import java.util.Map;

/**
 * @author benny
 * @version 1.0
 * description:
 */
public interface GenerateService {
    /**
     * 查询数据库中的所有表
     *
     * @return
     */
    List<TableEntity> queryAllTables();

    /**
     * 根据表明查询表实体信息
     *
     * @param tableName
     * @return
     */
    TableEntity queryTableByTableName(String tableName);

    /**
     * 查询表中的所有列
     *
     * @param tableName
     * @return
     */
    List<ColumnEntity> queryTableColumns(String tableName);

    /**
     * 生成代码
     *
     * @param tableName 表名
     * @return
     */
    Map<String,String> generate(String tableName);

    /**
     * 保存生成代码配置
     *
     * @param configEntity 表名
     * @return
     */
    String saveGenerateConfig(ConfigEntity configEntity);
}
