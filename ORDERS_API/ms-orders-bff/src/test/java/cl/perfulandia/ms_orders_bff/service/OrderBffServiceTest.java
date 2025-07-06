package cl.perfulandia.ms_orders_bff.service;
import cl.perfulandia.ms_orders_bff.clients.OrdersBusinessClient;
import cl.perfulandia.ms_orders_bff.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bff.model.dto.OrderItemDTO;
import feign.FeignException;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderBffServiceTest {

    @Mock
    private OrdersBusinessClient ordersBusinessClient;

    @InjectMocks
    private OrderBffService orderBffService;

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
    void createOrder_Success_ReturnsOrderDTO() {
        // Given
        ResponseEntity<OrderDTO> mockResponse = new ResponseEntity<>(testOrderDTO, HttpStatus.CREATED);
        when(ordersBusinessClient.createOrderWithPayment(any(OrderDTO.class))).thenReturn(mockResponse);

        // When
        OrderDTO result = orderBffService.createOrder(testOrderDTO);

        // Then
        assertNotNull(result);
        assertEquals("ORD-001", result.getOrderId());
        assertEquals("USER-001", result.getUserId());
        assertEquals(new BigDecimal("99.99"), result.getTotalAmount());
        verify(ordersBusinessClient, times(1)).createOrderWithPayment(testOrderDTO);
    }

    @Test
    void createOrder_RetryableException_ThrowsRuntimeException() {
        // Given
        RetryableException retryableException = mock(RetryableException.class);
        when(ordersBusinessClient.createOrderWithPayment(any(OrderDTO.class))).thenThrow(retryableException);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> orderBffService.createOrder(testOrderDTO));
        
        assertEquals("Orders service is unavailable.", exception.getMessage());
        assertEquals(retryableException, exception.getCause());
        verify(ordersBusinessClient, times(1)).createOrderWithPayment(testOrderDTO);
    }

    @Test
    void createOrder_FeignNotFoundException_ThrowsRuntimeException() {
        // Given
        FeignException.NotFound notFoundException = mock(FeignException.NotFound.class);
        when(ordersBusinessClient.createOrderWithPayment(any(OrderDTO.class))).thenThrow(notFoundException);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> orderBffService.createOrder(testOrderDTO));
        
        assertEquals("Orders service endpoint not found", exception.getMessage());
        assertEquals(notFoundException, exception.getCause());
    }

    @Test
    void getOrderById_Success_ReturnsOrderDTO() {
        // Given
        String orderId = "ORD-001";
        ResponseEntity<OrderDTO> mockResponse = new ResponseEntity<>(testOrderDTO, HttpStatus.OK);
        when(ordersBusinessClient.getOrderById(orderId)).thenReturn(mockResponse);

        // When
        OrderDTO result = orderBffService.getOrderById(orderId);

        // Then
        assertNotNull(result);
        assertEquals("ORD-001", result.getOrderId());
        verify(ordersBusinessClient, times(1)).getOrderById(orderId);
    }

    @Test
    void getOrderById_Exception_ThrowsRuntimeException() {
        // Given
        String orderId = "ORD-001";
        when(ordersBusinessClient.getOrderById(orderId)).thenThrow(new RuntimeException("Connection error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> orderBffService.getOrderById(orderId));
        
        assertEquals("Failed to fetch order", exception.getMessage());
    }

    @Test
    void getUserOrders_Success_ReturnsOrderList() {
        // Given
        String userId = "USER-001";
        List<OrderDTO> orderList = Arrays.asList(testOrderDTO);
        ResponseEntity<List<OrderDTO>> mockResponse = new ResponseEntity<>(orderList, HttpStatus.OK);
        when(ordersBusinessClient.getOrdersByUserId(userId)).thenReturn(mockResponse);

        // When
        List<OrderDTO> result = orderBffService.getUserOrders(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ORD-001", result.get(0).getOrderId());
        verify(ordersBusinessClient, times(1)).getOrdersByUserId(userId);
    }

    @Test
    void getUserOrders_Exception_ThrowsRuntimeException() {
        // Given
        String userId = "USER-001";
        when(ordersBusinessClient.getOrdersByUserId(userId)).thenThrow(new RuntimeException("Service error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> orderBffService.getUserOrders(userId));
        
        assertEquals("Failed to fetch user orders", exception.getMessage());
    }
}
