package com.example.attendenceapp.Acitivity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendenceapp.FirstPage;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import com.example.attendenceapp.R;

public class FbActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    ImageView profilepic;
    TextView UserName, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         FacebookSdk.sdkInitialize(getApplicationContext());       //Logging an activation event
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_fb);
         UserName=(TextView)findViewById(R.id.UserName);
        email=(TextView)findViewById(R.id.email);
        profilepic=(ImageView)findViewById(R.id.profilePic);
        loginButton = findViewById(R.id.login_button);


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {
            Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(profilepic);
            Log.e("TAG", "Username is: " +Profile.getCurrentProfile().getName());

            //Using Graph API

        }




       callbackManager = CallbackManager.Factory.create();               //to handle login response
        Log.e("Tag","callback ");
        loginButton.setReadPermissions("email", "public_profile"/*, "user_friends"*/);           //get user details from fb

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                //getUserProfile(loginResult);
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.e("API123", loggedIn + " ??");
                getUserProfile(AccessToken.getCurrentAccessToken());
                Toast.makeText(FbActivity.this, "onSuccess"+loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(FbActivity.this, "on cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code

                    Toast.makeText(FbActivity.this, "on Error "+exception.toString(), Toast.LENGTH_LONG).show();
                    Log.e("tag","Error "+exception.toString());



            }
        });



            }
        });

       printKeyHash(FbActivity.this);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));        //perform actual login


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("Tag","OnActivity "+requestCode+" responsecode"+resultCode);
    }



    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String Email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            UserName.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                            email.setText(Email);
                            Picasso.with(FbActivity.this).load(image_url).into(profilepic);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }




// For geting HashKey for f.b Login

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", "Key "+key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}

