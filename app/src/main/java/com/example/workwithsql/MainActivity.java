package com.example.workwithsql;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public String findByName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = findViewById(R.id.lvDatabase);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String nameText, jobText, emailText;
                TextView nameTv = (TextView)view.findViewById(R.id.Name);
                nameText = nameTv.getText().toString();
                TextView jobTv = (TextView)view.findViewById(R.id.Job);
                jobText = jobTv.getText().toString();
                TextView emailTv = (TextView)view.findViewById(R.id.Email);
                emailText = emailTv.getText().toString();
                setContentView(R.layout.edit_person);

                TextView editName = findViewById(R.id.editName);
                editName.setText(nameText);
                TextView editJob = findViewById(R.id.editJob);
                editJob.setText(jobText);
                TextView editEmail = findViewById(R.id.editEmail);
                editEmail.setText(emailText);
                findByName = nameText;
            }
        });
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
    public void GoBack(View v) {
        setContentView(R.layout.activity_main);
        GetList(v);
    }

    Connection connection;
    String ConnectionResult = "";

    @SuppressLint("NewApi")
    public void CreatePerson(View v){
        String name = "", job = "", email = "";

        EditText edName = findViewById(R.id.editName);
        EditText edJob = findViewById(R.id.editJob);
        EditText edEmail = findViewById(R.id.editEmail);

        name = edName.getText().toString();
        job = edJob.getText().toString();
        email = edEmail.getText().toString();

        if(name != "" || job != "" || email != ""){
            List<Map <String, String>> data = null;
            data = new ArrayList<Map <String, String>>();
            try
            {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connection = connectionHelper.connectionClass();
                if (connection != null){
                    String query = "INSERT INTO Personal_Inf (name, job, email) VALUES ('" + name + "', '" + job + "', '" + email + "')";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    ConnectionResult = "Есть контакт";
                    connection.close();
                }
                else {
                    ConnectionResult = "Не получилось";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        setContentView(R.layout.activity_main);
        GetList(v);
    }
    public void OpenEditPerson(View v){
        ListView lv = findViewById(R.id.lvDatabase);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String nameText, jobText, emailText;
                TextView nameTv = (TextView)view.findViewById(R.id.Name);
                nameText = nameTv.getText().toString();
                TextView jobTv = (TextView)view.findViewById(R.id.Job);
                jobText = jobTv.getText().toString();
                TextView emailTv = (TextView)view.findViewById(R.id.Email);
                emailText = emailTv.getText().toString();
                setContentView(R.layout.edit_person);

                TextView editName = findViewById(R.id.editName);
                editName.setText(nameText);
                TextView editJob = findViewById(R.id.editJob);
                editJob.setText(jobText);
                TextView editEmail = findViewById(R.id.editEmail);
                editEmail.setText(emailText);
                findByName = nameText;
            }
        });
    }
    public void EditPerson(View v){
        String name = "", job = "", email = "";

        EditText edName = findViewById(R.id.editName);
        EditText edJob = findViewById(R.id.editJob);
        EditText edEmail = findViewById(R.id.editEmail);

        name = edName.getText().toString();
        job = edJob.getText().toString();
        email = edEmail.getText().toString();

        if(name != "" || job != "" || email != ""){
            List<Map <String, String>> data = null;
            data = new ArrayList<Map <String, String>>();
            try
            {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connection = connectionHelper.connectionClass();
                if (connection != null){
                    String query = "UPDATE Personal_Inf\n" +
                            "SET name = '" + name + "', job = '" + job + "', email = '" + email + "'\n" +
                            "WHERE name = '"+ findByName +"'";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    ConnectionResult = "Есть контакт";
                    connection.close();
                }
                else {
                    ConnectionResult = "Не получилось";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }



        setContentView(R.layout.activity_main);


    }
    public void DeletePerson(View v){
        setContentView(R.layout.activity_main);
        GetList(v);
    }
}