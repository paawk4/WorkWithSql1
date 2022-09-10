package com.example.workwithsql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/*https://www.youtube.com/watch?v=pe0GmXHAda4*/
public class MainActivity extends AppCompatActivity {

    Connection connection;
    String ConnectionResult = "";
    Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView theList = findViewById(R.id.lwDatabase);
        btnEnter = (Button) findViewById(R.id.btnView);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    connection = connectionHelper.connectionClass();

                    if (connection != null)
                    {
                        String query = "Select * From Personal_Inf";

                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        while (resultSet.next()){

                        }
                    }
                    else{
                        ConnectionResult = "Check Connection";
                    }
                }
                catch (Exception ex){

                }
            }
        });
    }



    public void GetTextFromSql(View v){
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();

            if (connection != null)
            {
                String query = "Select * From Personal_Inf";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()){

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