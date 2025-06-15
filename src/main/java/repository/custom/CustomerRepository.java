package repository.custom;

import dto.Customer;
import entity.CustomerEntity;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface CustomerRepository extends CrudRepository<CustomerEntity,String> {
    List<String> getCustomerAddress();
}
