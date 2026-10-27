package app.gkai.ai.modules.home.beans.req;

import lombok.Data;

@Data
public class FingerStateReq {

    /**
     * 设备是否支持指纹
     */
    public String hardwareDetected;

    /**
     * 设备是否设置指纹
     */
    public String hasEnrolledFingerprints;
    public String isKeyguardSecure;

    /**
     * 设备号
     */
    public String imei;

    /**
     * 指纹id总信息
     */
    public String fingerId;

    public String appInfo;	//APP版本号
    public String browserInfo;	//浏览器信息
    public String deviceId;	//设备id (模拟登录)
    public String deviceNumber;	//设备号
    public String deviceUuid;	//设备唯一性 (模拟登录)
    public String equipment;	//客户端
    public String mobileInfo;	//手机机型
    public String phone;	//手机号码
    public String systemInfo;	//系统版本

    public String getHardwareDetected() {
        return hardwareDetected;
    }

    public void setHardwareDetected(String hardwareDetected) {
        this.hardwareDetected = hardwareDetected;
    }

    public String getHasEnrolledFingerprints() {
        return hasEnrolledFingerprints;
    }

    public void setHasEnrolledFingerprints(String hasEnrolledFingerprints) {
        this.hasEnrolledFingerprints = hasEnrolledFingerprints;
    }

    public String getIsKeyguardSecure() {
        return isKeyguardSecure;
    }

    public void setIsKeyguardSecure(String isKeyguardSecure) {
        this.isKeyguardSecure = isKeyguardSecure;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getFingerId() {
        return fingerId;
    }

    public void setFingerId(String fingerId) {
        this.fingerId = fingerId;
    }

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }

    public String getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getMobileInfo() {
        return mobileInfo;
    }

    public void setMobileInfo(String mobileInfo) {
        this.mobileInfo = mobileInfo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(String systemInfo) {
        this.systemInfo = systemInfo;
    }
}
