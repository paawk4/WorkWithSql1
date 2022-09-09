package com.example.workwithsql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    String ConnectionResult = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void GetTextFromSql(View v){
        TextView ID = findViewById(R.id.txtID);
        TextView Name = findViewById(R.id.txtName);
        TextView Age = findViewById(R.id.txtAge);

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();

            if (connection != null)
            {
                String query = "Select * From Personal_Inf";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()){
                    ID.setText(resultSet.getString(1));
                    Name.setText(resultSet.getString(2));
                    Age.setText(resultSet.getString(3));
                }
            }
            else{
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex){

        }
    }
}