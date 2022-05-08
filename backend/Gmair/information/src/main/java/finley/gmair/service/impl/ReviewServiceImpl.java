package finley.gmair.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import finley.gmair.dao.review.ReviewMapper;
import finley.gmair.dto.review.ReviewDTO;
import finley.gmair.model.chatlogReview.Customer;
import finley.gmair.model.chatlogReview.Message;
import finley.gmair.model.chatlogReview.Session;
import finley.gmair.model.chatlogReview.Staff;
import finley.gmair.pagination.PageResult;
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
    public PageResult getSessionByCondition(Map<String, Object> condition, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Session> sessions = reviewMapper.getSessionByCondition(condition);
        PageInfo<Session> pageInfo = new PageInfo<>(sessions);
        System.out.println("sessions "+pageInfo.getPages());
        PageResult pageResult = new PageResult();
        pageResult.setList(sessions);
        pageResult.setTotalNum(pageInfo.getPages());
        return pageResult;
    }
    @Override
    public     List<Message> getMessageByCondition(Map<String,Object> condition){
        return reviewMapper.getMessageByCondition(condition);
    }
}
