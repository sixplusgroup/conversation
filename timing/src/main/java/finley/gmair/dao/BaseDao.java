package finley.gmair.dao;

import org.apache.ibatis.session.SqlSession;


public class BaseDao {

    protected SqlSession sqlSession;

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
}
