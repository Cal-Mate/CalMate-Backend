//package com.ateam.calmate.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;
//import reactor.netty.resources.ConnectionProvider;
//
//import java.time.Duration;
//import java.util.List;
//
//@Configuration
//public class HttpClientConfig {
//    // [1] 커넥션 풀(전역) 설정
//    //  - HTTP 커넥션의 "개수/대기열/수명"을 관리하는 실제 풀 객체입니다.
//    //  - 애플리케이션에 1개만 두고 여러 WebClient가 공유하는 패턴
//    @Bean
//    public ConnectionProvider globalPool() {
//        return ConnectionProvider.builder("global-http-pool")
//                .maxConnections(500)               // 동시에 유지 가능한 커넥션 수(전역)
//                .pendingAcquireMaxCount(2000)      // 풀 바닥났을 때 대기열 허용 개수
//                .maxIdleTime(Duration.ofSeconds(30)) // 유휴 커넥션 정리 기준
//                .maxLifeTime(Duration.ofMinutes(5))  // 커넥션 최대 수명(롤링/LB 대비)
//                .build();
//    }
//
//    // [2] 커넥터(전송 계층) 구성
//    //  - Reactor Netty HttpClient에 [1]의 풀을 물리고, 타임아웃 등 전송 옵션을 설정
//    //  - 이 커넥터를 WebClient가 사용해 실제로 소켓/커넥션을 빌리고 반환
//    @Bean
//    public ReactorClientHttpConnector connector(ConnectionProvider globalPool) {
//        HttpClient httpClient = HttpClient.create(globalPool)
//                .responseTimeout(Duration.ofSeconds(5)); // 응답 타임아웃(헤더/바디 첫 바이트까지)
//        return new ReactorClientHttpConnector(httpClient);
//    }
//
//    // [3] 전역 WebClient.Builder (설정용 팩토리)
//    //  - 불변 WebClient를 "만드는" 공장(빌더 자체는 싱글톤 빈).
//    //  - 여기서 기본 헤더/필터(로깅, 에러매핑, 트레이싱)를 넣어두면
//    //    이후 서비스별 WebClient 만들 때 상속/복제해 쓰기 좋음
//    @Bean
//    public WebClient.Builder webClientBuilder(ReactorClientHttpConnector connector) {
//        return WebClient.builder()
//                .clientConnector(connector) // 전송 계층(풀 포함) 연결
//                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE); // 전역 기본 Accept (요청/서비스에서 덮어쓰기 가능)
//        // .filter(observabilityFilter())
//        // .filter(errorMappingFilter())
//    }
//
//    // [4] WebClient (싱글톤 객체)
//    //  - 애플리케이션 시작시 한 번 생성 → 이후 계속 재사용
//    //  - 커넥션은 "1개"가 아니라 [1]의 풀에서 "필요한 만큼" 요청 시마다 자동 대여/반환
//    @Bean
//    public WebClient servicePublicDataClient(WebClient.Builder builder) {
//        return builder.clone()                // 전역 builder 상태 복제(스레드 안전)
//                .defaultHeaders(h -> {
//                    h.setAccept(List.of(MediaType.APPLICATION_JSON));
//                })
//                .build();                     // 완성된 불변 WebClient
//    }
//}
