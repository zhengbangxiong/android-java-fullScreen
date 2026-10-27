package app.gkai.ai.modules.mine.beans.resp;

import java.io.Serializable;

import lombok.Data;

/**
 * 谷歌验证码
 */
@Data
public class GoogleAuthResp implements Serializable {

    /**
     * google 私钥
     */
    String google_auth_key;

    /**
     * 二维码内容
     */
    String google_auth_qrcode_text;

    /**
     * apk地址
     */
    String google_download_apk;

}
