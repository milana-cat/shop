package com.example.buyme2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

public class LoginService extends AppCompatActivity {
    public static String host = "82.179.140.18";
    public static int port = 45146;
    public static String password;
    public String login;
    public static AtomicReference<String> Login;
    public static String username;
    public AtomicReference<String> messageLogin;
    public AtomicReference<String> Logpass;
    private Button loginButton;
    private EditText loginInput;
    private Button loginBtn;
    private String Response;

    private ProgressDialog loadingBar;
    private EditText usernameInput, passwordInput;
    //private String login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginInput = (EditText) findViewById(R.id.login_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginAccount();
            }
        });
        Button backbtn = (Button) findViewById(R.id.back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainIntent = new Intent(LoginService.this, MainActivity.class);
                startActivity(MainIntent);
                ;
            }
        });
    }

    private void LoginAccount() {
        login =loginInput.getText().toString();
        Login=new AtomicReference<String>(login);
        password = passwordInput.getText().toString();
        if (TextUtils.isEmpty(login)) {
            Toast.makeText(this, "Введите логин", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        } else {
            // Создаем объект MessageDigest с использованием алгоритма SHA-256
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            // Преобразуем пароль в байтовый массив и вычисляем хэш-значение
            byte[] hash = md.digest(password.getBytes());

            // Кодируем хэш-значение в Base64 и выводим на экран
            try {
                login();

            } catch (Exception e) {
                errorMessage();
            }

           // Intent ProductIntent = new Intent(LoginService.this, ProductService.class);
            //startActivity(ProductIntent);
        }
    }

    //String str ="Select login, pass from public.clients where (login='" + login + "'and pass='" +password + "')";
    private void login() {

        Thread thrd1 = new Thread(new Runnable() {

            @Override
            public void run() {
                messageLogin=new AtomicReference<>(" ");
                //Logpass=new AtomicReference<>(" ");
                try {
                    InetSocketAddress sa = new InetSocketAddress(host, port);
                    Socket socket = new Socket();
                    socket.connect(sa, 5000);
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                    out.write("select login, pass from public.clients where (login='" + login + "')");
                    //out.write("select login, pass from public.clients");
                    out.flush();
                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader buf = new BufferedReader(in);
                    String response = buf.readLine();
                    if(response.contentEquals("DB server (version 0.1)")){
                        Response = buf.readLine();
                    }

                    socket.close();
                    String[] log = Response.split("\t");
                    String logname, logpass;
                    logname = log[0];
                    Response = log[1];
                    Logpass=new AtomicReference<String>(Response);
                    password = passwordInput.getText().toString();
                    MessageDigest md1 = null;
                    try {
                        md1 = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    // Преобразуем пароль в байтовый массив и вычисляем хэш-значение
                    byte[] hash1 = md1.digest(password.getBytes());

                    // Кодируем хэш-значение в Base64 и выводим на экран
                    password = Base64.getEncoder().encodeToString(hash1);
                    String l =Logpass.get();
                    String l2 =Logpass.get();
                    if (l2.contentEquals(password)) {

                        //Toast.makeText(this, "Вы ввели неверный пароль", Toast.LENGTH_SHORT).show();
                        Intent ProductIntent = new Intent(LoginService.this, ProductService.class);
                        startActivity(ProductIntent);
                    }
                    else {
                        messageLogin=new AtomicReference<String>("Такого пользователя не существует");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorMessage();
                            }
                        });
                        //Intent ProductIntent = new Intent(LoginActivity.this, MainActivity.class);
                        //startActivity(ProductIntent);
                        //Toast.makeText(LoginService.this, "Такого пользователя не существует", Toast.LENGTH_SHORT).show();
                    }
                    if(messageLogin.get().contentEquals(" ")){
                        Intent MainIntent=new Intent(LoginService.this, ProductService.class);
                        startActivity(MainIntent);}
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorMessage();
                            }
                        });
                    }
                    //Logpass=new AtomicReference<String>(logpass);
                    //Logpass.set(Response);

                } catch (Exception ex) {
                    //ex.printStackTrace();
                    messageLogin = new AtomicReference<String>("Нет соединения с сервером");
                    //throw new RuntimeException();
                }


            }


        });
        thrd1.start();
    }
    private void errorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageLogin.get())
                .setTitle("Ошибка!");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

