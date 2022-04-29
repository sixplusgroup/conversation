package finley.gmair.service.impl;

import com.github.pagehelper.PageHelper;
import finley.gmair.dao.ReviewMapper;
import finley.gmair.dto.review.ReviewDTO;
import finley.gmair.model.chatlogReview.Customer;
import finley.gmair.model.chatlogReview.Message;
import finley.gmair.model.chatlogReview.Session;
import finley.gmair.model.chatlogReview.Staff;
import finley.gmair.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewMapper reviewMapper;
    @Override
    public List<ReviewDTO> getRecommend(int pageIndex, int pageSize){
        return null;
    }

    @Override
    public List<Staff> getAllStaff(){
        return reviewMapper.getAllStaff();
    }

    @Override
    public List<Customer> getAllCustomerByStaff(int staffId){
        return reviewMapper.getAllCustomerByStaff(staffId);
    }

    @Override
    public     List<Session> getSessionByCondition(Map<String, Object> condition, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        return reviewMapper.getSessionByCondition(condition);
    }
    @Override
    public     List<Message> getMessageByCondition(Map<String,Object> condition){
        return reviewMapper.getMessageByCondition(condition);
    }
}
