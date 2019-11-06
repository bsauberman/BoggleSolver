package boggle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bensauberman
 */
public class Letter {
    private String letter = "";
    private int value = 0;
    private int wordMult = 1;
    private int letMult = 1;
    private int special = 0;
    private boolean available = true;
    private int row = 0;
    private int col = 0;
    private boolean usedAsFirst = false;
    public Letter() {
        this.letter = "_";
    }
    public Letter(String letter, int value, int special, int row, int col) {
        this.letter = letter;
        this.value = value;
        this.special = special;
        this.row = row;
        this.col = col;
        
        if(special == 1) {
            letMult = 2;
        }
        if(special == 2) {
            letMult = 3;
        }
        if(special == 3) {
            wordMult = 2;
        }
        if(special == 4) {
            wordMult = 3;
        }
    }
    
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public boolean getAvailable() {
        return available;
    }
    
    public int getWordMult() {
        return wordMult;
    }
    
    public int getLetMult() {
        return letMult;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLetter() {
        return letter;
    }
    
    public int getSpecial() {
        return special;
    }
    
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
   
    public void setUsedAsFirst(boolean usedAsFirst) {
        this.usedAsFirst = usedAsFirst;
    }
    public boolean getUsedAsFirst() {
        return usedAsFirst;
    }
    
    
    
}
