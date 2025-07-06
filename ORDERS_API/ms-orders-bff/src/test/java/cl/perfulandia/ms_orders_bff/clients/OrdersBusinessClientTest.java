package cl.perfulandia.ms_orders_bff.clients;
import cl.perfulandia.ms_orders_bff.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bff.model.dto.OrderItemDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "ms-orders-bs.url=http://localhost:9999"
})
class OrdersBusinessClientTest {

    @Autowired
    private OrdersBusinessClient ordersBusinessClient;

    @Autowired
    private ObjectMapper objectMapper;

    private WireMockServer wireMockServer;
    private OrderDTO testOrderDTO;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();

        OrderItemDTO testOrderItem = new OrderItemDTO();
        testOrderItem.setProductId("PROD-001");
        testOrderItem.setQuantity(2);

        testOrderDTO = new OrderDTO();
        testOrderDTO.setUserId("USER-001");
        testOrderDTO.setCurrency("CLP");
        testOrderDTO.setReturnUrl("https://example.com/return");
        testOrderDTO.setItems(Arrays.asList(testOrderItem));
        testOrderDTO.setCustomerEmail("test@example.com");
        testOrderDTO.setShippingAddress("123 Test Street");
        testOrderDTO.setPaymentMethod("CREDIT_CARD");
        testOrderDTO.setOrderId("ORD-001");
        testOrderDTO.setTotalAmount(new BigDecimal("99.99"));
        testOrderDTO.setStatus("PENDING");
        testOrderDTO.setCreatedAt(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void createOrderWithPayment_Success_ReturnsOrderDTO() throws JsonProcessingException {
        // Given
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/orders"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(testOrderDTO))));

        // When
        ResponseEntity<OrderDTO> response = ordersBusinessClient.createOrderWithPayment(testOrderDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ORD-001", response.getBody().getOrderId());
        assertEquals("USER-001", response.getBody().getUserId());
    }

    @Test
    void createOrderWithPayment_ServerError_ThrowsFeignException() {
        // Given
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/orders"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Internal server error\"}")));

        // When & Then
        assertThrows(FeignException.class, () -> {
            ordersBusinessClient.createOrderWithPayment(testOrderDTO);
        });
    }

    @Test
    void getOrderById_Success_ReturnsOrderDTO() throws JsonProcessingException {
        // Given
        String orderId = "ORD-001";
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/orders/" + orderId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(testOrderDTO))));

        // When
        ResponseEntity<OrderDTO> response = ordersBusinessClient.getOrderById(orderId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ORD-001", response.getBody().getOrderId());
    }

    @Test
    void getOrderById_NotFound_ThrowsFeignException() {
        // Given
        String orderId = "ORD-999";
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/orders/" + orderId))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Order not found\"}")));

        // When & Then
        assertThrows(FeignException.NotFound.class, () -> {
            ordersBusinessClient.getOrderById(orderId);
        });
    }

    @Test
    void getOrdersByUserId_Success_ReturnsOrderList() throws JsonProcessingException {
        // Given
        String userId = "USER-001";
        List<OrderDTO> orderList = Arrays.asList(testOrderDTO);
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/orders/user/" + userId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(orderList))));

        // When
        ResponseEntity<List<OrderDTO>> response = ordersBusinessClient.getOrdersByUserId(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("ORD-001", response.getBody().get(0).getOrderId());
    }

    @Test
    void getOrdersByUserId_EmptyList_ReturnsEmptyList() throws JsonProcessingException {
        // Given
        String userId = "USER-999";
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/orders/user/" + userId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        // When
        ResponseEntity<List<OrderDTO>> response = ordersBusinessClient.getOrdersByUserId(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getOrdersByUserId_ServiceUnavailable_ThrowsFeignException() {
        // Given
        String userId = "USER-001";
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/orders/user/" + userId))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"Service unavailable\"}")));

        // When & Then
        assertThrows(FeignException.class, () -> {
            ordersBusinessClient.getOrdersByUserId(userId);
        });
    }
}
