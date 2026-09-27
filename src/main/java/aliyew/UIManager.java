package aliyew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JLabel;

public class UIManager {
        private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        private static final int dynamicWidth = (int) (screenSize.width * 0.60);
        private static final int dynamicHeight = (int) (screenSize.height * 0.70);
        private static final String LOGIN_PANEL = "LOGIN_PANEL";
        private static final String RECORDS_PANEL = "RECORDS_PANEL";
        private static final String CREATE_RECORD_PANEL = "CREATE_RECORD_PANEL";
        private static final String SETTINGS_PANEL = "SETTINGS_PANEL";
        private static JPanel rightPanel;
    
    public static void main(String[] args) {
        JFrame myFrame = getFrame();

        // PANELS
        JPanel leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());


        rightPanelConfigurationMethod(rightPanel);
        leftPanelConfigurationMethod(leftPanel);
        
        myFrame.add(leftPanel, java.awt.BorderLayout.WEST);
        myFrame.add(rightPanel, java.awt.BorderLayout.CENTER); 
        myFrame.setVisible(true);

    }

    public static JFrame getFrame() {
        JFrame myFrame = new JFrame("Budget Planner");
        myFrame.setLayout(new BorderLayout());

        myFrame.setSize(dynamicWidth, dynamicHeight);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setLocationRelativeTo(null);
        myFrame.setResizable(false);

        return myFrame;
    }

    public static void leftPanelConfigurationMethod(JPanel leftPanel) {
        leftPanel.setBackground(Color.decode("#598392"));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension( (int) (dynamicWidth*0.20), dynamicHeight));
        
        JButton recordsButton = new JButton("Records");
        JButton createRecordButton = new JButton("Create Record");
        JButton synchronizationButton = new JButton("Synchronization");
        JButton settingsButton = new JButton("Settings");

        leftPanelButtonConfigruations(leftPanel, recordsButton);
        leftPanelButtonConfigruations(leftPanel, createRecordButton);
        leftPanelButtonConfigruations(leftPanel, synchronizationButton);
        leftPanelButtonConfigruations(leftPanel, settingsButton);
        
        recordsButton.addActionListener(e -> {
            ((CardLayout) (rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
        });

        createRecordButton.addActionListener(e -> {
            ((CardLayout) (rightPanel.getLayout())).show(rightPanel, CREATE_RECORD_PANEL);
        });
        
        settingsButton.addActionListener(e -> {
            ((CardLayout) (rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
        });

        
    }

    public static void leftPanelButtonConfigruations(JPanel jPanel, JButton btn) {
        btn.setBackground(Color.decode("#124559"));
        btn.setAlignmentX(JButton.CENTER_ALIGNMENT);
        //btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.decode("#000000")));
        btn.setFocusable(false);
        btn.setForeground(Color.decode("#dbd8d8"));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) (screenSize.height * 0.10)));
        btn.setPreferredSize(new Dimension(0, (int) (jPanel.getHeight()*0.1)));

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
                // btn.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.WHITE));
            }
            
        });
        
        jPanel.add(btn);
    }

    public static void rightPanelConfigurationMethod(JPanel rightPanel) {
        rightPanel.setBackground(Color.decode("#aec3b0"));        
        // rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        rightPanel.setLayout(new CardLayout());
        
        JPanel loginPanel = new JPanel(new BorderLayout());
        JPanel recordsPanel = new JPanel(new BorderLayout());
        JPanel createRecordPanel = new JPanel(new GridBagLayout());
        JPanel settingsPanel = new JPanel(new BorderLayout());
        
        loginPanelConfig(loginPanel);
        recordsPanelConfig(recordsPanel);
        createRecordPanelConfig(createRecordPanel);
        settingsPanelConfig(settingsPanel);
        
        rightPanel.add(loginPanel, LOGIN_PANEL);
        rightPanel.add(recordsPanel, RECORDS_PANEL);
        rightPanel.add(createRecordPanel, CREATE_RECORD_PANEL);
        rightPanel.add(settingsPanel, SETTINGS_PANEL);
        


    }
    
    private static void loginPanelConfig(JPanel panel) {
        
    }
    
    private static void recordsPanelConfig(JPanel panel) {
        String[] records = {"t1","t2","t3","t4"};
        JList recordList = new JList(records);
        recordList.setFixedCellWidth((int) (dynamicWidth*0.8));
        recordList.setFixedCellHeight((int) (dynamicHeight*0.1));
        
        panel.add(recordList, BorderLayout.CENTER);
        
    }
    
    private static void createRecordPanelConfig(JPanel panel) {
        int panelWidth = (int) (dynamicWidth * 0.4);
        int panelHeight = (int) (dynamicHeight * 0.1);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets((int) (panelHeight*0.9), (int) (panelWidth*0.1), (int) (panelHeight*0.1), (int) (panelWidth*0.1));
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        panel.add(new JLabel("Record Name: "), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField recordNameField = new JTextField(10);
        panel.add(recordNameField, gbc);
        
        gbc.insets = new Insets((int) (panelHeight*0.1), (int) (panelWidth*0.1), (int) (panelHeight*0.1), (int) (panelWidth*0.1));
        
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Record Income:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField recordIncomeField = new JTextField(10);
        panel.add(recordIncomeField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Record Saving"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField recordSavingField = new JTextField(10);
        panel.add(recordSavingField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            recordNameField.setText("");
            recordIncomeField.setText("");
            recordSavingField.setText("");
        });
        panel.add(clearButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton createButton = new JButton("Create");
        panel.add(createButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        panel.add(new JLabel(""), gbc);
        
        panel.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.decode("#000000")));
        
        
    }
    
    private static void settingsPanelConfig(JPanel panel) {
        
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
}