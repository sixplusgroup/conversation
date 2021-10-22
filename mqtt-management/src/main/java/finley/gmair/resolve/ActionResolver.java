package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;

/**
 * 行为处理器接口
 *
 * @author lycheeshell
 * @date 2020/12/8 15:23
 */
public interface ActionResolver {

    /**
     * 处理行为
     *
     * @param topic mqtt主题topic，topic的第一个"/"已经删除
     * @param json  mqtt消息内容payload的json
     */
    void resolve(String topic, JSONObject json);

}
