package cn.okjava.bennycodegenerator.generator.repository;

import cn.okjava.bennycodegenerator.generator.bean.TableEntity;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author benny
 * @version 1.0
 * description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TableRepositoryTest {

    @Resource
    private TableRepository generateRepository;

    @Test
    public void should_return_all_db_table_info() {
        List<TableEntity> all = generateRepository.findByTableSchemaEquals("okbbb");
        Assertions.assertThat(all).as("校验查询指定数据所有的表信息").isNotNull().hasSizeGreaterThan(0);
    }

    @Test
    public void findByTableSchemaAndTableName() {
        TableEntity tableEntity = generateRepository.findByTableSchemaAndTableName("okbbb", "ad");
        Assertions.assertThat(tableEntity).as("根据表明查询表详情信息").isNotNull();
    }
}