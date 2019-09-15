package cn.okjava.bennycodegenerator.generator.repository;

import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@Repository
public interface ColumnRepository extends JpaRepository<ColumnEntity, Long> {

    /**
     * 查询表中字段信息
     *
     * @param tableSchema
     * @param tableName
     * @return
     */
    List<ColumnEntity> findByTableSchemaAndTableNameOrderByOrdinalPositionAsc(String tableSchema, String tableName);
}
