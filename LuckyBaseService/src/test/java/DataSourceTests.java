import com.lucky.service.base.LuckyBaseServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

/**
 * @Author:
 * @Date:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes={LuckyBaseServiceApplication.class})// 指定启动类
public class DataSourceTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testDataSource() throws Exception {
        // 获取配置的数据源
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        // 查看配置数据源信息
       log.info("------dataSource connection info:{}",dataSource.getConnection());
    }

}
