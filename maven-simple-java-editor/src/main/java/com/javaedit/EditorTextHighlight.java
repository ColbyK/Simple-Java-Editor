package com.javaedit;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

// Threaded Class for highlighting text in JTextPane
class EditorTextHighlight extends SwingWorker<Void,Object> {
    private JTextPane textPane;
    private StyleContext style;
    private AttributeSet textStyle;
    private JLabel keywordsLabel;
    // TODO - might grab number of keywords here, would be simple but potential threading problems
    //private Integer numKeywords;
    
    public EditorTextHighlight(JTextPane text, JLabel label) {
    	textPane = text;
    	keywordsLabel = label;
    }
    // Matches specific regular expressions to apply style to text
    private void matching(String str){
        style = StyleContext.getDefaultStyleContext();
        // Recolor all text back to default (black) (necessary as typing between or extending existing colored text would still apply color)
        textStyle = style.addAttribute(style.getEmptySet(),StyleConstants.Foreground, Color.BLACK);
        textPane.getStyledDocument().setCharacterAttributes(0,str.length(),textStyle, false);
        
        // Color all conditionals BLUE
        textStyle = style.addAttribute(style.getEmptySet(),StyleConstants.Foreground, Color.BLUE);
        String regexLogic = "\\b(if|else|for|while)\\b";
        String input = str;
        Pattern p = Pattern.compile(regexLogic);
        Matcher m = p.matcher(input);
        int numKeywords = 0;
        while(m.find()) {
        	textPane.getStyledDocument().setCharacterAttributes(m.start(),(m.end() - m.start()),textStyle, false);
            numKeywords++;
        }
        keywordsLabel.setText("Number of Keywords: " + numKeywords);
        // Color all arithmetic and logic RED
        textStyle = style.addAttribute(style.getEmptySet(),StyleConstants.Foreground, Color.RED);
        String regexArith = "\\b(true|True|TRUE|false|False|FALSE)\\b|[\\/+%*-]|\\|\\||&&";
        p = Pattern.compile(regexArith);
        m = p.matcher(input);
        while(m.find()) {
        	textPane.getStyledDocument().setCharacterAttributes(m.start(),(m.end() - m.start()),textStyle, false);
        }
        
        // Color all Strings GREEN
        textStyle = style.addAttribute(style.getEmptySet(),StyleConstants.Foreground, Color.GREEN.darker());
        String regexString = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1";
        p = Pattern.compile(regexString);
        m = p.matcher(input);
        while(m.find()) {
        	textPane.getStyledDocument().setCharacterAttributes(m.start(),(m.end() - m.start()),textStyle, false);
        }
    }
    @Override
    // Active after Object creation
    protected Void doInBackground() {
        try {
			matching(textPane.getDocument().getText(0, textPane.getDocument().getLength()));
			
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
        return null;
    }
    @Override
    protected void done() {
    }
}
