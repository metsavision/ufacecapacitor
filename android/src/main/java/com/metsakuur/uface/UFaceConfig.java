package com.metsakuur.uface;

public class UFaceConfig {
    public static volatile UFaceConfig instance;

    //region Singleton
    public static UFaceConfig getInstance() {
        if (instance == null)
            instance = new UFaceConfig();
        return instance;
    }

    public String[] modelList = {
            "eyeblink.onnx",
            "landmark_106_bin.dat",
            "maskface_20210525_bin.dat",
            "metsakuur_v2se_230405_20210525.bin",
            "mobileattack.onnx",
            "mobilefacenet_bin.dat"
    };

    //2034.12.31
    public String licenseKey = "";
    //server ip
    public String BASE_URL = "http://ekyc.metsafr.com:3333";

    public String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzZXJ2aWNlIjoib25lNHUiLCJzdWIiOiJvbmU0dSIsImp0aSI6IjE0YTdiZTExLTY1N2UtNDgwZi05M2I4LTAzMGM0ODA1ZWY4MCIsImlhdCI6MTcwMjUzMjAzMiwiZXhwIjoxNzM0MDY4MDMyfQ.4eFOmxM-WQQeucB7HWvTE2ksKindvJ2xEfgngFiQ0Ok";
    public String service_name = "face";
    public String idCardBase64;
    public String scoreValue;

}
