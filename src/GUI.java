import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class GUI implements ActionListener{

    // Defines some components for use in all classes and methods.
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private GridBagLayout gbl = new GridBagLayout();
    private GridBagConstraints gcon = new GridBagConstraints();

    // The GUI method, where all the work to create the GUI happens.
    public GUI() {

        // Defines 15 different buttons.
        Button btn1 = new Button("Btn 1");
        Button btn2 = new Button("Btn 2");
        Button btn3 = new Button("Btn 3");
        Button btn4 = new Button("Btn 4");
        Button btn5 = new Button("Btn 5");
        Button btn6 = new Button("Btn 6");
        Button btn7 = new Button("Btn 7");
        Button btn8 = new Button("Btn 8");
        Button btn9 = new Button("Btn 9");
        Button btn10 = new Button("Btn 10");
        Button btn11 = new Button("Btn 11");
        Button btn12 = new Button("Btn 12");
        Button btn13 = new Button("Btn 13");
        Button btn14 = new Button("Btn 14");
        Button btn15 = new Button("Btn 15");
        Button btn16 = new Button("Btn 16");
        // // JTextField diceTypeField = new JTextField(5);


        // // JTextField diceTypeField = new JTextField(5);

        // Sets the border and layout of the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(gbl);
        

        // Sets the gcon settings that apply to all components, like weight and how it'll fill the screen.
        gcon.weightx = 1;
        gcon.weighty = 1;
        gcon.fill = GridBagConstraints.BOTH;


        // Creates an array with lots of buttons and the spaces they occupy. if the same button occupies 2 indexes, it's because it's a larger button. In a later version of this program, the buttons will be switched with different components. It is then sent into a method which adds them to the panel in a formated way according to how they've been placed in the array.
        Component[] buttonArray = {btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn8, btn9, btn10, btn10, btn11, btn12, btn13};
        componentFormater(buttonArray, 3);

        // Creates arrays for the last 3 components and their positions + dimensions for feeding into a method that manually adds their widths, heights and positions.
        Component[] buttonArray2 = {btn14, btn15, btn16};
        int[][] positions = {
            {0, 0, 1, 1},
            {0, 1, 1, 1},
            {1, 0, 2, 2}
        };
        manualComponentFormater(buttonArray2, buttonArray, positions);


        // Adds the panel to the frame and changes some frame settings before setting it to be visible.
        frame.setSize(180, 480);
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
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("pass");
    }
}
