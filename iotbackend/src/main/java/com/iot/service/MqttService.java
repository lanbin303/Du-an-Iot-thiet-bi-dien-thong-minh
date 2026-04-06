package com.iot.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.model.TelemetryData;
import com.iot.repository.DeviceRepository;
import com.iot.repository.TelemetryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service @Slf4j @RequiredArgsConstructor
public class MqttService {
    private final TelemetryRepository telemetryRepository;
    private final DeviceRepository deviceRepository;
    private final SimpMessagingTemplate websocket;
    private final MessageChannel mqttOutboundChannel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<String> message) {
        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC, String.class);
        String payload = message.getPayload();
        if (topic == null) return;
        if (topic.endsWith("/telemetry")) handleTelemetry(payload);
        else if (topic.endsWith("/status")) handleStatus(topic, payload);
    }

    private void handleTelemetry(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            String deviceId = node.get("device_id").asText();
            TelemetryData data = TelemetryData.builder()
                    .deviceId(deviceId)
                    .voltage(node.get("voltage").asDouble())
                    .current(node.get("current").asDouble())
                    .power(node.get("power").asDouble())
                    .energy(node.get("energy").asDouble())
                    .frequency(node.get("frequency").asDouble())
                    .powerFactor(node.get("power_factor").asDouble())
                    .status(node.get("status").asText())
                    .timestamp(LocalDateTime.now())
                    .build();
            telemetryRepository.save(data);
            deviceRepository.findByDeviceId(deviceId).ifPresent(device -> {
                device.setLastSeen(LocalDateTime.now());
                device.setOnline(true);
                device.setStatus(data.getStatus());
                deviceRepository.save(device);
            });
            websocket.convertAndSend("/topic/device/" + deviceId + "/telemetry", data);
        } catch (Exception e) { log.error("Error handling telemetry: {}", e.getMessage()); }
    }

    private void handleStatus(String topic, String payload) {
        try {
            String[] parts = topic.split("/");
            if (parts.length < 2) return;
            String deviceId = parts[1];
            JsonNode node = objectMapper.readTree(payload);
            String status = node.get("status").asText();
            deviceRepository.findByDeviceId(deviceId).ifPresent(device -> {
                device.setStatus(status);
                device.setLastSeen(LocalDateTime.now());
                device.setOnline(true);
                deviceRepository.save(device);
            });
            websocket.convertAndSend("/topic/device/" + deviceId + "/status",
                    Map.of("deviceId", deviceId, "status", status));
        } catch (Exception e) { log.error("Error handling status: {}", e.getMessage()); }
    }

    public void sendCommand(String deviceId, String command) {
        String topic = "device/" + deviceId + "/control";
        String payload = "{\"command\":\"" + command + "\"}";
        Message<String> msg = MessageBuilder.withPayload(payload)
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.QOS, 1).build();
        mqttOutboundChannel.send(msg);
        log.info("Command sent to {}: {}", deviceId, command);
    }
}
