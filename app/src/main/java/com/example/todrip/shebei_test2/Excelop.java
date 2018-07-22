package com.example.todrip.shebei_test2;
import android.util.Log;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Excelop {
    private static final String TAG = MainActivity.class.getSimpleName();
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH)+1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);


    //public UpDownfile upDownfile;

    ///  UP UP  check excelfile
    //获得用户签到时间
    public String get_UserCheckTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss

        Date date = new Date(System.currentTimeMillis());
        String get_userchecktime;
        get_userchecktime= simpleDateFormat.format(date);
        return get_userchecktime;
    }

    //设置excel文件的名字 日期+morning 日期+afternoon
    public String set_UpExcelFileName(String outputfiledir){
        String checkfilename;
        if (hour<=12){
            checkfilename=year +""+ month +""+ day + "UserCheckMorning_uploadfile"+".xlsx";}
        else {
            checkfilename = year +""+ month +""+ day + "UserCheckAfternoon_uploadfile"+".xlsx";
        }

        String outputFileName=outputfiledir +checkfilename;
        return outputFileName;
    }

    //产生本地excel文件在安卓机上
    public void export_ExcelFile(String excelFile, int user_ID, String user_Name, String user_checktime) {



        try {
            File objFile = new File(excelFile);
            XSSFWorkbook workbook = null;
            XSSFSheet sheet = null;
            String table_name = "sheet1";
            //创建文件
            if (!objFile.exists()) {   //文件不存在
                //创建新的Excel 工作簿
                System.out.println("检测到文件不存在,正在创建文件...");
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(table_name);
                //新建一输出文件流
                FileOutputStream fOut = new FileOutputStream(excelFile);
                //把相应的Excel 工作簿存盘
                workbook.write(fOut);
                fOut.flush();
                //操作结束，关闭文件
                fOut.close();
            }
            //如果文件存在,将直接插入数据.
            //创建对Excel工作簿文件的引用
            workbook = new XSSFWorkbook(new FileInputStream(excelFile));
            //创建对工作表的引用。
            //本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
            sheet = workbook.getSheet(table_name);
            int get_currentRowNum = sheet.getLastRowNum();//获得最后一行
            Row row;//如果是当前行是第一行则直接操作
            row=sheet.getRow(get_currentRowNum);
            if(get_currentRowNum==0 && row==null){
                row = sheet.createRow(get_currentRowNum);

            }
            else{
                row = sheet.createRow(get_currentRowNum + 1);
            }

            System.out.println(row);



            Cell cell0 = row.createCell(0);
            cell0.setCellValue(user_ID);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(user_Name);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(user_checktime);
            //新建一输出文件流
            FileOutputStream fOut = new FileOutputStream(excelFile);
            //把相应的Excel 工作簿存盘
            workbook.write(fOut);
            fOut.flush();
            // 操作结束，关闭文件
            fOut.close();
            System.out.println("1111文件已生成!");
        } catch (Exception e) {
            Log.e(TAG,"已运行 xlCreate() : " , e);
        }
    }

    //当确定通过的时候直接往外部内存写文件
    public String write_ExcelFile(String outputfiledir,int user_id,String user_name){
        String set_excelfilename=set_UpExcelFileName(outputfiledir);
        String user_checktime=get_UserCheckTime();
        export_ExcelFile(set_excelfilename,user_id,user_name,user_checktime);
        return  set_excelfilename;
    }

    ////// case simple  自己做的测试时 做一个case
    //传入一个simple feature 是一维数组

    public void write_ExcelFilefacefeture(String outputfiledir,int user_ID,String user_Name,float feature[]){
        System.out.println("进入simplefile !");
        String simple_excelfilename=outputfiledir+"1111"+".xlsx";
        try {
            File objFile = new File(simple_excelfilename);
            XSSFWorkbook workbook = null;
            XSSFSheet sheet = null;
            String table_name = "sheet1";
            //创建文件
            if (!objFile.exists()) {   //文件不存在
                //创建新的Excel 工作簿
                System.out.println("检测到文件不存在,正在创建文件...");
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(table_name);
                //新建一输出文件流
                FileOutputStream fOut = new FileOutputStream(simple_excelfilename);

                //把相应的Excel 工作簿存盘
                workbook.write(fOut);
                fOut.flush();
                //操作结束，关闭文件
                fOut.close();
            }
            //如果文件存在,将直接插入数据.
            //创建对Excel工作簿文件的引用
            workbook = new XSSFWorkbook(new FileInputStream(simple_excelfilename));
            //创建对工作表的引用。
            //本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
            sheet = workbook.getSheet(table_name);
            int get_currentRowNum = sheet.getLastRowNum();//获得最后一行
            Row row;//如果是当前行是第一行则直接操作
            row = sheet.getRow(get_currentRowNum);
            if (get_currentRowNum == 0 && row == null) {
                row = sheet.createRow(get_currentRowNum);

            } else {
                row = sheet.createRow(get_currentRowNum + 1);
            }

            System.out.println(row);
//  写入数据


            Cell cell0 = row.createCell(0);
            cell0.setCellValue(user_ID);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(user_Name);

//            Cell cell2 = row.createCell(2);
//            cell2.setCellValue(get_userfeature[0]);
//            Cell cell3 = row.createCell(3);
//            cell3.setCellValue(get_userfeature[1]);
//            Cell cell4 = row.createCell(4);
//            cell4.setCellValue(get_userfeature[2]);


            Cell cell2;
            int featurenum=feature.length;
            for (int i = 2; i < 2+featurenum; i++)
            {
                cell2 = row.createCell(i);
                cell2.setCellValue(feature[i-2]);

            }


            //新建一输出文件流
            FileOutputStream fOut = new FileOutputStream(simple_excelfilename);
            //把相应的Excel 工作簿存盘
            workbook.write(fOut);
            fOut.flush();
            // 操作结束，关闭文件
            fOut.close();
            System.out.println("文件simple已生成!");
        } catch (Exception e) {
            Log.e(TAG,"已运行 xlCreate() : " , e);
        }



    }






