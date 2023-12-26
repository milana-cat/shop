package com.example.buyme2;



import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public class Product extends AtomicReference<Product> {
    private static String Response;
    public int ID;
    public double Price;
    public String Descriptor;
    public String Name;
    public int Count;
    public int Lot;
    public boolean CanBuy(){
        if (Lot>Count){
            return false;
        }else{
            return true;
        }
    }
    public Product(){
        this.ID=1;
        this.Price=0.00;
        this.Descriptor="Description";
        this.Name="Product";
        this.Count=100;
        this.Lot=0;
    }
    public Product(int id, double price, String desc, String name, int count){
        this.ID=id;
        this.Price=price;
        this.Descriptor=desc;
        this.Name=name;
        this.Count=count;
    }
    public void Add(){
        if(this.CanBuy()){
            this.Lot+=1;
        }
    }
    public void Remove(){
        if(this.Lot!=0){
            this.Lot-=1;
        }
    }
    public String GetPicName(int id){
        if(id%5==0){
            return "p1";
        }else if(id%5==1){
            return "p2";
        }else if(id%5==2){
        return "p3";
        }else if(id%5==3){
            return "p4";
        }
        else{
            return "p5";
        }
    }
    public void GetProductByID(int id){
        this.ID=id;


    }
    public void ServerTalk(int id){

    }

}
