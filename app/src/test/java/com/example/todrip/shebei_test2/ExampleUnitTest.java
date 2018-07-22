package com.example.todrip.shebei_test2;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
        try {
            JSONObject Lan1 = new JSONObject();//实例一个lan1的JSON对象

            Lan1.put("id",123456);
            Lan1.put("checktime","2018070845");
            //UpDownfile.uploadJson("http://youkangbao.cn/ykb/back/checkin/add.php",Lan1);
            System.out.println(Lan1.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}