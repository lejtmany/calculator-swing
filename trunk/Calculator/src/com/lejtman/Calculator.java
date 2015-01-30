//Yosef Shalom Lejtman

package com.lejtman;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lejtman
 */
public class Calculator extends JFrame{

   JLabel display;
   JPanel buttonPad;
    
    public Calculator(){
       this.display = new JLabel();
       this.buttonPad = new JPanel();
       this.setLayout(new GridLayout(3,3));
       addButtons();
   }

    private void addButtons() {
        for(int i = 1; i < 10; i++){
            addButton(i+"");
        }
    }

    private void addButton(String text) {
        JButton button = new JButton(text);
        buttonPad.add(button);
        button.addActionListener(null);
    }

    
    public static void main(String[] args) {
        // TODO code application logic here
    }

    
    
    
}
