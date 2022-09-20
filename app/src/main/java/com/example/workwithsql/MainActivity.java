package com.example.workwithsql;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public String findByName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = findViewById(com.google.android.material.R.id.ghost_view);
        ListOperations(v);
    }

    public void ListOperations(View v) {
        SimpleAdapter simpleAdapter;
        ListView listView = findViewById(R.id.lvDatabase);

        List<Map<String, String>> myDataList;
        ListItem myData = new ListItem();
        myDataList = myData.getList();

        String[] fromView = {"Name", "Job", "Email"};
        int[] toView = {R.id.Name, R.id.Job, R.id.Email};
        simpleAdapter = new SimpleAdapter(MainActivity.this, myDataList, R.layout.list_template, fromView, toView);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String nameText, jobText, emailText;
            TextView nameTv = view.findViewById(R.id.Name);
            nameText = nameTv.getText().toString();
            TextView jobTv = view.findViewById(R.id.Job);
            jobText = jobTv.getText().toString();
            TextView emailTv = view.findViewById(R.id.Email);
            emailText = emailTv.getText().toString();
            setContentView(R.layout.edit_person);

            TextView editName = findViewById(R.id.editName);
            editName.setText(nameText);
            TextView editJob = findViewById(R.id.editJob);
            editJob.setText(jobText);
            TextView editEmail = findViewById(R.id.editEmail);
            editEmail.setText(emailText);
            findByName = nameText;
        });
    }

    public void DbOperations(View v) {
        Connection connection;

        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        String name, job, email;

        EditText edName = findViewById(R.id.editName);
        EditText edJob = findViewById(R.id.editJob);
        EditText edEmail = findViewById(R.id.editEmail);

        name = edName.getText().toString();
        job = edJob.getText().toString();
        email = edEmail.getText().toString();

        if (!name.equals("") & !job.equals("") & !email.equals("")) {
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connection = connectionHelper.connectionClass();
                String query = "";
                if (v.equals(btnCreate)) {
                    query = "INSERT INTO Personal_Inf (name, job, email) VALUES ('" + name + "', '" + job + "', '" + email + "')";
                } else if (v.equals(btnEdit)) {
                    query = "UPDATE Personal_Inf\n" +
                            "SET name = '" + name + "', job = '" + job + "', email = '" + email + "'\n" +
                            "WHERE name = '" + findByName + "'";
                } else if (v.equals(btnDelete)) {
                    query = "DELETE Personal_Inf WHERE name = '" + findByName + "'";
                }
                Statement statement = connection.createStatement();
                statement.executeQuery(query);
                connection.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            setContentView(R.layout.activity_main);
            ListOperations(v);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введите данные", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void Navigation(View v) {
        Button viewCreate = findViewById(R.id.btnViewCreate);
        Button goBack = findViewById(R.id.btnGoback);
        if (v.equals(viewCreate)) {
            setContentView(R.layout.create_person);
        } else if (v.equals(goBack)) {
            setContentView(R.layout.activity_main);
            ListOperations(v);
        }
    }
}