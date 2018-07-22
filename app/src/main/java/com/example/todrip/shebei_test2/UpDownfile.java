package com.example.todrip.shebei_test2;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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

    private static final int CONNECT_TIMEOUT = 3;
    private static final int WRITE_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 5;
    private static final String CONTENT_TYPE = "application/octet-stream";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//    private static final MediaType JSON = MediaType.parse("application/formdata; charset=utf-8");
    private static OkHttpClient client;


//

//



//          lan.put("Title", "计算机科学");



//        lan1.("id",1);//对lan1对象添加数据
//        lan1.put("ide","Eclipse");//对lan1对象添加数据

    static {
        client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

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



    //上传json
//    String json1="{\"ID\": \"张三 \"\"checktime\": \"23121521 \" }";
//    JSONObject Lan1 = new JSONObject();//实例一个lan1的JSON对象
//
//        Lan1.put("ID",123456);
//        Lan1.put("checktime","2018070845");

    public static void uploadDataToServer(String url,int id,String checktime)   {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",String.valueOf(id))
                .addFormDataPart("checktime",checktime)
                .build();



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
}
