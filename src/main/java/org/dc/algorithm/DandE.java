package org.dc.algorithm;

import org.apache.log4j.Logger;
import org.dc.Judge;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lotus_work on 2015/9/17.
 * 示例评分算法2
 */
public class DandE implements Judge{
    private static Logger logger= Logger.getLogger(DandE.class);
    public static void main(String[] args) {
        DandE correctPercent = new DandE();
        System.out.println("last:"+correctPercent.judge("C:\\Users\\lotus_work\\Desktop\\用户提交的.csv","C:\\Users\\lotus_work\\Desktop\\测试集.csv"));
    }

    @Override
    public Float judge(String produceFile, String judgeFile) {

        float resultStr=0f;
        logger.info("Beging compute");
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

        Map<String, Map<String, String>> judgeMap = getRankList(newFile);
        Map<String, Map<String, String>> resultMap= getRankList(oldfile);

        if (judgeMap.size()==0){
            return resultStr;
        }

        logger.info("完成数据加载");
        float result = computeResult(resultMap, judgeMap);
        logger.info("完成得分计算");
        return result;

    }
    private float computeResult(Map<String, Map<String, String>> resultMap, Map<String, Map<String, String>> judgeMap) {
        float sum = 0f;
        for (Map.Entry<String, Map<String, String>> entry : resultMap.entrySet()) {
            Map judge_Map = judgeMap.get(entry.getKey());
            if (judge_Map == null){
                continue;
            }
            if (judge_Map.get(entry.getKey()).equals(entry.getValue().get(entry.getKey()))){
                sum += 1;
            }
        }
        return sum/8;
    }

    private Map<String, Map<String, String>> getRankList(File file) {

        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        BufferedReader resultBfReader = null;
        String line = "";
        try {
            resultBfReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            line = resultBfReader.readLine();//read header
            if (line==null)
                return dataMap;
            int count = 0;
            while ((line = resultBfReader.readLine()) != null) {
                line = line.trim();
                count++;
                System.out.println(count+","+line);
                //System.out.println(count);
                if (line.length() == 0) {
                    logger.info(line);
                    continue;
                }
                String[] key = line.split(",");
                if (key.length<2){
                    logger.info(line);
                    continue;
                }
                Map<String, String> map = dataMap.get(key[0]);
                if (map == null) {
                    map = new HashMap<String, String>();
                    dataMap.put(key[0], map);
                }
                String[] value = line.split(",");
                if (value.length < 2) {
                    logger.info(line);
                    continue;
                }
                map.put(value[0].trim(), value[1].trim());
            }
            System.out.println("count:"+count);
            logger.info("count:" + count);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return dataMap;
        }
        return dataMap;

    }
}
