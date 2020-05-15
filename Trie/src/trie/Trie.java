package trie;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class implements a Trie. 
 *
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root = new TrieNode(null, null, null);
		if(allWords == null) {
			return root;
		}
		else {
			root.firstChild = new TrieNode(new Indexes(0, (short) 0, (short)(allWords[0].length()-1)), null, null);
			for(int i = 1; i < allWords.length; i++) {
				TrieNode prev = root.firstChild, curr = root.firstChild;
				String temp = allWords[i];
				int first = -1, last = -1, current = -1, word = -1;
		while(curr != null) {
		  first = curr.substr.startIndex;
          last = curr.substr.endIndex;
          word = curr.substr.wordIndex;
		if(first > temp.length()){
            prev = curr;
            curr = curr.sibling;
          }
          current = commonPrefixes(allWords[word].substring(first, last+1), temp.substring(first));
          if(current != -1){
            current += first;
          }
          if(current == -1){
            prev = curr;
            curr = curr.sibling;
          }
          else{
            if(current == last){
              prev = curr;
              curr = curr.firstChild;
            }
            else if(current < last){
              prev = curr;
              break;
            }
          }
				}
		if(curr == null) {
            Indexes butthead = new Indexes(i, (short)first, (short)(temp.length()-1));                
            prev.sibling = new TrieNode(butthead, null, null);
          }
              else {
              Indexes poop = prev.substr; 
              TrieNode orphan = prev.firstChild;
              Indexes beavis = new Indexes(poop.wordIndex, (short) (current+1), poop.endIndex);
              poop.endIndex = (short) current;
              prev.firstChild = new TrieNode(beavis, null, null);
              prev.firstChild.firstChild = orphan;
              prev.firstChild.sibling = new TrieNode(new Indexes(i, (short) (current+1), (short) (temp.length()-1)), null, null);
          }
		}
	}
	return root;
}

private static int commonPrefixes(String temp, String temp2) {
		int count = 0;
  	while((count < temp.length()) && (count < temp2.length()) && (temp.charAt(count) == temp2.charAt(count))){
      count++;
    }
  	return count-1;
}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION

	
		ArrayList<TrieNode> completed = new ArrayList<TrieNode>();
		if(prefix.length() == 0) {
			return retrieveSubstrings(root, allWords, prefix);
		}
		if(root.substr == null) {
			if(root.firstChild == null) {
				return null;
			}
			else {
				return completionList(root.firstChild, allWords, prefix);
			}
		}
		int prefixnumbers = prefixCheck(root, prefix, allWords);
		if(prefixnumbers == -1) {
			if(root.sibling == null) {
				return null;
			}else {
				return completionList(root.sibling, allWords, prefix);
			}
		}
		if(prefixnumbers == 1) {
			if(root.firstChild != null) {
				completed.addAll(retrieveSubstrings(root.firstChild, allWords, prefix));
			}else {
				completed.add(root);
			}
		}
		if(prefixnumbers == 2) {
			if(root.firstChild == null) {
				return null;
			}
			return completionList(root.firstChild, allWords, prefix);
		}
		if(completed.isEmpty()) {
			return null;
		}
		else {
			return completed;
		}
	}
	
	private static ArrayList<TrieNode> retrieveSubstrings(TrieNode ptr, String[] allWords, String prefix){
		ArrayList<TrieNode> templist = new ArrayList<TrieNode>();
		if(ptr.sibling != null) {
			templist.addAll(retrieveSubstrings(ptr.sibling, allWords, prefix));
		}
		if(ptr.firstChild != null) {
			templist.addAll(retrieveSubstrings(ptr.firstChild, allWords, prefix));
		}else {
			templist.add(ptr);
			return templist;
		}
		return templist;
	}
	
	private static int prefixCheck(TrieNode node, String prefix, String[] allWords){
		String nodeWord = allWords[node.substr.wordIndex];
		int endIndex = node.substr.endIndex;

		if (prefix.equals(nodeWord.substring(0, Math.min(endIndex+1, prefix.length())))) {
			
			return 1;
		}else {
			if (prefix.length()-1 > endIndex) {
				if ((prefix.substring(0, endIndex+1).equals(nodeWord.substring(0, endIndex+1)))) {
					return 2;
				}
			}
			return -1;
		}
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
