package finley.gmair.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ChatlogFetchService {
    // 每月最后一日1am获取
    @Scheduled(cron = "0 0 1 L * ?")
    public void fetch() {
        fetchChatList();
        fetchChatLog();
        sendChatLog();
    }

    private void fetchChatList() {
    }

    private void fetchChatLog() {
    }

    private void sendChatLog() {
    }
}
