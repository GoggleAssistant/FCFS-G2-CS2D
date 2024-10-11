import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class GanttChartGeneration {
    private JFrame frame;
    private JPanel progressBar; // Renamed from ganttChartPanel
    private JPanel unitBar;
    private int[][] ganttData;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    private JLabel avgWaitingTimeLabel;
    private JLabel avgWaitingTimeValue;
    private JLabel avgTurnaroundTimeLabel;
    private JLabel avgTurnaroundTimeValue;

    public GanttChartGeneration(int[][] ganttData) {
        this.ganttData = ganttData;

        // Print ganttData to console
        printGanttData();

        // Create the main frame
        frame = new JFrame("Gantt Chart");
        frame.setSize(1100, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Set the frame's content pane to white
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(null); // Use null layout for absolute positioning

        // Create the Gantt chart panel (now called progress bar)
        progressBar = new JPanel(null); // Use null layout
        progressBar.setBackground(Color.DARK_GRAY);

        // Calculate the position to center the panel horizontally and raise it vertically
        int x = (1100 - 1000) / 2;
        int y = 100;
        progressBar.setBounds(x, y, 1000, 100);

        // Create the unit bar panel (same size as progress bar)
        unitBar = new JPanel(null); // Use null layout
        unitBar.setBackground(Color.WHITE); // Set background to white
        unitBar.setBounds(x, y + 110, 1000, 50); // Same width as progress bar

        // Create and position the "FCFS" label
        JLabel fcfsLabel = new JLabel("FCFS");
        fcfsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        fcfsLabel.setBounds(x, y - 30, 100, 20); // Positioned above the raised panel

        // Add the FCFS label, progress bar, and unit bar to the frame
        frame.add(fcfsLabel);
        frame.add(progressBar);
        frame.add(unitBar);

        // Call FCFS method to populate the bars
        FCFS();

        // Add the final time label
        addFinalTimeLabel();

        // Add average time labels
        addAverageTimeLabels();

        // Make the frame visible
        frame.setVisible(true);
    }

    // You can add methods here to implement the FCFS algorithm and draw the Gantt chart

    private void printGanttData() {
        System.out.println("Gantt Data in GanttChartGeneration:");
        System.out.println("------------------------------------");
        System.out.printf("%-10s %-15s %-15s%n", "Process", "Arrival Time", "Burst Time");
        System.out.println("------------------------------------");
        for (int i = 0; i < ganttData.length; i++) {
            System.out.printf("%-10s %-15d %-15d%n", 
                "P" + (i + 1), 
                ganttData[i][0], // Arrival Time
                ganttData[i][1]); // Burst Time
        }
        System.out.println("------------------------------------");
    }

    private void FCFS() {
        int[] processOrder = calculateProcessOrder();
        int totalTime = totalTime();
        
        // Remove layout managers
        progressBar.removeAll();
        unitBar.removeAll();
        
        // Calculate unit width based on the actual width of the progress bar
        int progressBarWidth = progressBar.getWidth();
        int unitWidth = progressBarWidth / totalTime;
        
        // Calculate start and finish times
        int[] start = new int[ganttData.length];
        int[] finish = new int[ganttData.length];
        int currentTime = 0;

        for (int i = 0; i < processOrder.length; i++) {
            int processIndex = processOrder[i];
            int arrivalTime = ganttData[processIndex][0];
            int burstTime = ganttData[processIndex][1];

            if (arrivalTime > currentTime) {
                currentTime = arrivalTime;
            }

            start[processIndex] = currentTime;
            currentTime += burstTime;
            finish[processIndex] = currentTime;
        }

        // Create units for progress bar and unit bar
        for (int i = 0; i < totalTime; i++) {
            addUnitToBar(progressBar, new Color(200, 200, 200, 50), i, false, false, unitWidth, totalTime);
            boolean showLabel = (i == 0) || isStartOrFinishTime(i, start, finish);
            addUnitToBar(unitBar, Color.WHITE, i, true, showLabel, unitWidth, totalTime);
        }

        // Calculate average waiting time and average turnaround time
        calculateAverageTimes(start, finish);

        // Add process visualization to the progress bar
        for (int i = 0; i < processOrder.length; i++) {
            int processIndex = processOrder[i];
            addItem(processIndex, start[processIndex], finish[processIndex]);
        }
    }

    private void calculateAverageTimes(int[] start, int[] finish) {
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int totalProcesses = ganttData.length;

        for (int i = 0; i < totalProcesses; i++) {
            int arrivalTime = ganttData[i][0];
            int burstTime = ganttData[i][1];
            int turnaroundTime = finish[i] - arrivalTime;
            int waitingTime = turnaroundTime - burstTime;

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;
        }

        avgWaitingTime = (double) totalWaitingTime / totalProcesses;
        avgTurnaroundTime = (double) totalTurnaroundTime / totalProcesses;
    }

    private boolean isStartOrFinishTime(int time, int[] start, int[] finish) {
        for (int i = 0; i < start.length; i++) {
            if (time == start[i] || time == finish[i]) {
                return true;
            }
        }
        return false;
    }

    private void addUnitToBar(JPanel bar, Color color, int position, boolean isUnitBar, boolean showLabel, int unitWidth, int totalUnits) {
        JPanel unit = new JPanel(new BorderLayout());
        unit.setBackground(color);
        
        // Calculate the width of this unit
        int width = (position == totalUnits - 1) ? bar.getWidth() - (position * unitWidth) : unitWidth;
        
        unit.setBounds(position * unitWidth, 0, width, bar.getHeight());
        
        if (isUnitBar && showLabel) {
            // Add number label to the unit bar
            JLabel label = new JLabel("| " + position);
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            unit.add(label, BorderLayout.WEST);
        }

        bar.add(unit);
    }

    private int[] calculateProcessOrder() {
        int[] order = new int[ganttData.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        //bubble sort
        for (int i = 0; i < order.length - 1; i++) {
            for (int j = 0; j < order.length - i - 1; j++) {
                if (shouldSwap(order[j], order[j + 1])) {
                    int temp = order[j];
                    order[j] = order[j + 1];
                    order[j + 1] = temp;
                }
            }
        }

        return order;
    }

    //swap the order of the processes based on the arrival time and the process number
    private boolean shouldSwap(int index1, int index2) {
        // Compare arrival times
        if (ganttData[index1][0] != ganttData[index2][0]) {
            return ganttData[index1][0] > ganttData[index2][0];
        }
        // If arrival times are equal, compare process numbers
        return index1 > index2;
    }

    private int totalTime() {
        int[] processOrder = calculateProcessOrder();
        int totalTime = 0;
        int currentTime = 0;
        int idleTime = 0;

        for (int i = 0; i < processOrder.length; i++) {
            int processIndex = processOrder[i];
            int arrivalTime = ganttData[processIndex][0];
            int burstTime = ganttData[processIndex][1];

            if (arrivalTime > currentTime) {
                // CPU is idle
                idleTime += arrivalTime - currentTime;
                currentTime = arrivalTime;
            }

            // Add the burst time of the process
            currentTime += burstTime;
        }

        totalTime = currentTime;

        // Print the results
        System.out.println("Total Time (including idle): " + totalTime);
        System.out.println("Idle Time: " + idleTime);
        System.out.println("Total Burst Time: " + (totalTime - idleTime));

        return totalTime;
    }

    private void addItem(int processIndex, int startTime, int finishTime) {
        String processName = "P" + (processIndex + 1);
        JButton processButton = new JButton(processName);
        
        int progressBarWidth = progressBar.getWidth();
        int totalTime = totalTime();
        int unitWidth = progressBarWidth / totalTime;
        
        int x = startTime * unitWidth;
        int width = (finishTime - startTime) * unitWidth;
        
        // Ensure the last process button extends to the end of the progress bar
        if (finishTime == totalTime) {
            width = progressBarWidth - x;
        }
        
        processButton.setBounds(x, 0, width, progressBar.getHeight());

        Color[] colors = {
            Color.decode("#ff99c8"),
            Color.decode("#fec8c3"),
            Color.decode("#fcf6bd"),
            Color.decode("#d0f4de"),
            Color.decode("#a9def9"),
            Color.decode("#e4c1f9")
        };
        processButton.setBackground(colors[processIndex % colors.length]);

        processButton.setOpaque(true);
        processButton.setBorderPainted(false);
        processButton.setFocusPainted(false);

        processButton.addActionListener(e -> showProcessInfo(processIndex, startTime, finishTime));

        progressBar.add(processButton);
    }

    private void showProcessInfo(int processIndex, int startTime, int finishTime) {
        DecimalFormat df = new DecimalFormat("#.##");
        int arrivalTime = ganttData[processIndex][0];
        int burstTime = ganttData[processIndex][1];
        int turnaroundTime = finishTime - arrivalTime;
        int waitingTime = turnaroundTime - burstTime;

        String message = String.format(
            "Process: P%d\n" +
            "Starting Time: %d\n" +
            "Completion Time: %d\n" +
            "Duration (Burst Time): %d\n" +
            "-----------\n" +
            "TaT (Turnaround Time): %d\n" +
            "WT (Waiting Time): %d\n\n",
            processIndex + 1,
            startTime,
            finishTime,
            burstTime,
            turnaroundTime,
            waitingTime,
            df.format(avgWaitingTime),
            df.format(avgTurnaroundTime)
        );

        JOptionPane.showMessageDialog(frame, message, "Process Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addFinalTimeLabel() {
        int totalTime = totalTime();
        
        JLabel finalTimeLabel = new JLabel("| " + totalTime);
        finalTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        finalTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        finalTimeLabel.setBackground(Color.WHITE); // Set background to white
        finalTimeLabel.setOpaque(true); // Make the label opaque to show the background color

        // Calculate position
        int x = unitBar.getX() + unitBar.getWidth();
        int y = unitBar.getY() + (unitBar.getHeight() / 2) - (finalTimeLabel.getPreferredSize().height / 2);

        finalTimeLabel.setBounds(x, y, finalTimeLabel.getPreferredSize().width, finalTimeLabel.getPreferredSize().height);

        frame.add(finalTimeLabel);
    }

    private void addAverageTimeLabels() {
        // Create labels for average waiting time and average turnaround time
        avgWaitingTimeLabel = new JLabel("Average Waiting Time:");
        avgWaitingTimeValue = new JLabel(String.format("%.2f", avgWaitingTime));
        avgTurnaroundTimeLabel = new JLabel("Average Turnaround Time:");
        avgTurnaroundTimeValue = new JLabel(String.format("%.2f", avgTurnaroundTime));

        // Set font and alignment for labels
        avgWaitingTimeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        avgWaitingTimeValue.setFont(new Font("Arial", Font.BOLD, 30));
        avgTurnaroundTimeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        avgTurnaroundTimeValue.setFont(new Font("Arial", Font.BOLD, 30));

        // Position labels under the process panel and unit panel
        int x = unitBar.getX();
        int y = unitBar.getY() + unitBar.getHeight() + 30;
        avgWaitingTimeLabel.setBounds(x, y, 500, 60);
        avgWaitingTimeValue.setBounds(x + 500, y, 100, 60);
        avgTurnaroundTimeLabel.setBounds(x, y + 60, 500, 60);
        avgTurnaroundTimeValue.setBounds(x + 500, y + 60, 100, 60);

        // Add labels to the frame
        frame.add(avgWaitingTimeLabel);
        frame.add(avgWaitingTimeValue);
        frame.add(avgTurnaroundTimeLabel);
        frame.add(avgTurnaroundTimeValue);
    }


}