package ch1_1;

public class ex1_3 {

	private static int josephus(int m, int n){
		boolean[] isSelected = new boolean[n];
		
		int location = 0;
		for(int i = 0; i < n; i++){
			int counter = 0;
			while(counter < m){
				if(!isSelected[location]){
					if(counter == m-1){
						//System.out.println(location);
						isSelected[location] = true;
						if(i == n-1)
							return location;
					}
					counter++;
				}
				location = (location+1)%n;	
			}					
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		// 1.3.37
		System.out.println(josephus(7,15));

	}

}
