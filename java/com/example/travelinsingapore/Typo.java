package com.example.travelinsingapore;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Typo {
    private static double substringW = 0.6;
    private static double sequenceW = 0.4;
    public static double getLCSubstring(String x, String y) {
        int max = 0;
        int m = x.length();
        int n = y.length();
        int l = m>n?m:n;
        if(m>n) {
            String temp = x;
            x = y;
            y = temp;
            int tmp = m;
            m = n;
            n = tmp;
        }
        for(int i=1;i<n+m;i++) {
            int xs = m-i, xe = m-(i-n), ys = i-m, ye = i;
            if(xs<0) xs = 0;
            if(xe>m) xe = m;
            if(ys<0) ys = 0;
            if(ye>n) ye = n;
            String subx = x.substring(xs,xe);
            String suby = y.substring(ys,ye);
            int j = 0;
            while(j<subx.length()) {
                int tmax = 0;
                while(j<subx.length()&&subx.substring(j,j+1).equalsIgnoreCase(suby.substring(j,j+1))){
                    tmax++;
                    j++;
                }
                j++;
                max = tmax>max? tmax:max;
            }
        }
        return max/(double) l;
    }

    public static double getLCSequence(String x, String y) {
        int m = x.length();
        int n = y.length();
        int l = m>n?m:n;
        int[][] c = new int[m+1][n+1];
        for (int i = 1; i <= m; i++) {
            c[i][0] = 0;
        }
        for(int i = 0;i <= n;i++) {
            c[0][i] = 0;
        }
        for(int i=1;i<=m;i++) {
            for(int j=1;j<=n;j++) {
                if(x.substring(i-1,i).equalsIgnoreCase(y.substring(j-1,j))) c[i][j] = c[i-1][j-1]+1;
                else if(c[i-1][j]>=c[i][j-1]) c[i][j] = c[i-1][j];
                else c[i][j] = c[i][j-1];
            }
        }
        return c[m][n]/(double) l;
    }

    public static double getSimilarity(String x, String y) {
        double substring = getLCSubstring(x,y);
        double sequence = getLCSequence(x,y);
        return substringW*substring+sequenceW*sequence;
    }

    public static String getResults(String x, String[] y){
        ArrayList<Double> similarity = new ArrayList<>();
        similarity.add((double) 0);
        ArrayList<String> results = new ArrayList<>();
        String[] subx = x.split(" ");
        for(String s:subx) {
            results.add(s);
        }
        for(int i=0;i<y.length;i++) {
            ArrayList<Double> tSimilarity = new ArrayList<>();
            ArrayList<String> subResutls = new ArrayList<>();
            String[] suby = y[i].split(" ");
            String cbbr = "";
            for(int j=0;j<suby.length;j++) {
                if(Character.isUpperCase(suby[j].charAt(0))){
                    cbbr += suby[j].substring(0,1);
                } else continue;
                double subSimilarity = 0;
                for(int k=0;k<subx.length;k++){
                    double tSubSimilarity = getSimilarity(subx[k], suby[j]);
                    if(tSubSimilarity>subSimilarity&&tSubSimilarity>0.5) {
                        subSimilarity = tSubSimilarity;
                    }
                }
                if(subSimilarity!=0){
                    tSimilarity.add(subSimilarity);
                    subResutls.add(suby[j]);
                }
            }
            double cSimilarity = getSimilarity(x, cbbr);
            Collections.sort(tSimilarity);
            Collections.reverse(tSimilarity);
            for(int j=0;j<=similarity.size();j++) {
                if(j==tSimilarity.size()) break;
                if(j==similarity.size()||tSimilarity.get(j)>similarity.get(j)) {
                    similarity = tSimilarity;
                    results = subResutls;
                    results.add(y[i]);
                    break;
                }
            }
            if(cSimilarity==1&&x.length()==cbbr.length()) {
                similarity = new ArrayList<>();
                similarity.add(cSimilarity);
                results = new ArrayList<>();
                results.add(y[i]);
            }
        }
        String opt = results.get(results.size()-1);
        /*for(int i=1;i<results.size()-1;i++){
            opt += " "+results.get(i);
        }*/
        return opt;
    }

    public static void main(String[] args) {
        getResults("marina bay sands", SomeAddress.address);
    }
}
