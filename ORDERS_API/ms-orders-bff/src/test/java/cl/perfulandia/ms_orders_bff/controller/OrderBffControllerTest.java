package cl.perfulandia.ms_orders_bff.controller;
import cl.perfulandia.ms_orders_bff.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bff.model.dto.OrderItemDTO;
import cl.perfulandia.ms_orders_bff.service.OrderBffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderBffController.class)
class OrderBffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderBffService orderBffService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDTO testOrderDTO;
    private OrderItemDTO testOrderItem;

    @BeforeEach
    void setUp() {
        testOrderItem = new OrderItemDTO();
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

    @Test
    void createOrder_Success_Returns201() throws Exception {
        // Given
        when(orderBffService.createOrder(any(OrderDTO.class))).thenReturn(testOrderDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value("ORD-001"))
                .andExpect(jsonPath("$.userId").value("USER-001"))
                .andExpect(jsonPath("$.totalAmount").value(99.99))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderBffService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void createOrder_ServiceReturnsNull_Returns400() throws Exception {
        // Given
        when(orderBffService.createOrder(any(OrderDTO.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderDTO)))
                .andExpect(status().isBadRequest());

        verify(orderBffService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void createOrder_ServiceThrowsException_Returns500() throws Exception {
        // Given
        when(orderBffService.createOrder(any(OrderDTO.class))).thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderDTO)))
                .andExpect(status().isInternalServerError());

        verify(orderBffService, times(1)).createOrder(any(OrderDTO.class));
    }

    @Test
    void getOrder_Success_Returns200() throws Exception {
        // Given
        String orderId = "ORD-001";
        when(orderBffService.getOrderById(orderId)).thenReturn(testOrderDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("ORD-001"))
                .andExpect(jsonPath("$.userId").value("USER-001"))
                .andExpect(jsonPath("$.totalAmount").value(99.99));

        verify(orderBffService, times(1)).getOrderById(orderId);
    }

    @Test
    void getOrder_OrderNotFound_Returns404() throws Exception {
        // Given
        String orderId = "ORD-999";
        when(orderBffService.getOrderById(orderId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isNotFound());

        verify(orderBffService, times(1)).getOrderById(orderId);
    }

    @Test
    void getOrder_ServiceThrowsException_Returns500() throws Exception {
        // Given
        String orderId = "ORD-001";
        when(orderBffService.getOrderById(orderId)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isInternalServerError());

        verify(orderBffService, times(1)).getOrderById(orderId);
    }

    @Test
    void getUserOrders_Success_Returns200() throws Exception {
        // Given
        String userId = "USER-001";
        List<OrderDTO> orderList = Arrays.asList(testOrderDTO);
        when(orderBffService.getUserOrders(userId)).thenReturn(orderList);

        // When & Then
        mockMvc.perform(get("/api/v1/orders/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].orderId").value("ORD-001"))
                .andExpect(jsonPath("$[0].userId").value("USER-001"));

        verify(orderBffService, times(1)).getUserOrders(userId);
    }

    @Test
    void getUserOrders_NoOrdersFound_Returns404() throws Exception {
        // Given
        String userId = "USER-999";
        when(orderBffService.getUserOrders(userId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/v1/orders/user/{userId}", userId))
                .andExpect(status().isNotFound());

        verify(orderBffService, times(1)).getUserOrders(userId);
    }

    @Test
    void getUserOrders_ServiceThrowsException_Returns500() throws Exception {
        // Given
        String userId = "USER-001";
        when(orderBffService.getUserOrders(userId)).thenThrow(new RuntimeException("Service unavailable"));

        // When & Then
        mockMvc.perform(get("/api/v1/orders/user/{userId}", userId))
                .andExpect(status().isInternalServerError());

        verify(orderBffService, times(1)).getUserOrders(userId);
    }
}
