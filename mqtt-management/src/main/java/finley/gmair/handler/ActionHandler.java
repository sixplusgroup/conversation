package finley.gmair.handler;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 行为校验处理器
 *
 * @author lycheeshell
 * @date 2020/12/5 00:12
 */
@Component
public class ActionHandler {

    /**
     * 从数据库加载行为可以包含的属性并记录到内存中
     */
    @PostConstruct
    public void init() {
        //todo
    }


}
