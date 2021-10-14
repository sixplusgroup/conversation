package finley.gmair.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.model.membership.MembershipUser;
import finley.gmair.service.MembershipService;
import finley.gmair.util.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author Joby
 */
@RestController
@RequestMapping("/membership/membership")
@AllArgsConstructor
@Validated
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping(value = "/enroll")
    public ResponseData<Void> enroll(@NotBlank String consumerId) {
        if(membershipService.count(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getConsumerId, consumerId))!=0){
            throw new MembershipGlobalException("该用户已是会员, 请联系管理员处理!");
        }
        membershipService.createMembership(consumerId);
        return ResponseData.ok();
    }

    @PostMapping(value = "/isMembership")
    public ResponseData<Boolean> isMembership(@NotBlank String consumerId){
        int num = membershipService.count(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getConsumerId, consumerId));
        if(num==1) return ResponseData.ok(true);
        else return ResponseData.ok(false);
    }


}
