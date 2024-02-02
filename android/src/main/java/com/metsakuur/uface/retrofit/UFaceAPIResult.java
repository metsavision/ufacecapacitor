package com.metsakuur.uface.retrofit;

public class UFaceAPIResult {
    private String code;
    private String msg;
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }


    @Override
    public String toString() {
        return "UFaceAPIResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
