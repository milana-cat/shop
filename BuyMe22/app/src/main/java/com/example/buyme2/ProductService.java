package com.example.buyme2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
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
import java.util.concurrent.atomic.AtomicReference;

public class ProductService extends AppCompatActivity {

    //public AtomicIntegerArray order[];
    AtomicReference<Product> product;
    public static Order order;
    //public static Product [] BuyList;
    public static String BuyList ="";
    //public static float Sum;
    private Button BuyButton, ListButton, ExitButton;
    private TextView TextName, TextDesc, TextPrice;
    //Product product = new Product();
    private String Response;
    ImageView Image;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Product product = new Product();
        id=1;
        GetProduct();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        order=new Order();
        BuyButton=(Button) findViewById((R.id.buy_btn));
        ListButton=(Button) findViewById((R.id.list_btn));
        ExitButton=(Button) findViewById((R.id.exit_btn));

        ListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent OrderIntent=new Intent(ProductService.this, OrderService.class);
                //OrderIntent.putExtra("Score", BuyList);
               startActivity(OrderIntent);
            }
        });
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ExitIntent=new Intent(ProductService.this,MainActivity.class);
                startActivity(ExitIntent);
            }
        });

        BuyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Product p =product.get();
                String name=p.Name;
                Integer i =p.Lot+1;
                Double price =p.Price;
                order.Add(name,i,price);
                p.Add();

            }

        });
    }
    private void ProductsList(){
    }

    float fromPosition=0;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        String Price;
        String [] prices;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: // Пользователь нажал на экран, т.е. начало движения
                // fromPosition - координата по оси X начала выполнения операции
                fromPosition = event.getX();
                break;
            case MotionEvent.ACTION_UP: // Пользователь отпустил экран, т.е. окончание движения
                float toPosition = event.getX();
                if (fromPosition > toPosition){

                    //Получаем данные о новом продукте и меняем содержание
                    if (id == 5) {
                        id=1;
                    }
                    else{

                        id+=1;
                    }
                    GetProduct();
                    Product p = product.get();
                    //String name = "p"+id;
                    String name =p.GetPicName(id);
                    int id = getResources().getIdentifier(name, "drawable", getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    Image.setImageDrawable(drawable);

                }
                    //
                else if (fromPosition < toPosition){
                    //Получаем данные о прошлом продукте и меняем содержание страницы

                        if (id == 1) {
                            id=5;
                        }
                        else{

                            id-=1;
                        }
                        GetProduct();
                        Product p = product.get();
                    //Product p = new Product(id,Float.parseFloat(log[3]),log[2],log[1], Integer.parseInt(log[4]));
                        //String name ="p"+id;
                        String name =p.GetPicName(id);
                        int id = getResources().getIdentifier(name, "drawable", getPackageName());
                        Drawable drawable = getResources().getDrawable(id);
                        Image.setImageDrawable(drawable);

                }
                    //flipper.showPrevious();
            default:
                break;
        }
        //product.GetProductByID(id);

        return true;
    }
    public void GetProduct(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress sa = new InetSocketAddress(LoginService.host, LoginService.port);
                try {
                    Socket socket = new Socket();
                    socket.connect(sa, 5000);
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());


                    //BufferedWriter buf = new BufferedWriter(out);
                    out.write("select * from public.product where product_id='" + id +"'");
                    out.flush();
                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader buf = new BufferedReader(in);
                    String response = buf.readLine();
                    Response = buf.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String [] log = Response.split("\t");
                            Product product1 = new Product(id,Float.parseFloat(log[3]),log[2],log[1], Integer.parseInt(log[4]));
                           // product1.Name=log[1];
                            //product1.Descriptor=log[2];
                            //product1.Price=Float.parseFloat(log[3]);
                            //product1.Count =Integer.parseInt(log[4]);
                            product =new AtomicReference<Product>(product1);
                            //product.set(new Product(id,Float.parseFloat(log[3]),log[2],log[1], Integer.parseInt(log[4])));
                            TextName=(TextView)findViewById((R.id.product_name));
                            TextName.setText(product1.Name);
                            TextDesc=(TextView)findViewById((R.id.product_descript));
                            TextDesc.setText(product1.Descriptor);
                            TextPrice=(TextView)findViewById((R.id.product_price));
                            TextPrice.setText("Цена: "+product1.Price+" мяукоин");
                            Image = (ImageView)findViewById((R.id.product_pic));


                            //int id = getResources().getIdentifier(name, "drawable", getPackageName());
                            //Drawable drawable = getResources().getDrawable(id);
                            //Image.setImageDrawable(drawable);
                        }
                    });

                    socket.close();


                } catch (Exception ex) {
                    ex.printStackTrace();

                }

            }
        }).start();
    }
}
