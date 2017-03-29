package com.hulb.capricorn.hanlp;

import com.hankcs.hanlp.HanLP;

import java.util.List;

/**
 * Created by hulb on 17/3/28.
 * 自然语言处理
 * 1. 提取文字
 * 2. 语言转换简体繁体
 * 3. 积极消极
 * 4. 关键字 关键语义
 */
public class hanlpTest {
    public static String sourceString = "我是 这样 一个人,你是怎样的人呢";

    public static void main(String[] arg){
        pinyin();
    }

    /**
     * 繁体转简体
     */
    public static void jianti(){
        String result = HanLP.convertToTraditionalChinese(sourceString);
        System.out.println(result);
    }


    /**
     * 拼音
     */
    public static void pinyin(){
        String pinyin = HanLP.convertToPinyinString(sourceString," ",true);
        System  .out.println(pinyin);
    }

    /**
     * 提取主干
     */
    public static void zhugan(){
        List<String> mainci = HanLP.extractSummary(sourceString,5);
        mainci.stream().forEach(System.out::println);
    }
}
