package com.iot.repository;
import com.iot.model.TelemetryData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime; import java.util.List; import java.util.Optional;
public interface TelemetryRepository extends JpaRepository<TelemetryData, Long> {
    List<TelemetryData> findByDeviceIdOrderByTimestampDesc(String deviceId, Pageable pageable);
    List<TelemetryData> findByDeviceIdAndTimestampBetween(String deviceId, LocalDateTime from, LocalDateTime to);
    Optional<TelemetryData> findTopByDeviceIdOrderByTimestampDesc(String deviceId);
}
