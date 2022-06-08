package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.InventoryModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDaoJdbc implements InventoryDao {

    private DataSource dataSource;

    public InventoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(InventoryModel inventory) {

    }

    @Override
    public void update(InventoryModel inventory) {

    }

    @Override
    public InventoryModel get(int id) {
        return null;
    }

    @Override
    public List<InventoryModel> getAll() {
        return null;
    }
}
