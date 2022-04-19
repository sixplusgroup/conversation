package finley.gmair.controller;

import finley.gmair.model.knowledgebaseAuth.KnowledgebaseUser;
import finley.gmair.util.ResultData;
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
     * 将用户名、密码、对应角色Id以name,pwd,releId传进来
     * @Author great fish
     * @Date 16:47 2022/4/17
     */
    @PostMapping("create")
    public ResultData create(@RequestBody Map<String,String > userInfo){
        //todo 写创建用户接口
        //todo 回传到前端的用户信息不要给密码，加个用户类型Type
        //todo pathparam改成requestpram/requestbody
        //todo 给其它controller加上鉴权
        System.out.println(userInfo.get("name"));
        System.out.println(userInfo.get("pwd"));
        System.out.println(userInfo.get("roleId"));
        return ResultData.ok(null);
    }
}
