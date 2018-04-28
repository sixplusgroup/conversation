package finley.gmair.service.impl;

import finley.gmair.model.installation.Member;
import finley.gmair.service.InstallerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstallerDetailServiceImpl implements UserDetailsService {

    @Autowired
    private InstallerService installerService;

    @Override
    public UserDetails loadUserByUsername(String openid) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> authorities = List.of();
        ResultData response = installerService.exist(openid);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            throw new UsernameNotFoundException("no matching installer");
        }
        //user name login
        Member member = ((List<Member>) response.getData()).get(0);
        return new User(member.getMemberPhone(), "", authorities);
    }
}
