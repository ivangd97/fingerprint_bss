import mainClasses.Menu;
import Windows.*;

import javax.swing.*;

public class Main {
    public static void main (String[] args){

        JFrame mainFrame = new JFrame("PROYECTO DE BIOMETRÍA");
        mainFrame.setContentPane(new Window().main);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();

    }
}
