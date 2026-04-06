# 🔌 IoT Power Monitor System

Hệ thống giám sát và điều khiển thiết bị điện thông minh sử dụng ESP32, MQTT, Spring Boot và Flutter.

## 📋 Tổng quan hệ thống

```
ESP32 + PZEM-004T
      ↓ MQTT
EMQX Broker
      ↓
Spring Boot Backend
      ↓ REST API + WebSocket
Flutter Mobile App
```

## 🛠️ Công nghệ sử dụng

| Thành phần | Công nghệ |
|------------|-----------|
| IoT Device | ESP32 + PZEM-004T + Relay |
| Firmware | ESP-IDF (C) |
| Message Broker | EMQX 5.0 |
| Backend | Spring Boot 3.2 + Java 17 |
| Database | PostgreSQL |
| Mobile App | Flutter |
| Protocol | MQTT + WebSocket + REST API |
| Auth | JWT Token |

## 📁 Cấu trúc project

```
baocaomonhocIOT/
├── iotbackend/          # Spring Boot Backend
│   ├── src/
│   │   └── main/java/com/iot/
│   │       ├── config/       # MQTT, Security, WebSocket config
│   │       ├── controller/   # REST API endpoints
│   │       ├── service/      # Business logic
│   │       ├── model/        # JPA Entities
│   │       ├── repository/   # Database repositories
│   │       ├── security/     # JWT Filter, UserDetails
│   │       └── dto/          # Data Transfer Objects
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── pom.xml
│
└── TinyUI/
    └── nguyentrinhlan/  # Flutter Mobile App
        ├── lib/
        │   ├── config/       # API config
        │   ├── services/     # Auth, Device services
        │   └── main.dart
        └── pubspec.yaml
```

## 🚀 Hướng dẫn chạy

### Yêu cầu
- Docker Desktop
- Java 17+
- Flutter SDK
- Android Studio / VS Code

### 1. Khởi động Infrastructure

```bash
# Khởi động PostgreSQL và EMQX
docker start my-postgres
docker start emqx
```

### 2. Chạy Backend

```bash
cd iotbackend
./mvnw spring-boot:run
```

Backend chạy tại: http://localhost:8080

### 3. Chạy Flutter App

```bash
cd TinyUI/nguyentrinhlan
flutter pub get
flutter run
```

## 📡 API Endpoints

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | /api/auth/register | Đăng ký tài khoản |
| POST | /api/auth/login | Đăng nhập |
| GET | /api/devices | Danh sách thiết bị |
| POST | /api/devices/register | Đăng ký thiết bị mới |
| POST | /api/devices/{id}/control | Điều khiển ON/OFF |
| GET | /api/devices/{id}/telemetry | Lịch sử cảm biến |

## 📊 MQTT Topics

| Topic | Chiều | Mô tả |
|-------|-------|-------|
| device/{id}/telemetry | ESP32 → Server | Dữ liệu cảm biến |
| device/{id}/status | ESP32 → Server | Trạng thái ON/OFF |
| device/{id}/control | Server → ESP32 | Lệnh điều khiển |

## 📱 Tính năng Mobile App

- Đăng ký / Đăng nhập
- Xem danh sách thiết bị
- Điều khiển ON/OFF từ xa
- Xem dữ liệu cảm biến realtime (Voltage, Current, Power, Energy, Frequency, Power Factor)
- Đăng ký thiết bị mới qua mã QR

## ⚙️ Cấu hình

### Backend (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/IoT
    username: postgres
    password: your_password

mqtt:
  broker: tcp://localhost:1883

jwt:
  secret: your-secret-key
  expiration: 86400000
```

### Flutter (lib/config/api_config.dart)
```dart
class ApiConfig {
  static const String baseUrl = 'http://YOUR_IP:8080';
}
```

## 👥 Thành viên nhóm
| Nguyễn Trịnh Lân | | Flutter Developer |
| | | Backend Developer |
| | | Hardware Engineer |

## 📄 License

MIT License
