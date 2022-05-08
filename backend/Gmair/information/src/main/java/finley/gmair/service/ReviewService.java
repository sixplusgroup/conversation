package finley.gmair.service;

import finley.gmair.dto.review.ReviewDTO;
import finley.gmair.model.chatlogReview.Customer;
import finley.gmair.model.chatlogReview.Message;
import finley.gmair.model.chatlogReview.Staff;
import finley.gmair.pagination.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReviewService {
    List<ReviewDTO> getRecommend(int pageIndex, int pageSize);

    List<Staff> getAllStaff();

    List<Customer> getAllCustomerByStaff(int staffId);


    /**
     *@Description 根据条件分页拿会话
     *@Author great fish
     *@Date  2022/4/29
     *@param condition，一个map，key可以有sessionId、userId、waiterId、productId、isTypical
     *@param pageNum
     *@param pageSize
     *
     * @return {@link null }
     */

    PageResult getSessionByCondition(Map<String, Object> condition, int pageNum, int pageSize);

    /**
     *@Description 根据条件拿消息
     *@Author great fish
     *@Date  2022/4/29
     *@param condition  一个map，key可以有messageId、sessionId、isFromWaiter、label
     *
     * @return {@link null }
     */

    List<Message> getMessageByCondition(Map<String, Object> condition);

}
