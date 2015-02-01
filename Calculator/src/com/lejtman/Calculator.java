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
    
    public Calculator() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("js");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 600);
        this.setLayout(new BorderLayout());
        display = new JLabel(" ");
        buttonPad = new JPanel();
        addButtons();
        buttonPad.setLayout(new GridLayout(4, 5));
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
        for (int i = 1; i < 10; i++) {
            addButton(i + "", new StringTextAdder());
        }
        addButton("C", new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                equationString = " ";
                updateDisplay(equationString);
            }
        });
        
        addButton("=", new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    equationString = engine.eval(equationString).toString();  
                } catch (ScriptException ex) {
                    equationString = "SYNTAX ERROR";
                }
                finally{
                    updateDisplay(equationString);
                    equationString = " ";
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
            equationString += e.getActionCommand();
            updateDisplay(equationString);
        }
        
    }
    
    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }
    
}