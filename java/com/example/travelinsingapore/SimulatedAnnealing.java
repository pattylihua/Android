package com.example.travelinsingapore;

import java.util.ArrayList;

public class SimulatedAnnealing {
    double currentSpend;
    double currentTime;
    double nextSpend;
    double nextTime;
    double money;
    double T = 100.1;
    double rate = 0.1;
    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public boolean e;
    public boolean f;
    public ArrayList<Integer> sequence;
    public ArrayList<Integer> transport;
    public ArrayList<Integer> nextSequence;
    public ArrayList<Integer> nextTransport;
    public static double[][][] tcost = {{{0,0,0},{17,3,14},{26,14,69},{35,19,76},{19,8,28},{84,30,269}},
            {{17,6,14},{0,0,0},{31,13,81},{38,18,88},{24,8,39},{85,29,264}},
            {{24,12,69},{29,14,81},{0,0,0},{10,9,12},{18,11,47},{85,31,270}},
            {{33,13,76},{38,14,88},{10,4,12},{0,0,0},{27,12,55},{92,32,285}},
            {{18,7,28},{23,8,39},{19,9,47},{28,14,55},{0,0,0},{83,30,264}},
            {{86,32,269},{87,29,264},{86,32,270},{96,36,285},{84,30,264},{0,0,0}}};;
    public static double[][][] mcost = {{{0,0,0},{0.83,3.22,0},{1.18,6.96,0},{4.03,8.5,0},{0.88,4.98,0},{1.96,18.4,0}},
            {{0.83,4.32,0},{0,0,0},{1.26,7.84,0},{4.03,9.38,0},{0.98,4.76,0},{1.89,18.18,0}},
            {{1.18,8.3,0},{1.26,7.96,0},{0,0,0},{2,4.54,0},{0.98,6.42,0},{1.99,22.58,0}},
            {{1.18,8.74,0},{1.26,8.4,0},{0,3.22,0},{0,0,0},{0.98,6.64,0},{1.99,22.8,0}},
            {{0.88,5.32,0},{0.98,4.76,0},{0.98,4.98,0},{3.98,6.52,0},{0,0,0},{1.91,18.4,0}},
            {{1.88,22.48,0},{1.96,19.4,0},{2.11,21.48,0},{2.11,23.68,0},{1.91,21.6,0},{0,0,0}}};;

    public SimulatedAnnealing(double money, boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
        this.money = money;
        this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;
        this.e=e;
        this.f=f;
    }
    public void initialize(){
        sequence = new ArrayList<>();
        if(b==true){
            sequence.add(1);
        }
        if(c==true){
            sequence.add(2);
        }
        if(d==true){
            sequence.add(3);
        }
        if(e==true){
            sequence.add(4);
        }
        if(f==true){
            sequence.add(5);
        }
        ArrayList<Integer> tmp = new ArrayList<>();
        while(!sequence.isEmpty()){
            int choose = (int)Math.floor(Math.random()*sequence.size());
            tmp.add(sequence.remove(choose));
        }
        sequence = tmp;
        transport = new ArrayList<>();
        for(int i=0;i<=sequence.size();i++) {
            int trans = (int)Math.floor(Math.random()*3);
            transport.add(trans);
        }
    }
    public String solve(){
        initialize();
        caculate();
        for(int i=0;i<1000;i++) {
            int choose = (int)Math.floor(Math.random()*2);
            if(choose==1) newRoute();
            else newTrans();
            caculateNext();
            if(nextSpend<=money) {
                if(nextTime<currentTime){
                    currentTime = nextTime;
                    currentSpend = nextSpend;
                    sequence = nextSequence;
                    transport = nextTransport;
                } else {
                    double difference = nextTime-currentTime;
                    double prob = Math.exp(-difference/T);
                    if(Math.random()<prob) {
                        currentTime = nextTime;
                        currentSpend = nextSpend;
                        sequence = nextSequence;
                        transport = nextTransport;
                    }
                }
            }
            T -= rate;
        }
        return sendMessage();
    }

