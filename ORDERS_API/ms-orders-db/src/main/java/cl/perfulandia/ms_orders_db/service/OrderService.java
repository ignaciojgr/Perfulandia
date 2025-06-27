package cl.perfulandia.ms_orders_db.service;

import cl.perfulandia.ms_orders_db.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_db.model.dto.OrderItemDTO;
import cl.perfulandia.ms_orders_db.model.entities.Order;
import cl.perfulandia.ms_orders_db.model.entities.OrderItem;
import cl.perfulandia.ms_orders_db.model.entities.OrderStatus;
import cl.perfulandia.ms_orders_db.model.entities.Product;
import cl.perfulandia.ms_orders_db.model.repository.OrderRepository;
import cl.perfulandia.ms_orders_db.model.repository.OrderItemRepository;
import cl.perfulandia.ms_orders_db.model.repository.OrderStatusRepository;
import cl.perfulandia.ms_orders_db.model.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    OrderItemRepository orderItemRepository;
    
    @Autowired
    OrderStatusRepository orderStatusRepository;
    
    @Autowired
    ProductRepository productRepository;

    public OrderDTO findOrderById(String orderId) {
        Optional<Order> order = orderRepository.findByOrderId(orderId);
        if (order.isPresent()) {
            return convertToDTO(order.get());
        }
        return null;
    }

    public List<OrderDTO> findOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        
        for (Order order : orders) {
            orderDTOs.add(convertToDTO(order));
        }
        
        return orderDTOs;
    }    
    
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setUserId(orderDTO.getUserId());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setCurrency(orderDTO.getCurrency());
        order.setReturnUrl(orderDTO.getReturnUrl());        
        order.setCustomerEmail(orderDTO.getCustomerEmail());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setPaymentId(orderDTO.getPaymentId());
        order.setPaymentStatus(orderDTO.getPaymentStatus());
        order.setPaymentToken(orderDTO.getPaymentToken());
        order.setPaymentUrl(orderDTO.getPaymentUrl());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Optional<OrderStatus> defaultStatus = orderStatusRepository.findById(1L);
        if (defaultStatus.isPresent()) {
            order.setStatus(defaultStatus.get());
        }
        
        Order savedOrder = orderRepository.save(order);

        if (orderDTO.getItems() != null) {
            for (OrderItemDTO itemDTO : orderDTO.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                
                Optional<Product> product = productRepository.findById(itemDTO.getProductId());
                if (product.isPresent()) {
                    orderItem.setProduct(product.get());
                    orderItem.setQuantity(itemDTO.getQuantity());
                    orderItem.setUnitPriceAtOrder(itemDTO.getUnitPrice());
                    orderItem.setTotalPrice(itemDTO.getTotalPrice());
                    orderItemRepository.save(orderItem);
                }
            }
        }
        
        return convertToDTO(savedOrder);
    }    
    
    public OrderDTO updateOrderStatus(String orderId, Long statusCode) {
        log.info("Updating order {} status to: {}", orderId, statusCode);
        Optional<Order> orderOpt = orderRepository.findByOrderId(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            Optional<OrderStatus> statusOpt = orderStatusRepository.findById(statusCode);
            if (statusOpt.isPresent()) {
                order.setStatus(statusOpt.get());
                order.setUpdatedAt(LocalDateTime.now());
                Order updatedOrder = orderRepository.save(order);
                return convertToDTO(updatedOrder);
            }
        }
        return null;
    }

    public OrderDTO updatePaymentInfo(String orderId, String paymentId, String paymentStatus, 
                                     String paymentToken, String paymentUrl) {
        log.info("Updating payment info for order: {}", orderId);
        Optional<Order> orderOpt = orderRepository.findByOrderId(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setPaymentId(paymentId);
            order.setPaymentStatus(paymentStatus);
            order.setPaymentToken(paymentToken);
            order.setPaymentUrl(paymentUrl);
            order.setUpdatedAt(LocalDateTime.now());
            Order updatedOrder = orderRepository.save(order);
            return convertToDTO(updatedOrder);
        }
        return null;
    }

    public List<OrderDTO> findOrdersByStatus(Long statusCode) {
        log.info("Finding orders by status: {}", statusCode);
        List<Order> orders = orderRepository.findByStatusCode(statusCode);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        
        for (Order order : orders) {
            orderDTOs.add(convertToDTO(order));
        }
        
        return orderDTOs;
    }

    public List<OrderDTO> findOrdersByUserIdAndStatus(String userId, Long statusCode) {
        log.info("Finding orders for user {} with status: {}", userId, statusCode);
        List<Order> orders = orderRepository.findByUserIdAndStatusCode(userId, statusCode);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        
        for (Order order : orders) {
            orderDTOs.add(convertToDTO(order));
        }
        
        return orderDTOs;
    }

    private OrderDTO convertToDTO(Order order) {
        List<OrderItemDTO> items = new ArrayList<>();
        
        if (order.getItems() != null) {
            for (OrderItem orderItem : order.getItems()) {
                OrderItemDTO itemDTO = new OrderItemDTO(
                    orderItem.getProduct().getProductId().toString(),
                    orderItem.getProduct().getProductName(),
                    orderItem.getUnitPriceAtOrder(),
                    orderItem.getQuantity(),
                    orderItem.getTotalPrice()
                );
                items.add(itemDTO);
            }
        }
          return new OrderDTO(
            order.getOrderId(),
            order.getUserId(),
            order.getTotalAmount(),
            order.getCurrency(),
            order.getReturnUrl(),
            items,
            order.getCustomerEmail(),
            order.getShippingAddress(),
            order.getPaymentMethod(),
            order.getStatus() != null ? order.getStatus().getStatusCode() : null,
            order.getStatus() != null ? order.getStatus().getStatusName() : null,
            order.getPaymentStatus(), 
            order.getPaymentToken(),  
            order.getPaymentUrl(),    
            order.getPaymentId(),     
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
}
