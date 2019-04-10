package finley.gmair.service.impl;

import finley.gmair.model.installation.Member;
import finley.gmair.service.InstallerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName: MemberServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/2 6:13 PM
 */
@Service
public class MemberServiceImpl implements UserDetailsService {

    @Autowired
    private InstallerService installerService;

    @Override
    public UserDetails loadUserByUsername(String openid) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(openid)) {
            throw new UsernameNotFoundException("请提供用户的openid");
        }
        ResultData response = installerService.profileByOpenid(openid);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            throw new UsernameNotFoundException("该用户当前不存在");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            throw new UsernameNotFoundException("处理出现异常，请稍后尝试");
        }
        Member member = (((List<Member>) response.getData()).get(0));
        return new User(member.getMemberId(), "", null);
    }
}
