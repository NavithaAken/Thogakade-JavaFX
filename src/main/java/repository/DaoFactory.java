package repository;

import repository.custom.impl.CustomerRepositoryImpl;
import service.SuperService;
import service.custom.impl.CustomerServiceImpl;
import service.custom.impl.ItemServiceImpl;
import util.RepositoryType;
import util.ServiceType;

public class DaoFactory {
    private static DaoFactory instance;
    private DaoFactory(){}

    public static DaoFactory getInstance() {
        return instance==null?instance=new DaoFactory():instance;
    }
    public <T extends SuperRepository>T getRepositoryType(RepositoryType type){

        switch (type){
            case CUSTOMER: return (T) new CustomerRepositoryImpl();
        }
        return null;

    }
}
