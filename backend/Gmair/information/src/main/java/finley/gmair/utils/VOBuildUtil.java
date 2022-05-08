package finley.gmair.utils;

import finley.gmair.dao.review.ReviewMapper;
import finley.gmair.model.chatlogReview.Message;
import finley.gmair.model.chatlogReview.Session;
import finley.gmair.utils.TimeUtil;
import finley.gmair.vo.chatlogReview.DetailVO;
import finley.gmair.vo.chatlogReview.SentiGraphVO;
import finley.gmair.vo.chatlogReview.ThumbnailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VOBuildUtil {
    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    TimeUtil timeUtil;
    public ThumbnailVO buildThumbnailVO (Session session, List<Message> messages){
        ThumbnailVO thumbnailVO = new ThumbnailVO();
        thumbnailVO.setPid(session.getProductId());
        thumbnailVO.setCname(reviewMapper.getCustomerById(session.getUserId()).getUserName());
        thumbnailVO.setSession_id(session.getSessionId());
        List<SentiGraphVO> sentiGraphVOS = new ArrayList<>();
        for (Message  message:messages){
            SentiGraphVO sentiGraphVO = new SentiGraphVO();
            sentiGraphVO.setEmoRate(message.getScore());
            sentiGraphVO.setWaiterSend(message.isFromWaiter());
            sentiGraphVO.setTime(timeUtil.getDateTime(message.getTimestamp()));
            sentiGraphVOS.add(sentiGraphVO);
        }
        thumbnailVO.setSentigraph(sentiGraphVOS);
        return thumbnailVO;
    }

    public DetailVO buildDetailVO(Session session, List<Message> messages){
        DetailVO detailVO = new DetailVO();
        detailVO.setPid(session.getProductId());
        detailVO.setCname(reviewMapper.getCustomerById(session.getUserId()).getUserName());
        detailVO.setSession_id(session.getSessionId());
        detailVO.setCustomerAverageScore(session.getCustomerAverageScore());
        detailVO.setCustomerExtremeNegativeCount(session.getCustomerExtremeNegativeCount());
        detailVO.setWaiterNegativeCount(session.getWaiterNegativeCount());

        List<SentiGraphVO> sentiGraphVOS = new ArrayList<>();
        for (Message  message:messages){
            SentiGraphVO sentiGraphVO = new SentiGraphVO();
            sentiGraphVO.setEmoRate(message.getScore());
            sentiGraphVO.setWaiterSend(message.isFromWaiter());
            sentiGraphVO.setTime(timeUtil.getDateTime(message.getTimestamp()));
            sentiGraphVO.setContent(message.getContent());
            sentiGraphVOS.add(sentiGraphVO);
        }
        detailVO.setSentigraph(sentiGraphVOS);
        return detailVO;
    }
}
