package com.example.attendenceapp.Acitivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendenceapp.R;

import org.w3c.dom.Text;

public class FbData extends AppCompatActivity {
    ImageView img;
    TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_data);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        img=(ImageView)findViewById(R.id.img);
        Intent intent=getIntent();
        name.setText(intent.getStringExtra("name"));
      startActivity(intent);
    }
}
