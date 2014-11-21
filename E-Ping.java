import java.util.ArrayList;
import java.util.Scanner;

public class Ping {

	public static String formatOutput (ArrayList<Integer> input){
		String out = "";
		for (int i = 0; i < input.size() - 1; i++){
			out += input.get(i) + " ";
		}
		out += input.get(input.size() - 1);
		return out;
	}

	public static ArrayList<Integer> invertByFrequency(ArrayList<Integer> existing, int freq){
		ArrayList<Integer> newArrayList = existing;

		for (int i = 0; i < existing.size(); i += freq){
			if (existing.get(i) == 0)
				newArrayList.set(i, 1);
			else
				newArrayList.set(i, 0);
		}

		return newArrayList;
	}

	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		ArrayList<Integer> knownFreqs;
		ArrayList<Integer> predicted;

		while(true){
			knownFreqs = new ArrayList<Integer>();
			predicted = new ArrayList<Integer>();

			String line = in.nextLine();
			if(line.length() == 1 && line.charAt(0) == '0')
				break;

			for(int i = 0; i < line.length(); i++){
				predicted.add(0);
			}

			for(int i = 1; i < line.length(); i++){
				if(line.charAt(i) != Integer.toString(predicted.get(i)).charAt(0)){
					predicted = invertByFrequency(predicted, i);
					knownFreqs.add(i);
				}
			}

			System.out.println(formatOutput(knownFreqs));
		}

		in.close();
	}
}