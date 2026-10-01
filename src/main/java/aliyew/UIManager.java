package aliyew;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.*;

import java.util.ArrayList;

public class UIManager {
        private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        private static final int dynamicWidth = (int) (screenSize.width * 0.60);
        private static final int dynamicHeight = (int) (screenSize.height * 0.70);
        private static final String LOGIN_PANEL = "LOGIN_PANEL";
        private static final String RECORDS_PANEL = "RECORDS_PANEL";
        private static final String CREATE_RECORD_PANEL = "CREATE_RECORD_PANEL";
        private static final String SETTINGS_PANEL = "SETTINGS_PANEL";
        private static final String REVIEW_PANEL = "REVIEW_PANEL";
        private static JPanel rightPanel;
        
        private static JPanel reviewPanel = new JPanel();
        private static Record record = new Record();
        private static ArrayList<Expense> allExpenses = new ArrayList<>();
    
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
        rightPanel.setBackground(Color.decode("#f1f1f1"));        
        rightPanel.setBorder(BorderFactory.createEmptyBorder((int) (dynamicHeight*0.05),(int) (dynamicWidth*0.05),(int) (dynamicHeight*0.05),(int) (dynamicWidth*0.05)));


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
        rightPanel.add(reviewPanel, REVIEW_PANEL);
        


    }
    
    private static void loginPanelConfig(JPanel panel) {
        
    }
    
    private static void recordsPanelConfig(JPanel panel) {
        panel.setBackground(Color.decode("#e3e3e3"));
        
        ArrayList<Record> allRecords = JsonManager.getRecords();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.decode("#e3e3e3"));
        
        for (Record rec : allRecords) {
            JPanel p = new JPanel(new BorderLayout());
            JLabel icon = new JLabel(javax.swing.UIManager.getIcon("FileView.directoryIcon"));
            icon.setBorder(BorderFactory.createEmptyBorder(0,(int) (dynamicWidth*0.1), 0, 0));
            JLabel recordName = new JLabel(rec.getRecordName());
            recordName.setHorizontalAlignment(SwingConstants.CENTER);
            JButton deleteButton = new JButton("X");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.decode("#ed574c"));
            JButton reviewButton = new JButton(">");
            reviewButton.setForeground(Color.WHITE);
            reviewButton.setBackground(Color.decode("#4487eb"));
            reviewButton.setName(rec.getRecordName());
            
            reviewButton.addActionListener( e -> {
                for (Record rec2 : allRecords) {
                    if (reviewButton.getName().equals(rec2.getRecordName())) {
                        record = rec2;
                    }
                }
                
                allExpenses = JsonManager.getExpenses(record);
                
                reviewPanelConfig();
                
                ((CardLayout) (rightPanel.getLayout())).show(rightPanel, REVIEW_PANEL);
            });
            
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 0, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            
            buttonPanel.add(deleteButton, gbc);
            
            gbc.gridx = 1;
            buttonPanel.add(reviewButton, gbc);
            
            //buttonPanel.setMaximumSize(new Dimension((int) (dynamicWidth*0.05), (int) (dynamicHeight*0.1)));
            buttonPanel.setBackground(Color.LIGHT_GRAY);
            
        
            deleteButton.setMaximumSize(new Dimension((int) (dynamicWidth*0.8), (int) (dynamicHeight*0.05)));
            reviewButton.setMaximumSize(new Dimension((int) (dynamicWidth*0.8), (int) (dynamicHeight*0.05)));
           
            p.setPreferredSize(new Dimension((int) (dynamicWidth*0.7), (int) (dynamicHeight*0.1)));
            p.setMaximumSize(new Dimension((int) (dynamicWidth*0.7), (int) (dynamicHeight*0.10)));
            p.setMinimumSize(new Dimension((int) (dynamicWidth*0.7), (int) (dynamicHeight*0.1)));
            
            p.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.decode("#000000")));
            p.setBackground(Color.LIGHT_GRAY);
            p.add(icon, BorderLayout.WEST);
            p.add(recordName, BorderLayout.CENTER);
            p.add(buttonPanel, BorderLayout.EAST);
            
            contentPanel.add(p);

        }
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        panel.add(scrollPane , BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.decode("#cccccc")));
        

    }
    
    
    private static void createRecordPanelConfig(JPanel panel) {
        int panelWidth = (int) (dynamicWidth * 0.4);
        int panelHeight = (int) (dynamicHeight * 0.1);
        panel.setBackground(Color.decode("#e3e3e3"));
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets((int) (panelHeight*0.1), (int) (panelWidth*0.1), (int) (panelHeight*0.15), (int) (panelWidth*0.1));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Record Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField recordNameField = new JTextField(10);
        recordNameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        panel.add(recordNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Record Income:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField recordIncomeField = new JTextField(10);
        recordIncomeField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        panel.add(recordIncomeField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Record Saving:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField recordSavingField = new JTextField(10);
        recordSavingField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        panel.add(recordSavingField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton clearButton = new JButton("Clear");
        clearButton.setForeground(Color.WHITE);
        clearButton.setBackground(Color.decode("#ed574c"));
        clearButton.addActionListener(e -> {
            recordNameField.setText("");
            recordIncomeField.setText("");
            recordSavingField.setText("");
        });
        panel.add(clearButton, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton createButton = new JButton("Create");
        createButton.setForeground(Color.WHITE);
        createButton.setBackground(Color.decode("#4487eb"));
        panel.add(createButton, gbc);
        
        panel.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.decode("#cccccc")));
        
        
    }
    
    private static void settingsPanelConfig(JPanel panel) {
        panel.setBackground(Color.decode("#e3e3e3"));
        
    }

    private static void reviewPanelConfig() {
        reviewPanel.setLayout(new BorderLayout());
        
        JLabel recordName = new JLabel("Record Name : "+record.getRecordName());
        JLabel recordCreationDate = new JLabel("Record Date : "+record.getCreationDate());
        JLabel recordIncome = new JLabel("Record Income : "+record.getRecordIncome());
        JLabel recordSaving =new JLabel("Record Saving : "+record.getRecordSaving());
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(recordName);
        infoPanel.add(recordCreationDate);
        infoPanel.add(recordIncome);
        infoPanel.add(recordSaving);
        
        reviewPanel.add(infoPanel, BorderLayout.NORTH);
        
    }
}