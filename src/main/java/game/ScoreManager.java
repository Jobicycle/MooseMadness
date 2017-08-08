/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Score Manager Class - Used to maintain a score text file
 * @author Meghan A. Snow
 */
public class ScoreManager {

    private ArrayList<Integer> scores;
    private static final String HIGHSCORE_FILE = "scores.txt";
/**
 * ScoreManager constructor. 
 * @param score int
 */
    public ScoreManager(int score) {

        try {
            scores = new ArrayList<Integer>();
            BufferedReader input = null;
            input = new BufferedReader(new FileReader(HIGHSCORE_FILE));
            String line = null;
            while ((line = input.readLine()) != null) {
                scores.add(Integer.valueOf(line));
            }

            scores.add(score);
            input.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoreManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScoreManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Collections.sort(scores);
        Collections.reverse(scores);
        saveScore(scores);
    }
/**
 * saveScore - saves the score of a play to the text file
 * @param scores 
 */
    private static void saveScore(ArrayList<Integer> scores) {
        BufferedWriter output = null;
        FileWriter fos = null;
        try {
            fos = new FileWriter(HIGHSCORE_FILE, false);
            output = new BufferedWriter(fos);

            for (int i = 0; i < scores.size(); i++) {
                String j = String.valueOf(scores.get(i));
                output.write(j);
                output.newLine();
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
/**
 * topThreeScores, returns the top three scores in an ArayList<Integer>
 * @return 
 */
    public ArrayList topThreeScores() {
        ArrayList<Integer> array = new ArrayList<Integer>();
        try {
            Scanner input = new Scanner(new File(HIGHSCORE_FILE));
            int counter = 0;
            while (input.hasNextLine() && counter < 3) {
                array.add(Integer.parseInt(input.nextLine()));
                counter++;
            }    
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoreManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            return array;
    }
    
    /**
     * topTenScores, returns the top ten scores in an ArrayList<Integer>
     * @return 
     */
    public ArrayList topTenScores(){
         ArrayList<Integer> array = new ArrayList<Integer>();
        try {
            Scanner input = new Scanner(new File(HIGHSCORE_FILE));
            int counter = 0;
            while (input.hasNextLine() && counter < 10) {
                array.add(Integer.parseInt(input.nextLine()));
                counter++;
            }    
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoreManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            return array;
    }

}
