package aliyew;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        JFrame myFrame = getFrame();


        

        // PANELS
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        JPanel rightPanel = new JPanel(new FlowLayout());

        leftPanelConfigurationMethod(leftPanel);
        rightPanelConfigurationMethod(rightPanel);


        myFrame.add(leftPanel);
        myFrame.add(rightPanel); 
        myFrame.setVisible(true);

    }

    public static JFrame getFrame() {
        JFrame myFrame = new JFrame("Budget Planner");
        myFrame.setLayout(new BoxLayout(myFrame.getContentPane(), BoxLayout.X_AXIS));     

        myFrame.setSize(1000, 600);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return myFrame;
    }

    public static void leftPanelConfigurationMethod(JPanel leftPanel) {
        leftPanel.setBackground(Color.decode("#598392"));
        leftPanel.setPreferredSize(new Dimension(200, 600));

        JButton createRecordButton = new JButton("Create Record");
        JButton showRecordsButton = new JButton("Show Records");

        leftPanelButtonConfigruations(createRecordButton);
        leftPanelButtonConfigruations(showRecordsButton);
        

        createRecordButton.addActionListener(e -> {
            createRecordFrame();
        });

        leftPanel.add(createRecordButton);
        leftPanel.add(showRecordsButton);

    }

    public static void leftPanelButtonConfigruations(JButton btn) {
        btn.setBackground(Color.decode("#124559"));
        btn.setPreferredSize(new Dimension(200, 75));
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        btn.setForeground(Color.decode("#dbd8d8"));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(41, 128, 185));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.decode("#124559"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                btn.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.WHITE));
            }
            
        });
    }

    public static void rightPanelConfigurationMethod(JPanel rightPanel) {
        rightPanel.setBackground(Color.decode("#aec3b0"));
        rightPanel.setPreferredSize(new Dimension(800, 600));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel panel21 = new JPanel();
        panel21.setPreferredSize(new Dimension(800, 550));
        panel21.setBackground(Color.decode("#eff6e0"));
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