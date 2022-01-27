package finley.gmair.common;

public interface JingdongCommon {
    // url
    String BASE_URL = "https://api.jd.com/routerjson";
    String CHATLOG_GET_API = "jingdong.im.pop.chatlog.get";
    String VERSION = "2.0";
    String DEFAULT_CHARSET = "UTF-8";

    // key_message
    String KEY_CONTENT ="content";
    String KEY_IS_FROM_WAITER="isFromWaiter";
    String KEY_TIMESTAMP="timestamp";

    String KEY_SESSION_ID="sessionId";

    //key_session
    String KEY_USER_NAME="userName";
    String KEY_USER_ID="userId";
    String KEY_WAITER_NAME="waiterName";
    String KEY_PRODUCT_ID="productId";
    String KEY_CHAT_MESSAGES="chatMessages";

}
