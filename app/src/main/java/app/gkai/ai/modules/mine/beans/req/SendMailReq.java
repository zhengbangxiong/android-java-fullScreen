package app.gkai.ai.modules.mine.beans.req;

import lombok.Data;

/**
 * 忘记密码--发送邮件验证码
 */
@Data
public class SendMailReq {

   /**
     * 邮箱
     */
    private String name;

    /**
     * 类型：reg (注册)、 forgot (忘记密码)
     */
    private String from;


    /**
     * 滑块验证码通过标识(data)<br/>
     * 图片验证码标识(uuid)<br/>
     * GoogleCode(谷歌验证码传固定值)
     */
    private String checkCodeToken;


    /**
     * 图片验证码<br/>
     * 谷歌验证码
     */
    private String checkCode;

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getFrom() {
  return from;
 }

 public void setFrom(String from) {
  this.from = from;
 }

 public String getCheckCodeToken() {
  return checkCodeToken;
 }

 public void setCheckCodeToken(String checkCodeToken) {
  this.checkCodeToken = checkCodeToken;
 }

 public String getCheckCode() {
  return checkCode;
 }

 public void setCheckCode(String checkCode) {
  this.checkCode = checkCode;
 }
}
