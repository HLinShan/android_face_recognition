package com.example.todrip.shebei_test2;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpDownfile {
    private static final String TAG = UpDownfile.class.getSimpleName();

    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 10;
    private static final String CONTENT_TYPE = "application/octet-stream";
    private static OkHttpClient client;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH)+1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);



    static {
        client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }
//上传文件
    public static void uploadFile(String url, String part, final File file) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(part, file.getName(), RequestBody.create(MediaType.parse(CONTENT_TYPE), file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"failed to upload file to "+call.request().url(), e);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Preconditions.checkArgument(response.isSuccessful(), "HTTP请求失败");
                Preconditions.checkNotNull(response.body(), "HTTP请求响应体为空");

                closeSilently(response);
                Log.i(TAG,"successful upload file "+ file.getAbsolutePath()+"to"+call.request().url());
            }
        });
    }



//上传 ID 值
//    http://youkangbao.cn/ykb/back/checkin/add.php
    public  void uploadDataToServer(String url,int id)   {
        Excelop excelop=new Excelop();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",String.valueOf(id))
                .build();
//                .addFormDataPart("checktime",checktime)
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"failed to upload file to "+call.request().url(), e);

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Preconditions.checkArgument(response.isSuccessful(), "HTTP请求失败");
                Preconditions.checkNotNull(response.body(), "HTTP请求响应体为空");
                if (response.isSuccessful()) {
                    Log.i(TAG, "onResponse: " + response.body().string());

                }
                closeSilently(response);
                Log.i(TAG,"successful upload file ");

            }
        });
    }


//从服务器下载文件csv文件
    public static void downloadFile(String url, final File file) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"failed to download file to "+call.request().url(), e);
            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Preconditions.checkArgument(response.isSuccessful(), "HTTP请求失败");
//                Preconditions.checkNotNull(response.body(), "HTTP请求响应体为空");

                Files.write(response.body().bytes(), file);
                closeSilently(response);
                Log.i(TAG,"successful upload file "+ file.getAbsolutePath()+" to "+call.request().url());
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void closeSilently(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }

    //TODO 设置每个月下载的用户信息CSV的名称
    //csv文件
    public String set_DownCsvFileName(String outputfiledir){
        String filename;
        filename=year +""+ month +"UserInfo_downloadfile"+".csv";
        String outputFileName=outputfiledir +filename;
        return outputFileName;
    }
    //下载文件操作 TODO
    public String load_DownCsvFileFromServer(String serverurl,String outputfiledir){

        String load_downexcelfile=set_DownCsvFileName(outputfiledir);
        UpDownfile.downloadFile(serverurl, new File(load_downexcelfile));
        return load_downexcelfile;
    }
    //下载Info文件
    public String set_DownInfoTxtFileName(String outputfiledir){
        String filename;
        filename=year +""+ month +"ParameterInfo_downloadfile"+".txt";
        String outputFileName=outputfiledir +filename;
        return outputFileName;
    }
    //下载文件操作 TODO
    public String load_DownInfoTxtFromServer(String serverurl,String outputfiledir){
        String load_downexcelfile=set_DownInfoTxtFileName(outputfiledir);
        UpDownfile.downloadFile(serverurl, new File(load_downexcelfile));
        return load_downexcelfile;
    }

    //删除该文件夹下的所有文件 environment/downloadfiledir
    public boolean del_AllFiles(File downloadfiledir) {
        File files[] = downloadfiledir.listFiles();
        if (files != null) {
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
            return  true;
        }else {
            return false;
        }

    }



}
