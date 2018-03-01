# --Memcache
（1）下载Memcache

     下载地址：http://www.jb51.net/softs/205839.html
     
    a）下载解压之后放在硬盘的目录下，如：D:\memcached . 
    
    b）然后在运行中输入cmd进入命令行，进入到Memcached.exe 所在的目录，例如：D:\memcached
    
    c）然后在Memcache目录，输入memcached.exe –d install，添加memcached.exe为服务
    
    d）输入memcached.exe -d start，运行memcached程序。
        C:\users\Test>d:
        D:\>cd d:\memcache
        d:\memcache>memcached.exe -d install
        d:\memcache>memcached.exe -d start
        
    e）检测是否安装成功  
        telnet 192.168.1.2 11211（默认端口）
       
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

