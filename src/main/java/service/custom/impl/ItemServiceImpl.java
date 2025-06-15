package service.custom.impl;

import dto.Item;
import service.custom.ItemService;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemServiceImpl implements ItemService {
    @Override
    public Boolean addItem(Item item) {
        return null;
    }

    @Override
    public Boolean updateItem(Item item) {
        return null;
    }

    @Override
    public Item searchById(String id) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item WHERE code=?",id);
        resultSet.next();
        return new Item(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getDouble(4)
        );
    }

    @Override
    public List<Item> getAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item");
        ArrayList<Item> arrayList = new ArrayList<>();

        while (resultSet.next()){
            arrayList.add(new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            ));
        }

        return arrayList;
    }

    @Override
    public Boolean deleteById(String id) {
        return null;
    }

    @Override
    public List<String> getAllItemCodes() throws SQLException {
        List<Item> all = getAll();
        ArrayList<String> itemCodeList = new ArrayList<>();
        all.forEach(item ->{
            itemCodeList.add(item.getItemCode());
        });
        return itemCodeList;
    }
}
