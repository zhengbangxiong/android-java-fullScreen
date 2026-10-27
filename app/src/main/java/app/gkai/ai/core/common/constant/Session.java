package app.gkai.ai.core.common.constant;

public class Session {

    /**
     * true:登录，false:未登录
     */
    public static boolean islogin = false;

    public static String token = "";

    /**
     * 状态：0：手机/邮箱未验证，1：正常，2：冻结
     */
    public static String status = "";

    /**
     * 用户名
     */
    public static String name = "";

    /**
     * 用户昵称
     */
    public static String nickName = "";

    /**
     * 邮箱
     */
    public static String mail = "";

    /**
     * 0--未激活 1--已激活（判断燃料账户是否激活）
     */
    public static String jihuo = "";

    /**
     * 0--非节点 1--普通节点 2--初级节点 3--中级节点 4--高级节点
     */
    public static String jibie = "";

    /**
     * 是否后台设置的用户级别，1、是，0否
     */
    public static String isSettingSet = "";

    /**
     * 是否实名认证通过
     */
    public static String isRealname = "";

    /**
     * 是否手机验证
     */
    public static String isPhone = "";

    /**
     * 是否设置收款方式
     */
    public static String isPaymentSet = "";

    /**
     * 是否绑定邮箱
     */
    public static String isMail = "";

    /**
     * 用户的邀请码
     */
    public static String invitationCode = "";

    /**
     * 介绍人id
     */
    public static String introduceMemberId = "";

    /**
     * id
     */
    public static String id = "";

    /**
     * headImg
     */
    public static String headImg = "";


    /**
     * 谷歌验证状态，0未启用 1启用 2关闭
     */
    public static String googleStatus = "";

    /**
     * 谷歌验证码秘钥
     */
    public static String googleAuthKey = "";

    /**
     * googleAuthCode
     */
    public static String googleAuthCode = "";

    /**
     * 信誉度
     */
    public static String credibility = "";

    /**
     * 个性签名
     */
    public static String tag = "";

    /**
     * 性别
     */
    public static String sex = "";


    /**
     * 广告宽度
     */
    public static int adv_width;

    /**
     * 设备号
     */
    public static String IMEI = "";

    /**
     * 版本号
     */
    public static String version = "";

    /**
     *  T的开关，地址切换
     */
    public static String isSwitch;

    /**
     *  切换钱包、defi理财
     *  0：钱包，1：defi理财
     */
    public static int wallet_index = 0;

    /**
     * 语言 1：中文，2：繁体，3：英文
     */
    public static String LANGUAGE = "";
}
