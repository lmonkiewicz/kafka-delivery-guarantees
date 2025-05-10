package pl.effectivedev.webinar.kafka.guarantee.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static pl.effectivedev.webinar.kafka.guarantee.consumer.StuffExecutedEventHandler.BINDING;
import static pl.effectivedev.webinar.kafka.guarantee.consumer.StuffExecutedEventHandler.StuffExecutedEvent;

@Component(BINDING)
@Slf4j
public class StuffExecutedEventHandler implements Consumer<Message<StuffExecutedEvent>> {
    public static final String BINDING = "stuffExecutedEvent";

    @Override
    public void accept(Message<StuffExecutedEvent> message) {
        var key = message.getHeaders().get(KafkaHeaders.RECEIVED_KEY);
        log.info("Received event: {}:{}", key, message.getPayload());
    }


    public record StuffExecutedEvent(UUID eventId, UUID relatedCommandId, LocalDateTime timestamp, String stuffId) {
    }

}
