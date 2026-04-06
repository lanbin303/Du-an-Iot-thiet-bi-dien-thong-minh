package com.iot.controller;
import com.iot.dto.ControlRequest;
import com.iot.dto.DeviceRegisterRequest;
import com.iot.service.DeviceService;
import com.iot.service.MqttService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/devices") @RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;
    private final MqttService mqttService;

    @GetMapping public ResponseEntity<?> getDevices(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(deviceService.getDevicesByUser(user.getUsername()));
    }
    @GetMapping("/{deviceId}") public ResponseEntity<?> getDevice(@PathVariable String deviceId) {
        return ResponseEntity.ok(deviceService.getDevice(deviceId));
    }
    @PostMapping("/register") public ResponseEntity<?> register(@RequestBody DeviceRegisterRequest body,
                                                                @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(deviceService.registerDevice(body.getDeviceId(), body.getName(), user.getUsername()));
    }
    @PostMapping("/{deviceId}/control") public ResponseEntity<?> control(@PathVariable String deviceId,
                                                                          @RequestBody ControlRequest body) {
        mqttService.sendCommand(deviceId, body.getCommand());
        return ResponseEntity.ok(Map.of("message", "Command sent", "command", body.getCommand()));
    }
    @GetMapping("/{deviceId}/telemetry") public ResponseEntity<?> getTelemetry(@PathVariable String deviceId,
                                                                                 @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(deviceService.getTelemetry(deviceId, limit));
    }
}
