package passoff;

import hangman.EmptyDictionaryException;
import hangman.EvilHangmanGame;
import hangman.GuessAlreadyMadeException;
import hangman.IEvilHangmanGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HangmanTest {

    private IEvilHangmanGame studentGame;
    private static final String DICTIONARY = "dictionary.txt";
    private static final String SMALL_DICTIONARY = "small.txt";
    private static final String EMPTY_DICTIONARY = "empty.txt";

    @BeforeEach
    @DisplayName("Setting up Evil Hangman Game (calling the empty constructor)")
    void setup(){
        try{
            studentGame = new EvilHangmanGame();
        }
        catch(Throwable t){
            fail(t.getClass() + ". Make sure class name is hangman.EvilHangmanGame");
        }
    }

    @Test
    @DisplayName("Loading Empty File Test")
    void testEmptyFileLoad(){
        Assertions.assertThrows(EmptyDictionaryException.class, ()-> studentGame.startGame(new File(EMPTY_DICTIONARY), 4), "Failed to throw EmptyDictionaryException.");
        Assertions.assertThrows(EmptyDictionaryException.class, ()-> studentGame.startGame(new File(DICTIONARY), 1), "Failed to throw EmptyDictionaryException.");
        Assertions.assertThrows(EmptyDictionaryException.class, ()-> studentGame.startGame(new File(SMALL_DICTIONARY), 15), "Failed to throw EmptyDictionaryException.");
    }

    @Test
    @DisplayName("Loading File With Word Length 0 Test")
    void testWordLength0(){
        try{
            Assertions.assertThrows(EmptyDictionaryException.class, ()-> studentGame.startGame(new File(DICTIONARY), 0), "Failed to throw EmptyDictionaryException.");
        }
        catch(Throwable e){
            fail("Loading file with word length 0 threw: " + e.getClass());
        }
    }

    @Test
    @DisplayName("Loading File Tests")
    void testLoadFiles(){
        try{
            studentGame.startGame(new File(DICTIONARY), 2);
            studentGame.startGame(new File(DICTIONARY), 10);
            studentGame.startGame(new File(SMALL_DICTIONARY), 10);
        }
        catch(Throwable e) {
            fail("Loading file with dictionary threw: " + e.getClass());
        }
    }

    @Test
    @DisplayName("Guess Already Made Test")
    void testGuessAlreadyMade(){
        try {
            studentGame.startGame(new File(DICTIONARY), 2);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        try{
            studentGame.makeGuess('a');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        Assertions.assertThrows(GuessAlreadyMadeException.class, ()-> studentGame.makeGuess('a'), "Failed to throw GuessAlreadyMadeException.");
        Assertions.assertThrows(GuessAlreadyMadeException.class, ()-> studentGame.makeGuess('A'), "Failed to throw GuessAlreadyMadeException with uppercase letter.");

        try{
            studentGame.makeGuess('E');
        }
        catch(Throwable e){
            fail("Guessing a letter after a previously guessed letter threw: " + e.getClass());
        }
        Assertions.assertThrows(GuessAlreadyMadeException.class, ()-> studentGame.makeGuess('E'), "Failed to throw GuessAlreadyMadeException with uppercase letter.");
        Assertions.assertThrows(GuessAlreadyMadeException.class, ()-> studentGame.makeGuess('a'), "Failed to throw GuessAlreadyMadeException with previously guessed letter.");
    }

    @Test
    @DisplayName("2 Letter Word Test")
    void test2LetterWord(){
        try {
            studentGame.startGame(new File(DICTIONARY), 2);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        Set<String> possibleWords = new HashSet<>();
        try{
             possibleWords = studentGame.makeGuess('a');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(68, possibleWords.size(), "Incorrect set size.");
        String[] correctPossibilities = {"be","bi","bo","by","de","do","ef","eh","el","em","en","er","es","et","ex","go","he","hi","hm","ho","id","if","in","is","it","jo","li","lo","me","mi","mm","mo","mu","my","ne","no","nu","od","oe","of","oh","om","on","op","or","os","ow","ox","oy","pe","pi","re","sh","si","so","ti","to","uh","um","un","up","us","ut","we","wo","xi","xu","ye"};
        assertTrue(possibleWords.containsAll(Arrays.asList(correctPossibilities)), "Incorrect set contents after 1 guess");

        try{
            possibleWords = studentGame.makeGuess('a');
        }
        catch(GuessAlreadyMadeException e){
        }
        assertEquals(68, possibleWords.size(), "Incorrect set size after duplicate guess.");
        assertTrue(possibleWords.containsAll(Arrays.asList(correctPossibilities)),"Incorrect set contents after duplicate guess");

        try{
            possibleWords = studentGame.makeGuess('e');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(49, possibleWords.size(), "Incorrect set size after second guess.");
        String[] newCorrectPossibilities = {"bi","bo","by","do","go","hi","hm","ho","id","if","in","is","it","jo","li","lo","mi","mm","mo","mu","my","no","nu","od","of","oh","om","on","op","or","os","ow","ox","oy","pi","sh","si","so","ti","to","uh","um","un","up","us","ut","wo","xi","xu"};
        assertTrue(possibleWords.containsAll(Arrays.asList(newCorrectPossibilities)),"Incorrect set contents after second guess");
    }

    @Test
    @DisplayName("3 Letter Word Test")
    void test3LetterWord(){
        try {
            studentGame.startGame(new File(DICTIONARY), 3);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        Set<String> possibleWords = new HashSet<>();
        try{
            possibleWords = studentGame.makeGuess('a');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(665, possibleWords.size(), "Incorrect size after 1 guess");
        assertFalse(possibleWords.toString().contains("a"), "Incorrect contents after 1 guess");

        try{
            possibleWords = studentGame.makeGuess('o');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(456, possibleWords.size(), "Incorrect size after 2nd guess");
        assertFalse(possibleWords.toString().contains("o"), "Incorrect contents after 2nd guess");

        try{
            possibleWords = studentGame.makeGuess('e');
            possibleWords = studentGame.makeGuess('u');
            possibleWords = studentGame.makeGuess('i');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(110, possibleWords.size(), "Incorrect size after 5th guess");
        assertFalse(possibleWords.toString().contains("e"), "Incorrect contents after 5th guess");
        assertFalse(possibleWords.toString().contains("u"), "Incorrect contents after 5th guess");
        assertTrue(possibleWords.toString().contains("i"), "Incorrect contents after 5th guess");
        assertTrue(possibleWords.contains("bib"), "Incorrect contents after 5th guess");
        assertTrue(possibleWords.contains("fix"), "Incorrect contents after 5th guess");
        assertTrue(possibleWords.contains("zit"), "Incorrect contents after 5th guess");
    }

    @Test
    @DisplayName("10 Letter Word Test")
    void test10letterWord(){
        try {
            studentGame.startGame(new File(DICTIONARY), 10);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        Set<String> possibleWords = new HashSet<>();
        try{
            possibleWords = studentGame.makeGuess('t');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(5395, possibleWords.size(), "Incorrect size after 1 guess");
        assertFalse(possibleWords.toString().contains("t"), "Incorrect contents after 1 guess");

        try{
            possibleWords = studentGame.makeGuess('e');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(1091, possibleWords.size(), "Incorrect size after 2nd guess");
        assertTrue(possibleWords.contains("airmailing"), "Incorrect contents after 2nd guess");
        assertTrue(possibleWords.contains("micrograms"), "Incorrect contents after 2nd guess");
        assertTrue(possibleWords.contains("signalling"), "Incorrect contents after 2nd guess");
        assertFalse(possibleWords.toString().contains("e"), "Incorrect contents after 2nd guess");

        try{
            possibleWords = studentGame.makeGuess('a');
            possibleWords = studentGame.makeGuess('i');
            possibleWords = studentGame.makeGuess('r');
            possibleWords = studentGame.makeGuess('s');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(24, possibleWords.size(), "Incorrect size after 6th guess");
        assertFalse(possibleWords.toString().contains("a"), "Incorrect contents after 6th guess");
        assertTrue(possibleWords.contains("conglobing"), "Incorrect contents after 6th guess");
        assertTrue(possibleWords.contains("flummoxing"), "Incorrect contents after 6th guess");
        assertTrue(possibleWords.contains("unmuzzling"), "Incorrect contents after 6th guess");

        try{
            possibleWords = studentGame.makeGuess('u');
            possibleWords = studentGame.makeGuess('c');
            possibleWords = studentGame.makeGuess('p');
            possibleWords = studentGame.makeGuess('f');
        }
        catch(Throwable e){
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(1, possibleWords.size(), "Incorrect size after 10th guess");
        assertTrue(possibleWords.contains("hobnobbing"), "Incorrect contents after 10th guess");
    }

    @Test
    @DisplayName("Largest Group Test")
    void testLargestGroup() {
        try {
            studentGame.startGame(new File(SMALL_DICTIONARY), 6);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        Set<String> possibleWords = new HashSet<>();
        try {
            possibleWords = studentGame.makeGuess('u');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(5, possibleWords.size(), "Incorrect size after 1 guess");
        assertFalse(possibleWords.contains("chubby"), "Incorrect contents after 1 guess");

        try {
            possibleWords = studentGame.makeGuess('l');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(3, possibleWords.size(), "Incorrect size after 2nd guess");
        assertFalse(possibleWords.contains("little"), "Incorrect contents after 2nd guess");
        assertFalse(possibleWords.contains("nickle"), "Incorrect contents after 2nd guess");

        try {
            possibleWords = studentGame.makeGuess('s');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(2, possibleWords.size(), "Incorrect size after 3rd guess");
        assertFalse(possibleWords.contains("editor"), "Incorrect contents after 3rd guess");
        assertTrue(possibleWords.contains("brakes"), "Incorrect contents after 3rd guess");
        assertTrue(possibleWords.contains("chicks"), "Incorrect contents after 3rd guess");
    }

    @Test
    @DisplayName("Letter Does Not Appear Test")
    void testLetterDoesNotAppear() {
        try {
            studentGame.startGame(new File(SMALL_DICTIONARY), 5);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        Set<String> possibleWords = new HashSet<>();
        try {
            possibleWords = studentGame.makeGuess('a');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(4, possibleWords.size(), "Incorrect size after 1 guess");
        assertFalse(possibleWords.contains("lambs"), "Incorrect contents after 1 guess");
        assertFalse(possibleWords.contains("lakes"), "Incorrect contents after 1 guess");
        assertTrue(possibleWords.contains("toner"), "Incorrect contents after 1 guess");

        try {
            possibleWords = studentGame.makeGuess('o');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(2, possibleWords.size(), "Incorrect size after 2nd guess");
        assertFalse(possibleWords.contains("tombs"), "Incorrect contents after 2nd guess");
        assertFalse(possibleWords.contains("toner"), "Incorrect contents after 2nd guess");
        assertTrue(possibleWords.contains("title"), "Incorrect contents after 2nd guess");
        assertTrue(possibleWords.contains("silly"), "Incorrect contents after 2nd guess");

        try {
            possibleWords = studentGame.makeGuess('t');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(1, possibleWords.size(), "Incorrect size after 3rd guess");
        assertFalse(possibleWords.contains("title"), "Incorrect contents after 3rd guess");
        assertTrue(possibleWords.contains("silly"), "Incorrect contents after 3rd guess");
    }

    @Test
    @DisplayName("Pattern With Fewest Instances Test")
    void testFewestInstances() {
        try {
            studentGame.startGame(new File(SMALL_DICTIONARY), 7);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        Set<String> possibleWords = new HashSet<>();
        try {
            possibleWords = studentGame.makeGuess('z');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(2, possibleWords.size(), "Incorrect size after 1 guess");
        assertFalse(possibleWords.contains("zyzzyva"), "Incorrect contents after 1 guess");
        assertFalse(possibleWords.contains("zizzled"), "Incorrect contents after 1 guess");
        assertTrue(possibleWords.contains("buzzwig"), "Incorrect contents after 1 guess");


        try {
            studentGame.startGame(new File(SMALL_DICTIONARY), 8);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        possibleWords = new HashSet<>();
        try {
            possibleWords = studentGame.makeGuess('e');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(4, possibleWords.size(), "Incorrect size after 1 guess");
        assertFalse(possibleWords.contains("bythelee"), "Incorrect contents after 1 guess");
        assertFalse(possibleWords.contains("dronebee"), "Incorrect contents after 1 guess");
        assertFalse(possibleWords.contains("parmelee"), "Incorrect contents after 1 guess");
        assertFalse(possibleWords.contains("tuskegee"), "Incorrect contents after 1 guess");
        assertTrue(possibleWords.contains("gardened"), "Incorrect contents after 1 guess");
        assertTrue(possibleWords.contains("forgemen"), "Incorrect contents after 1 guess");
        assertTrue(possibleWords.contains("lingerer"), "Incorrect contents after 1 guess");
        assertTrue(possibleWords.contains("ohmmeter"), "Incorrect contents after 1 guess");
    }

    @Test
    @DisplayName("Pattern With Rightmost Instances Test")
    void testRightmostLetter() {
        try {
            studentGame.startGame(new File(SMALL_DICTIONARY), 3);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        Set<String> possibleWords = new HashSet<>();
        try {
            possibleWords = studentGame.makeGuess('a');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(2, possibleWords.size(), "Incorrect word count after 1st guess.");
        assertFalse(possibleWords.contains("abs"), "Incorrect content after 1st guess.");
        assertFalse(possibleWords.contains("are"), "Incorrect content after 1st guess.");
        assertFalse(possibleWords.contains("bar"), "Incorrect content after 1st guess.");
        assertFalse(possibleWords.contains("tag"), "Incorrect content after 1st guess.");
        assertTrue(possibleWords.contains("bra"), "Incorrect content after 1st guess.");
        assertTrue(possibleWords.contains("moa"), "Incorrect content after 1st guess.");

        try {
            studentGame.startGame(new File(SMALL_DICTIONARY), 12);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        possibleWords = new HashSet<>();
        try {
            possibleWords = studentGame.makeGuess('h');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(2, possibleWords.size(), "Incorrect word count after 1st guess.");
        assertFalse(possibleWords.contains("charmillions"), "Incorrect content after 1st guess.");
        assertFalse(possibleWords.contains("phylogenesis"), "Incorrect content after 1st guess.");
        assertTrue(possibleWords.contains("antimonarchy"), "Incorrect content after 1st guess.");
        assertTrue(possibleWords.contains("boxingweight"), "Incorrect content after 1st guess.");
    }

    @Test
    @DisplayName("Rightmost Instance of Multiple Instances Test")
    void testRightmostOfMultipleInstances(){
        try {
            studentGame.startGame(new File(SMALL_DICTIONARY), 9);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        Set<String> possibleWords = new HashSet<>();
        try {
            possibleWords = studentGame.makeGuess('g');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(2, possibleWords.size(), "Incorrect word count after 1st guess");
        assertFalse(possibleWords.contains("huggingly"), "Incorrect contents after 1st guess");
        assertFalse(possibleWords.contains("legginged"), "Incorrect contents after 1st guess");
        assertFalse(possibleWords.contains("dugogogue"), "Incorrect contents after 1st guess");
        assertFalse(possibleWords.contains("logogogue"), "Incorrect contents after 1st guess");
        assertTrue(possibleWords.contains("foglogged"), "Incorrect contents after 1st guess");
        assertTrue(possibleWords.contains("pigwiggen"), "Incorrect contents after 1st guess");

        try {
            studentGame.startGame(new File(SMALL_DICTIONARY), 10);
        } catch (IOException e) {
            fail("Start game threw IOException");
        } catch (EmptyDictionaryException e) {
            fail("Dictionary that contains words is counted as empty");
        }
        possibleWords = new HashSet<>();
        try {
            possibleWords = studentGame.makeGuess('t');
        } catch (Throwable e) {
            fail("Making a guess threw: " + e.getClass());
        }
        assertEquals(2, possibleWords.size(), "Incorrect word count after 1st guess");
        assertFalse(possibleWords.contains("thelittleo"), "Incorrect contents after 1st guess");
        assertFalse(possibleWords.contains("teakettles"), "Incorrect contents after 1st guess");
        assertFalse(possibleWords.contains("titeration"), "Incorrect contents after 1st guess");
        assertFalse(possibleWords.contains("tetrastoon"), "Incorrect contents after 1st guess");
        assertTrue(possibleWords.contains("triacetate"), "Incorrect contents after 1st guess");
        assertTrue(possibleWords.contains("tennantite"), "Incorrect contents after 1st guess");
    }

}
