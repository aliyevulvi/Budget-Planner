package aliyew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class UIManager {
        private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        private static final int dynamicWidth = (int) (screenSize.width * 0.60);
        private static final int dynamicHeight = (int) (screenSize.height * 0.70);
    
    public static void main(String[] args) {
        JFrame myFrame = getFrame();

        // PANELS
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());

        leftPanelConfigurationMethod(leftPanel);
        rightPanelConfigurationMethod(rightPanel);


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
        JButton createRecordButton = new JButton("Create Record");
        JButton synchronizationButton = new JButton("Synchronization");

        leftPanelButtonConfigruations(leftPanel, createRecordButton);
        leftPanelButtonConfigruations(leftPanel, synchronizationButton);
        

        createRecordButton.addActionListener(e -> {
            createRecordFrame();
        });

        leftPanel.add(createRecordButton);
        leftPanel.add(synchronizationButton);

    }

    public static void leftPanelButtonConfigruations(JPanel jPanel, JButton btn) {
        btn.setBackground(Color.decode("#124559"));
        btn.setAlignmentX(JButton.CENTER_ALIGNMENT);
        btn.setBorderPainted(false);
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
    }

    public static void rightPanelConfigurationMethod(JPanel rightPanel) {
        rightPanel.setBackground(Color.decode("#aec3b0"));        
        // rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panel21 = new JPanel();
        panel21.setLayout(new BoxLayout(panel21, BoxLayout.Y_AXIS));
        panel21.setBackground(Color.decode("#dee2e6"));
        JTable table = new JTable(5, 4);
        // table.set
        panel21.add(table);
        rightPanel.add(panel21, BorderLayout.CENTER);
    

        
        rightPanel.add(panel21);
        


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