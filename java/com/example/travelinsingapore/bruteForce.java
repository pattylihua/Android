package com.example.travelinsingapore;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by derek on 12/4/2017.
 */

public class bruteForce {
    public ArrayList<String> selectedPlace;
    public ArrayList<String []> adjacentList=new ArrayList<>();
    public planJsondata[] dataList;
    public Context context;
    public double money;
    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public boolean e;
    public boolean f;
    public bruteForce(boolean a,boolean b,boolean c,boolean d,boolean e,boolean f,double money,Context context){
        this.context=context;
        this.money=money;
        this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;
        this.e=e;
        this.f=f;
    }
    public String brutesolve(){

        minroute="";
        mintraffic="";
        timemax=Double.MAX_VALUE;
        finalmoney=0;

        initialize(a,b,c,d,e,f);
        parseJson();
        manangeData();
        managebrutePlan();
        String message="";
        String [] routeList=minroute.split(",");
        String [] trafficList=mintraffic.split(",");
        for(int i=0;i<routeList.length;i++){
            message+=routeList[i];
            if(i<routeList.length-1){
                message+=" ("+trafficList[i]+") ->\n";
            }
        }
        message+="\n";
        message+="\nTime: "+timemax+"\n";
        message+="\nMoney:"+finalmoney+"\n";
        return message;
    }

    public class planJsondata{
        String from;
        String to;
        String pttime;
        String ptcost;
        String taxitime;
        String taxicost;
        String foottime;
    }
    public void initialize(boolean a,boolean b,boolean c,boolean d,boolean e,boolean f){
        selectedPlace=new ArrayList<>();
        if(a==true){

        }
        if(b==true){
            selectedPlace.add("Singapore Flyer");
        }
        if(c==true){
            selectedPlace.add("Vivo City");
        }
        if(d==true){
            selectedPlace.add("Resorts World Sentosa");
        }
        if(e==true){
            selectedPlace.add("Buddha Tooth Relic Temple");
        }
        if(f==true){
            selectedPlace.add("Zoo");
        }
    }

    public String readData(){

        InputStream inputStream = context.getResources().openRawResource(R.raw.pictures);
        String line;
        String output="";
        try{
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

            while((line=reader.readLine())!=null){
                output+=line;
            }}catch (Exception ex){
            ex.printStackTrace();
        }

        return output;
    }
    public void parseJson(){
        String jsonData=readData();
        Gson gson = new Gson();
        dataList=gson.fromJson(jsonData,planJsondata[].class);

    }
    public void manangeData(){
        for (planJsondata i :dataList){
            if(selectedPlace.contains(i.from)&&selectedPlace.contains(i.to)){

                String [] a=new String[7];
                a[0]=i.from;
                a[1]=i.to;
                a[2]=i.pttime;
                a[3]=i.ptcost;
                a[4]=i.taxitime;
                a[5]=i.taxicost;
                a[6]=i.foottime;
                adjacentList.add(a);
            }
        }
    }
    public void managebrutePlan(){

        String prefix="";
        String suffix="";
        for(int i=1;i<=selectedPlace.size();i++){
            suffix+=i+"";
        }
        selectedPlace.add(0,"Marina Bay Sands");
        selectedPlace.add("Marina Bay Sands");
        brutePlan(prefix,suffix);

        for(String i:permutation){
            i="0"+i;
            i=i+(selectedPlace.size()-1);
            String traffic="";
            double price=0;
            double currenttime=0;
            String currentposition="";
            bruteCalculate(i,traffic,price,currenttime,currentposition);
        }


    }

    private ArrayList<String> permutation=new ArrayList<>();
    public void brutePlan(String a,String b){
        String newprefix;
        String newsuffix;
        int number=b.length();
        if(number==1){
            permutation.add(a+b);
        }else{
            for(int i=1;i<=number;i++){
                newsuffix=b.substring(1,number);
                newprefix=a+b.charAt(0);
                brutePlan(newprefix,newsuffix);
                b=newsuffix+b.charAt(0);
            }
        }
    }
    double publictime;
    double publiccost;
    double taxiTime;
    double taxiCost;
    double footTime;


    String minroute="";
    String mintraffic="";
    double timemax=Double.MAX_VALUE;
    double finalmoney=0;


    public void bruteCalculate(String i, String traffic,double price,double time,String position){
        if(i.length()==1){
            if(price<money&&time<timemax){
                finalmoney=price;
                timemax=time;
                minroute=position+","+selectedPlace.get(Integer.parseInt(i.charAt(0)+"") );
                minroute=minroute.substring(1);
                mintraffic=traffic;
                mintraffic=mintraffic.substring(1);
            }

        }
        else if(i.length()>1){
            for(String []a :adjacentList){
                if(selectedPlace.get(Integer.parseInt(i.charAt(0)+"")).equals(a[0])&&selectedPlace.get(Integer.parseInt(i.charAt(1)+"")).equals(a[1])){
                    publictime=Double.parseDouble(a[2]);
                    publiccost=Double.parseDouble(a[3]);
                    taxiTime=Double.parseDouble(a[4]);
                    taxiCost=Double.parseDouble(a[5]);
                    footTime=Double.parseDouble(a[6]);
                }
            }
            for (int j = 0; j <=2; j++) {
                if (j == 0) {

                    bruteCalculate(i.substring(1),traffic+",public transport",price+publiccost,time+publictime,position+","+selectedPlace.get(Integer.parseInt(i.charAt(0)+"") ));
                }else if(j==1){
                    bruteCalculate(i.substring(1),traffic+",taxi",price+taxiCost,time+taxiTime,position+","+selectedPlace.get(Integer.parseInt(i.charAt(0)+"") ) );
                }else{
                    bruteCalculate(i.substring(1),traffic+",on foot",price,time+footTime ,position+","+selectedPlace.get(Integer.parseInt(i.charAt(0)+"") ));
                }
            }
        }
    }


}
