package service.custom;

import dto.Customer;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService extends SuperService {
    Boolean addCustomer(Customer customer) throws SQLException;
    Boolean updateCustomer(Customer customer);
    Customer searchById(String id) throws SQLException;
    List<Customer> getAll() throws SQLException;

    List<String> getCustomerIds() throws SQLException;
}
