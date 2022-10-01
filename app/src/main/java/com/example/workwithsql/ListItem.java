//package com.example.workwithsql;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Base64;
//import android.widget.ListView;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ListItem {
//    Connection connection;
//    String ConnectionResult = "";
//    Boolean isSuccess = false;
//
//
//    public ArrayList<Profile> getList() {
//        ArrayList<Profile> data;
//        data = new ArrayList<>();
//        try {
//            ConnectionHelper connectionHelper = new ConnectionHelper();
//            connection = connectionHelper.connectionClass();
//            if (connection != null) {
//                String query = "Select * From Personal_Inf";
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery(query);
//
//                while (resultSet.next()) {
//                    String decodeImage = resultSet.getString("image");
//                    byte[] decodedString = Base64.decode(decodeImage,Base64.DEFAULT);
//                    Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
//                    data.add(new Profile(resultSet.getString("name"), resultSet.getString("job"), resultSet.getString("email"), base64Bitmap);
//                }
//                ConnectionResult = "Есть контакт";
//                isSuccess = true;
//                connection.close();
//                ProfileAdapter profileAdapter = new ProfileAdapter(this, data);
//                ListView lv =
//            } else {
//                ConnectionResult = "Не получилось";
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return data;
//    }
//}
