package finley.gmair.scene.service.impl;

import cn.jpush.api.push.PushResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import finley.gmair.scene.dao.repository.UserMsgPushMapper;
import finley.gmair.scene.dto.AppMsgPushDTO;
import finley.gmair.scene.entity.UserMsgPushDO;
import finley.gmair.scene.service.AppMsgPushService;
import finley.gmair.scene.utils.JPushUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : Lyy
 * @create : 2021-01-15 14:32
 **/
@Slf4j
@Service
public class AppMsgPushServiceImpl implements AppMsgPushService {

    @Resource
    private JPushUtil jPushUtil;

    @Resource
    UserMsgPushMapper userMsgPushMapper;

    @Override
    public boolean sendPush(AppMsgPushDTO appMsgPush) {
        Map<String, String> extraMap = Maps.newHashMap();
        if (MapUtils.isNotEmpty(appMsgPush.getExtrasMap())) {
            extraMap = appMsgPush.getExtrasMap();
        }
        PushResult result;
        if (CollectionUtils.isNotEmpty(appMsgPush.getAlias())) {
            String[] alias = appMsgPush.getAlias().toArray(new String[0]);
            result = sendPush(appMsgPush.getTitle(), appMsgPush.getContent(), extraMap, alias);
        } else {
            result = sendPush(appMsgPush.getTitle(), appMsgPush.getContent(), extraMap);
        }
        if (!result.isResultOK()) {
            log.error("app msg push err, code is: {},msg is: {}", result.error.getCode(), result.error.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 原生方式推送
     *
     * @param title     App通知栏标题
     * @param content   App通知栏内容（为了单行显示全，尽量保持在22个汉字以下）
     * @param extrasMap 额外推送信息（不会显示在通知栏，传递数据用）
     * @param alias     别名数组，设定哪些用户手机能接收信息（为空则所有用户都推送）
     */
    @Override
    public PushResult sendPush(String title, String content, Map<String, String> extrasMap, String... alias) {
        return jPushUtil.sendPush(title, content, extrasMap, alias);
    }

    // 获取表里所有开启了消息通知的用户ID
    @Override
    public List<String> getPushUsers() {
        QueryWrapper<UserMsgPushDO> wrapper = new QueryWrapper<>();
        wrapper.eq("status", true);
        List<UserMsgPushDO> userMsgPushDOS = userMsgPushMapper.selectList(wrapper);
        return userMsgPushDOS.stream().map(UserMsgPushDO::getConsumerId).collect(Collectors.toList());
    }
}
