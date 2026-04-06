import 'dart:convert';
import 'package:http/http.dart' as http;
import '../config/api_config.dart';
import 'auth_service.dart';

class DeviceService {

  static Future<List<dynamic>> getDevices() async {
    try {
      final headers = await AuthService.authHeaders();
      final res = await http.get(Uri.parse(ApiConfig.devices), headers: headers);
      if (res.statusCode == 200) return jsonDecode(res.body);
      return [];
    } catch (e) {
      return [];
    }
  }

  static Future<bool> registerDevice(String deviceId, String name) async {
    try {
      final headers = await AuthService.authHeaders();
      final res = await http.post(
        Uri.parse('${ApiConfig.devices}/register'),
        headers: headers,
        body: jsonEncode({'deviceId': deviceId, 'name': name}),
      );
      return res.statusCode == 200;
    } catch (e) {
      return false;
    }
  }

  static Future<bool> control(String deviceId, String command) async {
    try {
      final headers = await AuthService.authHeaders();
      final res = await http.post(
        Uri.parse(ApiConfig.control(deviceId)),
        headers: headers,
        body: jsonEncode({'command': command}),
      );
      return res.statusCode == 200;
    } catch (e) {
      return false;
    }
  }

  static Future<List<dynamic>> getTelemetry(String deviceId, {int limit = 50}) async {
    try {
      final headers = await AuthService.authHeaders();
      final res = await http.get(
        Uri.parse('${ApiConfig.telemetry(deviceId)}?limit=$limit'),
        headers: headers,
      );
      if (res.statusCode == 200) return jsonDecode(res.body);
      return [];
    } catch (e) {
      return [];
    }
  }
}
