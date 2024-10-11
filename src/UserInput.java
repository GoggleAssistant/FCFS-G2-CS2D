import javax.swing.*;
import java.awt.*;

public class UserInput extends JFrame {

    // Declare instance variables for the panels
    private JPanel panel1;
    private JPanel panel2; // Now an instance variable
    private JPanel p1Panel, p2Panel, p3Panel, p4Panel, p5Panel, p6Panel;

    // Add these instance variables
    private int visiblePanels = 0;
    private JLabel[] notAvailableLabels;

    public UserInput() {
        // Create a JFrame instance
        JFrame frame = new JFrame("User Input Window");

        // Set the size of the window to 1200x600
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window

        // Use BorderLayout for the main frame layout
        frame.setLayout(new BorderLayout());

        // Upper part - 2 panels (half the height) - Contains the number of process prompts and group details
        JPanel upperPanel = new JPanel(new GridLayout(1, 2)); // 1 row, 2 columns
        upperPanel.setPreferredSize(new Dimension(1200, 300)); // Half the height (300px)

        // Lower part - 6 panels (half the height) - Contains the process prompts.
        JPanel lowerPanel = new JPanel(new GridLayout(1, 6)); // 1 row, 6 columns (each column)
        lowerPanel.setPreferredSize(new Dimension(1200, 300)); // Half the height (300px)

        // Initialize the upper panels
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel1.setBackground(new Color(0xD3, 0xD2, 0xDB)); 
        panel2.setBackground(Color.BLACK);

        upperPanel.add(panel1);
        upperPanel.add(panel2);

        // Call the processNum() method to add GUI elements to panel1
        processNum();

        // Call the new method to set up panel2
        setupPanel2();

        // Initialize the lower panels as instance variables
        p1Panel = new JPanel();
        p2Panel = new JPanel();
        p3Panel = new JPanel();
        p4Panel = new JPanel();
        p5Panel = new JPanel();
        p6Panel = new JPanel();

        // Set different colors for each JPanel to differentiate them
        p1Panel.setBackground(Color.decode("#ff99c8")); // First color
        p2Panel.setBackground(Color.decode("#fec8c3")); // Second color
        p3Panel.setBackground(Color.decode("#fcf6bd")); // Third color
        p4Panel.setBackground(Color.decode("#d0f4de")); // Fourth color
        p5Panel.setBackground(Color.decode("#a9def9")); // Fifth color
        p6Panel.setBackground(Color.decode("#e4c1f9")); // Sixth color

        // Add the 6 panels to the lower part
        lowerPanel.add(p1Panel);
        lowerPanel.add(p2Panel);
        lowerPanel.add(p3Panel);
        lowerPanel.add(p4Panel);
        lowerPanel.add(p5Panel);
        lowerPanel.add(p6Panel);

        // Call the processPanel() method to add GUI elements to the 6 panels
        processPanel(p1Panel, p2Panel, p3Panel, p4Panel, p5Panel, p6Panel);

        // Add upper and lower panels to the frame
        frame.add(upperPanel, BorderLayout.NORTH); // Upper half
        frame.add(lowerPanel, BorderLayout.SOUTH); // Lower half

        // Make the frame visible
        frame.setVisible(true);

        // Initialize notAvailableLabels array
        notAvailableLabels = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            notAvailableLabels[i] = new JLabel("Not Available");
            notAvailableLabels[i].setFont(new Font("Arial", Font.BOLD, 20));
            notAvailableLabels[i].setBounds(20, 50, 200, 30);
            notAvailableLabels[i].setVisible(false);
        }
    }

    // New method to set up panel2
    private void setupPanel2() {
        panel2.setLayout(null); // Use null layout for absolute positioning

        // Add "First Come First Serve" label
        JLabel fcfsLabel = new JLabel("First Come First Serve");
        fcfsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        fcfsLabel.setForeground(Color.WHITE);
        fcfsLabel.setBounds(20, 80, 300, 30); // Moved down to align with panel1 elements

        // Add "Made by CS2D - Group 2" label
        JLabel groupLabel = new JLabel("Made by CS2D - Group 2");
        groupLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        groupLabel.setForeground(Color.WHITE);
        groupLabel.setBounds(20, 110, 300, 20); // Positioned below fcfsLabel

        // Add "Generate" button
        JButton generateButton = new JButton("Generate");
        generateButton.setFont(new Font("Arial", Font.BOLD, 18));
        generateButton.setBounds(20, 150, 150, 40);
        generateButton.setBackground(Color.WHITE);
        generateButton.setFocusPainted(false);

        // Add ActionListener to the generate button
        generateButton.addActionListener(e -> generateFCFS());

        // Add components to panel2
        panel2.add(fcfsLabel);
        panel2.add(groupLabel);
        panel2.add(generateButton);
    }

    // This method will process and add elements to the panels (panel1 and panel2 in the upper part)
    private void processNum() {
        // Set layout to null for absolute positioning in panel1
        panel1.setLayout(null);

        // Add JLabel for "Number of processes" and set its position
        JLabel processLabel = new JLabel("Number of processes");
        processLabel.setBounds(30, 40, 370, 30); // x, y, width, height
        processLabel.setFont(new Font("Arial", Font.BOLD, 30));
        panel1.add(processLabel);

        // Create a JPanel for the button grid, set layout to null for manual positioning
        JPanel buttonGrid = new JPanel();
        buttonGrid.setLayout(null);
        buttonGrid.setBounds(30, 80, 375, 250); // x, y, width, height
        buttonGrid.setBackground(new Color(0, 0, 0, 0)); // Transparent background

        // Set button positions and sizes manually (100x100 for each)
        for (int i = 1; i <= 6; i++) {
            JButton button = new JButton(String.valueOf(i)); // Create button with label
            button.setFont(new Font("Arial", Font.BOLD, 40)); // Set font size to 40
            button.setFocusPainted(false); // Disable focus painting
            button.setBackground(new Color(23, 22, 34)); // Default background color
            button.setForeground(Color.WHITE); // Default font color

            // Manually set position and size for each button
            int row = (i - 1) / 3;  // Determine the row (0 or 1)
            int col = (i - 1) % 3;  // Determine the column (0, 1, or 2)

            int x = col * 100;      // 100 is width + spacing (90 + 10px spacing)
            int y = row * 100;      // 100 is height + spacing (90 + 10px spacing)

            button.setBounds(x, y, 90, 90); // Set size to 90x90 for each button

            final int panelNumber = i;
            button.addActionListener(e -> {
                // Set all buttons back to default color and font color
                for (int j = 1; j <= 6; j++) {
                    JButton b = (JButton) buttonGrid.getComponent(j - 1);
                    b.setBackground(new Color(23, 22, 34)); // Reset to default color
                    b.setForeground(Color.WHITE); // Reset font color to white
                }
                // Set pressed button to highlight color and change font color to black
                button.setBackground(new Color(179, 180, 242)); // Set highlight color
                button.setForeground(Color.BLACK); // Change font color to black

                // Update visible panels
                updateVisiblePanels(panelNumber);
            });

            // Add button to the buttonGrid
            buttonGrid.add(button);
        }

        // Add the button grid to panel1
        panel1.add(buttonGrid);
    }

    private void updateVisiblePanels(int number) {
        visiblePanels = number;
        JPanel[] panels = {p1Panel, p2Panel, p3Panel, p4Panel, p5Panel, p6Panel};

        for (int i = 0; i < panels.length; i++) {
            if (i < visiblePanels) {
                showPanelContents(panels[i]);
                notAvailableLabels[i].setVisible(false);
            } else {
                hidePanelContents(panels[i]);
                notAvailableLabels[i].setVisible(true);
                panels[i].add(notAvailableLabels[i]);
            }
        }
    }

    private void showPanelContents(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (!(comp instanceof JLabel && ((JLabel) comp).getText().equals("Not Available"))) {
                comp.setVisible(true);
            }
        }
    }

    private void hidePanelContents(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (!(comp instanceof JLabel && ((JLabel) comp).getText().equals("Not Available"))) {
                comp.setVisible(false);
            }
        }
    }

    // This method will handle the GUI elements for p1Panel to p6Panel
    private void processPanel(JPanel p1Panel, JPanel p2Panel, JPanel p3Panel, JPanel p4Panel, JPanel p5Panel, JPanel p6Panel) {
        JPanel[] panels = {p1Panel, p2Panel, p3Panel, p4Panel, p5Panel, p6Panel};
        
        for (int i = 0; i < panels.length; i++) {
            JPanel panel = panels[i];
            panel.setLayout(null);

            JLabel processLabel = new JLabel("Process " + (i + 1));
            processLabel.setFont(new Font("Arial", Font.BOLD, 20));
            processLabel.setBounds(20, 50, 200, 30);
            panel.add(processLabel);

            JLabel arriveLabel = new JLabel("Arrival Time");
            arriveLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            arriveLabel.setBounds(20, 90, 200, 30);
            panel.add(arriveLabel);

            JTextField arriveTextField = new JTextField("0"); // Set default value to "0"
            arriveTextField.setBounds(20, 120, 100, 30);
            panel.add(arriveTextField);

            JLabel burstLabel = new JLabel("Burst Time");
            burstLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            burstLabel.setBounds(20, 160, 200, 30);
            panel.add(burstLabel);

            JTextField burstTextField = new JTextField("1"); // Set default value to "1"
            burstTextField.setBounds(20, 190, 100, 30);
            panel.add(burstTextField);

        }
    }

    // New method for FCFS generation
    private void generateFCFS() {
        // Check if the number of processes is selected
        if (visiblePanels == 0) {
            JOptionPane.showMessageDialog(null, "Please select the number of processes first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Array to store process details
        int[][] ganttData = new int[visiblePanels][2]; // [arrival time, burst time]

        // Collect data from visible panels
        JPanel[] panels = {p1Panel, p2Panel, p3Panel, p4Panel, p5Panel, p6Panel};
        for (int i = 0; i < visiblePanels; i++) {
            JTextField arrivalField = (JTextField) panels[i].getComponent(2);
            JTextField burstField = (JTextField) panels[i].getComponent(4);

            try {
                ganttData[i][0] = Integer.parseInt(arrivalField.getText());
                ganttData[i][1] = Integer.parseInt(burstField.getText());

                if (ganttData[i][1] <= 0) {
                    throw new NumberFormatException("Burst time must be positive");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input for Process " + (i + 1) + ". Please enter valid integers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Open the GanttChartGeneration window
        SwingUtilities.invokeLater(() -> new GanttChartGeneration(ganttData));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserInput());
    }
}