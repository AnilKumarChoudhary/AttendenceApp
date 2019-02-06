package com.example.attendenceapp.Acitivity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendenceapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class GoogleData extends AppCompatActivity {
TextView name,email,id;
ImageView img;
    GoogleSignInClient mGoogleSignInClient;
Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_data);
        name=(TextView)findViewById(R.id.name);
        id=(TextView)findViewById(R.id.id);
        email=(TextView)findViewById(R.id.email);
        img=(ImageView)findViewById(R.id.img);
        signout=(Button)findViewById(R.id.signout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null)
        {
            String Name=account.getDisplayName();
            String Id=account.getId();
            String Email=account.getEmail();
            Uri Image=account.getPhotoUrl();

            name.setText("Name is"+Name);
            id.setText("id is "+Id);
            email.setText("Email is "+Email);
            Picasso.with(this).load(Image).into(img);

        }
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   Signout();
            }
        });
    }
    private void Signout(){
mGoogleSignInClient.signOut().addOnCompleteListener(GoogleData.this, new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(GoogleData.this, "Successfully signedOut", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(GoogleData.this,GoogleLogInActivity.class);
        startActivity(intent);
    }
});
    }
}
