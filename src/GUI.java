import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.JSpinner;
public class GUI{

    // Defines some components for use in all classes and methods.
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private GridBagLayout gbl = new GridBagLayout();
    private GridBagConstraints gcon = new GridBagConstraints();

    private static JSpinner comp1;
    private static JLabel comp2;
    private static JSpinner comp3;
    private static JCheckBox comp4;
    private static JLabel comp5;
    private static JSpinner comp6;
    private static JCheckBox comp7;
    private static JLabel comp8;
    private static JCheckBox comp9;
    private static JLabel comp10;
    private static JCheckBox comp10a;
    private static JLabel comp10b;
    private static JCheckBox comp11;
    private static JLabel comp12;
    private static JComboBox comp13;
    private static JButton comp14;
    private static JButton comp15;
    private static JTextPane outputTextPane;
    private static JScrollPane comp16;
    private Random r = new Random();


    // The GUI method, where all the work to create the GUI happens.
    public GUI() {
        // Defines 15 different JButtons.
        comp1 = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        comp2 = new JLabel("d", JLabel.CENTER);
        comp3 = new JSpinner(new SpinnerNumberModel(2, 2, null, 1));

        comp4 = new JCheckBox();
        comp5 = new JLabel("Success");
        comp6 = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        
        comp7 = new JCheckBox();
        comp8 = new JLabel("Exploding crits");

        comp9 = new JCheckBox();
        comp10 = new JLabel("Double crits");

        comp10a = new JCheckBox();
        comp10b = new JLabel("1s cancel");


        comp11 = new JCheckBox();
        comp12 = new JLabel("Compute");
        String[] choiceStrings = {"+", "-", "*", "/"};
        comp13 = new JComboBox(choiceStrings);
        
        comp14 = new JButton("R");
        comp14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int diceAmount = (int) comp1.getValue();
                int diceType = (int) comp3.getValue();

                boolean successEnabled = (boolean) comp4.isSelected();
                int successNumber = (int) comp6.getValue();

                boolean explodes = (boolean) comp7.isSelected();
                boolean critDoubles = (boolean) comp9.isSelected();

                boolean cancelling1s = (boolean) comp10a.isSelected();

                boolean computes = (boolean) comp11.isSelected();
                String operationType = (String) comp13.getSelectedItem();
                
                String[] printLineStrings = {};

                int min = 1;
                int max = diceType+1;
                int[] diceRolls = {};
                for (int i = 0; i<diceAmount; i++) {
                    diceRolls = Arrays.copyOf(diceRolls, diceRolls.length+1);
                    diceRolls[i] = r.nextInt(min, max);
                }

                if (explodes) {
                    for (int i = 0; i<diceRolls.length; i++) {
                        if (diceRolls[i] == diceType) {
                            diceRolls = Arrays.copyOf(diceRolls, diceRolls.length+1);
                            diceRolls[diceRolls.length-1] = r.nextInt(min, max);
                        }
                    }
                }

                String diceRollString = "";
                for (int i = 0; i<diceRolls.length-1; i++) {
                    diceRollString += (Integer.toString(diceRolls[i]) + ", ");
                }
                diceRollString += diceRolls[diceRolls.length-1];
                printLineStrings = Arrays.copyOf(printLineStrings, printLineStrings.length+2);
                printLineStrings[0] = "Dice rolls:";
                printLineStrings[1] = diceRollString;

                if (successEnabled) {
                    int numberOfSuccesses = 0;
                    for (int i = 0; i<diceRolls.length; i++) {
                        if (critDoubles && diceRolls[i] == diceType) {
                            numberOfSuccesses += 2;
                        }else if (cancelling1s && diceRolls[i] == 1) {
                            numberOfSuccesses -= 1;
                        }else if (diceRolls[i] >= successNumber) {
                            numberOfSuccesses += 1;
                        }
                    }
                    printLineStrings = Arrays.copyOf(printLineStrings, printLineStrings.length+2);
                    printLineStrings[printLineStrings.length-2] = "Number of successes:";
                    printLineStrings[printLineStrings.length-1] = Integer.toString(numberOfSuccesses);
                }

                if (computes) {
                    float sum = diceRolls[0];
                    if (operationType == "+") {
                        sum = (float) IntStream.of(diceRolls).sum();
                    }else if (operationType == "-") {
                        for (int i = 1; i<diceRolls.length; i++) {
                            sum -= (float) diceRolls[i];
                        }
                    }else if (operationType == "*") {
                        for (int i = 1; i<diceRolls.length; i++) {
                            sum *= (float) diceRolls[i];
                        }
                    }else if (operationType == "/") {
                        for (int i = 1; i<diceRolls.length; i++) {
                            sum /= (float) diceRolls[i];
                        }
                    }
                    printLineStrings = Arrays.copyOf(printLineStrings, printLineStrings.length+2);
                    printLineStrings[printLineStrings.length-2] = "Sum:";
                    printLineStrings[printLineStrings.length-1] = Float.toString(sum);
                }
                Document doc = outputTextPane.getDocument();
                for (int i = 0; i<printLineStrings.length; i++) {
                    try {
                        doc.insertString(doc.getLength(), printLineStrings[i] + "\n", null);
                    } catch (BadLocationException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                try {
                    doc.insertString(doc.getLength(), "\n", null);
                } catch (BadLocationException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        comp15 = new JButton("C");
        comp15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextPane.setText("");
            }
        });
        outputTextPane = new JTextPane();
        outputTextPane.setEditable(false);
        comp16 = new JScrollPane(outputTextPane);
        comp16.setAutoscrolls(true);
        
        // comp16.setEditable(false);


        int border = 15;

        // Sets the border and layout of the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        panel.setLayout(gbl);
        

        // Sets the gcon settings that apply to all components, like weight and how it'll fill the screen.
        gcon.weightx = 1;
        gcon.weighty = 1;
        gcon.fill = GridBagConstraints.BOTH;


        // Creates an array with lots of JButtons and the spaces they occupy. if the same JButton occupies 2 indexes, it's because it's a larger JButton. In a later version of this program, the JButtons will be switched with different components. It is then sent into a method which adds them to the panel in a formated way according to how they've been placed in the array.
        Component[] JButtonArray = {comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp8, comp9, comp10, comp10, comp10a, comp10b, comp10b, comp11, comp12, comp13};
        componentFormater(JButtonArray, 3);

        // Creates arrays for the last 3 components and their positions + dimensions for feeding into a method that manually adds their widths, heights and positions.
        Component[] JButtonArray2 = {comp14, comp15, comp16};
        int[][] positions = {
            {0, 0, 1, 1},
            {0, 1, 1, 1},
            {1, 0, 2, 2}
        };
        manualComponentFormater(JButtonArray2, JButtonArray, positions);


        // Adds the panel to the frame and changes some frame settings before setting it to be visible.
        frame.setSize(180+border*2, 480+border*2);
        frame.setLocationRelativeTo(null);
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Dice Roller");
        // frame.pack();
        // frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        
    }
    

