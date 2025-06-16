package io.github.gomestdk.rest_with_spring_boot_and_java.config;

public interface TestConfigs {
    int SERVER_PORT = 8888;
    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_LOCALHOST = "http://localhost:8080";
    String ORIGIN_GOMES_TKD = "https://github.com/gomes-tkd";
    String ORIGIN_REACT = "http://localhost:3000/";
    String ORIGIN_ANGULAR = "http://localhost:4200/";
    String ORIGIN_WRONG = "https://localhost:1/";
}
