// Imports everuthing from the awt library, and a specific event and eventlistener.
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Imports some java utilities.
import java.util.Random;
import java.util.Arrays;
import java.util.stream.IntStream;

// Imports swing components and formatting tools.
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
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.JSpinner;

public class GUI{
    // Defines some components for use in all classes and methods.
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private GridBagLayout gbl = new GridBagLayout();
    private GridBagConstraints gcon = new GridBagConstraints();

    // Defines all the components to be used.
    private static JSpinner JDiceAmountSpinner;
    private static JLabel JDiceLabel;
    private static JSpinner JDiceTypeSpinner;
    private static JCheckBox JSuccessCheckBox;
    private static JLabel JSuccessLabel;
    private static JSpinner JSuccessSpinner;
    private static JCheckBox JExplodesCheckBox;
    private static JLabel JExplodesLabel;
    private static JCheckBox JDoubleCheckBox;
    private static JLabel JDoubleLabel;
    private static JCheckBox JCancelCheckBox;
    private static JLabel JCancelLabel;
    private static JCheckBox JComputationCheckBox;
    private static JLabel JComputationLabel;
    private static JComboBox JComputationComboBox;
    private static JButton JRollButton;
    private static JButton JClearButton;
    private static JTextPane JOutputTextPane;
    private static JScrollPane JOutputScrollPane;
    private Random r = new Random();


