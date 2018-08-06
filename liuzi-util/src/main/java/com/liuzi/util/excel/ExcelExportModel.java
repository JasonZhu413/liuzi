package com.liuzi.util.excel;

public class ExcelExportModel {
    private String exName;//文件名
    private String exPro;//字段名
    private String exTitle;//字段中文名

    public String getExName() {
        return exName;
    }
    public void setExName(String exName) {
        this.exName = exName;
    }
    public String getExPro() {
        return exPro;
    }
    public void setExPro(String exPro) {
        this.exPro = exPro;
    }
    public String getExTitle() {
        return exTitle;
    }
    public void setExTitle(String exTitle) {
        this.exTitle = exTitle;
    }
}
