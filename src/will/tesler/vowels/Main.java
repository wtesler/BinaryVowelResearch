package will.tesler.vowels;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Main {

	private static ArrayList<Language> languages = new ArrayList<Language>();
	private static LinkedHashSet<Vowel> vowelSet = new LinkedHashSet<Vowel>();

	// [high/mid/low][front/center/back][round][ATR]
	private static Vowel[][][][] grid = new Vowel[3][3][2][2];

	private static ArrayList<Vowel> alphabet = new ArrayList<Vowel>();

	private static final boolean VERBOSE = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		setAlphabet();

		readCSV();

		createVowelSet();

		placeVowels();

		testEveryCombination();
	}

	private static void testEveryCombination() {
		int n = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 2; k++) {
					for (int m = 0; m < 2; m++) {
						test(i, j, k, m);
					}
				}
			}
		}
	}

	private static void test(int height, int backness, int round, int atr) {

		// HEIGHT
		if (height == 0) {
			ArrayList<Vowel> vowels = new ArrayList<Vowel>();
			for (int i = 0; i < 3; i++) {
				Vowel v = grid[i][backness][round][atr];
				if (!v.vowel.equals(" ")) {
					vowels.add(v);
				}
			}
			analyze(vowels);

			analyzeSubsets(vowels, 2);
		}

		// BACKNESS
		if (backness == 0) {
			ArrayList<Vowel> vowels = new ArrayList<Vowel>();
			for (int i = 0; i < 3; i++) {
				Vowel v = grid[height][i][round][atr];
				if (!v.vowel.equals(" ")) {
					vowels.add(v);
				}
			}

			analyze(vowels);

			analyzeSubsets(vowels, 2);
		}

		// ROUND
		if (round == 0) {
			ArrayList<Vowel> vowels = new ArrayList<Vowel>();
			for (int i = 0; i < 2; i++) {
				Vowel v = grid[height][backness][i][atr];
				if (!v.vowel.equals(" ")) {
					vowels.add(v);
				}
			}

			analyze(vowels);

			analyzeSubsets(vowels, 2);
		}

		// ATR
		if (atr == 0) {
			ArrayList<Vowel> vowels = new ArrayList<Vowel>();
			for (int i = 0; i < 2; i++) {
				Vowel v = grid[height][backness][round][i];
				if (!v.vowel.equals(" ")) {
					vowels.add(v);
				}
			}

			analyze(vowels);

			analyzeSubsets(vowels, 2);
		}
	}

	private static void analyzeSubsets(ArrayList<Vowel> vowelSet, int i) {

		if (vowelSet.size() <= i) {
			return;
		}

		ArrayList<ArrayList<Vowel>> superSet = new ArrayList<ArrayList<Vowel>>();

		getSubsets(vowelSet, 2, 0, new HashSet<Vowel>(),   superSet);

		for (ArrayList<Vowel> vowels : superSet) {
			analyze(vowels);
		}
	}

	private static void analyze(ArrayList<Vowel> vowels) {

		if (vowels.size() <= 1) {
			return;
		}

		System.out.println("__Vowels __");

		boolean minimumPairFound = false;
		for (int i = 0; i < languages.size(); i++) {
			//Create a BitSet and initialize all values to 1.
			BitSet bs = new BitSet(vowels.size());
			bs.set(0, vowels.size());

			for (int j = 0; j < vowels.size(); j++) {
				if (languages.get(i).getVowels().contains(vowels.get(j))) {
					// When we see a matching vowel, set it to 0
					bs.set(j, false);
				}
			}

			if (bs.isEmpty()) {
				minimumPairFound = true;
				System.out.println(vowels + " : " + i);
				//break;
			}
		}
		if (!minimumPairFound) {
			System.out.println("- NO MINIMUM PAIR FOUND BETWEEN: " + vowels);
		}
	}

	/**
	 * Generate the initial IPA vowel alphabet
	 */
	private static void setAlphabet() {
		alphabet.add(new Vowel("ɛ"));
		alphabet.add(new Vowel("e"));
		alphabet.add(new Vowel("œ"));
		alphabet.add(new Vowel("ø"));
		alphabet.add(new Vowel("ɜ"));
		alphabet.add(new Vowel("ə"));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel("ɵ"));
		alphabet.add(new Vowel("ʌ"));
		alphabet.add(new Vowel("ɤ"));
		alphabet.add(new Vowel("ɔ"));
		alphabet.add(new Vowel("o"));
		alphabet.add(new Vowel("a"));
		alphabet.add(new Vowel("æ"));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel("a̠"));
		alphabet.add(new Vowel("ɐ"));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel("ɑ"));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel("ɒ"));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel("ɪ"));
		alphabet.add(new Vowel("i"));
		alphabet.add(new Vowel("ʏ"));
		alphabet.add(new Vowel("y"));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel("ɨ"));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel("ʉ"));
		alphabet.add(new Vowel(" "));
		alphabet.add(new Vowel("ɯ"));
		alphabet.add(new Vowel("ʊ"));
		alphabet.add(new Vowel("u"));

		if (VERBOSE) {
			System.out.print("Alphabet : ");
			for (Vowel vowel : alphabet) {
				System.out.print(vowel.vowel + ", ");
			}
			System.out.println();
		}
	}

	/**
	 * Places all vowels in their initial positions
	 */
	private static void placeVowels() {

		int n = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 2; k++) {
					for (int m = 0; m < 2; m++) {
						grid[i][j][k][m] = alphabet.get(n++);
					}
				}
			}
		}

		if (VERBOSE) {
		}
	}

	/**
	 * Gives us the set of all vowels
	 */
	public static void createVowelSet() {
		for (Language language : languages) {
			for (Vowel v : language.getVowels()) {
				vowelSet.add(v);
			}
		}

		if (VERBOSE) {
			System.out.println("The Set of Vowels: " + vowelSet
					+ " for a total of " + vowelSet.size());
		}
	}

	/**
	 * Read in CSV (Formatted a specific way) and parse it into Languages.
	 */
	public static void readCSV() {

		String csvFile = "csv/simple_vowels.csv";
		BufferedReader br = null;
		String line = "";
		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					csvFile), "UTF-8"));

			while ((line = br.readLine()) != null) {

				String[] splitLine = line.split(",,,|\"|,");

				Language language = new Language();
				for (int i = 1; i < splitLine.length - 1; i++) {
					if (splitLine[i].length() > 0) {
						language.add(new Vowel(splitLine[i]));
					}
				}
				languages.add(language);
			}

			if (VERBOSE) {
				System.out.println("There are a total of " + languages.size() + " languages.");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void getSubsets(ArrayList<Vowel> superSet, int k, int idx,
			Set<Vowel> current, ArrayList<ArrayList<Vowel>> solution) {
	    //successful stop clause
	    if (current.size() == k) {
	        solution.add(new ArrayList<Vowel>(current));
	        return;
	    }
	    //unseccessful stop clause
	    if (idx == superSet.size()) return;
	    Vowel x = superSet.get(idx);
	    current.add(x);
	    //"guess" x is in the subset
	    getSubsets(superSet, k, idx+1, current, solution);
	    current.remove(x);
	    //"guess" x is not in the subset
	    getSubsets(superSet, k, idx+1, current, solution);
	}
	/**
	 * Holds a list of vowels
	 */
	static class Language {

		private ArrayList<Vowel> vowels = new ArrayList<Vowel>();

		public void add(Vowel v) {
			vowels.add(v);
		}

		public ArrayList<Vowel> getVowels() {
			return vowels;
		}

		public void print() {
			System.out.print("Vowels:");
			for (Vowel vowel : vowels) {
				System.out.print("\t" + vowel);
			}
			System.out.println();
		}
	}

	/**
	 * Holds the IPA symbol that encodes this Vowel
	 */
	static class Vowel implements CharSequence {

		private final String vowel;

		public static final int LOW = 0;
		public static final int MID = 1;
		public static final int HIGH = 2;

		public static final int FRONT = 0;
		public static final int CENTER = 1;
		public static final int BACK = 2;

		public static final int UNROUND = 0;
		public static final int ROUND = 1;

		public static final int ADVANCED = 0;
		public static final int RETRACTED = 1;

		@Override
		public String toString() {
			return vowel;
		}

		Vowel(String v) {
			vowel = v;
		}

		@Override
		public char charAt(int arg0) {
			return vowel.charAt(arg0);
		}

		@Override
		public int length() {
			return vowel.length();
		}

		@Override
		public CharSequence subSequence(int arg0, int arg1) {
			return vowel.substring(arg0, arg1);
		}

		@Override
		public int hashCode() {
			return vowel.hashCode();
		}

		@Override
		public boolean equals(Object arg0) {
			try {
				return (arg0 instanceof Vowel)
						&& this.vowel.equals(((Vowel) arg0).vowel);
			} catch (ClassCastException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
