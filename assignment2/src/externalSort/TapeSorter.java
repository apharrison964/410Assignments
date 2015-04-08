package externalSort;

/**
 * Represents a machine with limited memory that can sort tape drives.
 */

public class TapeSorter {

	private int memorySize;
	private int tapeSize;
	public int[] memory;

	public TapeSorter(int memorySize, int tapeSize) {
		this.memorySize = memorySize;
		this.tapeSize = tapeSize;
		this.memory = new int[memorySize];
	}

	/**
	 * Sorts the first `size` items in memory via quicksort
	 */
	public void quicksort(int size) {
		quicksort(0, size - 1);
	}

	public void quicksort(int left, int right) {
		if(left + right >= 10) {        // Use quicksort if the input is greater than 10, otherwise insertion sort
			int m = left;
			int n = right;
			int pivot = medianThree(left, right);

			while (m <= n) {
				while(memory[m] < pivot) {
					m++;
				}

				while(memory[n] > pivot) {
					n--;
				}

				if(m <= n) {
					swap(m, n);
					m++;
					n--;
				}
			}

			if(left < n) {
				quicksort(left, n);
			}

			if(m < right) {
				quicksort(m, right);
			}

		} else {
			insertionSort(left, right);
		}
	}


	public int medianThree(int left, int right) {
		int med = ((left + right) / 2);

		if(memory[left] > memory[right]) {
			swap(left, right);
		}

		if(memory[med] < memory[left]) {
			swap(med, left);
		}

		if(memory[med] > memory[right]) {
			swap(med, right);
		}

		swap(med, right);
		return memory[right];
	}

	public void swap(int i, int j) {
		int temp = memory[i];
		memory[i] = memory[j];
		memory[j] = temp;
	}

	public void insertionSort(int left, int right) {
		for(int p = left + 1; p <= right; p++) {
			int temp = memory[p];
			int j;
			for(j = p; j > 0 && temp < memory[j - 1]; j--) {
				memory[j] = memory[j - 1];
			}

			memory[j] = temp;
		}
	}

	public void initialPass(TapeDrive in, TapeDrive out1, TapeDrive out2) {
		int remaining = tapeSize;
		boolean one = true;
		while(remaining >= memorySize) {
			for(int i = 0; i < memorySize; i++) {
				memory[i] = in.read();
				remaining--;
			}

			quicksort(memorySize);
			for(int i = 0; i < memorySize; i++) {
				if(one) {
					out1.write(memory[i]);
				} else {
					out2.write(memory[i]);
				}
			}

			one = !one;
		}

		if(remaining != 0) {
			for(int i = 0; i < remaining; i++) {
				memory[i] = in.read();
			}

			quicksort(remaining);
			for(int i = 0; i < remaining; i++) {
				if(one) {
					out1.write(memory[i]);
				} else {
					out2.write(memory[i]);
				}
			}

		}

		in.reset();
		out1.reset();
		out2.reset();
	}


	public void mergeChunks(TapeDrive in1, TapeDrive in2, TapeDrive out, int size1, int size2) {
		int position1 = 0;
		int position2 = 0;
		Integer temp1 = null;
		Integer temp2 = null;

		if(size1 > 0) {
			temp1 = in1.read();
		}

		if(size2 > 0) {
			temp2 = in2.read();
		}

		while((position1 < size1) && (position2 < size2)) {

			if(temp1 == null) {
				temp1 = in1.read();
			}

			if(temp2 == null) {
				temp2 = in2.read();
			}

			if(temp1 < temp2) {
				out.write(temp1);
				position1++;
				temp1 = null;
			} else {
				out.write(temp2);
				position2++;
				temp2 = null;
			}
		}

		while(temp1 != null) {
			out.write(temp1);
			position1++;
			if(position1 < size1) {
				temp1 = in1.read();
			} else {
				temp1 = null;
			}
		}

		while (temp2 != null) {
			out.write(temp2);
			position2++;
			if(position2 < size2) {
				temp2 = in2.read();
			} else {
				temp2 = null;
			}
		}

	}

	public void doRun(TapeDrive in1, TapeDrive in2, TapeDrive out1, TapeDrive out2, int runNumber) {
		int chunkSize = memorySize * ((int) Math.pow(2, runNumber));
		boolean one = true;
		for(int i = 0; i < (tapeSize / (chunkSize * 2)); i++) {

			if(one) {
				mergeChunks(in1, in2, out1, chunkSize, chunkSize);
			} else {
				mergeChunks(in1, in2, out2, chunkSize, chunkSize);
			}

			one = !one;
		}

		int remainder = (tapeSize % (chunkSize * 2));
		if((remainder > 0) && (remainder <= chunkSize)) {
			if(one) {
				mergeChunks(in1, in2, out1, remainder, 0);
			} else {
				mergeChunks(in1, in2, out2, remainder, 0);
			}
		}

		if((remainder > chunkSize) && (remainder < chunkSize * 2)) {
			if(one) {
				mergeChunks(in1, in2, out1, chunkSize, (remainder - chunkSize));
			} else {
				mergeChunks(in1, in2, out2, chunkSize, (remainder - chunkSize));
			}
		}

		in1.reset();
		in2.reset();
		out1.reset();
		out2.reset();
	}


	public void sort(TapeDrive t1, TapeDrive t2, TapeDrive t3, TapeDrive t4) {
		if(memorySize >= tapeSize) {
			for(int i = 0; i < tapeSize; i++) {
				memory[i] = t1.read();
			}

			quicksort(tapeSize);
			for(int i = 0; i < tapeSize; i++) {
				t1.write(memory[i]);
			}

			return;
		}

		initialPass(t1, t3, t4);
		boolean two = true;
		int numRuns = (int) (Math.log(tapeSize / memorySize) / Math.log(2)) + 1;
		for(int i = 0; i <= numRuns; i++) {
			if(two) {
				doRun(t3, t4, t1, t2, i);
			} else {
				doRun(t1, t2, t3, t4, i);
			}

			two = !two;
		}

		if(two) {
			t1 = t3;
		}

		t1.reset();
		t2.reset();
		t3.reset();
		t4.reset();
	}

	public static void main(String[] args) {
		// Example of how to test
		TapeSorter tapeSorter = new TapeSorter(10, 80);
		TapeDrive t1 = TapeDrive.generateRandomTape(80);
		TapeDrive t2 = new TapeDrive(80);
		TapeDrive t3 = new TapeDrive(80);
		TapeDrive t4 = new TapeDrive(80);

		tapeSorter.sort(t1, t2, t3, t4);
		int last = Integer.MIN_VALUE;
		boolean sorted = true;
		for (int i = 0; i < 80; i++) {
			int val = t1.read();
			System.out.println(val);
			sorted &= last <= val; // <=> sorted = sorted && (last <= val);
			last = val;
		}
		if (sorted)
			System.out.println("Sorted!");
		else
			System.out.println("Not sorted!");
	}

}
