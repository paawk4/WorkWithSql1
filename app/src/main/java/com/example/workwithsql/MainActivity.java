package com.example.workwithsql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/*https://www.youtube.com/watch?v=pe0GmXHAda4*/
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

        String[] fromView = {"ID", "Name", "Age"};
        int[] toView = {R.id.Id, R.id.Name, R.id.Age};
        simpleAdapter = new SimpleAdapter(MainActivity.this, myDataList,R.layout.list_template,fromView, toView);
        listView.setAdapter(simpleAdapter);
    }
}