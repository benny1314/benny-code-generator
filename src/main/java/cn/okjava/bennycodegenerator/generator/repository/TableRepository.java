package cn.okjava.bennycodegenerator.generator.repository;

import cn.okjava.bennycodegenerator.generator.bean.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    /**
     * 查询所有的数据库名字
     *
     * @param tableSchema
     * @return
     */
    List<TableEntity> findByTableSchemaEquals(String tableSchema);

}
