package com.example.workwithsql;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    SimpleAdapter simpleAdapter;
    public void GetList(View v)
    {
        ListView listView = (ListView) findViewById(R.id.lvDatabase);

        List<Map<String, String>> myDataList = null;
        ListItem myData = new ListItem();
        myDataList = myData.getList();

        String[] fromView = {"Name", "Job", "Email"};
        int[] toView = {R.id.Name, R.id.Job, R.id.Email};
        simpleAdapter = new SimpleAdapter(MainActivity.this, myDataList,R.layout.list_template,fromView, toView);
        listView.setAdapter(simpleAdapter);
    }
    public void ViewCreatePerson(View v)
    {
        setContentView(R.layout.create_person);
    }
    public void GoBack(View v) { setContentView(R.layout.activity_main);}

    Connection connection;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    @SuppressLint("NewApi")
    public void ViewList(View v){
        String name = "", job = "", email = "";

        EditText edName = findViewById(R.id.edName);
        EditText edJob = findViewById(R.id.edJob);
        EditText edEmail = findViewById(R.id.edEmail);

        name = edName.getText().toString();
        job = edJob.getText().toString();
        email = edEmail.getText().toString();

        if(name != null && job != null && email != null){

            List<Map <String, String>> data = null;
            data = new ArrayList<Map <String, String>>();
            try
            {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connection = connectionHelper.connectionClass();
                if (connection != null){
                    String query = " INSERT INTO Personal_Inf (name, job, email) VALUES ('" + name + "', '" + job + "', '" + email + "')";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

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
            setContentView(R.layout.activity_main);
        }
        else{

        }
    }
}