    // The GUI method, where all the work to create the GUI happens.
    public GUI() {
        // These are the dice related components.
        JDiceAmountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        JDiceLabel = new JLabel("d", JLabel.CENTER);
        JDiceTypeSpinner = new JSpinner(new SpinnerNumberModel(2, 2, null, 1));

        // These are the success specific components.
        JSuccessCheckBox = new JCheckBox();
        JSuccessLabel = new JLabel("Success");
        JSuccessSpinner = new JSpinner(new SpinnerNumberModel(2, 2, null, 1));
        
        // These are the components related to exploding dice.
        JExplodesCheckBox = new JCheckBox();
        JExplodesLabel = new JLabel("Exploding crits");

        // These are the components related to counting crits as double the successes.
        JDoubleCheckBox = new JCheckBox();
        JDoubleLabel = new JLabel("Double crits");

        // These are the components related to counting 1s as reducing the successes by one.
        JCancelCheckBox = new JCheckBox();
        JCancelLabel = new JLabel("1s cancel");

        // These are the components related to computing with the dice rolls.
        JComputationCheckBox = new JCheckBox();
        JComputationLabel = new JLabel("Compute");
        String[] choiceStrings = {"+", "-", "*", "/"};
        JComputationComboBox = new JComboBox(choiceStrings);
        
        // This is the button which rolls the dice and makes stuff happen.
        JRollButton = new JButton("R");
        JRollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetches the different values and states of the different components.
                int diceAmount = (int) JDiceAmountSpinner.getValue();
                int diceType = (int) JDiceTypeSpinner.getValue();

                boolean successEnabled = (boolean) JSuccessCheckBox.isSelected();
                int successNumber = (int) JSuccessSpinner.getValue();

                boolean explodes = (boolean) JExplodesCheckBox.isSelected();
                boolean critDoubles = (boolean) JDoubleCheckBox.isSelected();

                boolean cancelling1s = (boolean) JCancelCheckBox.isSelected();

                boolean computes = (boolean) JComputationCheckBox.isSelected();
                String operationType = (String) JComputationComboBox.getSelectedItem();
                
                // This is the string array which contains what will be showed in the output panel.
                String[] printLineStrings = {};

                // "Rolls dice" (generates a random number depending on the dice type) and adds them to the diceRolls array for further processing.
                int min = 1;
                int max = diceType+1;
                int[] diceRolls = {};
                for (int i = 0; i<diceAmount; i++) {
                    diceRolls = Arrays.copyOf(diceRolls, diceRolls.length+1);
                    diceRolls[i] = r.nextInt(min, max);
                }

                // Checks for "critical" (maximum) rolls, and rolls another dice for each one, including new criticals rolled.
                if (explodes) {
                    for (int i = 0; i<diceRolls.length; i++) {
                        if (diceRolls[i] == diceType) {
                            diceRolls = Arrays.copyOf(diceRolls, diceRolls.length+1);
                            diceRolls[diceRolls.length-1] = r.nextInt(min, max);
                        }
                    }
                }

                // Processes the dice rolls into a string to add to the output panel.
                String diceRollString = "";
                for (int i = 0; i<diceRolls.length-1; i++) {
                    diceRollString += (Integer.toString(diceRolls[i]) + ", ");
                }
                diceRollString += diceRolls[diceRolls.length-1];
                printLineStrings = Arrays.copyOf(printLineStrings, printLineStrings.length+2);
                printLineStrings[0] = "Dice rolls:";
                printLineStrings[1] = diceRollString;

                // Counts the number of successes based on whether doubling the successes of a roll on a critical or reducing the number of successes by one on a 1 is enabled, and whether the rolled number is at or above the success threshold. Then it adds it to the lines to be output.
                if (successEnabled) {
                    int numberOfSuccesses = 0;
                    for (int i = 0; i<diceRolls.length; i++) {
                        if (critDoubles && diceRolls[i] == diceType) {
                            numberOfSuccesses += 2;
                        }else if (cancelling1s && diceRolls[i] == 1) {
                            numberOfSuccesses -= 1;
                        }else if ((diceRolls[i] >= successNumber) || (diceRolls[i] == diceType)) {
                            numberOfSuccesses += 1;
                        }
                    }
                    printLineStrings = Arrays.copyOf(printLineStrings, printLineStrings.length+2);
                    printLineStrings[printLineStrings.length-2] = "Number of successes:";
                    printLineStrings[printLineStrings.length-1] = Integer.toString(numberOfSuccesses);
                }

                // If computing rolls is enabled, it will create a sum based on the operation type enabled, then add it to the strings to be output.
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

                // Adds the text to be output as lines in the document of the output textpane.
                Document doc = JOutputTextPane.getDocument();
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

        // The button which clears the output textpane of all text.
        JClearButton = new JButton("C");
        JClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOutputTextPane.setText("");
            }
        });

        // The Textpane and scrollpane in which the output is displayed
        JOutputTextPane = new JTextPane();
        JOutputTextPane.setEditable(false);
        JOutputScrollPane = new JScrollPane(JOutputTextPane);
        JOutputScrollPane.setAutoscrolls(true);

        // The border around the components and the edges of the window.
        int border = 15;

        // Sets the border and layout of the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        panel.setLayout(gbl);
        

        // Sets the gcon settings that apply to all components, like weight and how it'll fill the screen.
        gcon.weightx = 1;
        gcon.weighty = 1;
        gcon.fill = GridBagConstraints.BOTH;


        // Creates an array with lots of JButtons and the spaces they occupy. if the same JButton occupies 2 indexes, it's because it's a larger JButton. In a later version of this program, the JButtons will be switched with different components. It is then sent into a method which adds them to the panel in a formated way according to how they've been placed in the array.
        Component[] JButtonArray = {JDiceAmountSpinner, JDiceLabel, JDiceTypeSpinner, JSuccessCheckBox, JSuccessLabel, JSuccessSpinner, JExplodesCheckBox, JExplodesLabel, JExplodesLabel, JDoubleCheckBox, JDoubleLabel, JDoubleLabel, JCancelCheckBox, JCancelLabel, JCancelLabel, JComputationCheckBox, JComputationLabel, JComputationComboBox};
        componentFormater(JButtonArray, 3);

        // Creates arrays for the last 3 components and their positions + dimensions for feeding into a method that manually adds their widths, heights and positions.
        Component[] JButtonArray2 = {JRollButton, JClearButton, JOutputScrollPane};
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

    // The main loop.
    public static void main(String[] args) throws Exception {
        // Sets the LookAndFeel to be the motif LookAndFeel, because I like the retro look.
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

        // Calls on the GUI.
        new GUI();
    }

}
