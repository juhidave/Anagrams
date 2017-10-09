package com.google.engedu.anagrams;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer,ArrayList<String>> sizeToWords;
    private ArrayList<String> wordList;
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        String sortedWord;
        int wordSize;
        lettersToWord = new HashMap<String, ArrayList<String>>();
        wordSet = new HashSet<String>();
        wordList = new ArrayList<String>();
        sizeToWords = new HashMap<Integer, ArrayList<String>>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            wordSize = word.length();
            sortedWord = sortLetters(word);
            if(sizeToWords.containsKey(wordSize)){
                ArrayList<String> anagramOfSize;
                anagramOfSize = sizeToWords.get(wordSize);
                anagramOfSize.add(word);
                sizeToWords.put(wordSize,anagramOfSize);
            }
            else{
                ArrayList<String> anagramOfSize = new ArrayList<String>();
                anagramOfSize.add(word);
                sizeToWords.put(wordSize,anagramOfSize);
            }
            if(lettersToWord.containsKey(sortedWord)){
                ArrayList<String> anagramSet;
                anagramSet = lettersToWord.get(sortedWord);
                anagramSet.add(word);
                lettersToWord.put(sortedWord,anagramSet);
            }
            else{
                ArrayList<String> anagramSet = new ArrayList<String>();
                anagramSet.add(word);
                lettersToWord.put(sortedWord,anagramSet);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(word.contains(base)){
            return false;
        }
        if(wordList.contains(word)){
            return true;
        }
        return false;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result;
        result = getAnagramsWithOneMoreLetter(targetWord);
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String newWord,sortedWord,tempWord;
        ArrayList<String> anagramSet;

        for(char i = 'a';i <= 'z';i++){
            newWord = word + i;
            sortedWord = sortLetters(newWord);

            if(lettersToWord.containsKey(sortedWord)){
                anagramSet = lettersToWord.get(sortedWord);
            }else {
                anagramSet = new ArrayList<String>();
            }
            for(int j = 0;j < anagramSet.size();j++){
                tempWord = anagramSet.get(j);
                if(isGoodWord(tempWord,word)){
                    result.add(tempWord);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String tempStartWord;
        int numberOfAnagram = 0;
        ArrayList<String> StarterWords = new ArrayList<String>();
        do{
            StarterWords = sizeToWords.get(wordLength);
            tempStartWord = StarterWords.get(random.nextInt(StarterWords.size()));
            numberOfAnagram = getAnagramsWithOneMoreLetter(tempStartWord).size();
        }while (numberOfAnagram <= MIN_NUM_ANAGRAMS);
        Log.d("test", "numberofAnagram: "+numberOfAnagram);
        if(wordLength <= MAX_WORD_LENGTH){
            wordLength++;
        }
        return tempStartWord;
    }

    public String sortLetters(String str){
        char[] temp = str.toCharArray();
        Arrays.sort(temp);
        return String.valueOf(temp);
    }
}