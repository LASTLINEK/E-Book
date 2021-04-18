package edu.bjtu.sei.simplecrud.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadBook {
    public static String preview(String id,int chapter){

        if(id.equals("3")){
            return macSearch(chapter+"",(chapter + 1) + "",id);
        }
        String chapterS = edu.bjtu.sei.simplecrud.readBook.IntString.toCapital(chapter);
        String chapterN = edu.bjtu.sei.simplecrud.readBook.IntString.toCapital(chapter + 1);
        return macSearch(chapterS,chapterN,id);
    }
    public static String macSearch(String mac,String next, String pathName) {
        pathName = "src/main/resources/WEB-INF/upload/"+pathName;
        List<String> strList = new ArrayList<String>();// 定义一个List存储读取的文本内容
        int flags = 0;// 定义一个flag存储keyword出现的行
// 从给定的MAC地址中取出代表企业的关键部分；
        mac = "第"+mac+"章";
        next = "第"+next+"章";
        String keyword =mac;
        String content = "";
        Pattern p = Pattern.compile(keyword);// 调用Pattern的compile方法编译要匹配的正则
        Pattern p2 = Pattern.compile(next);// 调用Pattern的compile方法编译要匹配的正则
        Matcher m;
        int i = 0;
        try {
//MAC地址与企业名称对照文件
            Reader re = new FileReader(new File(
                    pathName+".txt"));
            BufferedReader bre = new BufferedReader(re);
            while (bre.ready()) {
                String str = bre.readLine();
                strList.add(str);
                m = p.matcher(str);
                if (m.find())// 查找正则匹配的子串是否存在
                {
                    content += str;
                    break;
                }

            }
            while (true){
                String str;
                str = bre.readLine();
                if (str == null || p2.matcher(str).find())// 查找正则匹配的子串是否存在
                {
                    break;
                }
                content += "\n"+str;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return content;
    }

    public static int count(){
        return 100;
    }

    public static void main(String[] args) {
        System.out.println(ReadBook.preview("3",1));
    }

}

