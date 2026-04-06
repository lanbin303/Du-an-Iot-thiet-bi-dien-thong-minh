package com.iot.service;
import com.iot.model.Device;
import com.iot.model.TelemetryData;
import com.iot.model.User;
import com.iot.repository.DeviceRepository;
import com.iot.repository.TelemetryRepository;
import com.iot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final TelemetryRepository telemetryRepository;
    private final UserRepository userRepository;

    public List<Device> getDevicesByUser(String username) {
        return deviceRepository.findByUserUsername(username);
    }
    public Device registerDevice(String deviceId, String name, String username) {
        if (deviceRepository.existsByDeviceId(deviceId))
            throw new RuntimeException("Device already registered");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return deviceRepository.save(Device.builder()
                .deviceId(deviceId).name(name != null ? name : deviceId)
                .status("OFF").online(false).user(user).build());
    }
    public Device getDevice(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found: " + deviceId));
    }
    public List<TelemetryData> getTelemetry(String deviceId, int limit) {
        return telemetryRepository.findByDeviceIdOrderByTimestampDesc(deviceId, PageRequest.of(0, limit));
    }
}
