/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author bensauberman
 */
public class Boggle {
    public static int checkCount = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        ArrayList<String> dictionary = new ArrayList<String>();
        addDictionary(dictionary);
        //TESTING
        ArrayList<String> foundWords = new ArrayList<String>();
        ArrayList<Integer> foundPoints = new ArrayList<Integer>();
        //System.out.print(binarySearch("MIND", dictionary, 0, dictionary.size(), foundWords));
        System.out.println("Please enter the amount of rows/columns you would like the board to be. Standard is 4.");
        int size = 4;
        Scanner s = new Scanner(System.in);
        try {
            size = s.nextInt();
        } catch(InputMismatchException e) {}
        
        System.out.println("Enter the board below from top row to bottom row.");
        Letter[][] board = new Letter[size][size];
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                board[x][y] = new Letter();
            }
        }
        enterBoard(board, size);
        ArrayList<Letter> word = new ArrayList<Letter>();
        
        for(int r = 0; r < size; r++) {
            for(int c = 0; c < size; c++) {
                if(!board[r][c].getUsedAsFirst()) {
                    word.add(board[r][c]);
                    board[r][c].setUsedAsFirst(true);
                    board[r][c].setAvailable(false);
                    findWords(word, board, dictionary, 0, foundWords, foundPoints);                                      
                }
            }
        }
        
        /*for(int x = 0; x < foundWords.size(); x++) {
            if(!hasVowels(foundWords.get(x))) {
                foundWords.remove(x);
                foundPoints.remove(x);
                x--;
            } else {
            }
        }*/
        
        for(int i = 0; i < foundWords.size(); i++) {
            for(int j = i; j < foundWords.size(); j++) {
                    if(foundPoints.get(i) < foundPoints.get(j)){
                            int temp = foundPoints.get(i);
                            foundPoints.set(i, foundPoints.get(j));
                            foundPoints.set(j, temp);
                            
                            String temp2 = foundWords.get(i);
                            foundWords.set(i, foundWords.get(j));
                            foundWords.set(j, temp2);
                    }
            }
        }
        
        displayBoard(board);
        int totalPoints = 0;
        for(int x = 0; x < foundPoints.size(); x++) {
            totalPoints += foundPoints.get(x);
        }
        System.out.println("Total Checks: " + checkCount);
        System.out.println("Total Words: " + foundWords.size());
        System.out.println("Total Points: " + totalPoints);
        for(int x = 0; x < foundWords.size(); x++) {
            System.out.println("Points: " + foundPoints.get(x) + " for " + foundWords.get(x));
        }

    }
    
    public static void enterBoard(Letter[][] board, int size) throws FileNotFoundException {
        System.out.println("Would you like to import the board using the File or a Scanner or Random Generator?\n1:File\n2:Scanner\n3:Random Generator");
        Scanner sc = new Scanner(System.in);
        String entered = sc.next();
        if(entered.equals("1")) {
            File file = new File("/Users/bensauberman/Desktop/CS/AP CS/SampleBoggleBoard.txt");
            Scanner s = new Scanner(file);
            while(s.hasNextLine()) {
                String letter = s.next();
                int val = pointValues(letter);
                int special = s.nextInt();
                int row = s.nextInt();
                int col = s.nextInt();
                Letter l = new Letter(letter, val, special, row, col);
                board[row][col] = l;
            }
                displayBoard(board);                            
        } else if(entered.equals("2")) {
            displayBoard(board);
            for(int row = 0; row < size; row++) {
                for(int col = 0; col < size; col++) {
                    String letter = "";
                    boolean validLetter = false;
                    while(!validLetter) {
                        letter = sc.next();
                        char let = letter.charAt(0);
                        if((let > 64 && let < 91) || (let > 96 && let < 123)) {
                            letter = letter.toUpperCase();
                            validLetter = true;
                        } else {
                            System.out.println("Please enter a valid letter.");
                        }
                    }
                    System.out.println("0: None\n1: Double Letter\n2: Triple Letter\n"
                            + "3: Double Word\n4: Triple Word\n5: Reenter Letter");
                    boolean validNumber = false;
                    while(!validNumber) {
                        int special = sc.nextInt();

                        if(special > -1 && special < 5) {
                            board[row][col] = new Letter(letter, pointValues(letter), special, row, col);
                            validNumber = true;
                        } 
                        else if(special == 5) {
                            col--;
                            validNumber = true;                        
                        }
                        else {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    displayBoard(board);
                }
            }
        } else {
            //System.out.println("YY");            
            randomBoard(board);
        }
        
    }
    
    public static void randomBoard(Letter[][] board) {
        for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[row].length; col++) {
                int vow = (int)((Math.random() * 3) + 1);
                String letter = "";
                if(vow == 1) {
                    letter = createVowel(board);
                } else {
                    letter = createConsonant(board);
                }
                Letter l = new Letter(letter, pointValues(letter), (int)(Math.random() * 5), row, col); 
                board[row][col] = l;
            }
        }
        displayBoard(board);
    }
    
    public static String createVowel(Letter[][] board) {
        int vowel = (int)(Math.random() * 9) + 1;
        switch(vowel) {
            case 1: return "A"; case 2: return "A";
            case 3: return "E"; case 4: return "E";                
            case 5: return "I"; case 6: return "I";
            case 7: return "O";
            case 8: return "U";
            case 9: return "Y";              
        }
        return "A";
    }
    
    public static String createConsonant(Letter[][] board) {
        int consonant = (int)(Math.random() * 25) + 1;
        String letter = "";
        switch(consonant) {
            case 1: letter = "B"; break; 
            case 2: letter = "C"; break;
            case 3: letter = "D"; break; case 4: letter = "D"; break;
            case 5: letter = "F"; break; 
            case 6: letter = "G"; break;
            case 7: letter = "H"; break;
            case 8: letter = "J"; break;
            case 9: letter = "K"; break;     
            case 10: letter = "L"; break; case 11: letter = "L"; break;
            case 12: letter = "M"; break;
            case 13: letter = "N"; break; 
            case 14: letter = "P"; break; 
            case 15: letter = "Q"; break;
            case 16: letter = "R"; break; case 17: letter = "R"; break;
            case 18: letter = "S"; break; case 19: letter = "S"; break;
            case 20: letter = "T"; break; case 21: letter = "T"; break; 
            case 22: letter = "V"; break; 
            case 23: letter = "W"; break; 
            case 24: letter = "X"; break;  
            case 25: letter = "Z"; break;                             
        }       
        return letter;
    }    
    public static void displayBoard(Letter[][] board) {
        for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[0].length; col++) {
                System.out.print(board[row][col].getLetter());
                System.out.print(convertSpecial(board[row][col]));
                if(convertSpecial(board[row][col]) != "") {
                    System.out.print("  ");
                } else { 
                    System.out.print("      ");
                }
            }
            System.out.println();
            System.out.println();
        }
    }
    
    public static String convertSpecial(Letter letter) {
        switch(letter.getSpecial()) {
            case 0: return "";
            case 1: return "(DL)";
            case 2: return "(TL)";
            case 3: return "(DW)"; 
            case 4: return "(TW)";    
                
        }
        return "";        
    }
    
    public static void addDictionary(ArrayList<String> dictionary) throws FileNotFoundException {
        File file = new File("/Users/bensauberman/Desktop/CS/TheDictionary.txt");
        Scanner s = new Scanner(file);
        
        while(s.hasNextLine()) {
            dictionary.add(s.next().toUpperCase());
        }
    }
    public static int findWords(ArrayList<Letter> word, Letter[][] board, ArrayList<String> dictionary, int direction, 
          ArrayList<String> foundWords, ArrayList<Integer> foundPoints) {

        while(direction < 8) {
            // return 0 means not in dictionary 
            //return 1 means in dictionary but not complete word yet
            //return 2 means in dictionary and complete word
            int inDict = binarySearch(toWord(word), dictionary, 0, dictionary.size(), foundWords);
            System.out.println(inDict + " " + toWord(word));
            if(inDict == 0) {
                word.get(word.size()-1).setAvailable(true);
                word.remove(word.get(word.size()-1));
                return 1;
            }
            
            if(inDict == 1) {
                direction = addLetter(board, word, direction);
                if(direction == 8) {
                    try {
                        word.get(word.size()-1).setAvailable(true);
                        word.remove(word.get(word.size()-1));
                        return 1;
                    } catch(ArrayIndexOutOfBoundsException e) {return 1;}
                }
                direction += findWords(word, board, dictionary, 0, foundWords, foundPoints);                 
                
            }
            
            if(inDict == 2) {
                foundWords.add(toWord(word));            
                foundPoints.add(calcPoints(word));
                System.out.println(toWord(word) + " added for " + calcPoints(word) + " points");
                direction = addLetter(board, word, direction);
                direction += findWords(word, board, dictionary, 0, foundWords, foundPoints);                 
            }
        }
        try {
            word.get(word.size()-1).setAvailable(true);
            word.remove(word.get(word.size()-1));
            return 1;
        } catch(ArrayIndexOutOfBoundsException e) {return 1;}
    }
    
    public static int addLetter(Letter[][] board, ArrayList<Letter> word, int direction) {
        /*
        DIRECTION = 0 -> Check Top Left
        DIRECTION = 1 -> Check Top Center
        DIRECTION = 2 -> Check Top Right
        DIRECTION = 3 -> Check Middle Left
        DIRECTION = 4 -> Check Middle Right
        DIRECTION = 5 -> Check Bottom Left
        DIRECTION = 6 -> Check Bottom Center
        DIRECTION = 7 -> Check Bottom Right*/

        int count = 0;
        for(int rowAdder = -1; rowAdder <= 1; rowAdder++) {
            for(int colAdder = -1; colAdder <= 1; colAdder++) {
                if(rowAdder == 0 && colAdder == 0) {
                    colAdder++;
                }
                if(count >= direction) {
                    try {
                        if(board[word.get(word.size()-1).getRow()+rowAdder][word.get(word.size()-1).getCol()+colAdder].getAvailable()) {
                            board[word.get(word.size()-1).getRow()+rowAdder][word.get(word.size()-1).getCol()+colAdder].setAvailable(false);
                            word.add(board[word.get(word.size()-1).getRow()+rowAdder][word.get(word.size()-1).getCol()+colAdder]);
                            return direction;
                        } else direction++;
                    }   catch(ArrayIndexOutOfBoundsException e) {direction++;}
                }
                count++;
            }
        }
        return direction;    
    }
    
    public static String toWord(ArrayList<Letter> list) {
        String word = "";
        for(int x = 0; x < list.size(); x++) {
            word = word.concat(list.get(x).getLetter());
        }
        return word;
    }
    
    public static int calcPoints(ArrayList<Letter> word) {
        int wordMult = 1;
        int value = 0;
        for(int x = 0; x < word.size(); x++) {
            value += (word.get(x).getValue() * word.get(x).getLetMult());
            wordMult *= word.get(x).getWordMult();
        }
        int lengthBonus = 0;
        switch(word.size()) {
            case 5: lengthBonus = 3; break;
            case 6: lengthBonus = 6; break;
            case 7: lengthBonus = 10; break;
            case 8: lengthBonus = 15; break;
        }
        if(word.size() > 8 ) {
            lengthBonus = 20;
        }
        
        return value * wordMult + lengthBonus;
    }
    
    public static int binarySearch(String word, ArrayList<String> dictionary, int beginning, int end, ArrayList<String> foundWords) {
        checkCount++;
        int val = 0;
        String mid = dictionary.get((beginning + end) / 2);
        //System.out.println(mid + " " + beginning + " " + end);
        if(word.compareTo(mid) == 0 && !checkRepeat(word, foundWords)) {
            dictionary.remove((beginning + end) / 2);            
            return 2;
        }
        
        if(end == beginning || Math.abs(end - beginning) == 1) {
            return 0;
        }
        else if(mid.length() >= word.length() && word.compareTo(mid) < 0 && word.equals(mid.substring(0,word.length()))) {
            end = (beginning + end)/2;
            if(binarySearch(word, dictionary, beginning, end, foundWords) == 2) {
                return 2;
            } else {
                return 1;
            }
        } 
        else if(word.compareTo(mid) < 0) {
            end = (beginning + end)/2;
            val = binarySearch(word, dictionary, beginning, end, foundWords);
        }     
        if(word.compareTo(mid) > 0) {
            beginning = (beginning + end)/2;
            val = binarySearch(word, dictionary, beginning, end, foundWords);
        } 
        
        return val;

    }
    
    public static boolean checkRepeat(String word, ArrayList<String> foundWords) {
        for(int x = 0; x < foundWords.size(); x++) {
            if(foundWords.get(x).equals(word)) {
                return true;
            }
        }
        return false;
    }
    
    public static int pointValues(String letter) {
        int value = 0;       
        switch(letter) {
            case "A": value = 1; break;
            case "B": value = 4; break;
            case "C": value = 4; break;  
            case "D": value = 2; break;
            case "E": value = 1; break;
            case "F": value = 4; break; 
            case "G": value = 3; break;
            case "H": value = 3; break;
            case "I": value = 1; break; 
            case "J": value = 10; break;
            case "K": value = 5; break;
            case "L": value = 2; break; 
            case "M": value = 4; break;
            case "N": value = 2; break;
            case "O": value = 1; break;  
            case "P": value = 4; break;
            case "Q": value = 10; break;
            case "R": value = 1; break; 
            case "S": value = 1; break;
            case "T": value = 1; break;
            case "U": value = 2; break; 
            case "V": value = 5; break;
            case "W": value = 4; break;
            case "X": value = 8; break;   
            case "Y": value = 3; break;
            case "Z": value = 10; break;    
        }
        return value;
        
    }
    
    public static boolean hasVowels(String word) {
        for(int x = 0; x < word.length(); x++) {
            if(word.substring(x, x+1).equals("A") ||
               word.substring(x, x+1).equals("E") ||
               word.substring(x, x+1).equals("I") ||
               word.substring(x, x+1).equals("O") ||
               word.substring(x, x+1).equals("U") ||
               word.substring(x, x+1).equals("Y")) {
                return true;
            }
        }
        return false;
    }
}

