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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.util.Comparator;
import java.time.*;
import java.time.format.*;
import java.io.File;
import java.time.ZoneId;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


public class UIManager {
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int dynamicWidth = (int)(screenSize.width * 0.60);
	private static final int dynamicHeight = (int)(screenSize.height * 0.70);
	private static final String LOGIN_PANEL = "LOGIN_PANEL";
	private static final String RECORDS_PANEL = "RECORDS_PANEL";
	private static final String CREATE_RECORD_PANEL = "CREATE_RECORD_PANEL";
	private static final String SETTINGS_PANEL = "SETTINGS_PANEL";
	private static final String REVIEW_PANEL = "REVIEW_PANEL";
	private static final String SYNC_PANEL = "SYNC_PANEL";
	private static final String COMPARE_PANEL = "COMPARE_PANEL";

	private static JPanel rightPanel;
	private static JPanel recordsPanel;
	private static JPanel settingsPanel;
	private static JPanel syncPanel;
	private static JPanel reviewPanel;
	private static JPanel comparePanel;
	private static JPanel chartsPanel = new JPanel();

	private static JScrollPane reviewScrollPane = new JScrollPane();
	private static Record record = new Record();
	private static ArrayList<Expense> allExpenses = new ArrayList<>();
	private static ArrayList<Record> allRecords = new ArrayList<>();

	public static void main(String[] args) {
// Pencere çerçevelerini işletim sistemi yerine Swing'in çizmesini sağlar
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

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
		leftPanel.setPreferredSize(new Dimension((int)(dynamicWidth * 0.20), dynamicHeight));

		JButton synchronizationButton = new JButton("Synchronization");
		JButton settingsButton = new JButton("Settings");

		leftPanelButtonConfigruations(leftPanel, settingsButton);
		leftPanelButtonConfigruations(leftPanel, synchronizationButton);

		synchronizationButton.addActionListener(e -> {
			syncPanelConfig();
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, SYNC_PANEL);
		});

