package ch1_1;

import java.util.Random;

public class ex1_1 {
	
	public static Integer[][] transposition(Integer[][] ar){
		
		for(int i = 0; i < ar.length; i++)
			for(int j = i; j < ar[i].length; j++){
				Integer temp = new Integer(ar[i][j]);
				ar[i][j] = new Integer(ar[j][i]);
				ar[j][i] = temp;
			}
		return ar;		
	}
	
	public static void printArray2D(Object[][] ar){
		StringBuffer outStr = new StringBuffer();
		
		int objectLength = 5;
		for(int i = 0; i < ar.length; i++){
			for(int j = 0; j < ar[i].length; j++){
				int len = ar[i][j].toString().length();
				if(len > objectLength)
					objectLength = len;
			}
		}
		
		//top title
		outStr.append(String.format("%6c", ' '));
		for(int i = 0; i < ar.length; i++)
			outStr.append(String.format("%" + objectLength + "d ", i));
		outStr.append(String.format("\n%5c", ' '));
		for(int i = 0; i < ar.length; i++){
			for(int j = 0; j < objectLength; j++)
				outStr.append("-");
			outStr.append("-");
		}
		outStr.append("-\n");
		
		for(int i = 0; i < ar.length; i++){
			//side title
			outStr.append(String.format("%4d |", i));
			
			//main content
			for(int j = 0; j < ar[i].length; j++)
				outStr.append(String.format("%"+ objectLength +"s|", ar[i][j].toString()));
			outStr.append("\n");
		}
		
		System.out.println(outStr.toString());
	}
	
	public static void printArray(Object[] a){
		StringBuffer outStr = new StringBuffer();
		
		int objectLength = 3;
		for(int i = 0; i < a.length; i++){
			int length = a[i].toString().length();
			if(length > objectLength)
				objectLength = length;
		}
		
		outStr.append(' ');
		for(int i = 0; i < a.length; i++)
			outStr.append(String.format("%" + objectLength + "d ",i));		
		outStr.append("\n-");
		for(int i = 0; i < a.length; i++)
			for(int j = 0; j <= objectLength; j++)
				outStr.append('-');
		outStr.append("\n|");
		for(int i = 0; i < a.length; i++)
			outStr.append(String.format("%" + objectLength + "s|", a[i].toString()));
		outStr.append("\n-");
		for(int i = 0; i < a.length; i++)
			for(int j = 0; j <= objectLength; j++)
				outStr.append('-');		
		
		System.out.println(outStr.toString());
	}
	
	public static int lg(int x){
		int count = 0;
		for(int i = x; i > 1; i /= 2)
			count++;
		return count;
	}
	
	public static Integer[] histogram(Integer[] a, int m){
		Integer[] output = new Integer[m];
		for(int i = 0; i < output.length; i++)
			output[i] = 0;
		
		for(int j = 0; j < a.length; j++)
			if(a[j] < m)
				output[a[j]]++;				
		
		return output;
	}

	public static void main(String[] args) {
		///1.1.11
		Boolean[][] a = new Boolean[3][3];
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[i].length; j++){
				Random r = new Random();
				a[i][j] = r.nextBoolean();
			}
		}		
		printArray2D(a);
		
		//1.1.13
		Integer[][] b = new Integer[4][4];
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b[i].length; j++){
				Random r = new Random();
				b[i][j] = r.nextInt(9999);
			}
		}	
		printArray2D(b);
		printArray2D(transposition(b));
		
		//1.1.14
		System.out.println(lg(16));
		
		//1.1.15
		Integer[] c = new Integer[25];
		for(int i = 0; i < c.length; i++){
			Random r = new Random();
			c[i] = r.nextInt(10);
		}
		printArray(c);
		printArray(histogram(c,10));
	}

}
