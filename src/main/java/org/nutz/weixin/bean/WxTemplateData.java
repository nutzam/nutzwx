package org.nutz.weixin.bean;

public class WxTemplateData {
    
    public static final String DFT_COLOR = "#173177";

    private String value;
    private String color = DFT_COLOR;
    public WxTemplateData() {}
    
    public WxTemplateData(String value, String color) {
        super();
        this.value = value;
        this.color = color;
    }

    public WxTemplateData(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
