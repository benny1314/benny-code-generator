package cn.okjava.bennycodegenerator.generator.repository;

import cn.okjava.bennycodegenerator.generator.bean.ColumnEntity;
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
@RunWith(SpringRunner.class)
@SpringBootTest
public class ColumnRepositoryTest {

    @Resource
    private ColumnRepository columnRepository;

    @Test
    public void should_return_table_columns_info() {
        List<ColumnEntity> columns = columnRepository.findByTableSchemaAndTableName("okbbb", "ad");
        Assertions.assertThat(columns).as("查询表中所有列信息").isNotNull().hasSizeGreaterThan(0);
    }

}