package finley.gmair.resolve;

import java.util.HashMap;
import java.util.Map;

/**
 * 行为处理器的管理工厂
 *
 * @author lycheeshell
 * @date 2020/12/8 15:43
 */
public class ActionResolverFactory {

    private static final Map<String, ActionResolver> ACTION_RESOLVER_MAP = new HashMap<>();

    /**
     * 根据行为名称获取行为处理器
     * 如果没有已注册的对应的行为处理器，则返回null
     *
     * @param action 行为名称
     * @return 行为处理器
     */
    public static ActionResolver getActionResolver(String action) {
        return ACTION_RESOLVER_MAP.get(action);
    }

    /**
     * 注册行为处理器
     *
     * @param action         行为名称
     * @param actionResolver 行为处理器
     */
    public static void register(String action, ActionResolver actionResolver) {
        ACTION_RESOLVER_MAP.put(action, actionResolver);
    }


}
