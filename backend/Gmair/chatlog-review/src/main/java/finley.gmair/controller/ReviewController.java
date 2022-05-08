package finley.gmair.controller;

import finley.gmair.model.chatlogReview.Message;
import finley.gmair.model.chatlogReview.Session;
import finley.gmair.pagination.PageParam;
import finley.gmair.service.ReviewService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/review")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @GetMapping("recommend")
    public ResultData getRecommend(@RequestBody PageParam pageParam){
        return null;
    }

    /**
     *@Description 获得所有客服信息，包括id和name
     *@Author great fish
     *@Date  2022/4/29
     *@param
     *
     * @return {@link null }
     */
    
    @GetMapping("getAllStaff")
    public ResultData getAllStaff(){
        return ResultData.ok(reviewService.getAllStaff());
    }

    /**
     *@Description 根据客服ID，获得所有客户ID
     *@Author great fish
     *@Date  2022/4/29
     *@param staffId，客服id
     *
     * @return {@link null }
     */
    
    @GetMapping("getAllCustomer")
    public ResultData getAllCustomer(@RequestParam("staffId") Integer staffId){
        System.out.println(staffId);
        return ResultData.ok(reviewService.getAllCustomerByStaff(staffId));
    }

    @PostMapping("history")
    public ResultData getHistory(@RequestParam("staffId") Integer staffId,@RequestParam("customerId") Integer customerId,@RequestBody PageParam pageParam){
        Map<String,Object> condition = new HashMap<>();
        condition.put("userId",customerId);
        condition.put("waiterId",staffId);
        List<Session> sessions = reviewService.getSessionByCondition(condition,pageParam.getPageNum(),pageParam.getPageSize());
        sessions.forEach((e)->{
            condition.clear();
            condition.put("sessionId",e.getSessionId());
        });
        return null;
    }
}
