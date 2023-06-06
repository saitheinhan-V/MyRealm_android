package com.example.myrealm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    private TextView textView;

    private FloatingActionButton fab;

    private List<DataModel> courseList;
    private CourseAdapter courseAdapter;
    private RecyclerView rvCourse;

    public static final int REQ_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        fab = findViewById(R.id.fab);
        rvCourse = findViewById(R.id.rvCourse);

        initRv();
        getAllCourses();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });

        findViewById(R.id.fabImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ImageActivity.class));
            }
        });

    }

    private void initRv() {
        if (courseList == null)
            courseList = new ArrayList<>();

        courseAdapter = new CourseAdapter(courseList);
        rvCourse.setAdapter(courseAdapter);
        rvCourse.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        rvCourse.setHasFixedSize(true);
        courseAdapter.notifyDataSetChanged();
    }

    private void getAllCourses() {

        RealmResults<DataModel> results = realm.where(DataModel.class).findAll();

        //            JSONObject object = new JSONObject(results.asJSON());
        JSONArray jsonArray = new JSONArray(results);
        List<DataModel> list = new Gson().fromJson(results.asJSON(), new TypeToken<List<DataModel>>() {
        }.getType());

        courseList = list;

        initRv();
//        courseAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            getAllCourses();
        } else {

        }
    }
}