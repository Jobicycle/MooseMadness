/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Meghan A. Snow
 */
public class ScoreManager {

    private ArrayList<Integer> scores;
    private static final String HIGHSCORE_FILE = "scores.txt";

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
            /**
             * try { FileOutputStream output = new
             * FileOutputStream("scores.dat"); properties.store(output, "Sample
             * Properties"); } catch (IOException e) { e.printStackTrace(); }
             *
             */
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

}
