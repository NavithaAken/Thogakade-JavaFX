package service.custom;

import dto.Order;
import service.SuperService;

public interface OrderService extends SuperService {
    Boolean placeOrder(Order order);
}