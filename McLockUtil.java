package com.module.util;

import java.util.Date;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class McLockUtil {
	private static McLockUtil mlu;
	private MemCachedClient memCachedClient = new MemCachedClient();
	SockIOPool pool;
	private McLockUtil() {
		String[] servers = { PropertiesUtil.get2("memCached", "memCached.ServerUrl") };
		this.pool = SockIOPool.getInstance();
		
		// 设置服务器信息 
		this.pool.setServers(servers);
		
		// 设置初始连接数、最小和最大连接数以及最大处理时间 
		this.pool.setInitConn(Integer.valueOf(PropertiesUtil.get2("memCached", "memCached.InitConn")));
		this.pool.setMinConn(Integer.valueOf(PropertiesUtil.get2("memCached", "memCached.MinConn")));
		this.pool.setMaxConn(Integer.valueOf(PropertiesUtil.get2("memCached", "memCached.MaxConn")));
		this.pool.setMaxIdle(Integer.valueOf(PropertiesUtil.get2("memCached", "memCached.MaxIdle"))); 
		
		//设置主线程的睡眠时间 
		this.pool.setMaintSleep(Integer.valueOf(PropertiesUtil.get2("memCached", "memCached.MaintSleep")));
		
		// 设置TCP的参数，连接超时等 
		this.pool.setNagle(Boolean.valueOf(PropertiesUtil.get2("memCached", "memCached.Nagle")));
		this.pool.setSocketTO(Integer.valueOf(PropertiesUtil.get2("memCached", "memCached.SocketTO")));
		this.pool.setSocketConnectTO(Integer.valueOf(PropertiesUtil.get2("memCached", "memCached.SocketConnectTO")));
		/*设置连接心跳监测开关。
		      设为true则每次通信都要进行连接是否有效的监测，造成通信次数倍增，加大网络负载，因此该参数应该在对HA要求比较高的场合设为TRUE，默认状态是false。
		 * */
		this.pool.setAliveCheck(Boolean.valueOf(PropertiesUtil.get2("memCached", "memCached.AliveCheck")));
		if(Boolean.valueOf(PropertiesUtil.get2("memCached", "memCached.Start"))){
			this.pool.initialize();
		}
	}

	public static McLockUtil get2Instance() {
		if (mlu == null) {
			synchronized (McLockUtil.class) {
				if (mlu == null) {
					mlu = new McLockUtil();
				}
			}
		}
		return mlu;
	}

	/**
	 * 缓存是否被锁住
	 * @param keyName key
	 * @param v value，如果为null则为默认值
	 * @param expiry 过期时间，如果为null则为默认值
	 * @return
	 */
	public boolean lock(String keyName, Object v, Date expiry) {
		try {
			if(keyName.length()>50){
				throw new RuntimeException("key过长");
			}
			if(v == null){
				v = 0L;
			}
			if(expiry == null){
				expiry = new Date(System.currentTimeMillis() + 1*24*3600*1000L);
			}
			long begin = System.currentTimeMillis();
			while (true) {
				boolean success = memCachedClient.add(keyName,v,expiry);
				if (success) {
					System.out.println(Thread.currentThread().getName()+":"+"McLockUtil : lock success :" + keyName);
					return success;
				}
				System.out.println(Thread.currentThread().getName()+":"+"McLockUtil : lock wait :" + keyName);
				Thread.sleep(1000L);
				long end = System.currentTimeMillis();
				if(end-begin>30000L){
					throw new RuntimeException("等待超时");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
		}
		return false;
	}

	public boolean unlock(String keyName) {
		try {
			boolean success = memCachedClient.delete(keyName);
			if (success) {
				System.out.println(Thread.currentThread().getName()+":"+"McLockUtil : unlock success :" + keyName);
				return success;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
		}
		return false;
	}
	//当key存在false,当key不存在true
	public boolean add(String k, Object v,Date expiry) {
		return memCachedClient.add(k,v,expiry);
	}
	public Object get(String k){
		return memCachedClient.get(k);
	}
	//当key存在true,当key不存在true
	public boolean set(String k, Object v,Date expiry) {
		return memCachedClient.set(k,v,expiry);
	}
	//当key存在true,当key不存在false
	public boolean replace(String k, Object v,Date expiry) {
		return memCachedClient.replace(k,v,expiry);
	}
	public boolean delete(String k) {
		return memCachedClient.delete(k);
	}
	public static void main(String[] args) {
		Date expiry = new Date(System.currentTimeMillis() + 30*60*1000L);//30 MIN
		McLockUtil.get2Instance().set("cache",0L,expiry);
		Object value = McLockUtil.get2Instance().get("cache");
		System.out.println(value);
	}
}