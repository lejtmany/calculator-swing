//Yosef Shalom Lejtman
package com.lejtman;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lejtman
 */
public class Calculator extends JFrame {
    
    JLabel display;
    JPanel buttonPad;
    String equationString = "";
    ScriptEngine engine;
    MathExpressionParser parser;
    
    public Calculator() {
        ScriptEngineManager manager = new ScriptEngineManager();
        parser = new MathExpressionParser();
        this.engine = manager.getEngineByName("js");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 600);
        this.setLayout(new BorderLayout());
        display = new JLabel(" ");
        buttonPad = new JPanel();
        addButtons();
        buttonPad.setLayout(new GridLayout(5, 5));
        this.add(display, BorderLayout.NORTH);
        this.add(buttonPad, BorderLayout.CENTER);        
        this.pack();
        this.setVisible(true);
    }
    
    private void addButtons() {
        String[] operators = {"+", "-", "*", "/", "."};
        for (String operator : operators) {
            addButton(operator, new StringTextAdder());
        }
        for (int i = 0; i < 10; i++) {
            addButton(i + "", new StringTextAdder());
        }
        addButton("C", new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                equationString = "";
                updateDisplay(equationString);
            }
        });
        
        addButton("=", new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                Double result = parser.parse(equationString);
                updateDisplay("" + ((result.intValue() == result)?  result.intValue() + "": result));             
                }
                catch(IllegalArgumentException ex){
                    updateDisplay(ex.getMessage());                  
                }
                finally{
                   equationString = ""; 
                }             
            }
        }
        );
        
    }
    
    private void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        buttonPad.add(button);
        button.addActionListener(listener);
    }
    
    private void updateDisplay(String text) {
        display.setText(text);
    }
    
    class StringTextAdder implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().matches("[*+/-]")){
                equationString += " " + e.getActionCommand() + " ";
            }
            else
                equationString += e.getActionCommand();
            updateDisplay(equationString);
        }
        
    }
    
    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }
    
}
