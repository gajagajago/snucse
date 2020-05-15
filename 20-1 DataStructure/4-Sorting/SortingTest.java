import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try
		{
			boolean isRandom = false;	
			int[] value;	
			String nums = br.readLine();	
			if (nums.charAt(0) == 'r')
			{
				
				isRandom = true;	

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	
				int rminimum = Integer.parseInt(nums_arg[2]);	
				int rmaximum = Integer.parseInt(nums_arg[3]);	

				Random rand = new Random();	

				value = new int[numsize];	
				for (int i = 0; i < value.length; i++)	
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	
				for (int i = 0; i < value.length; i++)	
					value[i] = Integer.parseInt(br.readLine());
			}

			
			while (true)
			{
				int[] newvalue = (int[])value.clone();	

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	
					default:
						throw new IOException("Wrong input");
				}
				if (isRandom)
				{
					
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Wrong input. Error : " + e.toString());
		}
	}
	
	private static void swap(int[] arr, int idx1, int idx2) {
		int temp = arr[idx1];
		arr[idx1] = arr[idx2];
		arr[idx2] = temp;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] arr)
	{		
		for(int itr = 0; itr < arr.length-1; ++itr) {
			for(int i = 0; i < arr.length-1-itr; ++i) {
				if(arr[i] > arr[i+1])
					swap(arr, i, i+1);
			}
		}
		
		return arr;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] arr)
	{
		for(int itr = 1; itr < arr.length; ++itr) {
			int i = itr;
			
			while(i > 0) {
				if(arr[i] < arr[i-1])
					swap(arr, i-1, i);
				
				--i;
			}
		}
		
		return arr;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void percolateDown(int[] arr, int size, int root)
	{
		int parent = root,
			lchild = 2*root + 1,
			rchild = 2*root + 2;

		//percolate up
		if(lchild < size && arr[parent] < arr[lchild])
			parent = lchild;
		if(rchild < size && arr[parent] < arr[rchild])
			parent = rchild;

		if(root != parent) {
			swap(arr, parent, root);
			percolateDown(arr, size, parent);
		}
	}

	private static int[] DoHeapSort(int[] arr)
	{
		int n = arr.length;

		//build max heap
		for(int i = (n+1)/2 - 1; i >= 0; --i) //last parent idx = (n+1)/2-1
			percolateDown(arr, n, i);	// build semi max heap from last p ~ root

		//place last node in root
		for(int i = n - 1; i > 0; --i) {
			swap(arr, 0, i);
			percolateDown(arr, i, 0);
		}

		return arr;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void merge(int[] arr, int low, int mid, int high) {
		int[] temp = new int[high - low];
		int temp_itr = 0, half1_itr = low, half2_itr = mid;

		//iterate and merge two arrays until one reaches end
		while (half1_itr < mid && half2_itr < high)
			temp[temp_itr++] = arr[ (arr[half1_itr] < arr[half2_itr]) ? half1_itr++ : half2_itr++ ];

		//push until the other also reaches end
		while (half1_itr < mid)
			temp[temp_itr++] = arr[half1_itr++];
		while (half2_itr < high)
			temp[temp_itr++] = arr[half2_itr++];

		//copy back
		for (int i = low; i < high; ++i)
			arr[i] = temp[i - low];
	}

	private static void m_sort(int[] arr, int low, int high) {
		//base case
		if (high - low < 2)
			return;
		
		int mid = (low + high) / 2;
		m_sort(arr, low, mid);
		m_sort(arr, mid, high);
		merge(arr, low, mid, high);
	}

	private static int[] DoMergeSort(int[] arr)
	{	
		m_sort(arr, 0, arr.length);
		
		return arr;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int partition(int[] arr, int left, int right) {
		int pivot = arr[(left + right) / 2];

		while(left <= right) {
			//find left idx element( >p )
			while(arr[left] < pivot)
				left++;
			//find right idx element( <p )
			while(arr[right] > pivot)
				right--;

			//swap found elements
			if(left <= right)
				swap(arr, left++, right--);
		}

		//found left+1 idx becomes next pivot
		return left;
	}
	
	private static void q_sort(int[] arr, int left, int right) {
		if(left >= right)
			return;
		
		int mid = partition(arr, left, right);

		q_sort(arr, left, mid-1);
		q_sort(arr, mid, right);
	}
	
	private static int[] DoQuickSort(int[] arr)
	{
		q_sort(arr, 0, arr.length-1);
		
		return arr;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void countSort(int[] arr, int digit) {
		int[] output = new int[arr.length];	//temporary list for each iterative output
		int[] count = new int[19];			//list to count result of %10+9; therefore, 0~18;

		Arrays.fill(count, 0);

		for(int i = 0; i < arr.length; ++i)
			count[(arr[i] / digit) % 10 + 9]++;

		//build accumulative count list
		for(int i = 1; i < count.length; ++i)
			count[i] += count[i-1];

		//build temporary output list
		//iterate from arr's back to consider previous digit comparison result
		for(int i = arr.length - 1; i >= 0; i--) {
			output[count[(arr[i] / digit) % 10 + 9] - 1] = arr[i];
			count[(arr[i] / digit) % 10 +9]--;
		}

		//update arr for this digit
		for(int i = 0; i < arr.length; ++i)
			arr[i] = output[i];
	}

	private static int[] DoRadixSort(int[] arr) {
		int maxVal = 0;
		int maxDigit = 1;

		//get absolute max value from the list
		for(int i = 0; i < arr.length; ++i) {
			int curr = Math.abs(arr[i]);
			maxVal = (curr > maxVal) ? curr : maxVal;
		}

		//get max digit of the absolute max value
		for(int i = 1; maxVal / i > 1; i *= 10)
			maxDigit *= 10;

		//recursive countsort of list elements on all digits
		for(int digit = 1; digit <= maxDigit; digit *= 10)
			countSort(arr, digit);

        return arr;
    }
}
