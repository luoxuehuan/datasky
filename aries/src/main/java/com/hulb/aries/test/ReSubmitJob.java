package com.hulb.aries.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dtwave.common.http.request.HttpResponse;
import com.dtwave.common.http.request.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hulb on 17/3/27.
 */
public class ReSubmitJob {

    /**
     * 接收一个job instance id 获取相关参数
     *
     * 解析相关参数,构造 一个新的 json请求
     *
     * 根据新的请求,发送相关信息。
     */
    public void reSubmitJob(String instanceId){

    }

    public static boolean showDataBase(){

        String path = " http://114.215.241.95:7188/spiluxio/job/createJob";
        String bodyString = FastJSON.createBody();
        HttpResponse response = new Request(path).body(bodyString).POST();
        if (!response.isError() && !response.value("content").equals("false")) {

            // System.out.print("查询成功"+response.value("content"));
            // logger.error("提交的插入血缘关系语句成功", response.errorInfo());
            /**
             * 怎么解析response
             *
             * fastjson json数组
             *
             * 转出一个普通的array 返回。
             */
            String content = response.value("content");
            JSONArray.parse(response.value("content"));
            //String text = JSON.toJSONString(obj); //序列化
            ArrayList vo = JSON.parseObject(content, ArrayList.class); //反序列化
            //System.out.print(vo);
            for(Object str:vo){
                System.out.println(str.toString());
            }
            return true;
        } else {
            String content = response.value("content");
            System.out.println(content);
            System.out.print("提交的插入血缘关系语句失败");
            //logger.error("提交的插入血缘关系语句出错", response.errorInfo());
            return false;
        }
    }






    public static void main(String[] arg){
        showDataBase();

    }

}
