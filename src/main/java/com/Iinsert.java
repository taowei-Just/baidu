package com;

import java.sql.SQLException;

public interface Iinsert<T extends Object> {

    void  insertData(T object) throws SQLException;

    void updata(T info) throws SQLException;

    String queryMaxPro() throws SQLException;
}
