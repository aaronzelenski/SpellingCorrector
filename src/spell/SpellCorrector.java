package spell;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class SpellCorrector implements ISpellCorrector {
  public HashMap<String,Integer> myMap = new HashMap<>();
  public Dictionary myDictionary = new Dictionary();

  @Override
  public void useDictionary(String dictionaryFileName) throws IOException {

    File dictionary = new File(dictionaryFileName);

    Scanner scan = new Scanner(dictionary);


    while(scan.hasNext()) {

      String myWord = scan.next().toLowerCase();

      myDictionary.add(myWord);
      if(myMap.containsKey(myWord)) {
        myMap.put(myWord, myMap.get(myWord)+1);
      }
      else {
        myMap.put(myWord, 1);
      }
    }

    scan.close();

  }

  @Override
  public String suggestSimilarWord(String inputWord) {

    inputWord = inputWord.toLowerCase();

    Set<String> editDistance1 = editDistance1(inputWord);
    Set<String> editDistance2 = new HashSet<>();
    HashMap<String, Integer> wordMatches = new HashMap<>();


    if(myDictionary.find(inputWord) != null && (myDictionary.find(inputWord).getValue() > 0) ) { // if find is not null that means that the word is there and we can return it I think??
      return inputWord;
    }

    int maxCount = 0;
    for (String s : editDistance1) {
      INode myNode = myDictionary.find(s);
      if(myNode != null && (myNode.getValue() > 0)) {
        wordMatches.put(s, myNode.getValue());
        if(myNode.getValue() > maxCount) {
          maxCount = myNode.getValue();
        }
      }
    }

    // EDIT DISTANCE 2 WORDS

    if(wordMatches.size() == 0) {
      for(String s : editDistance1) {
        editDistance2.addAll(editDistance1(s));
      }

      for(String s : editDistance2) {
        INode myNode = myDictionary.find(s);
        if(myNode != null && (myNode.getValue() > 0)) {
          wordMatches.put(s, myNode.getValue());
          if(myNode.getValue() > maxCount) {
            maxCount = myNode.getValue();
          }
        }
      }
    }

    if(wordMatches.size() == 0) {
      return null;
    }



    ArrayList<String> maxCounts = new ArrayList<>();
    for (Map.Entry<String,Integer> m : wordMatches.entrySet() ) {
      if(m.getValue() == maxCount) {
        maxCounts.add(m.getKey());
      }
    }
    Collections.sort(maxCounts);

    return maxCounts.get(0);

  }

  public Set<String> editDistance1(String inputWord) {
    Set<String> possibleWords = new HashSet<>();

    for(int i = 0; i < inputWord.length(); i++){
      StringBuilder editedInput = new StringBuilder(inputWord);
      editedInput.deleteCharAt(i);

      String finalOutput = editedInput.toString();
      possibleWords.add(finalOutput);
    }


    for(int i = 0; i < inputWord.length()-1; i++){
      StringBuilder editedInput = new StringBuilder(inputWord);

      editedInput.setCharAt(i, inputWord.charAt(i+1));
      editedInput.setCharAt(i+1, inputWord.charAt(i));

      String finalOutput = editedInput.toString();
      possibleWords.add(finalOutput);
    }

    int lengthOfAlphabet = 26;
    for(int i = 0; i < inputWord.length(); i++) {
      for(int j = 0; j < lengthOfAlphabet; j++) {

        char newChar = (char)('a' + j);
        String finalOutput =inputWord.substring(0, i) + newChar + inputWord.substring(i + 1);
        possibleWords.add(finalOutput);
      }
    }

    for(int i = 0; i <= inputWord.length(); i++){
      for(int j = 0; j < lengthOfAlphabet; j++){
        StringBuilder editedInput = new StringBuilder(inputWord);
        char childLetter = (char)('a' + j);

        editedInput.insert(i, childLetter);
        String finalOutput = editedInput.toString();
        possibleWords.add(finalOutput);
      }
    }

    return possibleWords;
  }

}