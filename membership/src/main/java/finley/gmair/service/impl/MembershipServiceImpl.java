package finley.gmair.service.impl;

import finley.gmair.dao.MembershipDao;
import finley.gmair.model.membership.MembershipConsumer;
import finley.gmair.service.MembershipService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MembershipServiceImpl
 * @Description TODO
 * @Author Joby
 * @Date 2021/7/16 17:30
 */
@Service
public class MembershipServiceImpl implements MembershipService {
    @Autowired
    private MembershipDao membershipDao;
    @Override
    public ResultData create(String consumerId) {
        MembershipConsumer member = new MembershipConsumer(consumerId);
        return membershipDao.insert(member);
    }
}
