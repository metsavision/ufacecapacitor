package com.metsakuur.uface.retrofit;


public class UFaceAPIRequest {
    private String apiName;
    private String service;
    private Object param;

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public void setService(String service) {
        this.service = service;
    }


    public void setParam(Object param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "UFaceAPIRequest{" +
                "apiName='" + apiName + '\'' +
                ", service='" + service + '\'' +
                ", param=" + param +
                '}';
    }

}
