package com.example.mu338.a1209test2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {

    String strNickname, strProfile;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvNickname = findViewById(R.id.txtNickName);
        TextView tvProfile = findViewById(R.id.txtImg);

        ImageView imgPro = findViewById(R.id.imgPro);

        Button btnLogout = findViewById(R.id.btnLogout);

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");

        tvNickname.setText(strNickname);
        tvProfile.setText(strProfile);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogout();
                Toast.makeText(getApplicationContext(), "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 1. imgPro.setImageResource(Integer.parseInt(strProfile));

        /*

        2.
        try {

            URL url = new URL(strProfile);

            URLConnection conn = url.openConnection();

            conn.connect();

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

            Bitmap bm = BitmapFactory.decodeStream(bis);

            bis.close();

            imgPro.setImageBitmap(bm);

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Thread thread = new Thread(){

            @Override
            public void run(){

                try{

                    URL url = new URL(strProfile);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setDoInput(true);

                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        };

        thread.start();

        try {

            thread.join();

            imgPro.setImageBitmap(bitmap);
        }catch (InterruptedException e){

        }

    }

    private void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}
