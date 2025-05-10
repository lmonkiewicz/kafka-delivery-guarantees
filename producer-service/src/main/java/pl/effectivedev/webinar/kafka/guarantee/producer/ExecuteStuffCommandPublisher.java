package pl.effectivedev.webinar.kafka.guarantee.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExecuteStuffCommandPublisher {

    private static final String BINDING = "executeStuffCommand";

    private final StreamBridge streamBridge;
    private final List<String> stuffIds = List.of(
            "Stuff_A",
            "Stuff_B",
            "Stuff_C",
            "Stuff_D",
            "Stuff_E",
            "Stuff_F"
    );
    private final SecureRandom secureRandom = new SecureRandom();

    public void publish() {
        final ExecuteStuffCommand payload = new ExecuteStuffCommand(
                UUID.randomUUID(),
                LocalDateTime.now(),
                stuffIds.get(secureRandom.nextInt(stuffIds.size()))
        );
        var commandKey = payload.stuffId();
        streamBridge.send(BINDING, MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.KEY, commandKey)
                .build());
        log.info("Message produced to {}: {}:{}", BINDING, commandKey, payload);
    }

    public record ExecuteStuffCommand(UUID commandId, LocalDateTime timestamp, String stuffId) {
    }
}
