package com.back.domain.order.order.service;

import com.back.global.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TossPaymentsService {
    private static final String BASE_URL = "https://api.tosspayments.com";
    private static final String PAYMENTS_PATH = "/v1/payments";

    private final RestClient restClient;

    @Value("${custom.toss.payments.secret-key:}")
    private String secretKey;

    public TossPaymentsService() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public Map<String, Object> confirmPayment(String paymentKey, String orderId, int amount) {
        return executeRequest(
                HttpMethod.POST,
                PAYMENTS_PATH + "/confirm",
                Map.of("paymentKey", paymentKey, "orderId", orderId, "amount", amount),
                "결제 승인"
        );
    }

    private Map<String, Object> executeRequest(
            HttpMethod method,
            String uri,
            Map<String, Object> requestBody,
            String operation
    ) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = createRequest(method, uri, requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) ->
                            log.error("{} HTTP 에러: {}", operation, res.getStatusCode())
                    )
                    .body(Map.class);

            validateResponse(response, operation);

            log.info("{} 성공", operation);
            return response;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("{} 중 오류 발생", operation, e);
            throw new BusinessException("PAYMENT-ERROR", operation + " 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * HTTP 메서드에 따라 요청 생성
     */
    private RestClient.RequestHeadersSpec<?> createRequest(
            HttpMethod method,
            String uri,
            Map<String, Object> requestBody
    ) {
        RestClient.RequestHeadersSpec<?> requestSpec;

        if (method == HttpMethod.GET) {
            requestSpec = restClient.get().uri(uri);
        } else if (method == HttpMethod.POST) {
            requestSpec = restClient.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody);
        } else {
            throw new IllegalArgumentException("지원하지 않는 HTTP 메서드: " + method);
        }

        return requestSpec
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.setBasicAuth(secretKey, ""));
    }

    /**
     * 응답 검증 (에러 필드 확인)
     */
    @SuppressWarnings("unchecked")
    private void validateResponse(Map<String, Object> response, String operation) {
        Optional.ofNullable(response)
                .filter(res -> res.containsKey("error"))
                .map(res -> (Map<String, Object>) res.get("error"))
                .ifPresent(error -> {
                    String code = (String) error.get("code");
                    String message = (String) error.get("message");
                    log.error("{} 실패: code={}, message={}", operation, code, message);
                    throw new BusinessException("400-" + code, message);
                });
    }
}
