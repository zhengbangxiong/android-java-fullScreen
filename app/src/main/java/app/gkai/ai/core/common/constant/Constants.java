package app.gkai.ai.core.common.constant;

import android.os.Environment;

/**
 *
 */
public class Constants {

    /**
     * 判断是否第一次进引导页 “1”：代表进去过了，其他代表没有
     */
    public static final String ISFIRST = "isfirst";

    /**
     * true:登录，false:未登录
     */
    public static final String ISLOGIN = "islogin";

    public static final String token = "token";

    /**
     * 状态：0：手机/邮箱未验证，1：正常，2：冻结
     */
    public static final String status = "status";

    /**
     * 用户名
     */
    public static final String name = "name";

    /**
     * 用户昵称
     */
    public static final String nickName = "nickName";

    /**
     * 邮箱
     */
    public static final String mail = "mail";

    /**
     * 0--未激活 1--已激活（判断燃料账户是否激活）
     */
    public static final String jihuo = "jihuo";

    /**
     * 0--非节点 1--普通节点 2--初级节点 3--中级节点 4--高级节点
     */
    public static final String jibie = "jibie";

    /**
     * 是否后台设置的用户级别，1、是，0否
     */
    public static final String isSettingSet = "isSettingSet";

    /**
     * 是否实名认证通过
     */
    public static final String isRealname = "isRealname";

    /**
     * 是否手机验证
     */
    public static final String isPhone = "isPhone";

    /**
     * 是否绑定邮箱
     */
    public static final String isMail = "isMail";

    /**
     * 用户的邀请码
     */
    public static final String invitationCode = "invitationCode";


    /**
     * id
     */
    public static final String id = "id";

    /**
     * headImg
     */
    public static final String headImg = "headImg";


    /**
     * 谷歌验证状态，0未启用 1启用 2关闭
     */
    public static final String googleStatus = "googleStatus";

    /**
     * 信誉度
     */
    public static final String credibility = "credibility";

    /**
     * 个性签名
     */
    public static final String person_tag = "person_tag";

    /**
     * 性别
     */
    public static final String sex = "sex";

    /**
     * google_download_apk下载地址
     */
    public static final String google_download_apk = "google_download_apk";

    /**
     * google 私钥
     */
    public static final String google_auth_key = "google_auth_key";

    /**
     * google 二维码
     */
    public static final String google_auth_qrcode_text = "google_auth_qrcode_text";

    /**
     * google 私钥 创建时间
     */
    public static final String google_auth_create_time = "google_auth_create_time";

    /**
     * 滑动令牌 YunPian,YunCun
     */
    public static final String slideCaptchaType = "slideCaptchaType";

    /**
     * T的开关，地址切换
     */
    public static final String isSwitch = "isSwitch";

    /**
     * 网络环境 0  1  2
     */
    public static final String PRODUCTION_ENVIRONMENT = "PRODUCTION_ENVIRONMENT";

    /**
     * 语言 1：中文，2：繁体，3：英文
     */
    public static final String LANGUAGE = "LANGUAGE";

    /**
     * IPFS
     */
    public static final String IPFS = "IPFS";

    /**
     * ETH
     */
    public static final String ETH = "ETH";

    /**
     * BTC
     */
    public static final String BTC = "BTC";

    /**
     * FIL
     */
    public static final String FIL = "FIL";

    /**
     * USDT
     */
    public static final String USDT = "USDT";

    /**
     * 中文语言
     */
    public static String LANGUAGE_CN = "CN";

    /**
     * 英语语言
     */
    public static String LANGUAGE_EN = "EN";

    /**
     * 繁体语言
     */
    public static String LANGUAGE_TW = "TW";


    public static final String MAIN_FILE_PATH                      = Environment.getExternalStorageDirectory() + "/data/app.gkai.ai/";

    //zip下载后解压文件存放目录
    public static final String BLACKTECH_HOT_UPDATE_FILE_PATH      = MAIN_FILE_PATH + "hot_update_file/";

    //zip下载后解压文件存放目录
    public static final String BLACKTECH_HOT_UPDATE_FILE_PATH_Name = MAIN_FILE_PATH + "hot_update_file";

    // 下载的缓存文件后缀名
    public static final String UXUE_TEMP_FILE_SUFFIX               = ".zip";

    // 下载的缓存文件后缀名
    public static final String HOT_UPDATE_FILE_NAME               = "YUNCUNONEH5.zip";
}
