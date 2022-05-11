package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame{
    private Set<String> words = new HashSet<>();
    private SortedSet<Character> guessedLetters = new TreeSet<>();
    private String key;

    public String getKey() {
        return key;
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        initializeDictionary(dictionary, wordLength);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            stringBuilder.append('_');
        }
        key = stringBuilder.toString();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        char guessChar = Character.toLowerCase(guess);
        if (guessedLetters.contains(guessChar)){
            throw new GuessAlreadyMadeException();
        } else { guessedLetters.add(guessChar); }
        Map<String, Set<String>> petitionGroup = new HashMap<>();
        for(String word: words) {
            String key = createKey(word, guess);
            if(!petitionGroup.containsKey(key)){
                petitionGroup.put(key, new HashSet<>());
            }
            petitionGroup.get(key).add(word);
        }
        petitionGroup = getLargestGroup(petitionGroup);
        return findTiebreaker(petitionGroup, guess);
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public void initializeDictionary(File dictionary, int wordLength) throws EmptyDictionaryException{
        words = new HashSet<>();
        try {
            Scanner scanner = new Scanner(dictionary);
            if (!scanner.hasNext()) {
                throw new EmptyDictionaryException();
            }
            while (scanner.hasNext()){
                String word = scanner.next();
                if (word.length() == wordLength){
                    words.add(word);
                }
            }
            if (words.size() == 0) {
                throw new EmptyDictionaryException();
            }
            if(scanner != null){
                scanner.close();
            }
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        }
    }

    private String createKey(String word, char guess){
        StringBuilder newKey = new StringBuilder();
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(c == guess){
                newKey.append(c);
            } else {
                newKey.append(key.charAt(i));
            }
        }
        return newKey.toString();
    }

    private Map<String, Set<String>> getLargestGroup(Map<String, Set<String>> groups){
        int largestGroup = 0;
        for(Set<String> group :groups.values()){
            largestGroup = group.size() > largestGroup ? group.size() : largestGroup;
        }

        Map<String, Set<String>> newMap = new HashMap<>();
        for(String key:groups.keySet()){
            Set<String> group = groups.get(key);
            if(group.size() == largestGroup){
                newMap.put(key, group);
            }
        }
        return newMap;
    }

    private Set<String> findTiebreaker(Map<String,Set<String>> input, char guess){
        int minimum = Integer.MAX_VALUE;
        for(String temp : input.keySet()){
            int weight = 1;
            int count = 0;
            int weighted = 0;
            int i;
            for(i = temp.length() - 1; i >= 0; --i){
                if(temp.charAt(i) == guess){
                    weighted += (++count * weight);
                }
                weight *= 2;
            }
            if(weighted < minimum){
                key = temp;
                minimum = weighted;
            }
        }
        words = input.get(key);
        return words;
    }
}
