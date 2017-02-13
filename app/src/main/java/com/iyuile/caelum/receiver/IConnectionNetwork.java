package com.iyuile.caelum.receiver;

import android.content.Intent;

/**
 *
 * @ClassName {@link IConnectionNetwork}
 * @author WangYao
 */
public interface IConnectionNetwork {
	
	/**
	 * 有网络
	 * 
	 * @param intent
	 */
	public void conn(Intent intent);
	
	/**
	 * 无网络
	 * 
	 * @param intent
	 */
	public void dissConn(Intent intent);
	
	/**
	 * 网络慢
	 * 
	 * @param intent
	 */
	public void connSlow(Intent intent);
	
}
