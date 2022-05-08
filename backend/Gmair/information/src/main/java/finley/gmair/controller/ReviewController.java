package finley.gmair.controller;

import cn.hutool.core.collection.CollectionUtil;
import finley.gmair.dao.review.ReviewMapper;
import finley.gmair.model.chatlogReview.Message;
import finley.gmair.model.chatlogReview.Session;
import finley.gmair.pagination.PageParam;
import finley.gmair.pagination.PageResult;
import finley.gmair.service.ReviewService;
import finley.gmair.util.ResultData;
import finley.gmair.utils.TimeUtil;
import finley.gmair.utils.VOBuildUtil;
import finley.gmair.vo.chatlogReview.ThumbnailVO;
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

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    TimeUtil timeUtil;

    @Autowired
    VOBuildUtil VOBuildUtil;
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

    /**
     *@Description 历史消息
     *@Author great fish
     *@Date  2022/4/29
     *
     * @return {@link null }
     */
    
    @PostMapping("history")
    public ResultData getHistory(@RequestParam("staffId") Integer staffId,@RequestParam("customerId") Integer customerId,@RequestBody PageParam pageParam){
        Map<String,Object> condition = new HashMap<>();
        condition.put("userId",customerId);
        condition.put("waiterId",staffId);
        return ResultData.ok(queryHistoryByCondition(condition,pageParam.getPageNum(),pageParam.getPageSize()));
    }

    /**
     *@Description 分页展示典型案例
     *@Author great fish
     *@Date  2022/4/29
     *@param
     *
     * @return {@link null }
     */

    @PostMapping("recommend")
    public ResultData getRecommend(@RequestBody PageParam pageParam){
        Map<String,Object> condition = new HashMap<>();
        condition.put("isTypical",1);
        return ResultData.ok(queryHistoryByCondition(condition,pageParam.getPageNum(),pageParam.getPageSize()));
    }

    /**
     *@Description 查看详情
     *@Author great fish
     *@Date  2022/4/29
     *@param
     *
     * @return {@link null }
     */

    @GetMapping("detail")
    public ResultData getDetail(@RequestParam("id") int id){
        Map<String,Object> condition = new HashMap<>();
        condition.put("sessionId",id);
        PageResult pageResult = reviewService.getSessionByCondition(condition,1,1);
        List<Session> sessions = pageResult.getList();
        if(CollectionUtil.isEmpty(sessions)){
            return ResultData.error("没有对应id的session");
        }
        Session session = sessions.get(0);
        condition.clear();
        condition.put("sessionId",session.getSessionId());
        List<Message> messages = reviewService.getMessageByCondition(condition);
        return ResultData.ok(VOBuildUtil.buildDetailVO(session,messages));
    }

    private PageResult queryHistoryByCondition(Map<String,Object> condition,int pageNum,int pageSize){
        PageResult pageResult = reviewService.getSessionByCondition(condition,pageNum,pageSize);
        List<Session> sessions = pageResult.getList();
        List<ThumbnailVO> thumbnailVOS = new ArrayList<>();
        for(Session session:sessions){
            condition.clear();
            condition.put("sessionId",session.getSessionId());
            List<Message> messages = reviewService.getMessageByCondition(condition);
            thumbnailVOS.add(VOBuildUtil.buildThumbnailVO(session,messages));
        };
        pageResult.setList(thumbnailVOS);
        return pageResult;
    }



}
