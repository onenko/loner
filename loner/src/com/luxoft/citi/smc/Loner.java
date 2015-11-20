package com.luxoft.citi.smc;

public interface Loner {

	/**
	 * tries to acquire the latch
	 * @return boolean success of the operation
	 */
	public boolean tryLock();

	/**
	 * should be called on exit
	 */
	public void release();
}
