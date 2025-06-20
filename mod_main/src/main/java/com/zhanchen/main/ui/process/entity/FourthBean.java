package com.zhanchen.main.ui.process.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourthBean {

    private String code = "200";
    private String msg = "服务器返回成功";
    private RepData repData;

    public static class RepData {
        private Map<String, List<String>> content;

        public Map<String, List<String>> getContent() {
            Map<String, List<String>> map = new HashMap<>();

            //一号位
            List<String> list11 = new ArrayList<>();
            list11.add("无电");
            list11.add("有电");

            //一级修+二号位
            List<String> list12 = new ArrayList<>();
            list12.add("无电");
            list12.add("有电");

            //一级修+三号位
            List<String> list13 = new ArrayList<>();
            list13.add("无电");
            list13.add("有电");

            //一级修+四号位
            List<String> list14 = new ArrayList<>();
            list14.add("无电");
            list14.add("有电");

            //一级修+自定义
            List<String> list1Other = new ArrayList<>();
            list1Other.add("无电");
            list1Other.add("有电");

            //AB类分析项目
            List<String> listAB = new ArrayList<>();
            listAB.add("自定义");

            //C类分析项目
            List<String> listC = new ArrayList<>();
            listC.add("自定义");
            //其他项目
            List<String> listOther = new ArrayList<>();
            listOther.add("自定义");
            //出库前分析项目
            List<String> listCK = new ArrayList<>();
            listCK.add("自定义");
            //延长修齿轮箱润滑油更换
            List<String> listYCGH = new ArrayList<>();
            listYCGH.add("自定义");
            //延长修齿轮箱及吊挂装置状态检查
            List<String> listYCJC = new ArrayList<>();
            listYCJC.add("自定义");
            //延长修齿轮箱接地装置分解检查
            List<String> listYCFJ = new ArrayList<>();
            listYCFJ.add("自定义");
            //延长修排障撒砂装置检查及扭矩校核
            List<String> listYCJH = new ArrayList<>();
            listYCJH.add("自定义");
            //延长修制动盘、制动夹钳装置检查
            List<String> listYCZD = new ArrayList<>();
            listYCZD.add("自定义");
            //延长修主空压机润滑油和油过滤器更换
            List<String> listYCKY = new ArrayList<>();
            listYCKY.add("自定义");
            //延长修主空压机滤芯更换
            List<String> listYCJL = new ArrayList<>();
            listYCJL.add("自定义");


            //B类故障
            List<String> listErrorB = new ArrayList<>();
            listErrorB.add("自定义");
            //C类故障
            List<String> listErrorC = new ArrayList<>();
            listErrorC.add("自定义");


            //     上水
            List<String> listSS = new ArrayList<>();
            listSS.add("自定义");
            //卸污
            List<String> listXW = new ArrayList<>();
            listXW.add("自定义");


            map.put("一号位", list11);
            map.put("二号位", list12);
            map.put("三号位", list13);
            map.put("四号位", list14);
            map.put("自定义位", list1Other);

            map.put("AB类分析项目", listAB);
            map.put("C类分析项目", listC);
            map.put("其他项目", listOther);
            map.put("出库前分析项目", listCK);
            map.put("延长修齿轮箱润滑油更换", listYCGH);
            map.put("延长修齿轮箱及吊挂装置状态检查", listYCJC);
            map.put("延长修齿轮箱接地装置分解检查", listYCFJ);
            map.put("延长修排障撒砂装置检查及扭矩校核", listYCJH);
            map.put("延长修制动盘、制动夹钳装置检查", listYCZD);
            map.put("延长修主空压机润滑油和油过滤器更换", listYCKY);
            map.put("延长修主空压机滤芯更换", listYCJL);

            map.put("B类故障", listErrorB);
            map.put("C类故障", listErrorC);

            map.put("上水", listSS);
            map.put("卸污", listXW);
            return map;
        }

        public void setContent(Map<String, List<String>> content) {
            this.content = content;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RepData getRepData() {
        return repData = new RepData();
    }

    public void setRepData(RepData repData) {
        this.repData = repData;
    }
}
