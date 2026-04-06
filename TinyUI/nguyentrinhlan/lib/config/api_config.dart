class ApiConfig {
  static const String baseUrl = 'http://192.168.1.42:8080';
  static const String wsUrl = 'ws://192.168.1.42:8080/ws/websocket';

  static const String login    = '$baseUrl/api/auth/login';
  static const String register = '$baseUrl/api/auth/register';
  static const String devices  = '$baseUrl/api/devices';

  static String device(String id)    => '$baseUrl/api/devices/$id';
  static String control(String id)   => '$baseUrl/api/devices/$id/control';
  static String telemetry(String id) => '$baseUrl/api/devices/$id/telemetry';
}
