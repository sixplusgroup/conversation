package finley.gmair.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import finley.gmair.dto.installation.MembershipUserDto;
import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.model.membership.MembershipUser;
import finley.gmair.param.installation.MembershipParam;
import finley.gmair.param.membership.MembershipInfoParam;
import finley.gmair.service.MembershipService;
import finley.gmair.util.PaginationParam;
import finley.gmair.util.ResponseData;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @Author Joby
 */
@RestController
@RequestMapping("/membership/membership")
@AllArgsConstructor
@Validated
public class MembershipController {

    private final MembershipService membershipService;

    private final MapperFacade mapperFacade;

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

    @PostMapping("/setMembershipInfo")
    public ResponseData<Void> setMembershipInfo(@Valid @RequestBody MembershipInfoParam membershipInfoParam){
        MembershipUser membershipUser = membershipService.getMembershipByConsumerId(membershipInfoParam.getConsumerId());
        if(membershipUser==null){
            throw new MembershipGlobalException("can not find this membership");
        }
        membershipUser.setUserMobile(membershipInfoParam.getUserMobile());
        membershipUser.setPic(membershipInfoParam.getPic());
        membershipUser.setNickName(membershipInfoParam.getNickName());
        membershipUser.setConsumerName(membershipInfoParam.getConsumerName());
        membershipService.updateMembership(membershipUser);
        return ResponseData.ok();
    }

    @GetMapping("/page")
    public ResponseData<PaginationParam<MembershipUserDto>> page(MembershipParam membershipParam, PaginationParam<MembershipUserDto> page){
        IPage<MembershipUser>  membershipusers = membershipService.page(new Page(page.getCurrent(),page.getSize(),page.getTotal()),
                new LambdaQueryWrapper<MembershipUser>()
                .orderByDesc(page.getCreateTimeSort(),MembershipUser::getCreateTime)
                .eq(membershipParam.getMembershipType()!=null,MembershipUser::getMembershipType,membershipParam.getMembershipType())
                .and(StrUtil.isNotBlank(membershipParam.getUserMobile())||StrUtil.isNotBlank(membershipParam.getConsumerName()) ,i->i
                    .like(StrUtil.isNotBlank(membershipParam.getUserMobile()),MembershipUser::getUserMobile,membershipParam.getUserMobile())
                    .or()
                    .like(StrUtil.isNotBlank(membershipParam.getConsumerName()),MembershipUser::getConsumerName,membershipParam.getConsumerName()))
                );

        page.setCurrent(membershipusers.getCurrent());
        page.setRecords(mapperFacade.mapAsList(membershipusers.getRecords(),MembershipUserDto.class));
        page.setSize(membershipusers.getSize());
        page.setTotal(membershipusers.getTotal());
        return ResponseData.ok(page);

    }
    @GetMapping("/delete")
    public ResponseData<Void> delete(@Valid @NotBlank String id){
        membershipService.deleteMembershipById(id);
        return ResponseData.ok();
    }

}
