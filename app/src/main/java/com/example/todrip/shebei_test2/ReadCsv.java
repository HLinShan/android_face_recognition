package com.example.todrip.shebei_test2;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class ReadCsv {



    public int get_TxtRowNum(String filename) throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            long fileLength = file.length();
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
            lineNumberReader.skip(fileLength);
            int rownum = lineNumberReader.getLineNumber();
            System.out.println("Total number of lines : " + rownum);
            lineNumberReader.close();
            return rownum;
        }
        return 0;
    }
    public float[][] readMonDataCsv(String filename) throws IOException {
        int i = 0;// 用于标记打印的条数
        int number=get_TxtRowNum(filename);
        float [][] feature=new float[number][513];
        try {
            File csv = new File(filename);// CSV文件路径
            BufferedReader br = new BufferedReader(new FileReader(csv));
            String line = "";
            while ((line = br.readLine()) != null ) { // 这里读取csv文件中的前10条数据
               //System.out.println("第" + i + "行：" );// 输出每一行数据
                String buffer[] = line.split(",");// 以逗号分隔
                //System.out.println(buffer);
                for(int c=0;c<513;c++)
                {
                    feature[i][c]=Float.parseFloat(buffer[c].replace("\"", ""));
                }
//                Log.i("feature","每行feature的个数" +feature[i].length);// 取第一列数据
            i=i+1;

            }
            Log.i("featureall","每行feature的个数" +feature.length);//
            br.close();
            return feature;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获得全部数组，然后将数组拆开获得第一列userid
    public int[] get_UserId(String filename) throws IOException {
        float[][] all=readMonDataCsv(filename);
        int length=all.length;
        int[] userid=new int[length];
        for(int i =0;i<length;i++)
        {
            userid[i]= (int) all[i][0];
        }
        Log.i("feature","userid的个数" +userid.length);//
        return userid;
    }
    //feature表
    public float[][] get_UserFeature(String filename) throws IOException {
        float[][] all=readMonDataCsv(filename);
        int row=all.length;
        int col=all[0].length;
       float[][] userfeature=new float[row][512];
        for(int i =0;i<row;i++)
        {
            for(int j=1;j<col;j++)
            {
            userfeature[i][j-1]=all[i][j];
            }
        }
        Log.i("feature","userfeature的个数" +userfeature.length);//
        Log.i("featurenumber","xxfsd"+userfeature[0].length);
         return userfeature;
    }
}
