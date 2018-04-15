package com.cto.entity;

import java.util.Date;

public class UserExtend {
    private Long id;

    private String idCard;

    private String nickName;

    private String remarks;

    private String sign;

    private String wechat;

    private String qq;

    private String icon;

    private Integer age;

    private Boolean sex;

    private Date birth;

    private String college;

    private String idCardFacePhoto;

    private String idCardBackPhoto;

    private Boolean isAuthentication;

    private Boolean isDelete;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getIdCardFacePhoto() {
        return idCardFacePhoto;
    }

    public void setIdCardFacePhoto(String idCardFacePhoto) {
        this.idCardFacePhoto = idCardFacePhoto;
    }

    public String getIdCardBackPhoto() {
        return idCardBackPhoto;
    }

    public void setIdCardBackPhoto(String idCardBackPhoto) {
        this.idCardBackPhoto = idCardBackPhoto;
    }

    public Boolean getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(Boolean isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", idCard=").append(idCard);
        sb.append(", nickName=").append(nickName);
        sb.append(", remarks=").append(remarks);
        sb.append(", sign=").append(sign);
        sb.append(", wechat=").append(wechat);
        sb.append(", qq=").append(qq);
        sb.append(", icon=").append(icon);
        sb.append(", age=").append(age);
        sb.append(", sex=").append(sex);
        sb.append(", birth=").append(birth);
        sb.append(", college=").append(college);
        sb.append(", idCardFacePhoto=").append(idCardFacePhoto);
        sb.append(", idCardBackPhoto=").append(idCardBackPhoto);
        sb.append(", isAuthentication=").append(isAuthentication);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}