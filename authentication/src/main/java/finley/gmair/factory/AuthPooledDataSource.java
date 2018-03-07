package finley.gmair.factory;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

public class AuthPooledDataSource extends PooledDataSourceFactory {
    public AuthPooledDataSource() {
        this.dataSource = new PooledDataSource();
    }
}
