import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

@SuppressWarnings("unchecked")

public class hits2792 {

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
		LinkedList<Integer> graph[] = new LinkedList[vertices];
		LinkedList<Integer> revGraph[] = new LinkedList[vertices];
		for (int i = 0; i < vertices; i++) {
			graph[i] = new LinkedList<Integer>();
			revGraph[i] = new LinkedList<Integer>();
		}
		for (int i = 0; i < edges; i++) {
			line = fStream.nextLine().split(" ");
			graph[Integer.parseInt(line[0])].add(Integer.parseInt(line[1]));
			revGraph[Integer.parseInt(line[1])].add(Integer.parseInt(line[0]));
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
		double[] aPrev = new double[vertices];
		Arrays.fill(aPrev, initialValue);
		double[] aCurr = new double[vertices];
		double[] hPrev = new double[vertices];
		Arrays.fill(hPrev, initialValue);
		double[] hCurr = new double[vertices];
		boolean stop = false;
		double temp;
		int iter = 0;
		DecimalFormat df = new DecimalFormat("0.0000000");
		
		// Print the base iteration 0 if total vertices <= 10
		if (vertices <= 10) {
			System.out.print(String.format("Base : %1$3s :", iter));
			for (int i = 0; i < vertices; i++) {
				System.out.print("A/H[ " + i + "]=" + df.format(aPrev[i]) + "/" + df.format(hPrev[i]) + " ");
			}
			System.out.println();
			iter++;
		}
		else {
			iter++;
		}

		while (!stop) {
			// Loop through all vertex i to update their authority values
			for (int i = 0; i < aCurr.length; i++) {
				temp = 0;
				// Loop through column i of graph to find which vertices j point to vertex i and add their hub values to tempSum
				for (Integer j : revGraph[i]) {  
					temp += hPrev[j];
				}
				aCurr[i] = temp;
			}
			
			// Loop through all vertex i to update their hub values
			for (int i = 0; i < hCurr.length; i++) {
				temp = 0;
				// Loop through row i of graph to find which vertices j vertex i points to and add their authority values to tempSum
				for (Integer j : graph[i]) {  
					temp += aCurr[j];
				}
				hCurr[i] = temp;
			}
			
			// Scale aCurr
			temp = 0;
			for (int i = 0; i < aCurr.length; i++) {
				temp += Math.pow(aCurr[i], 2);
			}
			temp = Math.sqrt(temp);
			for (int i = 0; i < aCurr.length; i++) {
				aCurr[i] /= temp;
			}
			
			// Scale hCurr
			temp = 0;
			for (int i = 0; i < hCurr.length; i++) {
				temp += Math.pow(hCurr[i], 2);
			}
			temp = Math.sqrt(temp);
			for (int i = 0; i < hCurr.length; i++) {
				hCurr[i] /= temp;
			}
			
			// Print aCurr and hCurr for all vertices i if vertices <= 10
			if (vertices <= 10) {
				System.out.print(String.format("Iter : %1$3s :", iter));
				for (int i = 0; i < vertices; i++) {
					System.out.print("A/H[ " + i + "]=" + df.format(aCurr[i]) + "/" + df.format(hCurr[i]) + " ");
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
				for (int i = 0; i < vertices; i++) {
					if (Math.abs(aCurr[i] - aPrev[i]) > stopCondition || Math.abs(hCurr[i] - hPrev[i]) > stopCondition) {
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

			// Set aPrev and hPrev equal to aCurr and hCurr respectively in case there is another iteration
			for (int i = 0; i < vertices; i++) {
				aPrev[i] = aCurr[i];
				hPrev[i] = hCurr[i];
			}
		}
		
		if (vertices > 10) {
			System.out.println("Iter : " + iter);
			for (int i = 0; i < vertices; i++) {
				System.out.println("A/H[ " + i + "]=" + df.format(aCurr[i]) + "/" + df.format(hCurr[i]) + " ");
			}
		}
	}
}
