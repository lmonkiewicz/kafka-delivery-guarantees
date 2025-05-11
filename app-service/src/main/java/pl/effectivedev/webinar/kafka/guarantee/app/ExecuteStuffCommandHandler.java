package pl.effectivedev.webinar.kafka.guarantee.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;

import static pl.effectivedev.webinar.kafka.guarantee.app.ExecuteStuffCommandHandler.*;

@Component(BINDING)
@Slf4j
@RequiredArgsConstructor
public class ExecuteStuffCommandHandler
        implements Function<Message<ExecuteStuffCommand>, Message<StuffExecutedEvent>> {

    private final IdempotencyGuard idempotencyGuard;

    public static final String BINDING = "executeStuffCommand";

    @Override
    public Message<StuffExecutedEvent> apply(Message<ExecuteStuffCommand> message) {
        final UUID commandId = message.getPayload().commandId();

        if (idempotencyGuard.isDuplicate(commandId)) {
            log.info("Duplicate detected (commandId: {}), skipping...", commandId);
            return null;
        }

        var key = message.getHeaders().get(KafkaHeaders.RECEIVED_KEY);
        var input = message.getPayload();
        log.info("Received command: {}:{}", key, input);

        var event = new StuffExecutedEvent(
                UUID.randomUUID(),
                input.commandId(),
                LocalDateTime.now(),
                input.stuffId()
        );
        var eventKey = event.stuffId();

        log.info("Producing event in response: {}:{}", eventKey, event);
        return MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.KEY, eventKey)
                .build();
    }

    public record ExecuteStuffCommand(UUID commandId, LocalDateTime timestamp, String stuffId) {
    }

    public record StuffExecutedEvent(UUID eventId, UUID relatedCommandId, LocalDateTime timestamp, String stuffId) {
    }

}
