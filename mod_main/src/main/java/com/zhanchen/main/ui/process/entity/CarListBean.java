package com.zhanchen.main.ui.process.entity;

import java.util.ArrayList;
import java.util.List;

public class CarListBean {

    private String code = "200";
    private String msg = "服务器返回成功";
    private RepData repData;

    public static class RepData {
        private List<String> carTypes;

        public List<String> getCarTypes() {
            List<String> list = new ArrayList<>();
            list.add("CRH2A");
            list.add("CRH-380AL");
            list.add("CR400AF");
            list.add("CR400BF");
            return list;
        }

        public void setCarTypes(List<String> carTypes) {
            this.carTypes = carTypes;
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
