 package other;
 /**
  * Reason: TODO ADD REASON. <br/>
  * Date: 2014年7月<br/>
  * 
  * @author shenqn
  * @version 1.0 Copyright (c) 2014, 河北腾翔软件科技有限公司 All Rights Reserved.
  */
 public class ReadDBConfig
 {
   private String driver;
   private String url;
   private String name;
   private String password;
   private String initialSize;
   private String maxActive;
   private String maxIdle;
   private String minIdle;
   private String maxWait;
   
   public String getDriver()
   {
     return this.driver;
   }
   
   public void setDriver(String driver)
   {
     this.driver = driver;
   }
   
   public String getUrl()
   {
     return this.url;
   }
   
   public void setUrl(String url)
   {
     this.url = url;
   }
   
   public String getName()
   {
     return this.name;
   }
   
   public void setName(String name)
   {
     this.name = name;
   }
   
   public String getPassword()
   {
     return this.password;
   }
   
   public void setPassword(String password)
   {
     this.password = password;
   }
   
   public String getInitialSize()
   {
     return this.initialSize;
   }
   
   public void setInitialSize(String initialSize)
   {
     this.initialSize = initialSize;
   }
   
   public String getMaxActive()
   {
     return this.maxActive;
   }
   
   public void setMaxActive(String maxActive)
   {
     this.maxActive = maxActive;
   }
   
   public String getMaxIdle()
   {
     return this.maxIdle;
   }
   
   public void setMaxIdle(String maxIdle)
   {
     this.maxIdle = maxIdle;
   }
   
   public String getMinIdle()
   {
     return this.minIdle;
   }
   
   public void setMinIdle(String minIdle)
   {
     this.minIdle = minIdle;
   }
   
   public String getMaxWait()
   {
     return this.maxWait;
   }
   
   public void setMaxWait(String maxWait)
   {
     this.maxWait = maxWait;
   }
 }

