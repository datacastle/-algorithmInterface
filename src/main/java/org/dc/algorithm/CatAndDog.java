package org.dc.algorithm;

import org.dc.Judge;

import java.io.*;
import java.util.*;

/**
 * Created by lotus_work on 2016/3/31.
 * 示例评分算法1
 */
public class CatAndDog implements Judge {
    public static  void main(String[] args){
        CatAndDog catAndDog = new CatAndDog();
        System.out.println(catAndDog.judge("C:\\Users\\lotus_work\\Desktop\\猫狗大战\\用户提交的.csv","C:\\Users\\lotus_work\\Desktop\\猫狗大战\\测试集.csv"));
    }

    @Override
    public Float judge(String produceFile, String judgeFile) {
        float resultStr=0f;
        File oldfile = new File(produceFile);
        if (!oldfile.exists()) {
            System.out.println("结果文件不存在");
            return resultStr;
        }
        if (oldfile.isDirectory()) {
            File[] files = oldfile.listFiles();
            if (files.length <= 0)
                return resultStr;
            oldfile = files[0];
        }
        File newFile = new File(judgeFile);
        if (!newFile.exists()) {
            System.out.println("测试集文件不存在");
            return resultStr;
        }
        if (newFile.isDirectory()) {
            File[] files = newFile.listFiles();
            if (files.length <= 0) {
                System.out.println("测试集文件不存在");
                return resultStr;
            }
        }

        Set<String> judgeList = getRankList(newFile);
        Set<String> resultList = getRankList(oldfile);

        if (judgeList.size()==0){
            return resultStr;
        }

        float result = computeResult(resultList, judgeList);

        return result;
    }

    private float computeResult(Set<String> resultList, Set<String> judgeList) {
        float sum = 0f;
        int count = 0;
        for (String string:resultList){
            count ++;
            if (count>=200){
                break;
            }
            if (judgeList.contains(string)){
                sum += 1;
            }
        }
        return sum/judgeList.size();
    }

    private Set<String> getRankList(File newFile) {
        Set<String> dataList = new HashSet<String>();
        BufferedReader resultBfReader = null;
        String line = "";
        try {
        resultBfReader = new BufferedReader(new InputStreamReader(new FileInputStream(newFile),"utf-8"));
            line = resultBfReader.readLine();//read header
            if (line==null)
                return dataList;
            int count = 0;
            while ((line = resultBfReader.readLine()) != null) {
                line = line.trim();
                count++;
                //System.out.println(count);
                if (line.length() == 0) {
                    continue;
                }
                if ("".equals(line)){
                    continue;
                }
                dataList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return dataList;
        }
        return dataList;
    }

}
