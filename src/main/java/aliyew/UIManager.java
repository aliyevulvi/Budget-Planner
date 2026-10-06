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
import javax.swing.table.*;

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

	private static JPanel reviewPanel = new JPanel();
	private static Record record = new Record();
	private static ArrayList<Expense> allExpenses = new ArrayList<>();

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

		JButton recordsButton = new JButton("Records");
		JButton createRecordButton = new JButton("Create Record");
		JButton synchronizationButton = new JButton("Synchronization");
		JButton settingsButton = new JButton("Settings");

		leftPanelButtonConfigruations(leftPanel, recordsButton);
		leftPanelButtonConfigruations(leftPanel, createRecordButton);
		leftPanelButtonConfigruations(leftPanel, synchronizationButton);
		leftPanelButtonConfigruations(leftPanel, settingsButton);

		recordsButton.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, RECORDS_PANEL);
		});

		createRecordButton.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, CREATE_RECORD_PANEL);
		});

		settingsButton.addActionListener(e -> {
			((CardLayout)(rightPanel.getLayout())).show(rightPanel, SETTINGS_PANEL);
		});


	}

	public static void leftPanelButtonConfigruations(JPanel jPanel, JButton btn) {
		btn.setBackground(Color.decode("#124559"));
		btn.setAlignmentX(JButton.CENTER_ALIGNMENT);
		//btn.setBorderPainted(false);
		btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#000000")));
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
		rightPanel.setBackground(Color.decode("#f1f1f1"));
		rightPanel.setBorder(BorderFactory.createEmptyBorder((int)(dynamicHeight * 0.05), (int)(dynamicWidth * 0.05), (int)(dynamicHeight * 0.05), (int)(dynamicWidth * 0.05)));


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
		panel.add(scrollPane, BorderLayout.CENTER);
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

		JLabel recordName = new JLabel(record.getRecordName(), SwingConstants.RIGHT);
		JLabel recordCreationDate = new JLabel(record.getCreationDate().substring(0, 15), SwingConstants.RIGHT);
		JLabel recordIncome = new JLabel(record.getRecordIncome() + "", SwingConstants.RIGHT);
		JLabel recordSaving = new JLabel(record.getRecordSaving() + "", SwingConstants.RIGHT);

		JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));

		infoPanel.add(new JLabel("Record Name ", SwingConstants.LEFT));
		infoPanel.add(recordName);
		infoPanel.add(new JLabel("Record Date ", SwingConstants.LEFT));
		infoPanel.add(recordCreationDate);
		infoPanel.add(new JLabel("Record Income ", SwingConstants.LEFT));
		infoPanel.add(recordIncome);
		infoPanel.add(new JLabel("Record Saving ", SwingConstants.LEFT));
		infoPanel.add(recordSaving);
		infoPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		reviewPanel.add(infoPanel, BorderLayout.NORTH);

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
				if (row == getRowCount() - 1) {
					super.setValueAt(value, row, col);
				}

				Expense originalExp = allExpenses.get(row);
				String valStr = String.valueOf(value).trim();

				if (col == 0) {
					if (!Validation.isValidDate(valStr)) {
						JOptionPane.showMessageDialog(null, "Geçersiz Tarih!");
						return; // super.setValueAt ÇAĞRILMAZ -> Hücre eski değerinde kalır!
					}
					//originalExp.setExpenseDate(LocalDate.parse(valStr, DateTimeFormatter.ofPattern("[dd.MM.yy][yyyy-MM-dd][d.M.yy][dd.M.yy][d.MM.yy]")));
				}
				else if (col == 1) {
					if (valStr.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Kategori seçiniz!");
						return;
					}
					//originalExp.setExpenseCat(valStr);
				}
				else if (col == 2) {
					if (!Validation.isValidAmount(valStr)) {
						JOptionPane.showMessageDialog(null, "Geçersiz Miktar!");
						return;
					}
					//originalExp.setExpenseAmt(Double.parseDouble(valStr));
				}

				super.setValueAt(value, row, col);
				//JsonManager.updateExpense(originalExp);
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


		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		reviewPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel opPanel = new JPanel(new CardLayout());

		JPanel op1Panel = new JPanel();
		JPanel op2Panel = new JPanel();

		JButton deleteBtn = new JButton("Delete");
		JButton updateBtn = new JButton("Update");
		JButton createBtn = new JButton("Create Expense");
		JButton quitBtn = new JButton("Quit");

		deleteBtn.setBackground(Color.decode("#ed574c"));
		deleteBtn.setForeground(Color.WHITE);
		updateBtn.setBackground(Color.decode("#4487eb"));
		updateBtn.setForeground(Color.WHITE);
		updateBtn.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();



		});

		createBtn.setBackground(Color.decode("#4487eb"));
		createBtn.setForeground(Color.WHITE);
		createBtn.addActionListener(e -> {
			String date = inputF1.getText().trim();
			String amt = inputF3.getText().trim();

			String category = "";

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

				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				dtm.insertRow(dtm.getRowCount() - 1, new Object[] {date, category, amt});

			}

			inputF1.setText("");
			inputF2.setSelectedIndex(-1);
			inputF3.setText("");
		});

		op1Panel.add(createBtn);
		op2Panel.add(quitBtn);
		op2Panel.add(deleteBtn);
		op2Panel.add(updateBtn);

		String OP_1 = "OP_1";
		String OP_2 = "OP_2";

		opPanel.add(op1Panel, OP_1);
		opPanel.add(op2Panel, OP_2);

		quitBtn.addActionListener(e -> {
			table.clearSelection();
			((CardLayout)(opPanel.getLayout())).show(opPanel, OP_1);
		});

		deleteBtn.addActionListener(ex -> {
			int selectedRow = table.getSelectedRow();
			int choice = JOptionPane.showConfirmDialog(null, "Are you sure delete Expense?", null, JOptionPane.YES_NO_OPTION);

			if (choice == JOptionPane.YES_OPTION) {
				JsonManager.deleteExpense(allExpenses.get(selectedRow));

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