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

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.*;

public class UIManager {
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int dynamicWidth = (int)(screenSize.width * 0.60);
	private static final int dynamicHeight = (int)(screenSize.height * 0.70);
	private static final String LOGIN_PANEL = "LOGIN_PANEL";
	private static final String RECORDS_PANEL = "RECORDS_PANEL";
	private static final String CREATE_RECORD_PANEL = "CREATE_RECORD_PANEL";
	private static final String SETTINGS_PANEL = "SETTINGS_PANEL";
	private static final String REVIEW_PANEL = "REVIEW_PANEL";
	private static JPanel rightPanel;
	private static JPanel recordsPanel;

	private static JPanel reviewPanel = new JPanel();
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

		leftPanelButtonConfigruations(leftPanel, synchronizationButton);
		leftPanelButtonConfigruations(leftPanel, settingsButton);

		synchronizationButton.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, CREATE_RECORD_PANEL);
		});

		settingsButton.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
		});


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
		JPanel settingsPanel = new JPanel(new BorderLayout());

		recordsPanelConfig(recordsPanel);
		createRecordPanelConfig(createRecordPanel);
		settingsPanelConfig(settingsPanel);

		rightPanel.add(recordsPanel, RECORDS_PANEL);
		rightPanel.add(createRecordPanel, CREATE_RECORD_PANEL);
		rightPanel.add(settingsPanel, SETTINGS_PANEL);
		rightPanel.add(reviewPanel, REVIEW_PANEL);



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
		createRecordPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
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
		gbc.gridy = 3;
		JButton createBtn = new JButton("Create");
		createBtn.setForeground(Color.WHITE);
		createBtn.setBackground(Color.decode("#4487eb"));
		createBtn.addActionListener(e -> {
		    if (!Validation.isValidName(recordNameField.getText().trim(), allRecords)) {
		        JOptionPane.showMessageDialog(null, "Record Name is Already Exists!");
		    } else if (!Validation.isValidAmount(recordIncomeField.getText().trim()) || !Validation.isValidAmount(recordSavingField.getText().trim())) {
		        JOptionPane.showMessageDialog(null, "Input Amounts Must Be Consist Of Digits!");
		    } else {
		        Record newRecord = new Record(recordNameField.getText().trim(), Double.parseDouble(recordIncomeField.getText().trim()), Double.parseDouble(recordSavingField.getText().trim()));
		        newRecord.setRecordId(JsonManager.getMinIdRecord());
		        JsonManager.createRecord(newRecord);
		        allRecords.add(newRecord);
		        recordNameField.setText("");
		        recordIncomeField.setText("");
		        recordSavingField.setText("");
		        recordsPanelConfig(recordsPanel);
		        ((CardLayout) (rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
		    }
		});
		panel.add(createBtn, gbc);

		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#cccccc")));


	}

	private static void settingsPanelConfig(JPanel panel) {
		panel.setBackground(Color.decode("#e3e3e3"));

	}

	private static void reviewPanelConfig() {
		//reviewPanel = new JPanel(new BorderLayout());
		reviewPanel.setLayout(new BorderLayout());
		reviewPanel.removeAll();
		reviewPanel.setBackground(Color.decode("#e3e3e3"));
		reviewPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#cccccc")));


		//    REVIEW PANEL NORTH
		JTextField recordName = new JTextField(record.getRecordName(), SwingConstants.RIGHT);
		JLabel recordCreationDate = new JLabel(record.getCreationDate().substring(0, 16), SwingConstants.RIGHT);
		JTextField recordIncome = new JTextField(record.getRecordIncome() + "", SwingConstants.RIGHT);
		JTextField recordSaving = new JTextField(record.getRecordSaving() + "", SwingConstants.RIGHT);

		recordName.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {

				allRecords = JsonManager.getRecords();
				if (Validation.isValidName(recordName.getText().trim(), allRecords)) {
					record.setRecordName(recordName.getText().trim());
					JsonManager.updateRecord(record);
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
				    recordSaving.setText(record.getRecordSaving() + "");
				    recordsPanelConfig(recordsPanel);
				} else {
				    recordSaving.setText(record.getRecordSaving() + "");
				}
			}
		});


		JPanel infoPanel = new JPanel(new GridLayout(4, 2, 30, 10));

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

		Border matteBorder = BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLACK);
		Border emptyBorder = BorderFactory.createEmptyBorder(10, 15, 10, 15);
		infoPanel.setBorder(BorderFactory.createCompoundBorder(matteBorder, emptyBorder));
		//infoPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		reviewPanel.add(infoPanel, BorderLayout.NORTH);


		//    REVIEW PANEL CENTER
		JPanel dataPanel = new JPanel(new BorderLayout());

		String[] columns = {"DATE", "CATEGORY", "AMOUNT"};

		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			int editableRow = -1;

			public boolean isCellEditable(int row, int column) {
				//if (row == getRowCount() - 1)
//					return row == getRowCount() - 1;
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
					if (!Validation.isValidDate(valStr)) {
						JOptionPane.showMessageDialog(null, "Invalid Date!");
						return; // super.setValueAt ÇAĞRILMAZ -> Hücre eski değerinde kalır!
					}
					originalExp.setExpenseDate(LocalDate.parse(valStr, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy]")));
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
				allExpenses = JsonManager.getExpenses(record);
			}
		};

		for (Expense exp : allExpenses) {
			model.addRow(new Object[] {exp.getExpenseDate(), exp.getExpenseCat(), exp.getExpenseAmt()});
		}


		model.addRow(new Object[] {"", "", ""});

		JTextField inputF1 = new JTextField();
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

		reviewPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				table.clearSelection();
			}
		});

		reviewPanel.add(scrollPane, BorderLayout.CENTER);


		//    REVIEW PANEL SOUTH
		JPanel opPanel = new JPanel(new CardLayout());

		JPanel op1Panel = new JPanel(new BorderLayout());
		JPanel op2Panel = new JPanel(new BorderLayout());

		JButton deleteBtn = new JButton("Delete");
		JButton createBtn = new JButton("Create Expense");
		JButton prevBtn = new JButton("<");


		deleteBtn.setBackground(Color.decode("#ed574c"));
        
        prevBtn.setBackground(Color.decode("#e8e8e8"));
        prevBtn.setForeground(Color.WHITE);
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

			if (!Validation.isValidDate(date)) {
				jop.showMessageDialog(null, "Date is not valid!");
			} else if (!Validation.isValidAmount(amt)) {
				jop.showMessageDialog(null, "Amount is not valid!");
			} else if (inputF2.getSelectedItem() == null) {
				jop.showMessageDialog(null, "Choose a category!");
			} else {
				Expense exp = new Expense(LocalDate.parse(date, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy]")), category, Double.parseDouble(amt));
				exp.setExpenseRecordId(record.getRecordId());
				exp.setExpenseId(JsonManager.getMinIdExpense());
				JsonManager.createExpense(exp);
				allExpenses.add(exp);
				dtm.insertRow(dtm.getRowCount() - 1, new Object[] {date, category, amt});

				dtm.setValueAt("", dtm.getRowCount() - 1, 0);
				dtm.setValueAt("", dtm.getRowCount() - 1, 1);
				dtm.setValueAt("", dtm.getRowCount() - 1, 2);
				inputF1.setText("");
				inputF2.setSelectedIndex(-1);
				inputF3.setText("");
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
				allExpenses.remove(selectedRow);
				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				dtm.removeRow(selectedRow);
				table.clearSelection();
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

		reviewPanel.add(opPanel, BorderLayout.SOUTH);
	}
}