package finley.gmair.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import finley.gmair.bean.exception.MembershipGlobalException;
import finley.gmair.model.MembershipUser;
import finley.gmair.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Joby
 */
@RestController
@RequestMapping("/p/membership")
@AllArgsConstructor

public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping(value = "/enroll")
    public ResponseEntity<Void> enroll(String consumerId) {
        if(membershipService.count(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getConsumerId, consumerId))!=0){
            throw new MembershipGlobalException("该用户已是会员, 请联系管理员处理!");
        }
        membershipService.createMembership(consumerId);
        return ResponseEntity.ok().build();
    }

}
