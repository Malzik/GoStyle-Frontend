package fr.epsi.goStyle.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Coupon implements Serializable {

    private String name="";
    private String code="";
    private String description="";
    private String logo="";
    private String deadline="";

    public Coupon(JSONObject jsonObject){
        name=jsonObject.optString("name","");
        code=jsonObject.optString("code","");
        description=jsonObject.optString("description","");
        logo=jsonObject.optString("logo","");
        deadline=jsonObject.optString("deadline","");
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
