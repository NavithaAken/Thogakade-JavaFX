package service.custom.impl;

import db.DBConnection;
import dto.Order;
import service.custom.OrderService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderServiceImpl implements OrderService {
    @Override
    public Boolean placeOrder(Order order) {
        String sql = "INSERT INTO orders VALUES(?,?,?)";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(sql);
            psTm.setObject(1,order.getId());
            psTm.setObject(2,order.getDate());
            psTm.setObject(3,order.getCustomerId());
            Boolean isOrderAdd = psTm.executeUpdate()>0;
            if (isOrderAdd){
                Boolean isOrderDetailAdd = new OrderDetailServiceImpl().addOrderDetails(order.getOrderDetails());
                if (isOrderDetailAdd){
                    
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}