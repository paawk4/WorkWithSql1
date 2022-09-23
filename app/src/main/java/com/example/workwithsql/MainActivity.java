package com.example.workwithsql;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
public class MainActivity extends AppCompatActivity {
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private static final String LOG_TAG = "Android Example";
    public String findByName;
    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

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

        EditText edName = findViewById(R.id.editName);
        EditText edJob = findViewById(R.id.editJob);
        EditText edEmail = findViewById(R.id.editEmail);

        String name = edName.getText().toString();
        String job = edJob.getText().toString();
        String email = edEmail.getText().toString();


        if (!name.equals("") & !job.equals("") & !email.equals("")) {
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connection = connectionHelper.connectionClass();
                String query = "";
                if (v.equals(btnCreate)) {
                    query = "INSERT INTO Personal_Inf (name, job, email, image) VALUES ('" + name
                            + "', '" + job + "', '" + email + "','" + encodedImage + "')";
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

    @SuppressWarnings("deprecation")
    public void ImageView(View v){
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_RESULT_CODE_FILECHOOSER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri fileUri = data.getData();
                    Log.i(LOG_TAG, "Uri: " + fileUri);

                    String filePath;
                    try {
                        filePath = FileUtils.getPath(this.getBaseContext(), fileUri);
                        ImageView image = findViewById(R.id.Avatar);
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        image.setImageBitmap(bitmap);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        byte[] byteArray = stream.toByteArray();

                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error: " + e);
                        Toast.makeText(this.getBaseContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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