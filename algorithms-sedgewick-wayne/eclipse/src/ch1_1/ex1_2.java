package ch1_1;

public class ex1_2 {

	public static void main(String[] args) {
		// 1.2.6
		String s = "ACTGACG";
		String t = "TGACGAC";
		boolean isCircularShift = s.length() == t.length() && ((s+s).indexOf(t)) != -1;
		System.out.println(isCircularShift);
		

	}

}
