package hangman;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException {
        String dictionaryFile = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);
        Scanner scanner = new Scanner(System.in);
        Set<String> wordList = new HashSet<>();

        File dictionary = new File(dictionaryFile);
        EvilHangmanGame hangman = new EvilHangmanGame();
        hangman.startGame(dictionary, wordLength);
        String currentGuess = hangman.getKey();

        while(guesses > 0){
            System.out.println("You have " + guesses + " guesses left");
            System.out.println("Used letters: " + hangman.getGuessedLetters());
            System.out.println("Word: " + currentGuess);
            System.out.print("Enter guess: ");
            char guess = scanner.next().charAt(0);

            while(!Character.isAlphabetic(guess)){
                System.out.println("Invalid input");
                System.out.print("Enter guess: ");
                guess = scanner.next().charAt(0);
            }

            try {
                wordList = hangman.makeGuess(guess);
            } catch (GuessAlreadyMadeException err) {
                System.out.println("You already used that letter\n");
                continue;
            }

            if(hangman.getKey().equals(currentGuess)){
                System.out.println("Sorry there are no " + guess + "'s\n");
                guesses--;
            } else {
                int count = 0;
                currentGuess = hangman.getKey();
                for(char c:currentGuess.toCharArray()){
                    if(c == guess){
                        count++;
                    }
                }

                String response = count > 1 ? "are " + count + " " + guess + "'s" : "is 1 " + guess;
                if(!currentGuess.contains("_")){
                    System.out.println("You Win!\nThe word was: "+currentGuess);
                    scanner.close();
                    return;
                }
                System.out.println("Yes there " + response+ "\n");
            }
        }
        for(String word:wordList){
            currentGuess = word;
            break;
        }
        System.out.println("You lose!\nThe word was: "+currentGuess);

        scanner.close();
    }

}

//3 parameters: dictionary file name, word length, number of guesses

//The program loads the dictionary into a Set<String>
//Throws away all words not of the specified length

//word_set: contains all the possible words remaining
//word_set is initialized to all the words in the dictionary file of the correct length

//1. player guesses a letter (has to be a new letter)
//2. program partitions word_set relative to the guessed letter (divide the words based on where the guessed letter appears in the word)
//3. word_set is set equal to the largest subset of the partitions
//4. if the guess is in the new word_set, the positions are displayed.
//5. if the guess is NOT in the new word_set, the program notifies the player and decrements the number of remaining guesses.
//6. if the player completely fills the word, they win.
//7. if the player runs out of guesses, they lose.

//TIP: Do not create all possible subsets, because most of them are empty; create them as needed.
// The subsets should be a map.
// SubsetKey: Key value is the guessed letter pattern "a___ or _a_a" (string is probably the easiest way to do this.)
// Word - Probably just a string
// Map<SubsetKey, Set<Word>> partition (A map that keeps track of the words in each subset in the partition

// Function -> SubsetKey getSubsetKey(Word word, char guessedLetter)
// loop through the characters of the word. If char == guessedLetter, stay same; if char != guessedLetter, then char = "_" (underscore or blank)
// Add word to the appropriate subset in the partition map

//The largest subset in the partition map becomes the new word set for the next guess; if subsets are equal, use tiebreakers.