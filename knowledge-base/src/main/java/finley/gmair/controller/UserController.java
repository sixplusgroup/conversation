package finley.gmair.controller;

import finley.gmair.enums.knowledgeBase.KnowledgebaseUserType;
import finley.gmair.model.knowledgebaseAuth.KnowledgebaseUser;
import finley.gmair.service.UserService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("knowledge-base/user")
public class UserController {
    /**
     * @Description
     * 超级管理员创建一个新用户
     * 将用户名、密码、对应角色Id以name,pwd,userType传进来
     * @Author great fish
     * @Date 16:47 2022/4/17
     */
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    /**
     *@Description 创建用户接口，只适用于创建普通用户
     *@Author great fish
     *@Date  2022/4/19
     *@param userInfo，需传name和pwd
     *
     * @return {@link ResultData }
     */
    @PreAuthorize("hasAuthority('user_create')")
    @PostMapping("create")
    public ResultData create(@RequestBody Map<String,String > userInfo){
        //todo pathparam改成requestpram/requestbody
        //todo 给其它controller加上鉴权
        String name =  userInfo.get("name");
        String pwd = userInfo.get("pwd");
        pwd = passwordEncoder.encode(pwd);
//        String userType = userInfo.get("userType");

        KnowledgebaseUser knowledgebaseUser = new KnowledgebaseUser();
        knowledgebaseUser.setName(name);
        knowledgebaseUser.setPwd(pwd);
        knowledgebaseUser.setType(KnowledgebaseUserType.USER.getCode());
        userService.create(knowledgebaseUser);

        return ResultData.ok(null);
    }
}
