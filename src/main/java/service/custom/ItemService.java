package service.custom;

import dto.Item;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface ItemService extends SuperService {
    Boolean addItem(Item item);
    Boolean updateItem(Item item);
    Item searchById(String id) throws SQLException;
    List<Item> getAll() throws SQLException;
    Boolean deleteById(String id);
    List<String> getAllItemCodes() throws SQLException;
}
