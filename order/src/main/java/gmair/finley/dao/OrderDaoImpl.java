package gmair.finley.dao;

import finley.gmair.util.ResultData;
import gmair.finley.model.GmairOrder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl extends BaseDao implements OrderDao {
    public ResultData insert(GmairOrder order) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.order.insert", order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
