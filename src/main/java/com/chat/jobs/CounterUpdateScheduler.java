package com.chat.jobs;

import com.chat.services.ApplicationService;
import com.chat.services.ChatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CounterUpdateScheduler {

        private final ApplicationService applicationService;
        private final ChatService chatService;

        public CounterUpdateScheduler(ApplicationService applicationService, ChatService chatService) {
            this.applicationService = applicationService;
            this.chatService = chatService;
        }

        @Scheduled(cron = "#{@environment.getProperty('scheduler.application-counter.cron')}")
        public void updateApplicationCounters() {
            System.out.println("updating application chat counter.....");
            applicationService.updateApplicationCounters();
        }

        @Scheduled(cron = "#{@environment.getProperty('scheduler.chat-counter.cron')}")
        public void updateChatCounters() {
            System.out.println("updating chat messages counter.....");
            chatService.updateChatCounters();
        }
}
