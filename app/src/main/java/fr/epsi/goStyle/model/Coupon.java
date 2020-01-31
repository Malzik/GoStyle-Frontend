package fr.epsi.goStyle.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Coupon implements Serializable {

    private String name="";
    private String code="";
    private String description="";
    private String urlPhoto="";
    private String deadLine="";

    public Coupon(JSONObject jsonObject){
        name=jsonObject.optString("name","");
        code=jsonObject.optString("code","");
        description=jsonObject.optString("description","");
        urlPhoto=jsonObject.optString("logo","");
        deadLine=jsonObject.optString("deadline","");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }
}
