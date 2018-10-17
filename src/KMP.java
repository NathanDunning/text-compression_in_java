/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {
	public boolean bruteForce = true;
	public int[] table;

	public KMP(String pattern, String text) {
		// Creating the table in the constructor
		if (pattern.length() == 0) {
			table = null;
		} else if (pattern.length() == 1) {
			int[] table = { -1 };
			this.table = table;
		}

		int pLength = pattern.length(); // pLength is also 'm' from lectures
		int[] table = new int[pLength]; // table is matrix 'M' from lectures
		table[0] = -1;
		if(pattern.length() > 1) {
			table[1] = 0;
		}
		int idx = 0; // idx is also 'j' from lectures
		int pos = 2;

		while (pos < pLength) {
			if (pattern.charAt(pos - 1) == pattern.charAt(idx)) {
				table[pos] = idx + 1;
				pos++;
				idx++;
			} else if (idx > 0) {
				idx = table[idx];
			} else { // case where idx = 0;
				table[pos] = 0;
				pos++;
			}
		}
		this.table = table;
	}

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		if((pattern.length() == 0) || (text.length() == 0)){
			return -1;
		}
		
		int patLen = pattern.length();
		int textLen = text.length();
		int patIndex = 0;	//Also 'i' from lectures
		int textIndex = 0;	//Also 'k' from lectures
		
		while((patIndex + textIndex) < textLen) {
			if(pattern.charAt(patIndex) == text.charAt(patIndex + textIndex)) {
				patIndex++;
				if(patIndex == patLen) {
					return textIndex;
				}
			}
			else if(table[patIndex] == -1) {
				patIndex = 0;
				textIndex = textIndex + patIndex + 1;
			}
			else {
				textIndex = textIndex + patIndex - table[patIndex];
				patIndex = table[patIndex];
			}
		}
		
		return -1;
	}

	public int bruteForceSearch(String pattern, String text) {
		// Using variables from lecture slides
		int m = pattern.length();
		int n = text.length();
		boolean found;
		for (int k = 0; k < n - m; k++) {
			found = true;
			for (int i = 0; i < m - 1; i++) {
				if (pattern.charAt(i) != text.charAt(k + i)) {
					found = false;
					break;
				}
			}
			if (found) {
				return k;
			}
		}
		return -1;
	}

}
