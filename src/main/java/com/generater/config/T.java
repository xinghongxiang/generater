package com.generater.config;

import java.util.*;

/**
 * @detail
 * @Author Created by Cita
 * @date on 2021/9/18 10:48
 */
public class T {
    public static void main(String[] args) {
        // 首先排序这东西有很多现成的接口可以直接实现， 我们只是学一下里面的思想
        // 1.模拟生成数据
        List<Map<String, Object>> result = new ArrayList<>();
        int minValue = 1990, maxValue = 2021 ;
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("Model" , "robot" + i + "号");
            map.put("No" , (int)(Math.random()*(maxValue - minValue) + minValue));
            result.add(map);
        }

        // 2.进行排序后比较替换
        int max = 0; //中间比大小值
        for (int i = 0; i < result.size(); i++) {
            //max = Integer.parseInt(result.get(i).get("需要排序字段名").toString());
            max = Integer.parseInt(result.get(i).get("No").toString());
            for (int j = i; j < result.size(); j++) {
                //int compare = Integer.parseInt(result.get(j).get("需要排序的字段名").toString());
                int compare = Integer.parseInt(result.get(j).get("No").toString());
                //if (compare < max) { //从小到大排序
                if (compare > max) {   //从大到小排序
                    Map<String, Object> temp = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temp);
                    max = compare; //若下一个值比上一个值大把最大值更新
                }
            }
        }
        System.out.println(result.toString());
    }
}