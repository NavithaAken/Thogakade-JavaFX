package service.custom.impl;

import dto.Customer;
import entity.CustomerEntity;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.CustomerRepository;
import service.custom.CustomerService;
import util.CrudUtil;
import util.RepositoryType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    CustomerRepository repository = DaoFactory.getInstance().getRepositoryType(RepositoryType.CUSTOMER);

    @Override
    public Boolean addCustomer(Customer customer) throws SQLException {
        CustomerEntity entity = new ModelMapper().map(customer, CustomerEntity.class);
        return repository.add(entity);
    }

    @Override
    public Boolean updateCustomer(Customer customer) {
        return null;
    }

    @Override
    public Customer searchById(String id) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE id=?", id);
        if (resultSet.next()) {
            return new Customer(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getDouble("salary")
            );
        }
        return null;
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
        ArrayList<Customer> customerArrayList = new ArrayList<>();
        while (resultSet.next()) {
            customerArrayList.add(new Customer(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getDouble("salary")
            ));
        }
        return customerArrayList;
    }

    @Override
    public List<String> getCustomerIds() throws SQLException {
        List<Customer> all = getAll();
        ArrayList<String> customerIdList = new ArrayList<>();
        all.forEach(customer->{
            customerIdList.add(customer.getId());
        });
        return customerIdList;
    }
}