		settingsButton.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
		});


		//    SOUTH (Limits)


	}

	public static void leftPanelButtonConfigruations(JPanel jPanel, JButton btn) {
		btn.setBackground(Color.decode("#124559"));
		btn.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btn.setBorderPainted(false);
//btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#000000")));
		btn.setFocusable(false);
		btn.setForeground(Color.decode("#dbd8d8"));
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(screenSize.height * 0.10)));
		btn.setPreferredSize(new Dimension(0, (int)(jPanel.getHeight() * 0.1)));

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
		rightPanel.removeAll();
		rightPanel.setBackground(Color.decode("#f1f1f1"));
		rightPanel.setBorder(BorderFactory.createEmptyBorder((int)(dynamicHeight * 0.05), (int)(dynamicWidth * 0.05), (int)(dynamicHeight * 0.05), (int)(dynamicWidth * 0.05)));


		rightPanel.setLayout(new CardLayout());

		recordsPanel = new JPanel(new BorderLayout());
		JPanel createRecordPanel = new JPanel(new GridBagLayout());
		settingsPanel = new JPanel(new BorderLayout());
		syncPanel = new JPanel(new BorderLayout());
		reviewPanel = new JPanel();
		comparePanel = new JPanel(new BorderLayout());

		recordsPanelConfig(recordsPanel);
		createRecordPanelConfig(createRecordPanel);
		settingsPanelConfig(settingsPanel);
		syncPanelConfig();

		rightPanel.add(recordsPanel, RECORDS_PANEL);
		rightPanel.add(createRecordPanel, CREATE_RECORD_PANEL);
		rightPanel.add(settingsPanel, SETTINGS_PANEL);
		rightPanel.add(reviewPanel, REVIEW_PANEL);
		rightPanel.add(syncPanel, SYNC_PANEL);
		rightPanel.add(comparePanel, COMPARE_PANEL);



	}

	private static void recordsPanelConfig(JPanel panel) {
		panel.removeAll();
		panel.setBackground(Color.decode("#e3e3e3"));

		allRecords = JsonManager.getRecords();

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(Color.decode("#e3e3e3"));

		for (Record rec : allRecords) {
			JPanel p = new JPanel(new BorderLayout());
			JLabel icon = new JLabel(javax.swing.UIManager.getIcon("FileView.directoryIcon"));
			icon.setBorder(BorderFactory.createEmptyBorder(0, (int)(dynamicWidth * 0.1), 0, 0));
			JLabel recordName = new JLabel(rec.getRecordName());
			recordName.setHorizontalAlignment(SwingConstants.CENTER);
			JButton deleteButton = new JButton("X");
			deleteButton.setForeground(Color.WHITE);
			deleteButton.setBackground(Color.decode("#ed574c"));
			JButton reviewButton = new JButton(">");
			reviewButton.setForeground(Color.WHITE);
			reviewButton.setBackground(Color.decode("#4487eb"));
			reviewButton.setName(rec.getRecordName());

			deleteButton.addActionListener(e -> {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure delete Record?", null, JOptionPane.YES_NO_OPTION);

				if (choice == JOptionPane.YES_OPTION) {
					JsonManager.deleteRecord(rec);
					new Synchronization("DELETE_RECORD", rec, null);
					rightPanelConfigurationMethod(rightPanel);
					((CardLayout)(rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
				}
			});

			reviewButton.addActionListener(e -> {
				for (Record rec2 : allRecords) {
					if (reviewButton.getName().equals(rec2.getRecordName())) {
						record = rec2;
					}
				}

				allExpenses = JsonManager.getExpenses(record);

				reviewPanelConfig();

				((CardLayout)(rightPanel.getLayout())).show(rightPanel, REVIEW_PANEL);
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


			deleteButton.setMaximumSize(new Dimension((int)(dynamicWidth * 0.8), (int)(dynamicHeight * 0.05)));
			reviewButton.setMaximumSize(new Dimension((int)(dynamicWidth * 0.8), (int)(dynamicHeight * 0.05)));

			p.setPreferredSize(new Dimension((int)(dynamicWidth * 0.7), (int)(dynamicHeight * 0.1)));
			p.setMaximumSize(new Dimension((int)(dynamicWidth * 0.7), (int)(dynamicHeight * 0.10)));
			p.setMinimumSize(new Dimension((int)(dynamicWidth * 0.7), (int)(dynamicHeight * 0.1)));

			p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#000000")));
			p.setBackground(Color.LIGHT_GRAY);
			p.add(icon, BorderLayout.WEST);
			p.add(recordName, BorderLayout.CENTER);
			p.add(buttonPanel, BorderLayout.EAST);

			contentPanel.add(p);

		}

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel createRecordPanel = new JPanel(new BorderLayout());
		createRecordPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JButton createRecordBtn = new JButton("+");
		createRecordBtn.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, CREATE_RECORD_PANEL);
		});
		createRecordPanel.add(createRecordBtn, BorderLayout.EAST);

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(createRecordPanel, BorderLayout.SOUTH);
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#cccccc")));


	}


	private static void createRecordPanelConfig(JPanel panel) {
		int panelWidth = (int)(dynamicWidth * 0.4);
		int panelHeight = (int)(dynamicHeight * 0.1);
		panel.setBackground(Color.decode("#e3e3e3"));
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets((int)(panelHeight * 0.1), (int)(panelWidth * 0.1), (int)(panelHeight * 0.15), (int)(panelWidth * 0.1));
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
		panel.add(new JLabel("Record Period: "), gbc);

		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		cal.add(Calendar.YEAR, -10);
		Date earliestDate = cal.getTime();
		cal.add(Calendar.YEAR, 10);
		Date latestDate = cal.getTime();

		SpinnerDateModel sdm = new SpinnerDateModel(currentDate, earliestDate, latestDate, Calendar.MONTH);

		JSpinner jspin = new JSpinner(sdm);

		JSpinner.DateEditor editor = new JSpinner.DateEditor(jspin, "MM yyyy");
		editor.getTextField().setEditable(false);
		jspin.setEditor(editor);

		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(jspin, gbc);


		gbc.gridx = 0;
		gbc.gridy = 4;
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setForeground(Color.WHITE);
		cancelBtn.setBackground(Color.decode("#ed574c"));
		cancelBtn.addActionListener(e -> {
			recordNameField.setText("");
			recordIncomeField.setText("");
			recordSavingField.setText("");
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
		});
		panel.add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		JButton createBtn = new JButton("Create");
		createBtn.setForeground(Color.WHITE);
		createBtn.setBackground(Color.decode("#4487eb"));
		createBtn.addActionListener(e -> {
			if (!Validation.isValidName(recordNameField.getText().trim(), allRecords)) {
				JOptionPane.showMessageDialog(null, "Record Name is Already Exists!");
			} else if (!Validation.isValidAmount(recordIncomeField.getText().trim()) || !Validation.isValidAmount(recordSavingField.getText().trim())) {
				JOptionPane.showMessageDialog(null, "Input Amounts Must Be Consist Of Digits!");
			} else {
				Date selectedDate = (Date) jspin.getValue();
				LocalDate period = YearMonth.from(selectedDate.toInstant().atZone(ZoneId.systemDefault())).atDay(1);
				Record newRecord = new Record(recordNameField.getText().trim(), Double.parseDouble(recordIncomeField.getText().trim()), Double.parseDouble(recordSavingField.getText().trim()), period);
				JsonManager.createRecord(newRecord);
				new Synchronization("CREATE_RECORD", newRecord, null);
				allRecords.add(newRecord);
				recordNameField.setText("");
				recordIncomeField.setText("");
				recordSavingField.setText("");
				recordsPanelConfig(recordsPanel);
				((CardLayout)(rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
			}
		});
		panel.add(createBtn, gbc);

		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#cccccc")));


	}

	private static void settingsPanelConfig(JPanel panel) {
		panel.removeAll();
		panel.setBackground(Color.decode("#e3e3e3"));

		panel.setLayout(new GridBagLayout());

		ArrayList<String> cats = JsonManager.getCats();

// ADD CATEGORY
		JLabel addLbl = new JLabel("Add Category");
		JTextField catFld = new JTextField("");
		JButton addBtn = new JButton("Add");

		catFld.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

		addBtn.setForeground(Color.WHITE);
		addBtn.setBackground(Color.decode("#4487eb"));
		addBtn.addActionListener(e -> {
			if (!Validation.isValidNameCat(catFld.getText().trim(), cats) || catFld.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "Category Already Exists");
			} else {
				JsonManager.createCat(catFld.getText().trim());
				JOptionPane.showMessageDialog(null, "New Category Added!");
//settingsPanelConfig(settingsPanel);
				rightPanelConfigurationMethod(rightPanel);
				((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
			}
		});


// DELETE CATEGORY
		JLabel delLbl = new JLabel("Delete Category");
		JComboBox<String> catBox = new JComboBox<>();
		JButton delBtn = new JButton("Delete");

		catBox.addItem("Default");
		for (String str : cats) {
			catBox.addItem(str);
		}

		delBtn.setForeground(Color.WHITE);
		delBtn.setBackground(Color.decode("#ed574c"));
		delBtn.addActionListener(e -> {
			String selectedCat = (String) catBox.getSelectedItem();

			if (selectedCat.equals("Default")) {
				JOptionPane.showMessageDialog(null, "You Can't Delete This Category!");
			} else {
				JsonManager.deleteCat(selectedCat);
				JOptionPane.showMessageDialog(null, "Category Deleted!");
//settingsPanelConfig(settingsPanel);
				rightPanelConfigurationMethod(rightPanel);
				((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
			}
		});

		JSeparator jsep = new JSeparator(SwingConstants.HORIZONTAL);

		int panelWidth = (int)(dynamicWidth * 0.4);
		int panelHeight = (int)(dynamicHeight * 0.1);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets((int)(panelHeight * 0.1), (int)(panelWidth * 0.1), (int)(panelHeight * 0.15), (int)(panelWidth * 0.1));
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(addLbl, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(catFld, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		panel.add(addBtn, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(delLbl, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(catBox, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		panel.add(delBtn, gbc);

		JButton prevBtn = new JButton("<");

		prevBtn.setBackground(Color.decode("#e8e8e8"));
		prevBtn.setForeground(Color.BLACK);
		prevBtn.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
		});

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(jsep, gbc);


// PAYMENT TEMPLATE CREATING
		JComboBox<String> categoryBox = new JComboBox<>();

		ArrayList<String> categories = JsonManager.getCats();
		for (String str : categories) {
			categoryBox.addItem(str);
		}
		JTextField catJtf = new JTextField(10);

		JButton payTempBtn = new JButton("Create");
		payTempBtn.addActionListener(e -> {

			if (Validation.isValidAmount(catJtf.getText().trim())) {
				JsonManager.createPT(new PT((String)(categoryBox.getSelectedItem()), Double.parseDouble(catJtf.getText().trim())));
				rightPanelConfigurationMethod(rightPanel);
				((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
			} else {
				JOptionPane.showMessageDialog(null, "Amount is Invalid!");
			}

			categoryBox.setSelectedIndex(-1);
			catJtf.setText("");
		});


//    PAYMENT TEMPLAYE DELETING
		JComboBox<String> ptCb = new JComboBox<>();
		ArrayList<PT> allPTs = JsonManager.getPTs();

		for (PT pt : allPTs) {
			ptCb.addItem(pt.toString());
		}

		JButton ptDelBtn = new JButton("Delete");
		ptDelBtn.addActionListener(e -> {
			int selIndex = ptCb.getSelectedIndex();

			JsonManager.deletePT(allPTs.get(selIndex));
			rightPanelConfigurationMethod(rightPanel);
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);

		});


		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		panel.add(new JLabel("Create Payment Template"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(new JLabel(""), gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		panel.add(new JLabel(""), gbc);



		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(categoryBox, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		panel.add(catJtf, gbc);

		gbc.gridx = 2;
		gbc.gridy = 5;
		panel.add(payTempBtn, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		panel.add(new JLabel("Delete Payment Template"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 6;
		panel.add(new JLabel(""), gbc);

		gbc.gridx = 2;
		gbc.gridy = 6;
		panel.add(new JLabel(""), gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		panel.add(ptCb, gbc);

		gbc.gridx = 1;
		gbc.gridy = 7;
		panel.add(new JLabel(""), gbc);

		gbc.gridx = 2;
		gbc.gridy = 7;
		panel.add(ptDelBtn, gbc);

		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(jsep, gbc);

		gbc.gridwidth =  1;
		gbc.gridx = 0;
		gbc.gridy = 9;
		panel.add(new JLabel("Data Operations"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 9;
		panel.add(new JLabel(""), gbc);

		gbc.gridx = 2;
		gbc.gridy = 9;
		panel.add(new JLabel(""), gbc);

		JButton resetBtn = new JButton("Reset");
		JButton backupBtn = new JButton("Backup");
		JButton restoreBtn = new JButton("Restore");

		resetBtn.setForeground(Color.WHITE);
		resetBtn.setBackground(Color.decode("#ed574c"));
		resetBtn.addActionListener(e -> {
			int choice = JOptionPane.showConfirmDialog(null, "Are you sure delete all data?", null, JOptionPane.YES_NO_OPTION);

			if (choice == JOptionPane.YES_OPTION) {
				allRecords = JsonManager.getRecords();

				for (Record rec : allRecords) {
					JsonManager.deleteRecord(rec);
					new Synchronization("DELETE_RECORD", rec, null);
				}

				allRecords.clear();
				rightPanelConfigurationMethod(rightPanel);
				((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
			}

		});

		backupBtn.setForeground(Color.WHITE);
		backupBtn.setBackground(Color.decode("#00B050"));
		backupBtn.addActionListener(e -> {
			JOptionPane.showMessageDialog(rightPanel, BackupManager.createBackup());
		});

		restoreBtn.setForeground(Color.WHITE);
		restoreBtn.setBackground(Color.decode("#4487eb"));
		restoreBtn.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Zip Files Only", "zip"));

			int result = fileChooser.showOpenDialog(rightPanel);

			if (result == JFileChooser.APPROVE_OPTION) {
				File selFile = fileChooser.getSelectedFile();

				if (selFile.getName().startsWith("budgetplanner_backup")) {
					JOptionPane.showMessageDialog(null, BackupManager.restoreBackup(selFile.getAbsolutePath()));
					rightPanelConfigurationMethod(rightPanel);
					((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
				} else {
					JOptionPane.showMessageDialog(null, "File Name Should Starts \"budgetplanner_backup\"");
				}
			}
		});


		gbc.gridx = 0;
		gbc.gridy = 10;
		panel.add(resetBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		panel.add(backupBtn, gbc);

		gbc.gridx = 2;
		gbc.gridy = 10;
		panel.add(restoreBtn, gbc);

		//    LIMIT OPERATIONS

		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 11;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

		JComboBox<String> limitCatBox = new JComboBox<>();
		JComboBox<String> recordBox = new JComboBox<>();
		JTextField limitAmt = new JTextField();

		limitCatBox.addItem("Default");
		limitCatBox.addItem("General");
		for (String cat : JsonManager.getCats()) {
			limitCatBox.addItem(cat);
		}

		recordBox.addItem("Default");
		for (Record rec : JsonManager.getRecords()) {
			recordBox.addItem(rec.getRecordName());
		}


		JButton addLimitBtn = new JButton("Add");
		addLimitBtn.addActionListener(e -> {
			String selCat = (String) limitCatBox.getSelectedItem();
			String selRec = (String) recordBox.getSelectedItem();
			String selAmt = (String) limitAmt.getText().trim();

			if (selCat.equals("Default")) {
				JOptionPane.showMessageDialog(null, "Please Choose Category!");
			} else if (selRec.equals("Default")) {
				JOptionPane.showMessageDialog(null, "Please Choose Record!");
			} else if (!Validation.isValidAmount(selAmt)) {
				JOptionPane.showMessageDialog(null, "Please Enter Valid Amount!");
			} else {
				JsonManager.createLimit(new Limit(selCat, Double.parseDouble(selAmt), selRec));
				JOptionPane.showMessageDialog(null, "Limit Created Successfully!");
				rightPanelConfigurationMethod(rightPanel);
				((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
			}




			limitCatBox.setSelectedItem(-1);
			recordBox.setSelectedItem(-1);
			limitAmt.setText("");


		});

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 12;
		panel.add(limitCatBox, gbc);

		gbc.gridx = 1;
		gbc.gridy = 12;
		panel.add(recordBox, gbc);

		gbc.gridx = 2;
		gbc.gridy = 12;
		panel.add(limitAmt, gbc);

		gbc.gridx = 2;
		gbc.gridy = 13;
		panel.add(addLimitBtn, gbc);

		JComboBox<String> deleteLimitBox = new JComboBox<>();

		deleteLimitBox.addItem("Default");
		for (Limit lim : JsonManager.getLimits()) {
			deleteLimitBox.addItem(lim.toString());
		}

		JButton deleteLimitBtn = new JButton("Delete");
		deleteLimitBtn.addActionListener(e -> {
			String selValue = (String) deleteLimitBox.getSelectedItem();
			if (!selValue.equals("Default")) {
				JsonManager.deleteLimit(Limit.fromString(selValue));
				JOptionPane.showMessageDialog(null, "Limit Deleted Successfully!");
				rightPanelConfigurationMethod(rightPanel);
				((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
			}
		});

		gbc.gridx = 0;
		gbc.gridy = 14;
		panel.add(deleteLimitBox, gbc);

		gbc.gridx = 1;
		gbc.gridy = 14;
		panel.add(new JLabel(""), gbc);

		gbc.gridx = 2;
		gbc.gridy = 14;
		panel.add(deleteLimitBtn, gbc);



		gbc.gridx = 2;
		gbc.gridy = 15;
		panel.add(prevBtn, gbc);

		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));




	}

	private static void reviewPanelConfig() {
		reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
		reviewPanel.removeAll();

		JPanel wrapperPanel = new JPanel();
		wrapperPanel.removeAll();
		wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
		wrapperPanel.setBackground(Color.decode("#e3e3e3"));
		wrapperPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#cccccc")));


//    REVIEW PANEL NORTH
		JTextField recordName = new JTextField(record.getRecordName(), SwingConstants.RIGHT);
		JLabel recordCreationDate = new JLabel(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.of("Europe/Istanbul")).format(record.getRecordCreationDate()), SwingConstants.RIGHT);
		JTextField recordIncome = new JTextField(record.getRecordIncome() + "", SwingConstants.RIGHT);
		JTextField recordSaving = new JTextField(record.getRecordSaving() + "", SwingConstants.RIGHT);

		Calendar cal = Calendar.getInstance();
		Date currentDate = Date.from(record.getRecordPeriod().atStartOfDay(ZoneId.systemDefault()).toInstant());
		cal.add(Calendar.YEAR, -10);
		Date earliestDate = cal.getTime();
		cal.add(Calendar.YEAR, 10);
		Date latestDate = cal.getTime();

		SpinnerDateModel sdm = new SpinnerDateModel(currentDate, earliestDate, latestDate, Calendar.MONTH);

		JSpinner periodSpinner = new JSpinner(sdm);

		JSpinner.DateEditor editor = new JSpinner.DateEditor(periodSpinner, "MM yyyy");
		editor.getTextField().setEditable(false);
		periodSpinner.setEditor(editor);

		periodSpinner.addChangeListener(e -> {
			Date selectedDate = (Date) periodSpinner.getValue();
			LocalDate period = YearMonth.from(selectedDate.toInstant().atZone(ZoneId.systemDefault())).atDay(1);

			record.setRecordPeriod(period);
			JsonManager.updateRecord(record);
			new Synchronization("UPDATE_RECORD", record, null);

		});

		recordName.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {

				allRecords = JsonManager.getRecords();
				if (Validation.isValidName(recordName.getText().trim(), allRecords)) {
					record.setRecordName(recordName.getText().trim());
					JsonManager.updateRecord(record);
					new Synchronization("UPDATE_RECORD", record, null);
					recordName.setText(record.getRecordName());
					recordsPanelConfig(recordsPanel);
				} else {
					recordName.setText(record.getRecordName());
				}
			}
		});

		recordIncome.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (Validation.isValidAmount(recordIncome.getText().trim())) {
					record.setRecordIncome(Double.parseDouble(recordIncome.getText().trim()));
					JsonManager.updateRecord(record);
					new Synchronization("UPDATE_RECORD", record, null);
					recordIncome.setText(record.getRecordIncome() + "");
					recordsPanelConfig(recordsPanel);
				} else {
					recordIncome.setText(record.getRecordIncome() + "");
				}


			}
		});

		recordSaving.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (Validation.isValidAmount(recordSaving.getText().trim())) {
					record.setRecordSaving(Double.parseDouble(recordSaving.getText().trim()));
					JsonManager.updateRecord(record);
					new Synchronization("UPDATE_RECORD", record, null);
					recordSaving.setText(record.getRecordSaving() + "");
					recordsPanelConfig(recordsPanel);
				} else {
					recordSaving.setText(record.getRecordSaving() + "");
				}
			}
		});

		JComboBox<String> ptCb = new JComboBox<>();
		ArrayList<PT> allPTs = JsonManager.getPTs();

		for (PT pt : allPTs) {
			ptCb.addItem(pt.toString());
		}

		JButton addPtBtn = new JButton("Add");
		addPtBtn.addActionListener(e -> {
			PT selPt = allPTs.get(ptCb.getSelectedIndex());
			Expense expenseTemp = new Expense(record.getRecordPeriod(), selPt.getPTCat(), selPt.getPTAmt());
			expenseTemp.setExpenseRecordId(record.getRecordId());
			JsonManager.createExpense(expenseTemp);
			new Synchronization("CREATE_EXPENSE", null, expenseTemp);
			allExpenses = JsonManager.getExpenses(record);
			rightPanelConfigurationMethod(rightPanel);
			reviewPanelConfig();
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, REVIEW_PANEL);


		});

		JComboBox<String> sortingBox = new JComboBox<>();
		sortingBox.addItem("Default");
		sortingBox.addItem("Chronological");
		sortingBox.addItem("Asc. Expense");
		sortingBox.addItem("Desc. Expense");
		allExpenses = JsonManager.getExpenses(record);

		JButton compareBtn = new JButton("Compare");
		compareBtn.addActionListener(e -> {
			if (JsonManager.getRecords().size() > 1) {
				comparePanelConfig();
				((CardLayout)(rightPanel.getLayout())).show(rightPanel, COMPARE_PANEL);
			} else {
				JOptionPane.showMessageDialog(null, "Another Record Doesn't Exist");
			}
		});



		JPanel infoPanel = new JPanel(new GridLayout(7, 2, 10, 10));

		infoPanel.setFocusable(true);
		recordName.addActionListener(e -> infoPanel.requestFocusInWindow());
		recordIncome.addActionListener(e -> infoPanel.requestFocusInWindow());
		recordSaving.addActionListener(e -> infoPanel.requestFocusInWindow());

		infoPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				infoPanel.requestFocusInWindow();
			}
		});

		infoPanel.add(new JLabel("Record Name ", SwingConstants.LEFT));
		infoPanel.add(recordName);
		infoPanel.add(new JLabel("Record Income ", SwingConstants.LEFT));
		infoPanel.add(recordIncome);
		infoPanel.add(new JLabel("Record Saving ", SwingConstants.LEFT));
		infoPanel.add(recordSaving);
		infoPanel.add(new JLabel("Record Date ", SwingConstants.LEFT));
		infoPanel.add(recordCreationDate);
		infoPanel.add(new JLabel("Record Period ", SwingConstants.LEFT));
		infoPanel.add(periodSpinner);
		infoPanel.add(ptCb);
		infoPanel.add(addPtBtn);
		infoPanel.add(sortingBox);
		infoPanel.add(compareBtn);

		Border matteBorder = BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLACK);
		Border emptyBorder = BorderFactory.createEmptyBorder(10, 15, 10, 15);
		infoPanel.setBorder(BorderFactory.createCompoundBorder(matteBorder, emptyBorder));
		wrapperPanel.add(infoPanel);


//    REVIEW PANEL CENTER
		JPanel dataPanel = new JPanel(new BorderLayout());

		String[] columns = {"DATE", "CATEGORY", "AMOUNT"};

		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			int editableRow = -1;

			public boolean isCellEditable(int row, int column) {
//if (row == getRowCount() - 1)
// return row == getRowCount() - 1;
				return true;
//return row == editableRow;
			}

			public void setValueAt(Object value, int row, int col) {
				if (row >= allExpenses.size()) {
					super.setValueAt(value, row, col);
					return;
				}

				Expense originalExp = allExpenses.get(row);
				String valStr = String.valueOf(value).trim();

				if (col == 0) {
					if (valStr.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Invalid Date!");
						return;
					}
					valStr = valStr + "/" + record.getRecordPeriod().getYear();
					originalExp.setExpenseDate(LocalDate.parse(valStr, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy][d/M/yyyy][dd/M/yyyy][d/MM/yyyy]")));
				} else if (col == 1) {
					if (valStr.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Choose Category!");
						return;
					}
					originalExp.setExpenseCat(valStr);
				} else if (col == 2) {
					if (!Validation.isValidAmount(valStr)) {
						JOptionPane.showMessageDialog(null, "Invalid Amount!");
						return;
					}
					originalExp.setExpenseAmt(Double.parseDouble(valStr));
				}

				super.setValueAt(value, row, col);
				JsonManager.updateExpense(originalExp);
				new Synchronization("UPDATE_EXPENSE", null, originalExp);
				allExpenses = JsonManager.getExpenses(record);
				createOrRefreshChartPanel();
			}
		};

		sortingBox.addActionListener(e -> {
			String selectedType = (String) sortingBox.getSelectedItem();

			if (selectedType == null || selectedType.equals("Default")) {
				allExpenses = JsonManager.getExpenses(record);
			} else if (selectedType.equals("Chronological")) {
				allExpenses.sort(Comparator.comparing(Expense::getExpenseDate));
			} else if (selectedType.equals("Asc. Expense")) {
				allExpenses.sort(Comparator.comparingDouble(Expense::getExpenseAmt));
			} else if (selectedType.equals("Desc. Expense")) {
				allExpenses.sort(Comparator.comparingDouble(Expense::getExpenseAmt).reversed());
			} else {
				return;
			}

			model.setRowCount(0);
			for (Expense exp : allExpenses) {
				model.addRow(new Object[] {exp.getExpenseDate().getDayOfMonth() + "/" + exp.getExpenseDate().getMonthValue(), exp.getExpenseCat(), exp.getExpenseAmt()});
			}
		});

		for (Expense exp : allExpenses) {
			model.addRow(new Object[] {exp.getExpenseDate().getDayOfMonth() + "/" + exp.getExpenseDate().getMonthValue(), exp.getExpenseCat(), exp.getExpenseAmt()});
		}


		model.addRow(new Object[] {"", "", ""});

		JComboBox<String> inputF1 = new JComboBox<>();

		YearMonth yearMonth = YearMonth.of(record.getRecordPeriod().getYear(), record.getRecordPeriod().getMonthValue());
		int daysAmt = yearMonth.lengthOfMonth();

		for (int i = 1; i <= daysAmt; i++) {
			inputF1.addItem(i + "/" + record.getRecordPeriod().getMonthValue());
		}

		JComboBox<String> inputF2 = new JComboBox<>();

		ArrayList<String> cats = JsonManager.getCats();
		for (String str : cats) {
			inputF2.addItem(str);
		}

		JTextField inputF3 = new JTextField();

		JTable table = new JTable(model) {

			public TableCellEditor getCellEditor(int row, int column) {

				if (row == getRowCount() - 1 && column == 0) {
					return new DefaultCellEditor(inputF1);
				}

				if (column == 1) {
					return new DefaultCellEditor(inputF2);
				}

				if (row == getRowCount() - 1 && column == 2) {
					return new DefaultCellEditor(inputF3);
				}

				return super.getCellEditor(row, column);
			}
		};

//table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (row == table.getRowCount() - 1) {
					c.setBackground(Color.WHITE);
				} else if (row % 2 == 0) {
					c.setBackground(new Color(152, 202, 255));
				} else {
					c.setBackground(new Color(206, 230, 255));
				}

				return c;
			}
		});


		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		scrollPane.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				table.clearSelection();
			}
		});

		wrapperPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				table.clearSelection();
			}
		});

		wrapperPanel.add(scrollPane);
		wrapperPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        
        //    LIMITS
        JPanel limitPanel = new JPanel();
        limitPanel.setLayout(new BoxLayout(limitPanel, BoxLayout.Y_AXIS));
        
        ArrayList<Limit> allLimits = new ArrayList<>();

		for (Limit limit : JsonManager.getLimits()) {
			if (limit.getLimitRecordName().equals(record.getRecordName())) {
				allLimits.add(limit);
			}
		}

		chartsPanel.setLayout(new GridLayout(3 + allLimits.size(), 1, 0, 10));

		if (allLimits.size() > 0) {

			for (Limit limit : allLimits) {
				limitPanel.add(new JLabel("Limit: "+ limit.getLimitCat()));
				limitPanel.add(createLimitBar(limit));
			}
			wrapperPanel.add(limitPanel);

		}
        
		createOrRefreshChartPanel();
		wrapperPanel.add(chartsPanel);

//    REVIEW PANEL SOUTH
		JPanel opPanel = new JPanel(new CardLayout());

		JPanel op1Panel = new JPanel(new BorderLayout());
		JPanel op2Panel = new JPanel(new BorderLayout());

		JButton deleteBtn = new JButton("Delete");
		JButton createBtn = new JButton("Create Expense");
		JButton prevBtn = new JButton("<");


		deleteBtn.setBackground(Color.decode("#ed574c"));

		prevBtn.setBackground(Color.decode("#e8e8e8"));
		prevBtn.setForeground(Color.BLACK);
		prevBtn.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
		});

		createBtn.setBackground(Color.decode("#4487eb"));
		createBtn.setForeground(Color.WHITE);
		createBtn.addActionListener(e -> {

			if (table.isEditing()) {
				table.getCellEditor().stopCellEditing();
			}


			DefaultTableModel dtm = (DefaultTableModel) table.getModel();
			int lastRow = dtm.getRowCount() - 1;

			String date = String.valueOf(dtm.getValueAt(lastRow, 0)).trim();
			String category = String.valueOf(dtm.getValueAt(lastRow, 1)).trim();
			String amt = String.valueOf(dtm.getValueAt(lastRow, 2)).trim();

			JOptionPane jop = new JOptionPane();

			if (inputF2.getSelectedItem() != null) {
				category = inputF2.getSelectedItem().toString();
			}

			date = date + "/" + record.getRecordPeriod().getYear();
			//date = date.replaceAll("[\\/]", ".");
			System.out.println(date);
			if (!Validation.isValidDate(date)) {
				jop.showMessageDialog(null, "Date is not valid!");
			} else if (!Validation.isValidAmount(amt)) {
				jop.showMessageDialog(null, "Amount is not valid!");
			} else if (inputF2.getSelectedItem() == null) {
				jop.showMessageDialog(null, "Choose a category!");
			} else {
				Expense exp = new Expense(LocalDate.parse(date, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy][dd/MM/yyyy][d/MM/yyyy][dd/M/yyyy]")), category, Double.parseDouble(amt));
				exp.setExpenseRecordId(record.getRecordId());
				JsonManager.createExpense(exp);
				new Synchronization("CREATE_EXPENSE", null, exp);
				allExpenses.add(exp);
				dtm.insertRow(dtm.getRowCount() - 1, new Object[] {date, category, amt});

				dtm.setValueAt("", dtm.getRowCount() - 1, 0);
				dtm.setValueAt("", dtm.getRowCount() - 1, 1);
				dtm.setValueAt("", dtm.getRowCount() - 1, 2);
				inputF1.setSelectedIndex(-1);
				inputF2.setSelectedIndex(-1);
				inputF3.setText("");
				createOrRefreshChartPanel();
			}

			((CardLayout)(opPanel.getLayout())).show(opPanel, "OP_1");
		});

		op1Panel.add(createBtn, BorderLayout.CENTER);
		op1Panel.add(prevBtn, BorderLayout.EAST);
		op2Panel.add(deleteBtn, BorderLayout.CENTER);

		String OP_1 = "OP_1";
		String OP_2 = "OP_2";

		opPanel.add(op1Panel, OP_1);
		opPanel.add(op2Panel, OP_2);

		deleteBtn.addActionListener(ex -> {
			int selectedRow = table.getSelectedRow();
			int choice = JOptionPane.showConfirmDialog(null, "Are you sure delete Expense?", null, JOptionPane.YES_NO_OPTION);

			if (choice == JOptionPane.YES_OPTION) {
				JsonManager.deleteExpense(allExpenses.get(selectedRow));
				new Synchronization("DELETE_EXPENSE", null, allExpenses.get(selectedRow));
				allExpenses.remove(selectedRow);
				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				dtm.removeRow(selectedRow);
				table.clearSelection();
				createOrRefreshChartPanel();
			} else {

			}

		});

		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = table.getSelectedRow();
				int rowCount = model.getRowCount();

				if (selectedRow != -1 && selectedRow != (rowCount - 1)) {
					((CardLayout)(opPanel.getLayout())).show(opPanel, OP_2);

				} else {
					((CardLayout)(opPanel.getLayout())).show(opPanel, OP_1);
				}
			}
		});


		//        EXPORT PANEL

		JPanel exportPanel = new JPanel();
		JButton pdfBtn = new JButton("Export As Pdf");
		pdfBtn.setForeground(Color.WHITE);
		pdfBtn.setBackground(Color.decode("#FF2C2C"));

		pdfBtn.addActionListener(e -> {
			HashMap<String, Double> categorizedMap = new HashMap<>();
			allExpenses = JsonManager.getExpenses(record);

			for (Expense exp : allExpenses) {
				if (!categorizedMap.containsKey(exp.getExpenseCat())) {
					categorizedMap.put(exp.getExpenseCat(), Math.round(exp.getExpenseAmt() * 100) / 100.0);
				} else {
					categorizedMap.replace(exp.getExpenseCat(), categorizedMap.get(exp.getExpenseCat()) + (Math.round(exp.getExpenseAmt() * 100) / 100.0));
				}
			}

			PDFManager.createPdf(new Report(record, allExpenses, categorizedMap));
		});
		JButton xlsxBtn = new JButton("Export As Xlsx");

		xlsxBtn.setForeground(Color.WHITE);
		xlsxBtn.setBackground(Color.decode("#00B050"));
		xlsxBtn.addActionListener(e -> {
			HashMap<String, Double> categorizedMap = new HashMap<>();
			allExpenses = JsonManager.getExpenses(record);

			for (Expense exp : allExpenses) {
				if (!categorizedMap.containsKey(exp.getExpenseCat())) {
					categorizedMap.put(exp.getExpenseCat(), Math.round(exp.getExpenseAmt() * 100) / 100.0);
				} else {
					categorizedMap.replace(exp.getExpenseCat(), categorizedMap.get(exp.getExpenseCat()) + (Math.round(exp.getExpenseAmt() * 100) / 100.0));
				}
			}

			XlsxManager.createXlsx(record, allExpenses, categorizedMap);
		});

		exportPanel.add(pdfBtn);
		exportPanel.add(xlsxBtn);

		reviewScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		reviewScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		reviewScrollPane.setViewportView(wrapperPanel);
		reviewPanel.add(reviewScrollPane);
		reviewPanel.add(opPanel);
		reviewPanel.add(exportPanel);

	}

	public static void syncPanelConfig() {
		syncPanel.removeAll();
		syncPanel.setBackground(Color.decode("#e3e3e3"));
		syncPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#cccccc")));


		//    CENTER PANEL
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		JLabel countLbl = new JLabel(JsonManager.getOps().size() + " Operations Pending", SwingConstants.CENTER);
		JPanel lblPanel = new JPanel();
		lblPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
		lblPanel.add(countLbl);

		centerPanel.add(lblPanel);

		if (JsonManager.getOps().size() != 0) {
			String[] columns = {"Object", "Operation", "Detail"};
			DefaultTableModel dtm = new DefaultTableModel(columns, 0);
			ArrayList<Synchronization> allOps = JsonManager.getOps();
			for (Synchronization syncOp : allOps) {
				dtm.addRow(new Object[] {syncOp.getOp().split("_")[1], syncOp.getOp().split("_")[0], (syncOp.getRecord() != null ? syncOp.getRecord().getRecordName() : syncOp.getExpense().getExpenseDate() + " | " + syncOp.getExpense().getExpenseCat() + " | " + syncOp.getExpense().getExpenseAmt())});
			}
			JTable table = new JTable(dtm);
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			centerPanel.add(scrollPane);
		}
		//    SOUTH PANEL
		JPanel southPanel = new JPanel();


		JButton syncBtn = new JButton("Start Synchron");
		syncBtn.setForeground(Color.WHITE);
		syncBtn.setBackground(Color.decode("#4487eb"));
		syncBtn.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, DBManager.sync());
			rightPanelConfigurationMethod(rightPanel);
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, SYNC_PANEL);
		});

		JButton prevBtn = new JButton("<");
		prevBtn.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
		});

		if (JsonManager.getOps().size() != 0) {
			southPanel.add(syncBtn);
		}
		southPanel.add(prevBtn);

		syncPanel.add(centerPanel, BorderLayout.CENTER);
		syncPanel.add(southPanel, BorderLayout.SOUTH);

	}

	public static void createOrRefreshChartPanel() {
		chartsPanel.removeAll();
		//    CHARTS PANEL
		int chartWidth = (int)(dynamicWidth * 0.50);
		int chartHeight = (int)(dynamicHeight * 0.3);
		Dimension chartSize = new Dimension(chartWidth, chartHeight);


		chartsPanel.setPreferredSize(new Dimension(chartWidth, dynamicHeight));


		//    RESULTS
		if (JsonManager.getExpenses(record).size() > 0) {

			JPanel resultPanel = new JPanel(new GridLayout(4, 4, 50, 10));
			Border matteBorder = BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLACK);
			Border emptyBorder = BorderFactory.createEmptyBorder(10, 15, 10, 15);

			resultPanel.setBorder(BorderFactory.createCompoundBorder(matteBorder, emptyBorder));

			Report recordReport = new Report(record, JsonManager.getExpenses(), null);

			resultPanel.add(new JLabel("Total Expense", SwingConstants.LEFT));
			resultPanel.add(new JLabel(recordReport.getTotalExpense() + "", SwingConstants.RIGHT));
			resultPanel.add(new JLabel("Total Balance", SwingConstants.LEFT));
			resultPanel.add(new JLabel((record.getRecordIncome() + recordReport.getTotalExpense()) + "", SwingConstants.RIGHT));
			resultPanel.add(new JLabel("First Expense", SwingConstants.LEFT));
			resultPanel.add(new JLabel(recordReport.getFirstExpense().getExpenseDate() + "", SwingConstants.RIGHT));
			resultPanel.add(new JLabel("Last Expense", SwingConstants.LEFT));
			resultPanel.add(new JLabel(recordReport.getLastExpense().getExpenseDate() + "", SwingConstants.RIGHT));

			chartsPanel.add(resultPanel);
		}

		//    PIE CHART
		allExpenses = JsonManager.getExpenses(record);

		HashMap<String, Double> categorizedMap2 = new HashMap<>();

		for (Expense exp : allExpenses) {
			if (!categorizedMap2.containsKey(exp.getExpenseCat())) {
				categorizedMap2.put(exp.getExpenseCat(), exp.getExpenseAmt());
				continue;
			}
			categorizedMap2.put(exp.getExpenseCat(), categorizedMap2.get(exp.getExpenseCat()) + exp.getExpenseAmt());

		}

		for (Map.Entry<String, Double> es : categorizedMap2.entrySet()) {
			categorizedMap2.put(es.getKey(), Math.abs(categorizedMap2.get(es.getKey())));
		}

		DefaultPieDataset dpd = new DefaultPieDataset();

		for (Map.Entry<String, Double> es : categorizedMap2.entrySet()) {
			dpd.setValue(es.getKey(), es.getValue());
		}

		JFreeChart pieChart = ChartFactory.createPieChart("Expense Distribution", dpd, false, false, false);

		ChartPanel pieChartPanel = new ChartPanel(pieChart);
		pieChartPanel.setPreferredSize(chartSize);
		pieChartPanel.setMaximumSize(chartSize);
		chartsPanel.add(pieChartPanel);

		//    LINE CHART
		TreeMap<LocalDate, Double> categorizedMap3 = new TreeMap<>();

		for (Expense exp : allExpenses) {
			if (!categorizedMap3.containsKey(exp.getExpenseDate())) {
				categorizedMap3.put(exp.getExpenseDate(), exp.getExpenseAmt());
				continue;
			}

			categorizedMap3.put(exp.getExpenseDate(), categorizedMap3.get(exp.getExpenseDate()) + exp.getExpenseAmt());
		}



		DefaultCategoryDataset dcd = new DefaultCategoryDataset();

		for (Map.Entry<LocalDate, Double> es : categorizedMap3.entrySet()) {
			dcd.addValue(es.getValue(), "Total Expense", es.getKey());
		}

		JFreeChart lineChart = ChartFactory.createLineChart("Expense Distribution", "Days", "Amount", dcd);
		ChartPanel lineChartPanel = new ChartPanel(lineChart);
		pieChartPanel.setPreferredSize(chartSize);
		pieChartPanel.setMaximumSize(chartSize);
		chartsPanel.add(lineChartPanel);
	}

	public static void comparePanelConfig() {
		comparePanel.removeAll();
		comparePanel.setLayout(new BorderLayout());

		//    NORTH (RECORD SELECTION)
		JComboBox<String> recordBox = new JComboBox<>();

		recordBox.addItem("-- Select Record to Compare --");
		ArrayList<Record> allRecords = JsonManager.getRecords();
		for (Record rec : allRecords) {
			if (rec.getRecordId().equals(record.getRecordId())) {
				continue;
			}

			recordBox.addItem(rec.getRecordName());
		}

		JPanel northPanel = new JPanel();
		northPanel.add(recordBox);

		comparePanel.add(northPanel, BorderLayout.NORTH);

		//    CENTER (STATISTICS)
		String[] columns = {" ", "Record 1", "Record 2"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		JTable compareTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(compareTable);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		recordBox.addActionListener(e -> {
			Record selRecord = null;
			String selRecName = (String) recordBox.getSelectedItem();

			if (selRecName == null || selRecName.equals("-- Select Record to Compare --")) {
				model.setRowCount(0);
				return;
			}

			for (Record rec : JsonManager.getRecords()) {
				if (rec.getRecordName().equals(selRecName)) {
					selRecord = rec;
					break;
				}
			}

			if (selRecord != null) {
				model.setRowCount(0);
				ArrayList<Expense> recExpenses = JsonManager.getExpenses(record);
				ArrayList<Expense> selRecExpenses = JsonManager.getExpenses(selRecord);

				double totalAmt1 = 0;
				double totalAmt2 = 0;

				for (Expense exp : recExpenses) {
					totalAmt1 += exp.getExpenseAmt();
				}

				for (Expense exp : selRecExpenses) {
					totalAmt2 += exp.getExpenseAmt();
				}

				model.addRow(new Object[] {"Record Name", record.getRecordName(), selRecord.getRecordName()});
				model.addRow(new Object[] {"Record Period", record.getRecordPeriod().getMonthValue() + "/" + record.getRecordPeriod().getYear(), selRecord.getRecordPeriod().getMonthValue() + "/" + selRecord.getRecordPeriod().getYear()});
				model.addRow(new Object[] {"Record Income", record.getRecordIncome() + "", selRecord.getRecordIncome() + ""});
				model.addRow(new Object[] {"Record Expenses", totalAmt1 + "", totalAmt2 + ""});

			}

			comparePanel.revalidate();
			comparePanel.repaint();


		});



		comparePanel.add(scrollPane, BorderLayout.CENTER);

		//    SOUTH (Prev)

		JButton prevBtn = new JButton("<");
		prevBtn.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, REVIEW_PANEL);
		});

		comparePanel.add(prevBtn, BorderLayout.SOUTH);
	}

	public static JProgressBar createLimitBar(Limit limit) {
		double totalExpense = 0;

		for (Expense exp : JsonManager.getExpenses(record)) {
			if (limit.getLimitCat().equals("General")) {
				totalExpense += exp.getExpenseAmt();
			} else if (limit.getLimitCat().equals(exp.getExpenseCat())) {
			    totalExpense += exp.getExpenseAmt();

			}
		}

		JProgressBar progressBar = new JProgressBar(0, (int) limit.getLimitAmt());
		progressBar.setValue((int) totalExpense);
		progressBar.setStringPainted(true);
		progressBar.setString(limit.getLimitCat() + " : \t" + progressBar.getString());
		
		if (limit.getLimitAmt() > totalExpense) {
		    progressBar.setForeground(Color.GREEN);
		} else {
		    progressBar.setForeground(Color.RED);
		}

		return progressBar;

	}
}
