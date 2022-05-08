package finley.gmair.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "finley.gmair.dao.review",sqlSessionTemplateRef  = "reviewSqlSessionTemplate")
public class ReviewDataSourceConfig {

    @Bean(name = "reviewDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.review")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "reviewSqlSessionFactory")
    public SqlSessionFactory knowledgebaseSqlSessionFactory(@Qualifier("reviewDataSource") DataSource dataSource) throws Exception {
        //SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/review/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "reviewSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("reviewSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

