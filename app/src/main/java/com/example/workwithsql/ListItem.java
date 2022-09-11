package com.example.workwithsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.collections.ArrayDeque;

public class ListItem {
    Connection connection;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    public List<Map <String, String>> getList()
    {
        List<Map <String, String>> data = null;
        data = new ArrayList<Map <String, String>>();
        try
        {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            if (connection != null){
                String query = "Select * From Personal_Inf";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()){
                    Map<String,String> dtName = new HashMap<String, String>();
                    dtName.put("ID",resultSet.getString("id"));
                    dtName.put("Name",resultSet.getString("name"));
                    dtName.put("Age",resultSet.getString("age"));

                    data.add(dtName);
                }
                ConnectionResult = "Есть контакт";
                isSuccess = true;
                connection.close();
            }
            else {
                ConnectionResult = "Не получилось";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return data;
    }
}
