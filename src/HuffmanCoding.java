import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

import com.sun.prism.image.CompoundTexture;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {
	
	private HuffmanTree Tree;
	private HashMap<Character, Integer> FreqMap;
	private HashMap<Character, String> PrefixCodes;
	
	public HuffmanCoding(String text) {
		this.FreqMap = getFreq(text.toCharArray());
		this.Tree = buildHuffmanTree(FreqMap);
		this.PrefixCodes = generatePrefixFreeCode(Tree);
		
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		//Using a char array StringBuilder because it runs faster than the string constructor
		char[] characters = text.toCharArray();
		StringBuilder binaryText = new StringBuilder();
		for(char c : characters) {
			binaryText.append(PrefixCodes.get(c));
		}
		return binaryText.toString();
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		//Using a char array StringBuilder because it runs faster than the string constructor
		char[] characters = encoded.toCharArray();
		StringBuilder decodedText = new StringBuilder();
		HuffmanNode root, pointer;
		
		//Traverse the tree in the order of the encoded string
		pointer = root = Tree.getRoot();
		for(int i=0; i<characters.length; i++) {
			char textPointer = characters[i];
			if(textPointer == '0') {
				//Get the left node
				pointer = pointer.getLeft();
				if(pointer.getLeft() == null) {
					//Add to string and back to root
					decodedText.append(pointer.getChar());
					pointer = root;
				}
			}
			else if(textPointer == '1') {
				//Get the right node
				pointer = pointer.getRight();
				if(pointer.getLeft() == null) {
					decodedText.append(pointer.getChar());
					pointer = root;
				}
			}
		}
		return decodedText.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
	}
	
	private class HuffmanTree {
		private HuffmanNode root;
		
		public HuffmanTree(HuffmanNode root) {
			this.root = root;
		}
		
		public HuffmanNode getRoot() {
			return this.root;
		}
	}
	
	/**
	 * 
	 * @param ch array of char containing all the text
	 * @return HashMap where all characters and values is a frequency
	 */
	private HashMap<Character, Integer> getFreq(char[] ch){
		HashMap<Character, Integer> freq = new HashMap<Character, Integer>();
		for(char c : ch) {
			if (freq.containsKey(c)) {
				freq.put(c, freq.get(c) + 1);
			}
			else {
				freq.put(c, 1);
			}
		}
		return freq;
	}
	
	/**
	 * 
	 * @param freq -- frequency array of the text
	 * @return HuffmanTree 
	 */
	private HuffmanTree buildHuffmanTree(HashMap<Character, Integer> freq) {
		PriorityQueue<HuffmanNode> nodeQueue = new PriorityQueue<HuffmanNode>();
		//adding the leaves into the priority queue
		for(char c : freq.keySet()) {
			HuffmanNode node = new HuffmanNode(c);
			node.setFrequency(freq.get(c));
			nodeQueue.offer(node);
		}
		
		//Iterate through the leaves and build up
		for(int i=1; i<nodeQueue.size(); i++) {
			HuffmanNode huffNode1 = nodeQueue.poll();
			HuffmanNode huffNode2 = nodeQueue.poll();
			HuffmanNode parent = new HuffmanNode('\0');
			parent.setFrequency(huffNode1.getFrequency() + huffNode2.getFrequency());
			parent.setLeft(huffNode1);
			parent.setRight(huffNode2);
			huffNode1.setParent(parent);
			huffNode2.setParent(parent);
			nodeQueue.offer(parent);
		}
		
		//Poll last node and return
		return new HuffmanTree(nodeQueue.poll());
	}

	/**
	 * Creates the prefix free code trees
	 * @param hTree The huffman tree
	 * @return 
	 */
	private HashMap<Character, String> generatePrefixFreeCode(HuffmanTree hTree){
		//Traverse through the tree and assign each node a code
		HashMap<Character, String> prefix = new HashMap<Character, String>();
		Stack<HuffmanNode> hStack = new Stack<HuffmanNode>();
		hStack.push(hTree.getRoot());
		
		while(!hStack.isEmpty()) {
			HuffmanNode node = hStack.pop();
			HuffmanNode left = node.getLeft();
			HuffmanNode right = node.getRight();
			
			if(left != null) {
				//Sets the prefix code of the children (left = parent0) and (right = parent1)
				//Compression 1/2 P13
				left.setPrefix(node.getPrefix() + '0');
				hStack.push(left);
				right.setPrefix(node.getPrefix() + '1');
				hStack.push(right);
			}
			else {
				prefix.put(node.getChar(), node.getPrefix());
			}
		}
		return prefix;
	}
	
	private class HuffmanNode implements Comparable<HuffmanNode>{
		
		private char character;
		private int frequency;
		private String prefix;
		private HuffmanNode parent;
		private HuffmanNode left;
		private HuffmanNode right;
		
		public HuffmanNode(char c) {
			this.character = c;
			frequency = 0;
			prefix = "";
		}
		
		@Override
		public int compareTo(HuffmanNode other) {
			return this.frequency - other.frequency;
		}
		
		public int getFrequency() {
			return this.frequency;
		}
		
		public void setFrequency(int f) {
			this.frequency = f;
		}
		
		public char getChar() {
			return this.character;
		}
		
		public void setParent(HuffmanNode parent) {
			this.parent = parent;
		}
		
		public void setLeft(HuffmanNode left) {
			this.left = left;
		}
		
		public HuffmanNode getLeft() {
			return this.left;
		}
		
		public void setRight(HuffmanNode right) {
			this.right = right;
		}
		
		public HuffmanNode getRight() {
			return this.right;
		}
		
		public void setPrefix(String s) {
			this.prefix = s;
		}
		
		public String getPrefix() {
			return this.prefix;
		}
		
	}
}
