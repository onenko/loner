package com.luxoft.citi.smc;

public class LonerFileImplTest {

	public static void main(String[] args) {

		String id = args.length == 0 ? "ZZZ" : args[0];
		System.out.println("LonerFileImplTest Invocation (" + id + ')');

//		Loner loner = new LonerFileImpl("LonerFileImpl");	// lock file in home directory
		Loner loner = new LonerFileImpl("C:\\Temp\\", "LonerFileImpl");	// lock file in custom directory

        if (loner.tryLock()) {
            System.out.println("Latch ours. We are winner ! Executing ! (" + id + ')');
            try {
                for(int i = 0; i < 20; i++) {
                	try {
                		System.out.print(".");
                		Thread.sleep(5 * 60);
                	}
                    catch(Exception e) {
                    	e.printStackTrace();
                    }
                }
             }
            catch (Exception e) { }
            System.out.println("Executed ! Exiting. (" + id + ')');
        }
        else {
            System.out.println("ANOTHER job already active. Exiting immediately.(" + id + ')');
            System.exit(1);
        }
	}

}
