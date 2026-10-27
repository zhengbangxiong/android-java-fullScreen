package app.gkai.ai.modules.mine.beans.req;

import lombok.Data;

/**
 * 实名认证
 */
@Data
public class AuthIdentityReq {

    /**
     * 真实姓名
     */
    private String familyName;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 身份证正面
     */
    private String idFrontImg;

    /**
     * 身份证背面
     */
    private String idBackImg;

    /**
     * 手持身份证照片
     */
    private String idHandheldImg;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdFrontImg() {
        return idFrontImg;
    }

    public void setIdFrontImg(String idFrontImg) {
        this.idFrontImg = idFrontImg;
    }

    public String getIdBackImg() {
        return idBackImg;
    }

    public void setIdBackImg(String idBackImg) {
        this.idBackImg = idBackImg;
    }

    public String getIdHandheldImg() {
        return idHandheldImg;
    }

    public void setIdHandheldImg(String idHandheldImg) {
        this.idHandheldImg = idHandheldImg;
    }
}
