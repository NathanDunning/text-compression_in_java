import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {
	int windowSize = 100;
	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	public String compress(String input) {
		StringBuilder sb = new StringBuilder();
		int cursor = 0;
		
		while(cursor < input.length()) {
			int next = 0;
			int prev = cursor;
			if(cursor > windowSize) {
				prev = windowSize;
			}
			//look for longest prefix of text
			while(true) {
				int index = search(input, cursor, next);
				//If found
				if(index != -1) {
					prev = index;
					next++;
				}
				else {
					// add [0,0, text[cursor]] to output advance cursor by length+1
					int window;
					if(cursor > windowSize) {
						window = windowSize;
					}
					else {
						window = cursor;
					}
					int offset = window - prev;
					Character c = null;
					if(cursor+next < input.length()) {
						 c = input.charAt(cursor+next);
					}

					sb.append(new Tuple(offset, next, c)).toString();
					cursor += next + 1;
					break;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public String decompress(String compressed) {
		StringBuilder sb = new StringBuilder();
		int cursor = 0;
		//Passing through the compressed string
		Scanner sc = new Scanner(compressed);
		sc.useDelimiter("\\]");
		ArrayList<Tuple> tuple = new ArrayList<Tuple>();
		
		//Creating tuples through the string constructor
		while (sc.hasNext()) {
			String string = sc.next();
			tuple.add(new Tuple(string));
		}
		
		for(Tuple t : tuple) {
			if(t.offset == 0 && t.length == 0) {
				sb.append(t.nextChar);
				cursor++;
			}
			else {
				sb.append(sb.substring(cursor - t.offset));
				cursor += t.length;
			}
			
		}
		
		sc.close();
		return sb.toString();
	}
	
	public int search(String text, int cursor, int next) {
		int startSearch = cursor - windowSize;
		//Ensure we can't start at a negative number
		if (startSearch < 0) {
			startSearch = 0;
		}
		//Whole text to search
		String searchText = text.substring(startSearch, cursor);
		
		//Ensure we cant search past the text
		if(cursor + next + 1 > text.length()) {
			return -1;
		}
		//Creating the substring 
		String searchTarget = text.substring(cursor, cursor + next + 1);
		
		//Using kmp to return me the index
		KMP k = new KMP(searchTarget, text);
		return k.search(searchTarget, searchText);

		//Array not initialising in KMP
		
		
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}
	
	/**
	 * Tuple class.
	 * @author Nathan
	 *
	 */
	private class Tuple {
		private int offset;
		private int length;
		private Character nextChar;
		
		//For encoding
		public Tuple(int offset, int length, Character cha) {
			this.offset = offset;
			this.length = length;
			this.nextChar = cha;
		}
		
		//For decoding
		public Tuple(String string) {
			String[] values = string.split("\\|");
			this.offset = Integer.parseInt(values[0]);
			this.length = Integer.parseInt(values[1]);
		}
	}
}
