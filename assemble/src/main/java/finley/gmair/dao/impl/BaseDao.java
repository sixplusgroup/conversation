package finley.gmair.dao.Impl;

import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;

public class BaseDao {

    @Resource
    protected SqlSession sqlSession;

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
}
