// Brandon Buckner cs610 2792 prp

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class mplexicon2792 {
	
	int[] T;
	char[] A;
	int a = 0, timesGrown = 0, itemsInT = 0;
	
	public static void main(String[] args) {
		String filename = args[0];
		try {
			batch(filename);
		}
		catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND");
		}
	}
	
	static void batch(String filename) throws FileNotFoundException {
		File file = new File(filename);
		Scanner fStream = new Scanner(file);
		String line = fStream.nextLine();
		mplexicon2792 L = new mplexicon2792(Integer.parseInt(line.substring(3)));
		
		while (fStream.hasNextLine()) {
			line = fStream.nextLine();
			if (line.charAt(1) == '0') {
				L.insert(line.substring(3));
			}
			else if (line.charAt(1) == '1') {
				L.delete(line.substring(3));
			}
			else if (line.charAt(1) == '2') {
				L.search(line.substring(3));
			}
			else if (line.charAt(1) == '3') {
				L.print();
			}
			else if (line.charAt(1) == '4') {
				L = new mplexicon2792(Integer.parseInt(line.substring(3)));
			}
		}
		fStream.close();
	}
	
	// Constructor (takes care of the "create" command, 14)
	mplexicon2792(int size) {
		T = new int[size];
		Arrays.fill(T, -1);
		A = new char[size * 15];
		Arrays.fill(A, ' ');
	}
	
	void insert(String word) {
		if (full() || (word.length() > A.length - a)) {
			grow();
		}
		int asciiVal, asciiSum = 0;
		for(int i = 0; i < word.length(); i++) {
			asciiVal = (int)word.charAt(i);
			asciiSum += asciiVal;
		}
		int t;
		for(int i = 0; i < T.length; i++) {
			t = (asciiSum + (int)Math.pow(i, 2)) % T.length;
			if (T[t] == -1) {
				T[t] = a;
				for(int j = 0; j < word.length(); j++) {
					A[a++] = word.charAt(j);
				}
				A[a++] = '\0';
				itemsInT++;
				return;
			}
			else {
				String compWord = "";
				for(int j = T[t]; A[j] != '\0'; j++) {
					compWord += A[j];
				}
				if (word.equals(compWord)) {
					return;
				}
			}
		}
		grow();
		insert(word);
	}
	
	void grow() {
		// Extract strings to Re-insert from A and store them temporarily so T and A can be resized.
		// Does not extract deleted words.
		String[] stringsToReinsert = new String[itemsInT];
		String word = "";
		int k = 0;
		for(int i = 0; i < T.length; i++) {
			if (T[i] != -1) {
				for(int j = T[i]; A[j] != '\0'; j++) {
					word += A[j];
				}
				stringsToReinsert[k++] = word;
			}
			word = "";
		}
		
		// Resize T
		// All entries in T are erased and not replaced in their original spots
		T = new int[T.length * (int)Math.pow(2, ++timesGrown)];
		Arrays.fill(T, -1);
		
		// Resize A
		A = new char[T.length * 15];
		Arrays.fill(A, ' ');
		a = 0;
		
		// Re-insert all strings back into the new T and A
		for(int i = 0; i < stringsToReinsert.length; i++) {
			insert(stringsToReinsert[i]);
		}
		
		// Make sure re-inserted strings do not count towards items in T
		itemsInT -= stringsToReinsert.length;
	}
	
	void delete(String word) {
		int asciiVal, asciiSum = 0;
		for(int i = 0; i < word.length(); i++) {
			asciiVal = (int)word.charAt(i);
			asciiSum += asciiVal;
		}
		int t;
		for(int i = 0; i < T.length; i++) {
			t = (asciiSum + (int)Math.pow(i, 2)) % T.length;
			if (T[t] != -1) {
				String compWord = "";
				for(int j = T[t]; A[j] != '\0'; j++) {
					compWord += A[j];
				}
				if (word.equals(compWord)) {
					for(int j = T[t]; A[j] != '\0'; j++) {
						A[j] = '*';
					}
					T[t] = -1;
					System.out.println(word + " deleted from slot " + t);
					itemsInT--;
					return;
				}
				compWord = "";
			}
		}
	}
	
	void search(String word) {
		int asciiVal, asciiSum = 0;
		for(int i = 0; i < word.length(); i++) {
			asciiVal = (int)word.charAt(i);
			asciiSum += asciiVal;
		}
		int t;
		String compWord = "";
		for(int i = 0; i < T.length; i++) {
			t = (asciiSum + (int)Math.pow(i, 2)) % T.length;
			if (T[t] != -1) {
				for(int j = T[t]; A[j] != '\0'; j++) {
					compWord += A[j];
				}
				if (word.equals(compWord)) {
					System.out.println(word + " found at slot " + t);
					return;
				}
				compWord = "";
			}
		}
		System.out.println(word + " not found");
	}
	
	boolean empty() {
		if (itemsInT == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	boolean full() {
		if (itemsInT == T.length) {
			return true;
		}
		else {
			return false;
		}
	}
	
	void print() {
		System.out.println();
		System.out.println("T");
		System.out.println("-----------------------------------");
		for(int i = 0; i < T.length; i++) {
			if (T[i] != -1) {
				System.out.println(i + ": " + T[i]);
			}
			else {
				System.out.println(i + ":");
			}
		}
		System.out.println("-----------------------------------");
		System.out.print("A: ");
		for(int i = 0; i < a; i++) {
			if (A[i] == '\0') {
				System.out.print('\\');
			}
			else {
				System.out.print(A[i]);
			}
		}
		System.out.println('\n');
	}
}
