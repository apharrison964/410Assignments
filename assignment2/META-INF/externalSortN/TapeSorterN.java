package externalSortN;


/**
 * Represents a machine with limited memory that can sort tape drives.
 */
public class TapeSorterN {

    private int memorySize;
    private int tapeSize;
    public int[] memory;

    public TapeSorterN(int memorySize, int tapeSize) {
        this.memorySize = memorySize;
        this.tapeSize = tapeSize;
        this.memory = new int[memorySize];
    }

    /**
     * Sorts the first `size` items in memory via quicksort.
     */
    public void quicksort(int size) {
    	quicksort(0, size-1);
    }
    
    /**
     * Takes a left and right index as arguments to sort specific portions of the memory.
     */
    public void quicksort(int left, int right) {
    	if (left+right >= 9) {
    		int i = left;
        	int j = right;
        	int pivot = median_three(left, right);
            while (i <= j) {
                while (memory[i] < pivot) {
                    i++;
                }
                while (memory[j] > pivot) {
                    j--;
                }
                if (i <= j) {
                    swapReferences(i, j);
                    i++;
                    j--;
                }
            }
            if (left < j) {
                quicksort(left, j);
            }
            if (i < right) {
                quicksort(i, right);
            }
    	}
    	else {
    		insertionsort(left, right);
    	}
    }
    
    /**
     * Returns the median value of the three checked (left, right, and middle element)
     * and preliminarily sorts these three values.
     */
    public int median_three(int left, int right) {
    	int mid = ((left+right)/2);
		if (memory[left] > memory[right]) {
			swapReferences(left, right);
		}
		if (memory[mid] < memory[left]) {
			swapReferences(mid, left);
		}
		if (memory[mid] > memory[right]) {
			swapReferences(mid, right);
		}
   		swapReferences(mid, right);
   		return memory[right];
    }
    
    /**
     * Swaps two elements, i and j, in the memory array.
     */
    public void swapReferences(int i, int j) {
    	int tmp = memory[i];
    	memory[i] = memory[j];
    	memory[j] = tmp;
    }
    
    /**
     * Called when the subarray to be sorted with quicksort has 10 or less elements
     * in order to maximize efficiency. 
     */
    public void insertionsort(int left, int right) {
    	for (int p = left+1; p <= right; p++) {
			int tmp = memory[p];
			int j;
			for (j = p; j > 0 && tmp < memory[j-1]; j--) {
				memory[j] = memory[j-1];
			}
			memory[j] = tmp;
		}
    }

    /**
     * Reads in numbers from drive `in` into memory (a chunk), sorts it, then writes it out
     * to a different drive. It writes chunks alternatively to drives `out1` and `out2` via
     * the boolean `out_1`. If `out_1` is true, the current output is written on `out1`, if
     * false, it is written on `out2`.
     *
     * If there are not enough numbers left on drive `in` to fill memory,
     * then it reads numbers until the end of the drive is reached.
     */
    public void initialPass(TapeDriveN in, TapeDriveN out1, TapeDriveN out2) {
    	int remaining = tapeSize;
    	boolean out_1 = true;
    	while (remaining >= memorySize) {
    		for (int i = 0; i < memorySize; i++) {
    			memory[i] = in.read();
    			remaining--;
    		}
        	quicksort(memorySize);
        	for (int i = 0; i < memorySize; i++) {
        		if (out_1) {
        			out1.write(memory[i]);
        		}
        		else {
        			out2.write(memory[i]);
        		}
        	}
        	out_1 = !out_1;
    	}
    	if (remaining != 0) {
    		for (int i = 0; i < remaining; i++) {
    			memory[i] = in.read();
    		}
    		quicksort(remaining);
    		for (int i = 0; i < remaining; i++) {
        		if (out_1) {
        			out1.write(memory[i]);
        		}
        		else {
        			out2.write(memory[i]);
        		}
        	}
    	}
    	in.reset();
    	out1.reset();
    	out2.reset();
    }

