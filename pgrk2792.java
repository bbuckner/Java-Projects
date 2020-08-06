// Brandon Buckner cs610 2792 prp

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

@SuppressWarnings("unchecked")

public class pgrk2792 {

	public static void main(String[] args) {
		
		try {
			algo(args);
		}
		catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND");
		}
	}
	
	static void algo(String[] args) throws FileNotFoundException {

		// Construct the graph from text file
		File file = new File(args[2]);
		Scanner fStream = new Scanner(file);
		String[] line = fStream.nextLine().split(" ");
		int vertices = Integer.parseInt(line[0]);
		int edges = Integer.parseInt(line[1]);
		LinkedList<Integer> revGraph[] = new LinkedList[vertices];
		int[] outDegree = new int[vertices];
		for (int i = 0; i < vertices; i++) {
			revGraph[i] = new LinkedList<Integer>();
		}
		for (int i = 0; i < edges; i++) {
			line = fStream.nextLine().split(" ");
			revGraph[Integer.parseInt(line[1])].add(Integer.parseInt(line[0]));
			outDegree[Integer.parseInt(line[0])]++;
		}
		fStream.close();
		
		// For large graphs...
		double stopCondition, initialValue;
		if (vertices > 10) {
			stopCondition = 0;
			initialValue = -1;
		}
		else {
			stopCondition = Integer.parseInt(args[0]);
			initialValue = Integer.parseInt(args[1]);
		}
		
		// Calculate the true stopCondition (iterations or errorRate)
		if (stopCondition == 0) {
			stopCondition = Math.pow(10, -5);
		}
		else if (stopCondition < 0) {
			stopCondition = Math.pow(10, stopCondition);
		}
		
		// Calculate the true initialValue for the vectors
		if (initialValue == -1) {
			initialValue = 1.0 / vertices;
		}
		else if (initialValue == -2) {
			initialValue = 1 / Math.sqrt(vertices);
		}

		// Create necessary data structures and constants
		double[] Pr_Prev = new double[vertices];
		Arrays.fill(Pr_Prev, initialValue);
		double[] Pr_Curr = new double[vertices];
		double z = ((1 - .85) / vertices);
		boolean stop = false;
		double m;
		int iter = 0;
		DecimalFormat df = new DecimalFormat("0.0000000");
		
		// Print the base iteration 0 if total vertices <= 10
		if (vertices <= 10) {
			System.out.print(String.format("Base : %1$3s :", iter));
			for (int i = 0; i < Pr_Prev.length; i++) {
				System.out.print("P[ " + i + "]=" + df.format(Pr_Prev[i]) + " ");
			}
			System.out.println();
			iter++;
		}
		else {
			iter++;
		}

		while (!stop) {

			// Loop through all vertex i to update their PageRank values
			for (int i = 0; i < Pr_Curr.length; i++) {
				m = 0;
				// Loop through column i of graph to find which vertices j point to vertex i and add their values to m
				for (Integer j : revGraph[i]) {  
					m += Pr_Prev[j] / outDegree[j];
				}
				m *= .85;
				Pr_Curr[i] = z + m;
			}

			// Print Pr_Curr for all i if vertices <= 10
			if (vertices <= 10) {
				System.out.print(String.format("Iter : %1$3s :", iter));
				for (int i = 0; i < Pr_Curr.length; i++) {
					System.out.print("P[ " + i + "]=" + df.format(Pr_Curr[i]) + " ");
				}
				System.out.println();
				iter++;
			}
			else {
				iter++;
			}

			// Check stopCondition
			if (0 < stopCondition && stopCondition < 1) {
				stop = true;
				for (int i = 0; i < Pr_Curr.length; i++) {
					if (Math.abs(Pr_Curr[i] - Pr_Prev[i]) > stopCondition) {
						stop = false;
					}
				}
			}
			else if (stopCondition > 1) {
				stopCondition--;
			}
			else {
				stop = true;
			}

			// Set Pr_Prev equal to Pr_Curr in case there is another iteration
			for (int i = 0; i < Pr_Prev.length; i++) {
				Pr_Prev[i] = Pr_Curr[i];
			}
		}
		
		if (vertices > 10) {
			System.out.println("Iter : " + iter);
			for (int i = 0; i < Pr_Curr.length; i++) {
				System.out.println("P[" + i + "]=" + df.format(Pr_Curr[i]));
			}
		}
	}
}
