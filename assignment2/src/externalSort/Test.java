package externalSort;

public class Test {
	public static void main(String[] args) {
		long epoch1 = System.currentTimeMillis();
		for(int i=50;i<1000;i++){
			for (int k=1;k<50;k++){
				int cs = 57;
				TapeSorter tapeSorter = new TapeSorter(k, i);
				TapeDrive t1 = TapeDrive.generateRandomTape(i);
				TapeDrive t2 = new TapeDrive(i);
				TapeDrive t3 = new TapeDrive(i);
				TapeDrive t4 = new TapeDrive(i);

				tapeSorter.sort(t1, t2, t3, t4);

				int last = Integer.MIN_VALUE;
				boolean sorted = true;
				for (int j = 0; j < i; j++) {
					int val = t1.read();
					sorted &= (last <= val);
					if (!sorted){
						//System.out.println( val + " " + j);
					}
					last = val;
				}
				if (sorted){}
				//System.out.println("Sorted!");
				else
					System.out.println("Not sorted! " + i);
			}
		}
		long epoch2 = System.currentTimeMillis();
		System.out.println("Runtime is " + ((epoch2 - epoch1)/1000) + " seconds.");
		System.out.println("Done");
	}
}
