package repository.custom.impl;

import entity.ItemEntity;
import repository.custom.ItemRepository;

import java.util.List;

public class ItemRepositoryImpl implements ItemRepository {

    @Override
    public boolean add(ItemEntity entity) {
        return false;
    }

    @Override
    public boolean update(ItemEntity entity) {
        return false;
    }

    @Override
    public boolean deleteById(String string) {
        return false;
    }

    @Override
    public ItemEntity searchById(String string) {
        return null;
    }

    @Override
    public List<ItemEntity> getAll() {
        return null;
    }
}
