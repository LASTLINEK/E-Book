package edu.bjtu.sei.simplecrud.util;

import java.util.ArrayList;
import java.util.HashMap;

public class IntString {
    private static String[] capital  =new String[] {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static HashMap<Integer,String> unit=new HashMap<Integer, String>() {{
        put(1,"个");
        put(10,"十");
        put(100,"百");
        put(1000,"千");
        put(10000,"万");
        put(100000000,"亿");
    }};
    private static ArrayList<String> toList(int value){
        String valueString=String.valueOf(value);
        ArrayList<String> list=new ArrayList<String>();
        int up=1;
        int lower=1;
        for(int i=valueString.length()-1;i>=0;i--) {
            int number=(int)(valueString.charAt(i)-48);
            if(up==10000||up==100000000) {
                String upUnit = unit.get(up);
                list.add(0,upUnit);
            }
            String lowerUnit = unit.get(lower);
            if(lowerUnit!=null) {
                list.add(0,lowerUnit);
            }
            list.add(0,capital[number]);
            up*=10;
            if(lower==1000) {
                lower=1;
            }else
                lower*=10;
        }
        return list;
    }
    public static String toCapital(int value) {
        ArrayList<String> list = toList(value);
        for(int i=0;i<list.size();i++) {
            String itme = list.get(i);
            if("零".equals(itme)) {
                if(i>0) {
                    String last = list.get(i-1);
                    if("零".equals(last)) {
                        list.remove(i);
                        list.remove(i);
                        i--;
                        continue;
                    }
                }
                String next = list.get(i+1);
                if("个".equals(next)) {
                    list.remove(i);
                    list.remove(i);
                    i--;
                }else{
                    list.remove(i+1);
                }
            }
        }
        String ret="";
        for(String e:list){
            if(! "个".equals(e)) {
                ret+=e;
            }
        }
        return ret.length()==0?"零":ret;
    }
    public static void main(String[] args) {
        System.out.println("("+0+")"+toCapital(0));
        System.out.println("("+100003+")"+toCapital(100003));
        for(int i=0;i<10;i++) {
            int value=(int) (Integer.MAX_VALUE * Math.random());
            System.out.println("("+value+")"+toCapital(value));
        }
    }
}
