package com.freebird.wx.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropertiesUtils {

  private PropertiesUtils(){}
  private static String propertiesFileName = "three-guys.properties";

  private static Properties properties = null;

  private static long lastModifed = 0;

  private static long lastReadTime = 0;

  private static void initProperties() {

    FileInputStream fis = null;
    File file = null;

    if (System.currentTimeMillis() - lastReadTime > 60000) {//间隔60秒检查文件是否被修改(修改配置项最多60秒后生效)
    	synchronized (PropertiesUtils.class) {
        if (System.currentTimeMillis() - lastReadTime > 60000) {
          try {
        	  //配置文件的路径是配置在启动脚本里面的
            file = new File("/data/appsystems/"+propertiesFileName);

            if (file.lastModified() > lastModifed) {
              lastModifed = file.lastModified();
              fis = new FileInputStream(file);
              properties = new Properties();
              properties.load(fis);
            }

            lastReadTime = System.currentTimeMillis();
          } catch (FileNotFoundException e) {
        	  e.printStackTrace();
          } catch (Exception e) {
        	  e.printStackTrace();
          } finally {
            if (fis != null)
              try {
                fis.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
          }
        }
      }
    }
  }

  static {
    initProperties();
  }

  public static void setPropertiesFile(String propertiesFile) {
    propertiesFileName = propertiesFile;
    initProperties();
  }

  public static String getPropertyValues(String key) {
    initProperties();
    return (String) properties.get(key);
  }

  public static String getPropertyValues(String key, String defaultValue) {
		String value = getPropertyValues(key);
		return value==null ? defaultValue : value;
  }
  public static void main(String[] args) {
	String  s = PropertiesUtils.getPropertyValues("wx.appId");
  }
}