//    DOWN USER INFO EXCEL  AND READ EXCEL retuen some infomation

    //设置每个月下载的用户信息xlsx的名称
    public String set_DownExcelFileName(String outputfiledir){
        String filename;
       filename=year +""+ month +"UserInfo_downloadfile"+".xlsx";
        String outputFileName=outputfiledir +filename;
        return outputFileName;
    }




    //TODO 下载文件操作TODO x下载 按钮
    public String load_DownExcelFromServer(String serverurl,String outputfiledir){

        String load_downexcelfile=set_DownExcelFileName(outputfiledir);

        UpDownfile.downloadFile(serverurl, new File(load_downexcelfile));
        return load_downexcelfile;
    }




    //1从服务器下载excel读uerid 用一维数组返回 从0列开始
    public int[] get_ExcelFileUserId(String excelFile){
        int[] get_exceluserid;
        int value;
        Cell cell;
        File objFile = new File(excelFile);
        if (!objFile.exists()) {   //文件不存在

            return null;
        }
        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFile));
            Sheet sheet = workbook.getSheetAt(0);//或者根据表名称读取：sheet=workbook.getSheet("表1");
            get_exceluserid=new int[sheet.getLastRowNum()];
            for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                cell = row.getCell(0);
                value = (int) cell.getNumericCellValue();
                get_exceluserid[i] = value;
            }
            return get_exceluserid;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }


    }
    //2从服务器下载excel读取特征值 用二维数组返回 从第1列开始
    public  float[][] get_ExcelFileUserFeature(String excelFile ) {
        float[][] get_exceluserfeature;
        float value;
        Cell cell;
        Row row;
        File objFile = new File(excelFile);
        if (!objFile.exists()) {   //文件不存在
            return null;
        }
        try {

            Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFile));
            Sheet sheet = workbook.getSheetAt(0);//或者根据表名称读取：sheet=workbook.getSheet("表1");
            get_exceluserfeature=new float[sheet.getLastRowNum()][];
            for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                get_exceluserfeature[i] = new float[row.getLastCellNum()];
                for (int j = 2; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    value = (float) cell.getNumericCellValue();
                    get_exceluserfeature[i][j-2] = value;

                }
            }
            return get_exceluserfeature;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //今天打开此文件夹找到相应的服务器上下载下来的excel

    public String get_ExcelFileFromLocal(String outputfiledir){
        String get_downexcelfile=set_DownExcelFileName(outputfiledir);

        File objFile = new File(get_downexcelfile);
        if (!objFile.exists()) {

            System.out.println("这个月没有下载相应的文件");
            //TODO 去网站上下载对应的文件保存在这个问价夹中
            //get_downexcelfile=load_DownExcelFromServer(outputfiledir)
            return get_downexcelfile;
        }//如果有这个文件的话返回绝对路径回去
        return get_downexcelfile;
    }





    //删除
    //设置每个月月初1号删除掉上个月的app导出和下载下来的xlsx文件
    //environment/2018715_UpDownFileDir/---删除该文件夹的所有文件
    public boolean delete_File(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
    public boolean delete_FileDir(String outputfiledir){
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!outputfiledir.endsWith(File.separator)) {
            outputfiledir = outputfiledir + File.separator;
        }
        File dirFile = new File(outputfiledir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = delete_File(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }
    // 匹配删除上个月excel文件夹 从environment 那里面取外部地址
    public boolean delete_LastMonthExcelDir(String outputfiledir) {
        boolean flag = false;
        File current_outputfiledir = new File(outputfiledir);
        String get_environmetpath = current_outputfiledir.getParent();
        if (!get_environmetpath.endsWith(File.separator)) {
            get_environmetpath = outputfiledir + File.separator;
        }
        File dirFile = new File(get_environmetpath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        File[] files = dirFile.listFiles();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String containstring = year+""+month+"_UpDownloadFileDir";
        if (day == 1) {

            //遍历删除文件夹下的所有文件(包括子目录)
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    //删除上个月文件
                    String lastmonthdir = files[i].getAbsolutePath();
                    if (lastmonthdir.indexOf(containstring) != -1) {

                        flag=delete_FileDir(lastmonthdir);

                    }
                }
            }
            if (!flag) return false;
            //删除当前空目录

        }
        return flag;
    }



//    删除该文件夹下的所有文件 environment/downloadfiledir
        public void del_AllFiles(File downloadfiledir) {
            File files[] = downloadfiledir.listFiles();
            if (files != null)
                for (File f : files) {
                    if (f.isDirectory()) { // 判断是否为文件夹
                        del_AllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    } else {
                        if (f.exists()) { // 判断是否存在
                            del_AllFiles(f);
                            try {
                                f.delete();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
        }


        public void init_Excelop()
    {
        // excel初始化 如果今天是1号 话就在下载user信息excel文件
        //并且删除上个月的签到信息和user信息excel 上个月
        // 如果今天不是1号的话就 判断下该文件夹里是否有用户信息文件
        //如果没有话 就去网站上下载

    }



}
