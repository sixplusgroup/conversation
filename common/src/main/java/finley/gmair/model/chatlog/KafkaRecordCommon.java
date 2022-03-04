package finley.gmair.model.chatlog;

public interface KafkaRecordCommon {
    String KEY_INT_SESSION_ID = "sessionId";
    String KEY_INT_MESSAGE_ID = "messageId";
    String KEY_STRING_MESSAGE_CONTENT = "messageContent";
    String KEY_JSON_ARRAY_MESSAGES = "messages";
}
