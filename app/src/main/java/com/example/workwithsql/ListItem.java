package com.example.workwithsql;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItem {
    Connection connection;
    String ConnectionResult = "";
    Boolean isSuccess = false;


    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> data;
        data = new ArrayList<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            if (connection != null) {
                String query = "Select * From Personal_Inf";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    Map<String, Object> dtName = new HashMap<>();
                    dtName.put("Name", resultSet.getString("name"));
                    dtName.put("Job", resultSet.getString("job"));
                    dtName.put("Email", resultSet.getString("email"));
                    String decodeImage = resultSet.getString("image");

                      byte[] decodedString = Base64.decode(decodeImage,Base64.DEFAULT);
                    Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                    dtName.put("Image", base64Bitmap);
                    data.add(dtName);
                }
                ConnectionResult = "Есть контакт";
                isSuccess = true;
                connection.close();
            } else {
                ConnectionResult = "Не получилось";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return data;
    }
}
