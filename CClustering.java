/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclusteringmodified;

import cclusteringmodified.utils.ExcelDataLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/**
 *
 * @author Vitalii Umanets
 */
public class CClustering {

    public static void main(String[] args) {
        try {
            double[][] data = ExcelDataLoader.loadXLSXFile("I:\\Book1.xlsx", false);
            StringBuilder spaces = new StringBuilder();
            int maxsize=0;
            int cellsize=0;
            for(int i=0;i<data.length;i++)
                for(int j=0;j<data[0].length;j++){
                    cellsize=String.valueOf(Math.round(data[i][j])).length();
                    maxsize=maxsize<cellsize?cellsize:maxsize;
            }

            for(int i=0;i<maxsize;i++)
            spaces.append(' ');

            for(int i=0;i<data.length;i++){
                for(int j=0;j<data[0].length;j++){
                    cellsize=String.valueOf(Math.round(data[i][j])).length();
                    spaces.delete(maxsize, spaces.length());
                    spaces.insert(maxsize, data[i][j]<0?"":" ");
                    spaces.append("%.").append(4).append("f ");
                    System.out.format(spaces.substring(cellsize-1), data[i][j]);
                }
                System.out.println();
            }
            System.out.println();

        } catch (IOException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(CClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
