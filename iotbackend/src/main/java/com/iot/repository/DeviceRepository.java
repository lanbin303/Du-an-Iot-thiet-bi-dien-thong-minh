package com.iot.repository;
import com.iot.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByDeviceId(String deviceId);
    List<Device> findByUserUsername(String username);
    boolean existsByDeviceId(String deviceId);
}
