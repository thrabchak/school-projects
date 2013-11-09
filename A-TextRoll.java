import java.util.Scanner;

public class TextRoll{

	public static int rollText(int numLines, String lines[]){

		int lastDropdown = 0;
		for(int i = 0; i < numLines; i++){
			for(int j = lastDropdown; j < lines[i].length(); j++){
				if(lines[i].charAt(j) == ' ' || lines[i].charAt(j)=='\n'){
					lastDropdown = j;
					break;
				} else {
					lastDropdown = j + 1;
				}
			}
		}

		return lastDropdown + 1;
	}

	public static void main (String[] args){
		Scanner sc = new Scanner(System.in);

		while(true){
			int numLines = sc.nextInt();

			if (numLines > 0){
				String lines[] = new String[numLines];

				sc.nextLine();

				for (int i = 0; i < numLines; i++){
					lines[i] = sc.nextLine();
				}

				System.out.println(rollText(numLines, lines));
			} else {
				break;
			}
		}
	}
}