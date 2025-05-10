package pl.effectivedev.webinar.kafka.guarantee.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class PublishingSchedule {

    private final ExecuteStuffCommandPublisher publisher;

    @Scheduled(fixedRate = 3000)
    public void publish() {
        publisher.publish();
    }

}
