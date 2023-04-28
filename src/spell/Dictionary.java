package spell;

public class Dictionary implements ITrie{

  private int wordCount;
  private int nodeCount = 1;
  private INode root = new TrieNode();// can you have several roots or is there just one root for the entire project??

  @Override
  public void add(String word) {
    // adds a string to the trie
    // this method will have to keep wordCount and nodeCount up to date.

    word = word.toLowerCase();

    var currentNode = root;

    for(int i = 0; i < word.length(); i++) {
      char letter= word.charAt(i);
      int indexOfLetter=letter - 'a'; // letter to index
//      System.out.println(indexOfLetter);
//      System.out.println("\n");

      if(currentNode.getChildren()[indexOfLetter] == null){ // if currentNode is empty
        currentNode.getChildren()[indexOfLetter]= new TrieNode(); // then we will create a new node
        currentNode = currentNode.getChildren()[indexOfLetter];
        nodeCount++;
        //System.out.println(nodeCount);
      }else{
        currentNode =currentNode.getChildren()[indexOfLetter];
      }
    }

    if(currentNode.getValue() == 0){
      wordCount++;
      //System.out.println(wordCount);
    }
    currentNode.incrementValue();
  }

  @Override
  public INode find(String word) {
    //letter = (char) ('a' + indexOfLetter); // going from index to letter
    // passes in a word and asks you if that word is in the dictionary
    // returns null if it is not found in the dictionary.

    word = word.toLowerCase();

    var currentNode = root;

    for(int i = 0; i < word.length(); i++) {
      char letter=word.charAt(i);
      int indexOfLetter=letter - 'a'; // letter to index

      if (currentNode.getChildren()[indexOfLetter] == null) {
        //System.out.println("didnt find word");
        return null;
      } else {
        currentNode=currentNode.getChildren()[indexOfLetter];
      }
    }

    if (currentNode.getValue() > 0){
      return currentNode;
    }else{
      return null;
    }
    //System.out.println("found the word");
  }

  @Override
  public int getWordCount() { // returns the number of unique nodes (non-zero numbers)
    return wordCount;
  }

  @Override
  public int getNodeCount() {  // return all the number of nodes that are in tree (including the ones that have zero counters)
    return nodeCount;
  }

  @Override
  public String toString() {   // return that has an alphabetized list of all the UNIQUE (only appears once) words in the trie.

    StringBuilder currWord = new StringBuilder();
    StringBuilder output = new StringBuilder();
    toString_helper(root, currWord, output);

    return output.toString();

  }

  private void toString_helper(INode aNode, StringBuilder currWord, StringBuilder output){ // recursive method

    if(aNode.getValue() > 0){
      output.append(currWord.toString()); // append the nodes word to the output
      output.append("\n");
    }

    for(int i = 0; i < aNode.getChildren().length; i++){
      INode childNode = aNode.getChildren()[i];

      if(childNode != null){
        char childLetter = (char)('a' + i); //  index to letter
        currWord.append(childLetter);

        toString_helper(childNode, currWord, output);

        currWord.deleteCharAt(currWord.length() - 1); // want to delete the last character in the currWord
        }
      }
    }

  @Override
  public boolean equals(Object obj) {

    if (obj == null) {
      return false;
    }

    if (obj == this) {
      return true;
    }

    if (obj.getClass() != this.getClass()) {
      return false;
    }

    Dictionary d = (Dictionary) obj;

    return equals_helper((TrieNode) this.root, (TrieNode) d.root);
  }

  private boolean equals_helper(TrieNode node1, TrieNode node2) {  // recursive method
    boolean is_equal=true;

    if (node1.getValue() != node2.getValue()) {
        return false;
    }

    for(int i = 0; i < 26; i++){
      if(node1.getChildren()[i] != null && node2.getChildren()[i] == null){
        return false;
      }if(node1.getChildren()[i] == null && node2.getChildren()[i] != null) {
        return false;
      }
    }
    for(int i = 0; i < 26; i++){
      if(node1.getChildren()[i] != null && node2.getChildren()[i] != null){
        is_equal = equals_helper((TrieNode) node1.getChildren()[i], (TrieNode) node2.getChildren()[i]);
      }

      if(!is_equal){
        return is_equal;
      }
    }
    return is_equal;
  }


  @Override
  public int hashCode() {
    var currentNode = root;
    int childrenOfRootNode = 0;

    for(int i = 0; i < root.getChildren().length; i++){
      if(root.getChildren()[i] != null){
        childrenOfRootNode = childrenOfRootNode + i;
      }
    }
    return (wordCount<<16 + nodeCount<<4) ^ childrenOfRootNode;
  }
}