    public String sendMessage(){
        String opt = "Marina Bay Sands";
        for(int i=0;i<sequence.size();i++){
            if(transport.get(i)==0) opt+=" (public) ->\n";
            else if(transport.get(i)==1) opt+=" (taxi) ->\n";
            else  opt+=" (foot) ->\n";
            if(sequence.get(i)==1) opt += "Singapore Flyer";
            else if(sequence.get(i)==2) opt += "Vivo City";
            else if(sequence.get(i)==3) opt += "Resorts World Sentosa";
            else if(sequence.get(i)==4) opt += "Buddha Tooth Relic Temple";
            else  opt += "Zoo";
        }
        if(transport.get(transport.size()-1)==0) opt+=" (public) ->\n";
        else if(transport.get(transport.size()-1)==1) opt+=" (taxi) ->\n";
        else  opt+=" (foot) ->\n";
        opt += "Marina Bay Sands\n";
        opt+="\nTime: "+currentTime+"\n";
        opt+="\nMoney:"+currentSpend+"\n";
        return opt;
    }

    public void caculate(){
        int start = 0;
        double time=0, spend=0;
        for(int i=0;i<sequence.size();i++){
            time += tcost[start][sequence.get(i)][transport.get(i)];
            spend += mcost[start][sequence.get(i)][transport.get(i)];
            start = sequence.get(i);
        }
        time += tcost[start][0][transport.get(transport.size()-1)];
        spend += mcost[start][0][transport.get(transport.size()-1)];
        currentSpend = spend;
        currentTime = time;
    }
    public void caculateNext(){
        int start = 0;
        double time=0, spend=0;
        for(int i=0;i<nextSequence.size();i++){
            time += tcost[start][nextSequence.get(i)][nextTransport.get(i)];
            spend += mcost[start][nextSequence.get(i)][nextTransport.get(i)];
            start = nextSequence.get(i);
        }
        time += tcost[start][0][nextTransport.get(nextTransport.size()-1)];
        spend += mcost[start][0][nextTransport.get(nextTransport.size()-1)];
        nextSpend = spend;
        nextTime = time;
    }

    public void newRoute(){
        int start = (int)Math.floor(Math.random()*sequence.size());
        int end = (int)Math.floor(Math.random()*sequence.size());
        while(Math.abs(end-start)<2){
            end = (int)Math.floor(Math.random()*sequence.size());
        }
        if(start>end){
            int temp = start;
            start = end;
            end = temp;
        }
        nextSequence = new ArrayList<>();
        boolean reverse = false;
        int count = 0;
        for(int i=0;i<sequence.size();i++){
            if(i==start){reverse=true;}
            if(reverse){
                nextSequence.add(sequence.get(end-count));
                count++;
            } else {
                nextSequence.add(sequence.get(i));
            }
            if(i==end){reverse=false;}
        }
        count = 0;
        nextTransport = new ArrayList<>();
        for(int i=0;i<transport.size();i++){
            if(i==start+1){reverse=true;}
            if(reverse){
                nextTransport.add(transport.get(end-count));
                count++;
            } else {
                nextTransport.add(transport.get(i));
            }
            if(i==end-1){reverse=false;}
        }
    }
    public void newTrans(){
        int start = (int)Math.floor(Math.random()*sequence.size());
        int end = (int)Math.floor(Math.random()*sequence.size());
        while(Math.abs(end-start)<1){
            end = (int)Math.floor(Math.random()*sequence.size());
        }
        if(start>end){
            int temp = start;
            start = end;
            end = temp;
        }
        nextTransport = new ArrayList<>();
        for(int i=0;i<transport.size();i++){
            if(i>=start&&i<=end) {
                int choose = (int)Math.floor(Math.random()*3);
                nextTransport.add(choose);
            } else {
                nextTransport.add(transport.get(i));
            }
        }
        nextSequence = sequence;
    }

    public static void main(String[] args) {
        SimulatedAnnealing solver = new SimulatedAnnealing(30, true, true, true, true, true, true);
        System.out.println(solver.solve());
    }
}
