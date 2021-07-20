package finley.gmair.scene.service;

import cn.jpush.api.push.PushResult;
import finley.gmair.scene.dto.AppMsgPushDTO;

import java.util.List;
import java.util.Map;

/**
 * @author : Lyy
 * @create : 2021-01-15 14:32
 * @description 消息推送接口，推送到App（android和iOS）
 **/
public interface AppMsgPushService {

    boolean sendPush(AppMsgPushDTO appMsgPushDTO);

    /**
     * 原生方式推送
     *
     * @param title     App通知栏标题
     * @param content   App通知栏内容（为了单行显示全，尽量保持在22个汉字以下）
     * @param extrasMap 额外推送信息（不会显示在通知栏，传递数据用）
     * @param alias     别名数组，设定哪些用户手机能接收信息（为空则所有用户都推送）
     */
    PushResult sendPush(String title, String content, Map<String, String> extrasMap, String... alias);

    // 获取所有符合条件的可推送用户
    List<String> getPushUsers();
}
