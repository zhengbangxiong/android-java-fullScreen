package app.gkai.ai.core.common.util;

import android.content.Context;
import android.widget.ImageView;

import app.gkai.ai.R;

import java.math.BigDecimal;
import java.util.Random;

/**
 *
 * @desc ${TODD}
 */
public class StringUtils {
	public final static String UTF_8 = "utf-8";

	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim())
				&& !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}


	/**
	 * 上传到后台的图片的名称
	 */
	public static String getUUIDByRules32Image() {
		StringBuffer generateRandStr = null;
		try {
			String rules = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			int rpoint = 0;
			generateRandStr = new StringBuffer();
			Random rand = new Random();
			int length = 6;
			for (int i = 0; i < length; i++) {
				if (rules != null) {
					rpoint = rules.length();
					int randNum = rand.nextInt(rpoint);
					generateRandStr.append(rules.substring(randNum, randNum + 1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (generateRandStr == null) {
			return "getUUIDByRules32Image.png";
		}
		return generateRandStr + ".png";
	}



	public static String getPBTBGB(double sizeB){
		if(1024 > sizeB){
			return sizeB + "GiB";
		}else if(1048576 > sizeB && 1024 <= sizeB){
			return  new BigDecimalCalculation("" + sizeB).div("1024").result() + "TiB";
		}else if(1048576 <= sizeB){
			return  new BigDecimalCalculation("" + sizeB).div("1048576").result() + "PiB";
		}else{
			return "";
		}

	}

	/**
	 * 去除小数点后面的0
	 * @param number
	 * @return
	 */
	public static String getDeleteZero(String number){
		BigDecimal value = new BigDecimal(number);
		BigDecimal noZeros = value.stripTrailingZeros();
		return noZeros.toPlainString();
	}

	/**
	 * 设置头像
	 * @param index
	 * @param imageView
	 * @param context
	 */
	public static void SetImageHead(String index, ImageView imageView, Context context){
		if("1".equals(index)){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.headimg_one));
		}else if("2".equals(index)){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.headimg_two));
		}else if("3".equals(index)){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.headimg_three));
		}else if("4".equals(index)){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.headimg_four));
		}else if("5".equals(index)){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.headimg_five));
		}else if("6".equals(index)){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.headimg_six));
		}else if("7".equals(index)){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.headimg_seven));
		}else if("8".equals(index)){
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.headimg_eight));
		}


	}

}
