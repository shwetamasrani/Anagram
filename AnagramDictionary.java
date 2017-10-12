/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private ArrayList wordList = new ArrayList<String>();
    private static int wordLength = DEFAULT_WORD_LENGTH;
    public ArrayList<String> dic = new ArrayList<>();
    public ArrayList<String> wordMapList = new ArrayList<String>();
    public HashSet<String> wordSet = new HashSet<String>();
    public HashMap<String, ArrayList> lettersToWord = new HashMap<String, ArrayList>();
    HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private Random random = new Random();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            if (sizeToWords.containsKey(word.length())) {
                wordMapList = sizeToWords.get(word.length());
                wordMapList.add(word);
                // sizeToWords.put(word.length(), wordMapList);
            } else {
                ArrayList<String> newWordList = new ArrayList<>();
                newWordList.add(word);
                sizeToWords.put(word.length(), newWordList);
            }

            if (lettersToWord.containsKey(sorted(word))) {
                dic = lettersToWord.get(sorted(word));
                dic.add(word);
            } else {
                dic = new ArrayList<String>();
                dic.add(word);
                lettersToWord.put(sorted(word), dic);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word) && !(word.contains(base)))
            return true;
        else
            return false;
    }

    public String sorted(String w) {
        char w1[] = w.toCharArray();
        Arrays.sort(w1);
        return new String(w1);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        Iterator iter = wordSet.iterator();
        while (iter.hasNext()) {
            String s = iter.next().toString();
            if ((sorted(s).compareTo(sorted(targetWord)) == 0) && !(s.equals(targetWord))) {
                //String ana=new String(iter.next());
                result.add(s);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> resultant;
        ArrayList<String> result = new ArrayList<String>();
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String newWord = word + alphabet;
            String extendedKey = sorted(newWord);
            if (lettersToWord.containsKey(extendedKey) && isGoodWord(word, newWord)) {
                resultant = new ArrayList();
                resultant = (ArrayList) lettersToWord.get(extendedKey);
                for (int i = 0; i < resultant.size(); i++)
                    result.add(String.valueOf(resultant.get(i)));
            }

        }
        return result;
    }

    public String pickGoodStarterWord()

    {
        String tempStartWord = null;
        String sortTempStartWord = null;
        int numberOfAnagram = 0;
        ArrayList<String> StarterWords = new ArrayList<String>();
        do{
            StarterWords = sizeToWords.get(wordLength);
            tempStartWord = StarterWords.get(random.nextInt(StarterWords.size()));

            numberOfAnagram = getAnagramsWithOneMoreLetter(tempStartWord).size();


        }while (numberOfAnagram <= MIN_NUM_ANAGRAMS);
        if(wordLength <= MAX_WORD_LENGTH){
            wordLength++;
        }
        return tempStartWord;
    }

}
