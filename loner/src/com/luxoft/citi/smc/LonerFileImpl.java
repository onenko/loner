package com.luxoft.citi.smc;

import java.io.*;
import java.nio.channels.*;

/**
 * LonerFileImpl - implement single process latch with lock file
 *
 * Idea is here: http://www.rgagnon.com/javadetails/java-0288.html
 *
 * @author onenko@luxoft.com
 */
public class LonerFileImpl implements Loner {

	private String fname;
	private String folder;
	private File file;
	private FileChannel channel;
	private FileLock lock;

	/**
	 * Constructor
	 *
	 * Uses user home to place lock file
	 *
	 * @param id the identifier of this process latch, used to form file name, do not use characters prohibited for file names
	 */
	public LonerFileImpl(String id) {
		this.fname = id;
		this.folder = System.getProperty("user.home");
	}

	/**
	 * Constructor
	 *
	 * @param folder - where to place lock file
	 * @param id the identifier of this process latch, used to form file name, do not use characters prohibited for file names
	 */
	public LonerFileImpl(String folder, String id) {
		this.fname = id;
		this.folder = folder;
	}

	@Override
	public boolean tryLock() {
		try {
	        file = new File(folder, fname + ".loner-lock");
	        channel = new RandomAccessFile(file, "rw").getChannel();
            try {
                lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {	// already locked
                closeLock();
                return false;
            }
            if (lock == null) {
	            closeLock();
	            return false;
	        }
            // destroy the lock when the JVM is closing
            Runtime.getRuntime().addShutdownHook(new Thread() {
            	public void run() {
	                closeLock();
	                deleteFile();
	            }
	        });
	    } catch (Exception e) {
	        closeLock();
	        return false;
	    }
		return true;
	}

	@Override
	public void release() {
        closeLock();
        deleteFile();
	}

	@Override
	public void finalize() {
		release();
	}

	private void closeLock() {
        try { lock.release();  }
        catch (Exception e) {  }
        try { channel.close(); }
        catch (Exception e) {  }
    }

	private void deleteFile() {
        try { file.delete(); }
        catch (Exception e) { }
    }

}
