package app.gkai.ai.modules.mine.beans.req;

import lombok.Data;

@Data
public class UnbindReq {

    private String email;	//邮箱
    private String familyName;	//姓名
    private String idBackImg;	//证件反面图片地址
    private String idFrontImg;	//证件正面图片地址
    private String idHandheldImg;	//手持证件图片地址
    private String idNumber;	//身份证明证件号码
    private String smsCode	;	//短信验证码

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getIdBackImg() {
        return idBackImg;
    }

    public void setIdBackImg(String idBackImg) {
        this.idBackImg = idBackImg;
    }

    public String getIdFrontImg() {
        return idFrontImg;
    }

    public void setIdFrontImg(String idFrontImg) {
        this.idFrontImg = idFrontImg;
    }

    public String getIdHandheldImg() {
        return idHandheldImg;
    }

    public void setIdHandheldImg(String idHandheldImg) {
        this.idHandheldImg = idHandheldImg;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
