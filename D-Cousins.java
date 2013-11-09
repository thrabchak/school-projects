import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Cousins {

	public static Node growTree(ArrayList<Integer> values, int special){
		Queue<Node> q = new LinkedList<Node>();

		Node tree = new Node(values.get(0));
		int previousValue = values.get(0);
		Node currentNode = tree;
		Node spec = null;

		for (in i = 1; i < values.size(); i++){
			if (i != 1 && previousValue + 1 != values.get(i))
				currentNode = q.remove();

			Node child;

			if (values.get(i) == special){
				spec = new Node(values.get(i), currentNode);
				child = spec;
			} else {
				child = new Node(values.get(i), currentNode);
			}

			currentNode.addChild(child);
			q.add(child);
			previousValue = values.get(i);
		}

		return spec;
	}

	public static void main(String[] args){
		Scanner sc1 = new Scanner(System.in);
		Scanner sc2;

		while(true){
			int size = sc1.nextInt();
			int special = sc1.nextInt();

			if (size == special && size == 0)
				break;

			ArrayList<Integer> nums = new ArrayList<Integer>();

			sc1.nextLine();
			String line = sc1.nextLine();

			sc2 = new Scanner(line);

			while(sc2.hasNext()){
				nums.add(sc2.nextInt());
			}
			sc2.close();

			Node specialNode = growTree(nums, special);

			if(specialNode != null)
				System.out.println(specialNode.getNumCousins());
			else
				System.out.println("0");
		}
		sc1.close();
	}

	public static class Node{
		public int value;
		public ArrayList<Node> children;
		public Node parent;

		public Node(int value){
			this.value = value;
			children = new ArrayList<Node>();
			parent = null;
		}

		public Node(int value, Node parent){
			this.value = value;
			this.parent = parent;
			children = new ArrayList<Node>();
		}

		public void addChild(Node child){
			children.add(child);
		}

		public int getNumCousins(){
			 Node grandparent = null;

			 if(this.parent != null && this.parent.parent != null)
			 	grandparent = this.parent.parent;
			 else
			 	return 0;

			 int numCousins = 0;

			 for (int i = 0; i < grandparent.children.size(); i++){
			 	if(grandparent.children.get(i).value != parent.value)
			 		numCousins += grandparent.children.get(i).children.size();
			 }

			 return numCousins;
		}
	}

}