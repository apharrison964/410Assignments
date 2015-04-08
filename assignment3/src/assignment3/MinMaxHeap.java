package assignment3;

public class MinMaxHeap<T extends Comparable<? super T>> {
	// DO NOT CHANGE THESE VARIABLES AND METHODS

	private int currentSize;
	private int[] arr;

	public MinMaxHeap(int capacity){   //Constructor
		arr = new int[capacity + 1];
		currentSize = 0;
	}

	public boolean isFull(){
		return currentSize == arr.length - 1;
	}

	public boolean isEmpty(){
		return currentSize == 0;
	}

	// COMPLETE THE FOLLOWING METHODS

	public void insert(int x) {      		//PRE: The heap is not full
		++currentSize; //currentSize++
		arr[currentSize] = x;
		percolateUp(currentSize);
	}

	public int min() {              	    //PRE: The heap is not empty
		return arr[1];
	}

	public int max() {              		//PRE: The heap is not empty 
		if(currentSize == 1) {
			return arr[1];
		} else {
			if(arr[2] > arr[3]) {
				return arr[2];
			} else {
				return arr[3];
			}
		}   
	}

	public int deleteMin() {
		int min = min();
		arr[1] = arr[currentSize];
		arr[currentSize] = 0;
		currentSize--;
		percolateDown(1);
		return min;          			//PRE: The heap is not empty
	}

	public int deleteMax(){          	//PRE: The heap is not empty
		int max;
		if(currentSize == 1) {
			max = 1;
		} else {
			if(arr[2] > arr[3]) {
				max = 2;
			} else {
				max = 3;
			}
		}
		int max2 = arr[max];
		arr[max] = arr[currentSize];
		arr[currentSize] = 0;
		currentSize--;
		percolateDown(max);
		return max2;
	}

	private void percolateUp(int i) {
		int level = (int) (Math.log(i) / Math.log(2));
		if(level % 2 == 0) {
			if(level > 0) {
				if(arr[i] > arr[i / 2]) {
					swap(i, i / 2);
					percolateUpMax(i / 2);
				} else {
					percolateUpMin(i);
				}
			}
		} else {
			if(arr[i] < arr[i / 2]) {
				swap(i, i / 2);
				percolateUpMin(i / 2);
			}
			percolateUpMax(i);
		}
	}

	private void percolateUpMin(int i) {
		int level = (int) (Math.log(i) / Math.log(2));
		if(level > 1) {
			if(arr[i] < arr[i / 4]) {
				swap(i, i / 4);
				percolateUpMin(i / 4);
			}
		}
	}

	private void percolateUpMax(int i) {
		int level = (int) (Math.log(i) / Math.log(2));
		if(level > 1) {
			if(arr[i] > arr[i / 4]) {
				swap(i, i / 4);
				percolateUpMax(i / 4);
			}
		}
	}

	private void swap(int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	private void percolateDown(int i) {
		int level = (int) (Math.log(i) / Math.log(2));
		if(level % 2 == 0) {
			percolateDownMin(i);
		} else {
			percolateDownMax(i);
		}
	}

	private void percolateDownMax(int i) {
		int max = max2(i);
		if(i * 4 <= max) {
			if(arr[max] > arr[i]) {
				swap(i, max);
			}
			if(arr[max] < arr[max / 2]) {
				swap(max, max / 2);
			}
			percolateDownMax(max);
		} else if(max != 0) {
			if(arr[max] > arr[i]) {
				swap(i, max);
			}
		}
	}

	private void percolateDownMin(int i) {
		int min = min2(i);
		if(i * 4 <= min) {
			if(arr[min] < arr[i]) {
				swap(i, min);
			} if (arr[min] > arr[min / 2]) {
				swap(min, min / 2);
			}
			percolateDownMin(min);
		} else if(min != 0) {
			if(arr[min] < arr[i]) {
				swap(i, min);
			}
		}
	}

	private int min2(int i) {
		int min = 0;
		for(int j = 2; j <= 4; j = j + 2) {
			for(int k = 0; k < j; k++) {
				if((i * j) + k <= currentSize) {
					if(min == 0) {
						min = i * 2;
					}
					if(arr[min] > arr[(i * j) + k]) {
						min = (i * j) + k;
					}
				}
			}
		}
		return min;
	}

	private int max2(int i) {
		int max = 0;
		for(int j = 2; j <= 4; j = j + 2) {
			for(int k = 0; k < j; k++) {
				if((i * j) + k <= currentSize) {
					if(max == 0) {
						max = i * 2;
					}
					if(arr[max] < arr[(i * j) + k]) {
						max = (i * j) + k;
					}
				}
			}
		}
		return max;
	}
}