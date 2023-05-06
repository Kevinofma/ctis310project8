package edu.guilford;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        Scanner scanFile = null;
        Path dataLocation = null; // location of the data file
        LinkedList<String> wordList = new LinkedList<>(); // I used a linked list because i would be needing to add and
                                                          // remove a lot of elements to the list
        String fileName = null;

        fileName = "exampleEssay.txt";
        try {
            dataLocation = Paths.get(Driver.class.getResource("/" + fileName).toURI()); // get the path to the file
            FileReader dataFile = new FileReader(dataLocation.toString());
            BufferedReader dataBuffer = new BufferedReader(dataFile);
            scanFile = new Scanner(dataBuffer);
            wordList = readData(scanFile);
        } catch (URISyntaxException | FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }

        for (String word : wordList) {
            System.out.println(word);
        }
        System.out.println("Number of words: " + wordList.size());

        LinkedList<String> countedList = countDuplicateWords(wordList);
        System.out.println("Number of unique words: " + countedList.size());
        System.out.println("Unique words and their counts: ");

        countedList = sortOccurrences(countedList);
        System.out.println("Unique words and their counts sorted by number of occurrences: ");
        for (String entry : countedList) {
            System.out.println(entry);
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a word to search for: ");
        String word = scan.next();
        System.out.println("The word " + word + " occurs " + getWordCount(countedList, word) + " times.");
    }

    private static LinkedList<String> readData(Scanner scanFile) {
        LinkedList<String> wordList = new LinkedList<>();

        while (scanFile.hasNext()) {
            String word = scanFile.next();
            word = removePunctuation(word);
            if (!word.isEmpty()) {
                wordList.add(word.toLowerCase());
            }
        }

        return wordList;
    }

    // create a method that removes punctuation from a word
    private static String removePunctuation(String word) {
        StringBuilder sb = new StringBuilder();
        for (char ch : word.toCharArray()) {
            if (Character.isLetter(ch)) { // only creates a new string from letter characters
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    // create a method that counts the number of occurrences of each word in the list and returns a list of strings in the format "word count"
    public static LinkedList<String> countDuplicateWords(LinkedList<String> wordList) {
        LinkedList<String> countedList = new LinkedList<>();
    
        while (!wordList.isEmpty()) {
            String word = wordList.removeFirst();
            int count = 1;
    
            for (int i = 0; i < wordList.size(); i++) {
                String currentWord = wordList.get(i);
                if (currentWord.equals(word)) {
                    count++;
                    wordList.remove(i);
                    i--; // Adjust the index to account for the removed element
                }
            }
    
            countedList.add(word + " " + count);
        }
    
        return countedList;
    }

    // create a method that sorts the list of strings in the format "word count" by the number of occurrences
    public static LinkedList<String> sortOccurrences(LinkedList<String> countedList) {
        countedList.sort(new Comparator<String>() {
            @Override
            public int compare(String entry1, String entry2) {
                int count1 = getOccurrences(entry1);
                int count2 = getOccurrences(entry2);
                return Integer.compare(count2, count1);
            }
        });
        return countedList;
    }

    private static int getOccurrences(String entry) {
        return Integer.parseInt(entry.split(" ")[1]);
    }

    // create a method that returns the number of occurrences of a word in the list of strings in the format "word count"
    public static int getWordCount(LinkedList<String> countedList, String word) {
        int count = 0;
        for (String entry : countedList) {
            if (entry.split(" ")[0].equals(word)) {
                count = Integer.parseInt(entry.split(" ")[1]);
                break;
            }
        }
        return count;
    }
}