    // A method for manually formating components, using an array of components to format, an array of previously formated components, and an array of the positions and dimensions of the components.
    private void manualComponentFormater(Component[] components, Component[] previousComponents, int[][] positions) {
        for (int i = 0; i<components.length; i++) {
            // It collects the information to give to gcon from the positions array.
            gcon.gridx = positions[i][0];
            gcon.gridy = positions[i][1] + (int) Math.floor((float) previousComponents.length/3);
            gcon.gridwidth = positions[i][2];
            gcon.gridheight = positions[i][3];
            gbl.setConstraints(components[i], gcon);
            panel.add(components[i]);
        }
    }


    // A method for organising components in a grid when some components are wider and taller than others. Only works when components are wider before they are taller. It's very specific to the planned layout.
    private void componentFormater(Component[] components, int gridW) {
        int objH;
        int objW;

        // Will go through every item in the array and add it to the panel with the gbl settings determined by how many times it occurs at once in the array.
        int occurances = 0;
        for (int i = 0; i<components.length; i++) {
            // Only allows the process to happen when the numbers of occurances is 0, meaning when there's a new item in the array.
            if (occurances == 0) {
                // Counts the number of occurances of the component in the array..
                boolean run = true;
                int j = i;
                while (run && j<components.length) {
                    if(components[j] == components[i]) {
                        occurances++;
                    }else{
                        run = false;
                    }
                    j++;
                }

                // Calculates the width and height of the component.
                if (occurances >= gridW) {
                    objW = gridW;
                }else {
                    objW = occurances;
                }

                if (i == 18) {
                    System.out.println("Shitbag");
                }



                objH = (int) Math.ceil((float) occurances/gridW);

                // Sets the gcon settings for the component.
                gcon.gridx = i%3;
                gcon.gridy = (int) Math.floor((float) i/3);
                gcon.gridwidth = objW;
                gcon.gridheight = objH;

                // Sets the constraints of the component, and adds it to the panel.
                
                gbl.setConstraints(components[i], gcon);
                panel.add(components[i]);
            }
            occurances--;
        }
    }


    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        new GUI();
    }

}
