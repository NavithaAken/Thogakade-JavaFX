package service.custom.impl;

import dto.OrderDetails;
import util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailServiceImpl {
    public Boolean addOrderDetails(List<OrderDetails> orderDetails) throws SQLException {
        for (OrderDetails details : orderDetails){
            Boolean isAdd = addOrderDetails(details);
            if(!isAdd){
                return false;
            }
        }
        return true;
    }
    public Boolean addOrderDetails(OrderDetails orderDetails) throws SQLException {
        return CrudUtil.execute("INSERT INTO orderdetail VALUES (?,?,?,?)",
                orderDetails.getId(),
                orderDetails.getItemCode(),
                orderDetails.getQty(),
                orderDetails.getUnitPrice()
        );
    }
}