    /**
     * Merges the first chunk on drives `in1` and `in2` and writes the sorted, merged data 
     * to drive `out`. Utilizes Integer object type to create a null integer
     * as a way of preventing the code from reading past the current chunk on the tape.
     * The size of the chunk on drive `in1` is `size1`.
     * The size of the chunk on drive `in2` is `size2`.
     */
    public void mergeChunks(TapeDriveN in1, TapeDriveN in2, TapeDriveN out, int size1, int size2) {
        int pos1 = 0;
        int pos2 = 0;
        Integer tmp1 = null;
        Integer tmp2 = null;
        if (size1 > 0) {
        	tmp1 = in1.read();
        }
        if (size2 > 0) {
        	tmp2 = in2.read();
        }
        while ((pos1 < size1) && (pos2 < size2)) {
        	if (tmp1 == null) {
        		tmp1 = in1.read();
        	}
        	if (tmp2 == null) {
        		tmp2 = in2.read();
        	}
        	if (tmp1 < tmp2) {
        		out.write(tmp1);
        		pos1++;
        		tmp1 = null;
        	}
        	else {
        		out.write(tmp2);
        		pos2++;
        		tmp2 = null;
        	}
        }
        
        // Writes all remaining values on TapeDrive `in1` to `out`.
        while (tmp1 != null) {
        	out.write(tmp1);
        	pos1++;
        	if (pos1 < size1) {
        		tmp1 = in1.read();
        	}
        	else {
        		tmp1 = null;
        	}
        }
        // Writes all remaining values on TapeDrive `in2` to `out`.
        while (tmp2 != null) {
        	out.write(tmp2);
        	pos2++;
        	if (pos2 < size2) {
        		tmp2 = in2.read();
        	}
        	else {
        		tmp2 = null;
        	}
        }
    }

    /**
     * Merges chunks from drives `in1` and `in2` and writes the resulting merged chunks
     * alternatively to drives `out1` and `out2`.
     * The `runNumber` argument denotes which run this is, where 0 is the first run.
     */
    public void doRun(TapeDriveN in1, TapeDriveN in2, TapeDriveN out1, TapeDriveN out2, int runNumber) {
    	int chunkSize = memorySize*((int)Math.pow(2, runNumber));
    	boolean out_1 = true;
       	for (int i = 0; i < (tapeSize/(chunkSize*2)); i++) {
    		if (out_1) {
    			mergeChunks(in1, in2, out1, chunkSize, chunkSize);
    		}
    		else {
    			mergeChunks(in1, in2, out2, chunkSize, chunkSize);
    		}
    		out_1 = !out_1;
    	}
    	int remainder = (tapeSize%(chunkSize*2));
        if ((remainder > 0) && (remainder <= chunkSize)) {
        	if (out_1) {
        		mergeChunks(in1, in2, out1, remainder, 0);
        	}
        	else {
        		mergeChunks(in1, in2, out2, remainder, 0);
        	}
        }
        if ((remainder > chunkSize) && (remainder < chunkSize*2)) {
        	if (out_1) {
        		mergeChunks(in1, in2, out1, chunkSize, (remainder-chunkSize));
        	}
        	else {
        		mergeChunks(in1, in2, out2, chunkSize, (remainder-chunkSize));
        	}
        }
    	in1.reset();
    	in2.reset();
    	out1.reset();
    	out2.reset();
    }

    /**
     * Sorts the data on drive `t1` using the external sort algorithm.
     * The sorted data ends up on drive `t1`.
     *
     * Initially, drive `t1` is filled to capacity with unsorted numbers.
     * Drives `t2`, `t3`, and `t4` are empty and are to be used in the sorting process.
     */
    public void sort(TapeDriveN t1, TapeDriveN t2, TapeDriveN t3, TapeDriveN t4) {
    	// Preliminarily checks if tape can fit on internal memory in order to bypass
    	// more complicated code and maximize efficiency.
    	if (memorySize >= tapeSize) {
    		for (int i = 0; i < tapeSize; i++) {
    			memory[i] = t1.read();
    		}
    		quicksort(tapeSize);
    		for (int i = 0; i < tapeSize; i++) {
    			t1.write(memory[i]);
    		}
    		return;
    	}
    	initialPass(t1, t3, t4);
    	boolean out_12 = true;
        int num_runs = (int) (Math.log(tapeSize/memorySize) / Math.log(2)) + 1;
        for (int i = 0; i <= num_runs; i++) {
        	if (out_12) {
         		doRun(t3, t4, t1, t2, i);
         	}
         	else {
         		doRun(t1, t2, t3, t4, i);
         	}
         	out_12 = !out_12;
        }
        if (out_12) {
   		 t1 = t3;
   	 	}
        t1.reset();
        t2.reset();
        t3.reset();
        t4.reset();
    }

    public static void main(String[] args) {
        TapeSorterN tapeSorter = new TapeSorterN(10, 80);
        TapeDriveN t1 = TapeDriveN.generateRandomTape(80);
        TapeDriveN t2 = new TapeDriveN(80);
        TapeDriveN t3 = new TapeDriveN(80);
        TapeDriveN t4 = new TapeDriveN(80);

        tapeSorter.sort(t1, t2, t3, t4);
        int last = Integer.MIN_VALUE;
        boolean sorted = true;
        for (int i = 0; i < 80; i++) {
            int val = t1.read();
            System.out.println(val);
            sorted &= last <= val;
            last = val;
        }
        if (sorted)
            System.out.println("Sorted!");
        else
            System.out.println("Not sorted!");
    }
}