package app.gkai.ai.core.common.util;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * 轻量数据保存 SharedPreferences
 */
public class ClientConfigDao {
	  public static String GetConfigString(Activity context,String name, String defaultValue) {
	        SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
	        return pres.getString(name, defaultValue);
	    }

	    public static void SetConfigString(Activity context,String name, String value) {
	        SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
	        SharedPreferences.Editor editor = pres.edit();
	        try {
	            editor.putString(name, value);
	            editor.commit();
	        } catch (Exception ignored) {

	        }
	    }

	    public static int GetConfigInt(Activity context,String name, int defaultValue) {
	        SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
	        return pres.getInt(name, defaultValue);
	    }

	    public static void SetConfigInt(Activity context,String name, int value) {
	        SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
	        SharedPreferences.Editor editor = pres.edit();
	        try {
	            editor.putInt(name, value);
	            editor.commit();
	        } catch (Exception ignored) {

	        }
	    }


	public static long GetConfigLong(Activity context,String name, long defaultValue) {
		SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
		return pres.getLong(name, defaultValue);
	}

	public static void SetConfigLong(Activity context,String name, long value) {
		SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
		SharedPreferences.Editor editor = pres.edit();
		try {
			editor.putLong(name, value);
			editor.commit();
		} catch (Exception ignored) {

		}
	}

	    public static Boolean GetConfigBoolean(Activity context,String name, Boolean defaultValue) {
	        SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
	        return pres.getBoolean(name, defaultValue);
	    }

	    public static void  SetConfigBoolean(Activity context,String name, Boolean value) {
	        SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
	        SharedPreferences.Editor editor = pres.edit();
	        try {
	            editor.putBoolean(name, value);
	            editor.commit();
	        } catch (Exception ignored) {

	        }
	    }
//       public static void SetConfigObject(Activity context,String key,List<Object> listMap){
//    	   SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
//    	   ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
//           ObjectOutputStream oos=null;
//           try {
//            oos = new ObjectOutputStream(baos);
//            oos.writeObject(listMap);
//            
//           } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//           }
//           // 将Product对象放到OutputStream中
//           // 将Product对象转换成byte数组，并将其进行base64编码
//           String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
//           SharedPreferences.Editor editor = pres.edit();
//           // 将编码后的字符串写到base64.xml文件中
//           editor.putString(key, newWord);
//           editor.commit();
//       }
       
//       public static  List<Object> GetConfigObject(Activity context,String key){
//    	   List<Object>  result=null;
//    	   SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(context.getBaseContext());
//    	    String wordBase64 = pres.getString(key, "");
//    	    // 对Base64格式的字符串进行解码
//    	        byte[] base64Bytes = Base64.decodeBase64(wordBase64 .getBytes());
//    	          ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
//    	       ObjectInputStream ois;
//    	  try {
//    	   ois = new ObjectInputStream(bais);
//    	    // 从ObjectInputStream中读取Product对象
//    	//   AddNewWord addWord= (AddNewWord ) ois.readObject();
//    	    result = (List<Object>) ois.readObject();
//    	   System.out.println("result = " + result);
//    	   
//    	  } catch (StreamCorruptedException e) {
//    	   // TODO Auto-generated catch block
//    	   e.printStackTrace();
//    	  } catch (IOException e) {
//    	   // TODO Auto-generated catch block
//    	   e.printStackTrace();
//    	  } catch (ClassNotFoundException e) {
//    	   // TODO Auto-generated catch block
//    	   e.printStackTrace();
//    	  }
//    	  return result;
//    	   }

}
