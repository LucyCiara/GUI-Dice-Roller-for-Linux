import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.StyledDocument;
import javax.swing.JSpinner;
public class GUI implements ActionListener{

    // Defines some components for use in all classes and methods.
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private GridBagLayout gbl = new GridBagLayout();
    private GridBagConstraints gcon = new GridBagConstraints();

    // The GUI method, where all the work to create the GUI happens.
    public GUI() {

        // Defines 15 different JButtons.
        JSpinner comp1 = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        
        JLabel comp2 = new JLabel("d", JLabel.CENTER);
        JSpinner comp3 = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));

        JCheckBox comp4 = new JCheckBox();
        JLabel comp5 = new JLabel("Success");
        JSpinner comp6 = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        
        JCheckBox comp7 = new JCheckBox();
        JLabel comp8 = new JLabel("Exploding crits");

        JCheckBox comp9 = new JCheckBox();
        JLabel comp10 = new JLabel("Double crits");

        JCheckBox comp11 = new JCheckBox();
        JLabel comp12 = new JLabel("Compute");
        String[] choiceStrings = {"+", "-", "*", "/"};
        JComboBox comp13 = new JComboBox(choiceStrings);
        
        JButton comp14 = new JButton("R");
        JButton comp15 = new JButton("C");
        JTextPane comp16 = new JTextPane();
        comp16.setEditable(false);
        comp16.setText("Output");
        
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
        Component[] JButtonArray = {comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp8, comp9, comp10, comp10, comp11, comp12, comp13};
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
                }else{
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
        javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("pass");
    }
}
