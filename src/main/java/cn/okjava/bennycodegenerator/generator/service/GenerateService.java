package cn.okjava.bennycodegenerator.generator.service;

import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
import cn.okjava.bennycodegenerator.generator.bean.TableEntity;

import java.util.List;

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
     * 查询表中的所有列
     *
     * @param tableName
     * @return
     */
    List<ColumnEntity> queryTableColumns(String tableName);

}
