package kafka.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;

@Builder
public record EnergyUsageEvent(
        Long deviceId,
        Double consumption,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp
) {
}
