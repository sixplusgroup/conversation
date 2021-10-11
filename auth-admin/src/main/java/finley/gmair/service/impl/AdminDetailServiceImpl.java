package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.model.admin.Admin;
import finley.gmair.service.AdminRoleService;
import finley.gmair.service.AdminService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.role.RoleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminDetailServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(AdminDetailServiceImpl.class);

    @Autowired
    AdminService adminService;

    @Autowired
    AdminRoleService adminRoleService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("username", userName);
        ResultData resultData = adminService.fetchAdmin(condition);

        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            //use email login
            condition.clear();
            condition.put("email", userName);
            resultData = adminService.fetchAdmin(condition);
            logger.info("email response: " + JSON.toJSONString(resultData));
            if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                throw new UsernameNotFoundException("no user");
            }
            Admin admin = ((List<Admin>) resultData.getData()).get(0);
            condition.clear();
            condition.put("adminId", admin.getAdminId());
            condition.put("blockFlag", false);
            resultData = adminRoleService.fetchAdminRole(condition);
            if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<RoleVo> list = (List<RoleVo>) resultData.getData();
                list.forEach(e -> authorities.add(new SimpleGrantedAuthority(e.getRoleName())));
            }
            return new User(admin.getEmail(), admin.getPassword(), authorities);
        }
        //user name login
        Admin admin = ((List<Admin>) resultData.getData()).get(0);
        condition.clear();
        condition.put("adminId", admin.getAdminId());
        condition.put("blockFlag", false);
        resultData = adminRoleService.fetchAdminRole(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<RoleVo> list = (List<RoleVo>) resultData.getData();
            list.forEach(e -> authorities.add(new SimpleGrantedAuthority(e.getRoleName())));
        }
        return new User(admin.getUsername(), admin.getPassword(), authorities);

    }
}
