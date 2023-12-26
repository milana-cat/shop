package com.example.buyme2;

import static java.lang.System.currentTimeMillis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class OrderService extends AppCompatActivity {

    private Button RemoveButton, AddButton, ExitButton, OrderButton;
    private TextView TextName, TextLot, TextPrice;
    Bundle extras;
    Product product = new Product();
    public AtomicReference<String> message=new AtomicReference<>(" ");
    public AtomicReference<String> list =new AtomicReference<>(ProductService.order.Checking());
    public  AtomicReference<Float> CHK=new AtomicReference<>(ProductService.order.Check);
    public  AtomicInteger checknumber=new AtomicInteger(0);
    ImageView Image;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_main);

        OrderButton=(Button) findViewById((R.id.final_order_btn));
        ExitButton=(Button) findViewById((R.id.exit_btn));
        TextName=(TextView) findViewById((R.id.product_name));
        ListShow();
        //TextLot=(TextView) findViewById((R.id.product_count));
        TextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //При нажатии на текст переход к записи продукта
                    //ProductActivity.id=
                    Intent ExitIntent=new Intent(OrderService.this, ProductService.class);
                    startActivity(ExitIntent);

            }
        });
        OrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long time = currentTimeMillis();
                Long number=(time%259200000);
                checknumber=new AtomicInteger(number.intValue());
                MakeOrder();
                ProductService.order.clear();
                TextName.setText("Номер заказа: "+number.toString());

            }
        });
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ExitIntent=new Intent(OrderService.this, ProductService.class);
                startActivity(ExitIntent);
            }
        });
    }
    float fromPosition=0;

    public void ListShow()
    {

        String productData= list.get();
        //Product product;
        /*for (int i=0;i<=order.size();i++)
        {
            product = order.get(i);
            productData=productData+product.Name+" "+product.Lot+" шт. * "+product.Price+" мяукоин\n";
        *///}
        //extras=getIntent().getExtras();
        //productData=extras.getString("Score");
        TextName.setText(productData+"Итог: "+ProductService.order.Check+" мяукоин");



    }
    private void MakeOrder(){
        Thread thrd1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    InetSocketAddress sa = new InetSocketAddress(LoginService.host, LoginService.port);
                    Socket socket = new Socket();
                    socket.connect(sa, 5000);
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());

                    out.write("insert into public.orders (login, product_list, ordernum, price) values ('" + LoginService.Login.get() + "','" + list.get() + "','" + checknumber.get() + "','"+ CHK.get() +"')");
                    //out.write("select login, pass from public.clients");
                    out.flush();
                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader buf = new BufferedReader(in);
                    String response = buf.readLine();
                    if(response.contentEquals("DB server (version 0.1)")){
                        response = buf.readLine();
                    }
                    socket.close();


                } catch (Exception ex) {
                    //ex.printStackTrace();
                    message = new AtomicReference<String>("Нет соединения с сервером");
                    //throw new RuntimeException();
                }

            }


        });thrd1.start();
    }
}
