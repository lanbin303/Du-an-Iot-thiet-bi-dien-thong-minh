idf_component_register(
    SRCS "main.c"
    INCLUDE_DIRS "."
    REQUIRES 
        wifi_provisioning
        protocomm
        esp_wifi
        nvs_flash
        mqtt
        driver
        qrcode
)