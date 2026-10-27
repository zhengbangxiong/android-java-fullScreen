package app.gkai.ai.core.common.constant;

public class AppUrl {

    /**
     * 1:正式
     * 0：测试
     * 2:线上测试
     */
    public static long PRODUCTION_ENVIRONMENT = 1;

    //测试网络地址
    public static String TEXT_API_BASE_URL = "http://192.168.1.11:8080/";
//    public static String TEXT_API_BASE_URL = "http://47.90.4.173:10000/";
    //生产网络地址
    public static String API_BASE_URL = "https://tradingbase.ai/";
    //T网络地址
    public static String T_BASE_URL = "http://47.90.4.173:8100/";

    //yunpian
    public static String YUNPIAN_ID = "862653aa0b0c42f3a941c190b0441478";

    //bugly id
    public static String BUGLY_ID = "433d22d25d";

    //企业id 测试 A公司
    public static String companyId = "1";

    /**
     * 获取网络地址
     * @return
     */
    public static String getEnvironment(){
        if(1 == PRODUCTION_ENVIRONMENT){
            return API_BASE_URL;
        }else {
            return TEXT_API_BASE_URL;
        }

    }

}
