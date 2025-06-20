package com.zhanchen.main.ui.process.entity;

import java.util.ArrayList;
import java.util.List;

public class FirstBean {

    private String code = "200";
    private String msg = "服务器返回成功";
    private RepData repData;

    public static class RepData {
        private List<String> content;

        public List<String> getContent() {
            List<String> list = new ArrayList<>();
            list.add("一级修");
            list.add("整备");
            list.add("二级修");
            list.add("临修");
            list.add("镟修");
            list.add("质量鉴定");
            return list;
        }

        public void setContent(List<String> content) {
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
