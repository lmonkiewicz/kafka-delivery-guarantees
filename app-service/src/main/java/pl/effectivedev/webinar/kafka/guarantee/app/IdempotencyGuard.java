package pl.effectivedev.webinar.kafka.guarantee.app;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class IdempotencyGuard {

    private final Set<UUID> seenCommandIds = new HashSet<>();

    public boolean isDuplicate(UUID commandId) {
        if (seenCommandIds.contains(commandId)) {
            return true;

        } else {
            seenCommandIds.add(commandId);
            return false;
        }
    }
}
