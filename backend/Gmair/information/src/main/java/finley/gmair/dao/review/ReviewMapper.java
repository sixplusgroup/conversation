package finley.gmair.dao.review;

import finley.gmair.model.chatlogReview.Customer;
import finley.gmair.model.chatlogReview.Message;
import finley.gmair.model.chatlogReview.Session;
import finley.gmair.model.chatlogReview.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReviewMapper {
    List<Staff> getAllStaff();

    List<Customer> getAllCustomerByStaff(@Param("staffId") int staffId);

    List<Session> getSessionByCondition(Map<String, Object> condition);

    List<Message> getMessageByCondition(Map<String, Object> condition);

    Customer getCustomerById(int id);
}
