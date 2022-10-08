package com.example.workwithsql;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private static final String LOG_TAG = "Android Example";
    String findByName;
    String encodedImage;
    ArrayList<Profile> profileList = new ArrayList<>();
    ArrayList<Profile> profileList_s;
    ProfileAdapter profileAdapter;
    Connection connection;

    ConnectionHelper connectionHelper = new ConnectionHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Employee Information");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View v = findViewById(com.google.android.material.R.id.ghost_view);
        ListOperations(v);
        SpinnerInit();
    }

    public void SpinnerInit(){
        try {
            Spinner spinner = findViewById(R.id.spinnerSort);
            spinner.setOnItemSelectedListener(this);

            List<String> categories = new ArrayList<>();
            categories.add("By name a-z");
            categories.add("By position a-z");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(dataAdapter);
        }
        catch (Exception ignored){
        }
    }

    public void DbOperations(View v) {
        try {
            Button btnCreate = findViewById(R.id.btnCreate);
            Button btnEdit = findViewById(R.id.btnEdit);
            Button btnDelete = findViewById(R.id.btnDelete);

            EditText edName = findViewById(R.id.editName);
            EditText edJob = findViewById(R.id.editJob);
            EditText edEmail = findViewById(R.id.editEmail);
            ImageView ivAvatar = findViewById(R.id.ivAvatar);

            String name = edName.getText().toString();
            String job = edJob.getText().toString();
            String email = edEmail.getText().toString();

            if (!name.equals("") & !job.equals("") & !email.equals("") & !((BitmapDrawable) ivAvatar.getDrawable()).getBitmap().toString().equals("")) {
                Bitmap avatarBm = ((BitmapDrawable) ivAvatar.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                avatarBm.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byteArray = stream.toByteArray();

                String encodedImageUpdate = Base64.encodeToString(byteArray, Base64.DEFAULT);
                try {
                    connection = connectionHelper.connectionClass();
                    String query = "";
                    if (v.equals(btnCreate)) {
                        query = "INSERT INTO Personal_Inf (name, job, email, image) VALUES ('" + name
                                + "', '" + job + "', '" + email + "','" + encodedImage + "')";
                    } else if (v.equals(btnEdit)) {
                        query = "UPDATE Personal_Inf\n" +
                                "SET name = '" + name + "', job = '" + job + "', email = '" + email + "', image = '" + encodedImageUpdate + "'\n" +
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
                SpinnerInit();
                ListOperations(v);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Введите данные", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        catch (Exception ex){
            Toast.makeText(v.getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("deprecation")
    public void ImageView(View v) {
        try {
            Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFileIntent.setType("*/*");
            chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
            startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
        }
        catch (Exception ex){
            Toast.makeText(v.getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == MY_RESULT_CODE_FILECHOOSER) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri fileUri = data.getData();
                        Log.i(LOG_TAG, "Uri: " + fileUri);

                        String filePath;
                        try {
                            filePath = FileUtils.getPath(this.getBaseContext(), fileUri);
                            ImageView image = findViewById(R.id.ivAvatar);
                            Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                            ByteArrayOutputStream outStream = BitmapCompress.BitmapCompressor(bitmap);

                            image.setImageBitmap(bitmap);

                            byte[] byteArray = outStream.toByteArray();

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
        catch (Exception ignored){
        }
    }

    public void Search(View v) {
        try {
            EditText searchBar = findViewById(R.id.searchBar);
            TextView tw = findViewById(R.id.textView2);
            if(searchBar.isFocused()){
                searchBar.setVisibility(View.GONE);
                tw.setVisibility(View.VISIBLE);
            }else{
                searchBar.setVisibility(View.VISIBLE);
                tw.setVisibility(View.GONE);

                searchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        profileAdapter.getFilter().filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }
        }
        catch (Exception ex){
            Toast.makeText(v.getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void ListOperations(View v) {
        ListView listViewDB = findViewById(R.id.lvDatabase);
        try {
            profileList.clear();
            connection = connectionHelper.connectionClass();
            if (connection != null) {
                String query = "Select * From Personal_Inf";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String decodeImage = resultSet.getString("image");
                    byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
                    Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profileList.add(new Profile(resultSet.getString("name"), resultSet.getString("job"), resultSet.getString("email"), base64Bitmap));
                }
                profileAdapter = new ProfileAdapter(MainActivity.this, profileList);
                listViewDB.setAdapter(profileAdapter);

                profileList_s = (ArrayList<Profile>) profileList.clone();

                listViewDB.setOnItemClickListener((parent, view, position, id) -> {
                    String nameText, jobText, emailText;
                    TextView nameTv = view.findViewById(R.id.Name);
                    nameText = nameTv.getText().toString();
                    TextView jobTv = view.findViewById(R.id.Job);
                    jobText = jobTv.getText().toString();
                    TextView emailTv = view.findViewById(R.id.Email);
                    emailText = emailTv.getText().toString();
                    ImageView avatar = view.findViewById(R.id.list_itemAvatar);
                    Bitmap avatarBm = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                    setContentView(R.layout.edit_person);

                    TextView editName = findViewById(R.id.editName);
                    editName.setText(nameText);
                    TextView editJob = findViewById(R.id.editJob);
                    editJob.setText(jobText);
                    TextView editEmail = findViewById(R.id.editEmail);
                    editEmail.setText(emailText);
                    ImageView ivAvatar = findViewById(R.id.ivAvatar);
                    ivAvatar.setImageBitmap(avatarBm);
                    findByName = nameText;
                });
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void Navigation(View v) {
        try {
            Button viewCreate = findViewById(R.id.btnViewCreate);
            Button goBack = findViewById(R.id.btnGoBack);
            if (v.equals(viewCreate)) {
                setContentView(R.layout.create_person);
            } else if (v.equals(goBack)) {
                setContentView(R.layout.activity_main);
                ListOperations(v);
                SpinnerInit();
            }
        }
        catch (Exception ex){
            Toast.makeText(v.getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            ListView listViewDB = findViewById(R.id.lvDatabase);
            String item = parent.getItemAtPosition(position).toString();
            if(item.equals("By name a-z")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(profileList, Comparator.comparing(o -> o.name.toLowerCase(Locale.ROOT)));
                }
                profileAdapter = new ProfileAdapter(MainActivity.this, profileList);
                listViewDB.setAdapter(profileAdapter);
            }
            else if(item.equals("By position a-z")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(profileList, Comparator.comparing(o -> o.job.toLowerCase(Locale.ROOT)));
                }
                profileAdapter = new ProfileAdapter(MainActivity.this, profileList);
                listViewDB.setAdapter(profileAdapter);
            }
        }
        catch (Exception ex){
            Toast.makeText(parent.getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
