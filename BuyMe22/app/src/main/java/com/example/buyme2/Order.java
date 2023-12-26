package com.example.buyme2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Order {
    public List<String> Titles ;
    public List <Integer> Lots;
    public List <Double> Prices;


    public float Check;
    public Order(Product p){
        Titles.add(p.Name);
        Lots.add(p.Lot);
        Prices.add(p.Price);
    }
    public Order(){
        Titles=new ArrayList<String>();
        Lots=new ArrayList<Integer>();
        Prices=new ArrayList<Double>();
    }
    public void Add(String name, Integer lot, Double price){
        if(!Titles.contains(name)){
            Titles.add(name);
            Lots.add(lot);
            Prices.add(price);
        }
        else{
            int i =Titles.indexOf(name);
            Lots.set(i, lot);
        }
        Check+=price;
    }
    public void clear(){
        Titles.clear();
        Prices.clear();
        Lots.clear();
        Check=0;
    }
    public String Checking(){
        String chk="";
        for(int i=0; i<Prices.size(); i++){
                chk+=Titles.get(i)+" "+Lots.get(i)+" шт. *"+ Prices.get(i) +" мяукоин\n";
        }
        return chk;
    }
}
