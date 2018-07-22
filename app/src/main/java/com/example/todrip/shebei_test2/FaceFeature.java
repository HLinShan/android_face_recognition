package com.example.todrip.shebei_test2;
/* by cjf 1801615 352871242@qq.com*/

import android.util.Log;

/**
 * 人脸特征(512维特征值)
 * 相似度取特征向量之间的欧式距离.
 */
public class FaceFeature {
    public static final int DIMS=512;
    private float fea[];
    FaceFeature(){
        fea=new float[DIMS];
    }
    public float[] getFeature(){
        return fea;
    }
    //比较当前特征和另一个特征之间的相似度
    public double compare(FaceFeature ff){
        double dist=0;
        for (int i=0;i<DIMS;i++)
            dist+=(fea[i]-ff.fea[i])*(fea[i]-ff.fea[i]);
        dist= Math.sqrt(dist);
        return dist;
    }
    //直接用一维数组与图片特征相似度
    public double compare_float(float[] feature){
        if (feature.length==0){
            Log.i("compare_float","compare_float");
            return 0;

        }
        double dist=0;
        for (int i=0;i<DIMS;i++)
        {
            dist+=(fea[i]-feature[i])*(fea[i]-feature[i]);
        }
        dist= Math.sqrt(dist);
        return dist;
    }

}
