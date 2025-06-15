package repository.custom.impl;

import dto.Customer;
import entity.CustomerEntity;
import repository.custom.CustomerRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public boolean add(CustomerEntity entity) {
        return false;
    }

    @Override
    public boolean update(CustomerEntity entity) {
        return false;
    }

    @Override
    public boolean deleteById(String string) {
        return false;
    }

    @Override
    public CustomerEntity searchById(String string) {
        return null;
    }

    @Override
    public List<CustomerEntity> getAll() {
        return null;
    }

    @Override
    public List<String> getCustomerAddress() {
        return null;
    }
}
