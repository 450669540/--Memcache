# Memcache
  定义：
  Memcache存储的是key/value的键值对，但是值必须是可序列化的对象（这里我说的java），还可以是json,xml,html等，使用于存储实时性要求不是很高的信息
  
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

    应用场景：不经常变动的数据
    例如：电子商城的首页分类，
          第一次显示的时候：
          判断memcached缓存中是否有该分类----没有----->执行一次或者多次sql从数据库中查询全站的商品分类----->放到memcached中------->>进入处理数             据------->>显示到页面

          第二次显示的判断memcached缓存中是否有该分类----有--->>-从memcached中取出数据-------->>进入处理数据------->>显示到页面

          更新数据库中分类的信息------->找到memcached中key值，删除------>重新插入到你的memcached中就可以了
