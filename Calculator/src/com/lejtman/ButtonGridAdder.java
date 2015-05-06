/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lejtman;

import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 *
 * @author student1
 */
public class ButtonGridAdder {
    
    private JButton[][] buttons;
    private final int rows, cols;
    
    public ButtonGridAdder(int cols, int rows){
        buttons = new JButton[cols][rows];
        this.rows = rows;
        this.cols = cols;
    }
    
    public boolean addButton(JButton button, Point point){
        return addButton(button, point.x, point.y);
    }
    
    public boolean addButton(JButton button, int col, int row){
       boolean isEmpty = buttons[col][row] == null;
        if(col > col -1 || row > row -1)
           throw new IllegalArgumentException("column or row is out of bounds");
        buttons[col][row] = button;       
        return isEmpty;
    }
    
    public void addButtonsTo(JComponent parent){
        for(int row = 0; row < rows; row++)
            for(int col = 0; col < cols; col++){
                if(buttons[col][row] != null)
                    parent.add(buttons[col][row]);
            }
    }


    public int getRows() {
        return rows;
    }


    public int getCols() {
        return cols;
    }
}
