package aliyew;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class UIManager {
    
    public static void main(String[] args) {
        JFrame myFrame = new JFrame("Budget Planner");
        myFrame.setLayout(new BoxLayout(myFrame.getContentPane(), BoxLayout.X_AXIS));        

        // myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        

        // PANELS
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        JPanel rightPanel = new JPanel(new FlowLayout());



        leftPanelConfigurationMethod(leftPanel);
        rightPanelConfigurationMethod(rightPanel);

        myFrame.add(leftPanel);
        myFrame.add(rightPanel);
        

        
        myFrame.setSize(1000, 600);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);

    }

    public static void leftPanelConfigurationMethod(JPanel leftPanel) {
        leftPanel.setBackground(Color.decode("#898989"));
        leftPanel.setPreferredSize(new Dimension(200, 600));

        JButton createRecordButton = new JButton("Create Record");
        createRecordButton.setBackground(Color.decode("#4a4a4a"));
        createRecordButton.setPreferredSize(new Dimension(200, 50));
        createRecordButton.setBorderPainted(false);
        createRecordButton.setFocusable(false);
        createRecordButton.setForeground(Color.decode("#dbd8d8"));

        createRecordButton.addActionListener(e -> {
            createRecordFrame();
        });
        leftPanel.add(createRecordButton);
    }

    public static void rightPanelConfigurationMethod(JPanel rightPanel) {
        rightPanel.setBackground(Color.decode("#d9d9d9"));
        rightPanel.setPreferredSize(new Dimension(800, 600));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel panel21 = new JPanel();
        panel21.setPreferredSize(new Dimension(800, 550));
        JPanel panel22 = new JPanel();
        panel22.setPreferredSize(new Dimension(800, 50));
        panel22.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        Thread networkConnThread = new Thread(() -> {
            JLabel networkLabel = new JLabel();
            panel22.add(networkLabel);

            while (true) {

            boolean isConnected = DBManager.connectDB();
            Color statusColor = isConnected ? Color.GREEN : Color.RED;
            String statusText = isConnected ? "ONLINE" : "OFFLINE";

            for (int i = 3; i > 0; i--) {
                String currentText = "Status : " + statusText + " (" + i + ")";

                SwingUtilities.invokeLater(() -> {
                    networkLabel.setText(currentText);
                    networkLabel.setForeground(statusColor);
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            }

            
        });

        networkConnThread.start();


        

        
        rightPanel.add(panel21);
        rightPanel.add(panel22);
        


    }

    public static void createRecordFrame() {
        JFrame newRecordFrame = new JFrame();
        newRecordFrame.setSize(420, 420);

        JPanel newRecordPanel = new JPanel();

        JTextField recordNameTextField = new JTextField("Record Name");
        JTextField recordIncomeTextField = new JTextField("Record Income");
        JTextField recordSavingTextField = new JTextField("Record Saving");

        newRecordPanel.add(recordNameTextField);
        newRecordPanel.add(recordIncomeTextField);
        newRecordPanel.add(recordSavingTextField);

        newRecordFrame.add(newRecordPanel);
        newRecordFrame.setTitle("Create Record");
        newRecordFrame.setVisible(true);

        
        
    }
}