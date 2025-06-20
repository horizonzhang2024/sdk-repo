package com.zhanchen.main.ui.process.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondBean {

    private String code = "200";
    private String msg = "服务器返回成功";
    private RepData repData;

    public static class RepData {
        private Map<String, List<String>> content;

        public Map<String, List<String>> getContent() {
            Map<String, List<String>> map = new HashMap<>();

            List<String> list1 = new ArrayList<>();
            list1.add("车侧");
            list1.add("车下");
            list1.add("车内");
            list1.add("车顶");
            list1.add("滤网");
            list1.add("看板");

            List<String> listZb = new ArrayList<>();
            listZb.add("车下");
            listZb.add("车内");
            listZb.add("车顶");
            listZb.add("滤网");
            listZb.add("看板");

            List<String> list2 = new ArrayList<>();
            list2.add("A类项目");
            list2.add("B类项目");
            list2.add("C类项目");

            List<String> listLx = new ArrayList<>();
            listLx.add("禁动牌挂设");
            listLx.add("作业指导书");
            listLx.add("合格证");
            listLx.add("工具确认");
            listLx.add("更换件确认");
            listLx.add("过程盯控");

            List<String> listXx = new ArrayList<>();
            listXx.add("禁动牌挂设");
            listXx.add("首轮和尾轮人工测");

            List<String> listJd = new ArrayList<>();
            listJd.add("自定义");


            map.put("一级修", list1);
            map.put("整备", listZb);
            map.put("二级修", list2);
            map.put("临修", listLx);
            map.put("镟修", listXx);
            map.put("质量鉴定", listJd);

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
