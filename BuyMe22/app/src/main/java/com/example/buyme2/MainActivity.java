package com.example.buyme2;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.PendingIntentCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    public static String host = "82.179.140.18";
    public static int port = 45146;
    private Button joinButton, loginButton;

    private static AtomicBoolean HasAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinButton=(Button) findViewById((R.id.main_join_now_btn));
        loginButton=(Button) findViewById((R.id.main_login_btn));
        HasAnswer=new AtomicBoolean(true);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serverResponse()){
                    Intent registerIntent=new Intent(MainActivity.this, RegisterService.class);
                    startActivity(registerIntent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Нет связи с сервером")
                            .setTitle("Ошибка!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }});
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(serverResponse()){
                    Intent loginIntent=new Intent(MainActivity.this, LoginService.class);
                    startActivity(loginIntent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Нет связи с сервером")
                            .setTitle("Ошибка!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                //Intent ProductIntent=new Intent(MainActivity.this, ProductActivity.class);
                //startActivity(ProductIntent);
            }
        });


    }
    private Boolean serverResponse(){
       Thread th= new Thread(new Runnable() {
            @Override
          public void run() {
                try {
                    InetSocketAddress sa = new InetSocketAddress(host, port);
                    Socket socket = new Socket();
                    socket.connect(sa, 5000);
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                    out.write("dsdfs");
                    out.flush();
                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader buf = new BufferedReader(in);
                    String i = buf.readLine();
                    HasAnswer=new AtomicBoolean(true);}
                catch(Exception e){
                    HasAnswer=new AtomicBoolean(false);}}});
        return HasAnswer.get();
    }

}
