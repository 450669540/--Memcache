# Memcache
（1）下载Memcache

     下载地址：www.newasp.net/soft/63735.html
    
    a）安装Memcache
    
      cd memcached.exe所在文件夹路径
      
      memcached.exe -d install 如果没有报错说明安装成功
      
      在服务中看是否有Memcached Server服务
        
    b）检测是否安装成功  
    
        telnet 本地ip 11211（默认端口）
       
（2）java中使用Memcache

    初始化：memcache 
    static {  
            String[] serverlist = { "server1.com:port", "server2.com:port" };  
            SockIOPool pool = SockIOPool.getInstance();  
            pool.setServers(serverlist);  
            pool.initialize();  
     }  

    创建一个client对象： 
    MemCachedClient mc = new MemCachedClient();  

    创建一个缓存： 
    MemCachedClient mc = new MemCachedClient();  
    String key = "cacheKey1";  
    Object value = SomeClass.getObject();  
    mc.set(key, value);  

    通过key删除一个缓存： 
    MemCachedClient mc = new MemCachedClient();  
    String key = "cacheKey1";  
    mc.delete(key);  

    通过key获取缓存对象： 
    MemCachedClient mc = new MemCachedClient();  
    String key = "key";  
    Object value = mc.get(key);  

    获取多个缓存对象： 
    MemCachedClient mc = new MemCachedClient();  
    String[] keys = { "key", "key1", "key2" };  
    Map<Object> values = mc.getMulti(keys);  

    刷新全部缓存： 
    MemCachedClient mc = new MemCachedClient();  
    mc.flushAll(); 

    Memcache的锁机制
    程序中的锁，可以独占某个资源（内存、文件），目的是为了保证数据的一致性
    
    举例：如果一个用户要进行抽奖，查询他是否抽过奖，
          如果否，判断锁是否存在，
          如果否，进行抽奖并入库删除锁，返回抽奖结果，
          如果抽过奖或者锁存在，结束抽奖
