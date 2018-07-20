package stock_analysis;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.ImageObserver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

/**
 * Creates the GUI for a portfolio management and analysis program.
 * @author Gabe Argush
 *
 */
@SuppressWarnings("serial")
public class wireframe extends JFrame { 
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static JFrame frame;
	private JLabel labelTicker, labelDate, labelPort;
	private JButton buttonAdd, buttonRemoveSel, buttonRemoveAll, buttonToday;
	private JTextField fieldTicker, fieldDate;
	private JPanel panel1, panel2, panel3, panel4, panel5, panel6;
	
	private JMenuBar menuBar;
	private JButton addPortfolio;
	private JButton choosePortfolio;
	private JButton removePortfolio;
	private JButton analysis;
	private JMenu exit;
	private JMenuItem port1;
	private JMenuItem restart, close;
	
	double result, total, gpa, targetGpa;
	static String username = "";

	// ArrayLists to store information about each entry
	ArrayList<String> arrayTicker = new ArrayList<String>();
	ArrayList<String> arrayDates = new ArrayList<String>();
	
	static ArrayList<String> portfolios = new ArrayList<String>();
	Hashtable<String, String> users = new Hashtable<String, String>();
	static Hashtable<String, String> positions = new Hashtable<String, String>();
	
	static JTable table = null;
	static DefaultTableModel model = null;
	
	
	/**
	 * Instantiates the frame, different panels, buttons, textfields, etc.
	 * @throws FileNotFoundException
	 */
	public wireframe() throws FileNotFoundException {
		frame = new JFrame();
		this.setLayout(new BorderLayout());
		this.setTitle("Portfolio Analysis");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(580, 250, 700, 600);
		
		table = new JTable(new DefaultTableModel(new Object[]{"Ticker", "Date Added"}, 0));
		model = (DefaultTableModel) table.getModel();
		table.setFont(new Font("Serif", Font.PLAIN, 20));
		table.setRowHeight(35);
		
		labelDate = new JLabel("Date:");
		labelTicker = new JLabel("Ticker:");
		labelPort = new JLabel("Portfolio:");
		
		fieldTicker = new JTextField(9);	
		fieldDate = new JTextField(12);
		
		buttonAdd = new JButton("Add"); 

		buttonRemoveSel = new JButton("Remove Selected"); 
		buttonRemoveSel.setEnabled(false);
		buttonRemoveAll = new JButton("Remove All");
		buttonRemoveAll.setEnabled(false);
		
		buttonToday = new JButton("Today's Date");
		
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();
		panel6 = new JPanel();
		
		panel1.add(labelTicker);
		panel1.add(fieldTicker);
		panel1.add(labelDate);
		panel1.add(fieldDate);
		panel1.add(buttonToday);
		panel1.add(buttonAdd);		
		
		panel5.add(labelPort);

		panel2.add(new JScrollPane(table));
		table.setFillsViewportHeight(true);
	
		panel6.add(panel5, BorderLayout.NORTH);
		panel6.add(panel2, BorderLayout.SOUTH);

		panel3.add(panel1, BorderLayout.NORTH);
		panel3.add(panel6, BorderLayout.SOUTH);
		
		panel4.add(buttonRemoveSel);
		panel4.add(buttonRemoveAll);
		//panel3.setBackground(new Color(103,178,187));
		
		add(panel3, BorderLayout.NORTH);
		add(panel4, BorderLayout.SOUTH);
		panel3.setVisible(false);
		panel4.setVisible(false);
		frame.pack();
		
		menuBar = new JMenuBar();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		menuBar.setBorder(blackline);
		//menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setJMenuBar(menuBar);		
		
		port1 = new JMenuItem("");
	
		/**
		 * Allows the user to add a portfolio to their list.
		 */
		addPortfolio = new JButton(" Add Portfolio ");
		addPortfolio.setBackground(Color.white);
		addPortfolio.setOpaque(true);
		addPortfolio.setBorderPainted(false);
		addPortfolio.setMargin(new Insets(0, 0, 0, 0));
		addPortfolio.setFont(new Font("SansSerif", Font.PLAIN, 18));

		//addPortfolio.setBorder(blackline);
		menuBar.add(addPortfolio);
		addPortfolio.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//addPortfolio.setSelected(false);
				menuBar.setSelected(addPortfolio);
				
				String input = JOptionPane.showInputDialog("Portfolio Name: ");
				
				if(portfolios.contains(input)) {
					JOptionPane.showMessageDialog(frame, "Portfolio already exists!", "Portfolio Error", JOptionPane.WARNING_MESSAGE);
				} else if(input!=null) {
					removal();
					port1 = new JMenuItem();
					port1.setText(input);
					port1.setVisible(true);
					portfolios.add(port1.getText());
					labelPort.setText(input);
					buttonRemoveSel.setEnabled(true);
					buttonRemoveAll.setEnabled(true);
					panel3.setVisible(true);
					panel4.setVisible(true);
					panel6.setVisible(true);

					try {
						databaseGet(username, port1.getText());
					} catch (ClassNotFoundException | SQLException e1) {
						e1.printStackTrace();
					}

				} else {
					JOptionPane.showMessageDialog(frame, "Error: Enter Portfolio Name", "Input Error", 
							JOptionPane.ERROR_MESSAGE);
				}

				fieldTicker.requestFocusInWindow();
			}
		});
		menuBar.add(Box.createHorizontalStrut(12));


		choosePortfolio = new JButton("Choose Portfolio");
		choosePortfolio.setBackground(Color.white);
		choosePortfolio.setOpaque(true);
		choosePortfolio.setBorderPainted(false);
		choosePortfolio.setMargin(new Insets(0, 0, 0, 0));
		choosePortfolio.setFont(new Font("SansSerif", Font.PLAIN, 18));

		menuBar.add(choosePortfolio);
		choosePortfolio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(portfolios.size() > 0) {
					int input = JOptionPane.showOptionDialog(frame, "Which portfolio would you like to use?", 
							"Portfolios", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
							null, portfolios.toArray(new String[portfolios.size()]), portfolios.get(0));
					if(input > -1) {
						removal();
						port1.setText(portfolios.get(input));;
						labelPort.setText(portfolios.get(input)); 
						try {
							databaseGet(username, labelPort.getText());
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
					}
					
					if(portfolios.size() > 0 && input > -1) {
						buttonRemoveSel.setEnabled(true);
						buttonRemoveAll.setEnabled(true);
						panel3.setVisible(true);
						panel4.setVisible(true);
						panel6.setVisible(true);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Add a portfolio to begin.");
				}
				fieldTicker.requestFocusInWindow();
			}	
		});
		
		menuBar.add(Box.createHorizontalStrut(12));
		
		/**
		 * Allows the user to remove a portfolio from their list.
		 */
		removePortfolio = new JButton("Remove Portfolio");
		removePortfolio.setBackground(Color.white);
		removePortfolio.setOpaque(true);
		removePortfolio.setBorderPainted(false);
		removePortfolio.setMargin(new Insets(0, 0, 0, 0));
		removePortfolio.setFont(new Font("SansSerif", Font.PLAIN, 18));
		
		
		menuBar.add(removePortfolio);
		removePortfolio.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(portfolios.size() > 0) {
					String[] arrayPort = new String[portfolios.size()];
					for(int i = 0; i < portfolios.size(); i++) {
						arrayPort[i] = portfolios.get(i);
					}
					int input2 = JOptionPane.showOptionDialog(frame, "Which portfolio would you like to delete?", 
							"Remove Portfolio", JOptionPane.DEFAULT_OPTION, 
							JOptionPane.QUESTION_MESSAGE, null, arrayPort, arrayPort[0]);
					if(input2 > -1) {
						portfolios.remove(input2);
						//choosePortfolio.remove(input2);
					}
				}
			}		
		});
		
		menuBar.add(Box.createHorizontalStrut(12));
		//menuBar.add(new JSeparator(SwingConstants.NORTH));

		
		analysis = new JButton("Analysis");
		analysis.setBackground(Color.white);
		analysis.setOpaque(true);
		analysis.setBorderPainted(false);
		analysis.setMargin(new Insets(0, 0, 0, 0));
		analysis.setFont(new Font("SansSerif", Font.PLAIN, 18));
			
		menuBar.add(analysis);
		analysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(positions.size() > 0) {
					
					String[] argos = {"123"};
					TabbedPane.main(argos, positions);
					
					Object[] temp = positions.keySet().toArray();
					try {
						stockChecker.analysis(Arrays.asList(temp).toArray(new String[temp.length]));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(frame, "You need to add positions to analyze!", "Empty Portfolio Error", 
							JOptionPane.ERROR_MESSAGE, icons("resources/empty.jpg", 5, 5));
				
				}
			}
			
			
		});	
		
		menuBar.add(Box.createHorizontalStrut(12));
		
		exit = new JMenu("Exit");
		menuBar.add(exit);
		restart = new JMenuItem("Restart");
		close = new JMenuItem("Close");
		exit.add(restart);		
		exit.addSeparator();
		exit.add(close);
		
		/**
		 * Exit the application.
		 */
		exit.addMenuListener(new MenuListener() {
			@Override
			public void menuCanceled(MenuEvent e) {
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuSelected(MenuEvent e) {

			}			
		});

		//restart.setEnabled(false);
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("Restarting...");
					username = "";
					portfolios.clear();
					positions.clear();
					panel3.setVisible(false);
					loginSystem();
					updatePorts();
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Goodbye!");
				System.exit(0);
			}
		});
		
		/**
		 * Add a stock and add date to their portfolio, and send the information to a database.
		 */
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(fieldTicker.getText().length() > 0 && fieldTicker.getText().length() < 6) {  
					fieldTicker.setText(fieldTicker.getText().toUpperCase());
					if(fieldDate.getText() == "") {
						fieldDate.setText(null);
					}
					model.addRow(new Object[] {fieldTicker.getText(), fieldDate.getText()}); 
					positions.put(fieldTicker.getText(), fieldDate.getText());
					
				} 
				
				try {
					databaseAdd(username, port1.getText(), fieldTicker.getText(), fieldDate.getText());
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				
				fieldTicker.setText("");
				fieldDate.setText("");;  // reset text boxes
			}
		});
		
		
		buttonToday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				ZoneId zonedId = ZoneId.of("America/New_York");
				LocalDate today = LocalDate.now( zonedId );
				fieldDate.setText(today.toString());
			}			
		});
		
		
		/**
		 * Remove selected stocks from the user's portfolio.
		 */
		buttonRemoveSel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int response = -1;
				if (table.getSelectedRows().length > 0) {
					response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete these positions?",
							"Confirm Selection", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
				}
				
				if (response == 0) {
					int[] rows = table.getSelectedRows();
					for (int i = 0; i < rows.length; i++) {
						try {

							databaseRemove(username, labelPort.getText(), model.getValueAt(rows[i] - i, 0).toString(),
									model.getValueAt(rows[i] - i, 1).toString());
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
					} 
				}	
			}
		});

		ActionListener removal = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete all positions?", 
						"Confirm Selection", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				if (response == 0) {
					table.selectAll();
					int[] rows = table.getSelectedRows();
					for (int i = 0; i < rows.length; i++) {
						try {
							databaseRemove(username, labelPort.getText(), model.getValueAt(rows[i] - i, 0).toString(),
									model.getValueAt(rows[i] - i, 1).toString());

						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
						}
					} 
				}
			}
		};		
		
		/**
		 * Remove all stocks in the user's portfolio.
		 */
		buttonRemoveAll.addActionListener(removal);

		setVisible(true);
	}
	
	/**
	 * Creates the login system for the application.
	 * Register: Creates a new user and password, adds the user to the list of users able to use the application.
	 * Log In: Log into an existing account with username and password.
	 * @throws IOException
	 */
	/**
	 * @throws IOException
	 * @throws AWTException 
	 */
	@SuppressWarnings("null")
	private static void loginSystem() throws IOException, AWTException {
		{
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		loginPanel.setLayout(new FlowLayout());
		String[] items = {"Log In" , "Register"};
		JComboBox<String> box = new JComboBox<String>(items);
		box.setEditable(false);
		loginPanel.add(box);

		JSeparator sep = new JSeparator(SwingConstants.VERTICAL);

		Dimension d = sep.getPreferredSize();
		Dimension f = box.getPreferredSize();
		d.height = box.getPreferredSize().height * 12;
		f.height = box.getPreferredSize().height * 2;
		f.width = box.getPreferredSize().width;
		sep.setPreferredSize(d);
		
		box.setPreferredSize(f);

		JTextField user = new JTextField(20);
				
		JPasswordField pass = new JPasswordField(20);
		//pass.setEchoChar('\u259F');
		//pass.setActionCommand("OK");
		loginPanel.add(new JLabel("Username: "), FlowLayout.CENTER);
		loginPanel.add(user);
		loginPanel.add(sep, "span");
		user.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(user.getText().equals("Please input username")) {
					user.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(user.getText().equals("")) {
					user.setText("Please input username");
				}
			}		
		});
		
		loginPanel.add(Box.createVerticalStrut(15));
		loginPanel.add(new JLabel("Password: "));
		loginPanel.add(pass);
		
		int w = JOptionPane.showConfirmDialog(null, loginPanel, " Log in/ Register", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE, icons("resources/rotunda-beginning.jpg", 1, 1));
		
		
		Login newUser = null;
		Map<String, String> users = new HashMap<String, String>();
		ArrayList<String> usernames = new ArrayList<String>();
		
		if(w == -1 || w == 2) {
			System.exit(NORMAL);
		}
		
		if(box.getSelectedItem().equals("Log In")) {
			String csvPath = "/Users/Gabe Argush/Desktop/logins.csv";

			CSVReader reader = null;

			reader = new CSVReader(new FileReader(csvPath), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1);

			String[] record;
			while ((record = reader.readNext()) != null) {
				String username = record[0];
				String password = record[1];
				usernames.add(username);
				users.put(username, password);			
			}
			
			newUser = new Login(user.getText(), pass.getPassword().toString());
			String newPass = "";
			String passText = "";

			if(!users.containsKey(user.getText())) {
				JOptionPane.showMessageDialog(null, "No account with that username was found.", "Account Error", 
						JOptionPane.ERROR_MESSAGE);
				System.out.println("Users on file: " + usernames);
				loginSystem();
			} else if (users.containsKey(user.getText())) {
				newPass = users.get(user.getText());
				passText = new String(pass.getPassword());
				if(!passText.equals(newPass)) {
					
					JOptionPane.showMessageDialog(null, "Password is incorrect.", "Account Error", JOptionPane.ERROR_MESSAGE);
					
					loginSystem();
				}
			}
			if(users.containsKey(newUser.getUsername()) && passText.equals(newPass)) {
				username = newUser.getUsername();
				System.out.println("Welcome, " + username + "!");
				//Window.main(username);
			}

			reader.close();

		} else if (box.getSelectedItem().equals("Register")) {

			String csvPath = "/Users/Gabe Argush/Desktop/logins.csv";

			CSVReader reader = null;

			reader = new CSVReader(new FileReader(csvPath), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
			newUser = new Login(user.getText(), pass.getPassword().toString());

			String[] record;
			while ((record = reader.readNext()) != null) {
				String username1 = record[0];
				//String password1 = record[1];
				if(username1.equals(user.getText())) {
					JOptionPane.showMessageDialog(null, "That username is already on file.", "Account Error", 
							JOptionPane.ERROR_MESSAGE);
					loginSystem();
				}
			}
			
			reader.close();
			username = newUser.getUsername();
			
			File loginFile = new File("/Users/Gabe Argush/Desktop/logins.csv");

			try{
				String passText = new String(pass.getPassword());
				FileWriter fileWriter = new FileWriter(loginFile, true);

				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(newUser.getUsername() + "," + passText + "\n");
				bufferedWriter.close();
				
				System.out.println("Account info added to the system.");
			} catch(IOException e) {
				System.out.println("Account info could not be added to the system.");
			}
		}
		
	}

	/**
	 * Method to send information about an added stock to a database.
	 * @param user
	 * @param portName
	 * @param ticker
	 * @param dateAdded
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void databaseAdd(String user, String portName, String ticker, String dateAdded) 
			throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String host = "localhost";
		int port = 5432; 
		String database = "stock_analysis";
		String url = "jdbc:postgresql://" + host + ":" + port  + "/" + database;
		
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","c0nc0rd777");
		props.setProperty("ssl","false");
		Connection connection = DriverManager.getConnection(url, props);
		
		Statement insert = connection.createStatement();
		insert.execute("INSERT INTO portfolios.portfolio (username, portName, ticker, dateAdded) "
				+ "values ('" + user + "', '" + portName + "', '" + ticker + "','" + dateAdded + "')");
		insert.close();
	}

	/**
	 * Retrieves the data entries in a database which match the username and portfolio name supplied.
	 * @param user
	 * @param portName
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static void databaseGet(String user, String portName) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		String host = "localhost";
		int port = 5432; 
		String database = "stock_analysis";
		String url = "jdbc:postgresql://" + host + ":" + port  + "/" + database;
		ResultSet results = null;
		
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","c0nc0rd777");
		props.setProperty("ssl","false");
		Connection connection = DriverManager.getConnection(url, props);
		
		
		Statement insert = connection.createStatement();
		if(!portName.equals("ANY")) {
			results = insert.executeQuery("SELECT ticker, dateAdded FROM portfolios.portfolio WHERE username = '" + 
					user + "' AND portname = '" + portName + "';");
		} else {
			results = insert.executeQuery("SELECT portName FROM portfolios.portfolio WHERE username = '" + user + "'");
		}

		while(results.next()) {
			if(!portName.equals("ANY")) {
				model.addRow(new Object[] {results.getString(1), results.getString(2)}); 
				positions.put(results.getString(1).toUpperCase(), results.getString(2));
			} else {
				if(!portfolios.contains(results.getString(1))) {
					portfolios.add(results.getString(1));
				} 
			}
		}
		insert.close();
	}
	
	/**
	 * Deletes entries from a database given all method parameters match the entry/entries.
	 * @param user
	 * @param portName
	 * @param ticker
	 * @param dateAdded
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static void databaseRemove(String user, String portName, String ticker, String dateAdded) 
			throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		String host = "localhost";
		int port = 5432; 
		String database = "stock_analysis";
		String url = "jdbc:postgresql://" + host + ":" + port  + "/" + database;
		@SuppressWarnings("unused")
		int results = 0;
		
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","c0nc0rd777");
		props.setProperty("ssl","false");
		Connection connection = DriverManager.getConnection(url, props);
		
		Statement insert = connection.createStatement();

		results = insert.executeUpdate("DELETE FROM portfolios.portfolio WHERE username = '" + user + "' AND "
					+ "portname = '" + portName + "' AND ticker = '" + ticker + "' AND dateAdded = '" + dateAdded + "';");
		
		for(int i = 0; i < model.getRowCount(); i++) {
			if(model.getValueAt(i, 0).equals(ticker) && model.getValueAt(i, 1).equals(dateAdded)) {
				positions.remove(model.getValueAt(i, 0).toString());
				model.removeRow(i);
			}
		}

		insert.close();
	}

	/**
	 * Scales pictures down to be used for icons.
	 * @param path
	 * @param xScaleDown
	 * @param yScaleDown
	 * @return
	 */
	private static ImageIcon icons(String path, int xScaleDown, int yScaleDown) {
		ImageIcon icon = new ImageIcon(path);
		int h = icon.getIconHeight();
		int w = icon.getIconWidth();
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(w / xScaleDown, h / yScaleDown, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		return newIcon;
	}

	/**
	 * Method to remove all entries in a table.
	 */
	private static void removal() {
		table.selectAll();
		int[] rows = table.getSelectedRows();
		for(int i = 0; i < rows.length; i++){
			model.removeRow(rows[i]-i);
		}
		positions.clear();
	}
	
	/**
	 * Update user's portfolios if they have created any in the past (instead of creating new portfolio)
	 */
	public static void updatePorts() {
		try {
			databaseGet(username, "ANY");
			for(String entry: portfolios) {
				JMenuItem port5 = new JMenuItem(entry);
				port5.setVisible(true);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Run the program.
	 * @param args
	 * @throws Exception  
	 */
	public static void main(String[] args) throws Exception{	
		loginSystem();
		updatePorts();
		new wireframe();
	}
	
}

/**
 * Class to create the Tabbed Pane for stock analysis.
 * @author Gabe Argush
 *
 */
@SuppressWarnings("serial")
class TabbedPane extends JFrame{
	JPanel panel = new JPanel();
	JPanel panel2 = new JPanel();
	JTextField text1 = new JTextField("");
	JTextField text2 = new JTextField("");
	JTabbedPane pane = new JTabbedPane();
	static String[] titles = { "Ticker", "P/E Ratio", "% Volume Diff",  "Stock Price", "Previous Close", "% Change Today" };

	public TabbedPane(Hashtable<String, String> positions){
		{
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JTabbedPane tb = new JTabbedPane();
		tb.setSize(900, 1000);
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		tb.addTab("Performance", panel);
		tb.addTab("Analysis", panel2);
		JLabel text = new JLabel();
		JTable table = new JTable(new DefaultTableModel(new Object[]{titles[0], titles[1], titles[2], titles[3], 
				titles[4], titles[5]}, 0) {
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.setFillsViewportHeight(true);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(90);
		columnModel.getColumn(1).setPreferredWidth(160);
		columnModel.getColumn(2).setPreferredWidth(175);
		columnModel.getColumn(3).setPreferredWidth(175);
		columnModel.getColumn(4).setPreferredWidth(175);
		columnModel.getColumn(5).setPreferredWidth(200);

		table.setFont(new Font("Monospaced Bold", Font.PLAIN, 18));
		table.setRowHeight(table.getRowHeight() + 10);
		table.setSize(ImageObserver.WIDTH + 300, ImageObserver.HEIGHT);
		panel.add(text);
		JScrollPane tble = new JScrollPane(table);
		Dimension r = tble.getPreferredSize();
		r.width = table.getWidth() + 600;
		tble.setPreferredSize(r);
		panel.add(tble);
		Date now = Calendar.getInstance().getTime();
		DateFormat time = new SimpleDateFormat("hh:mm:ss a");
		String s = time.format(now);
		JLabel timeLabel = new JLabel("Time taken: " + s);
		panel.add(timeLabel);
		
		text.setText("Total number of stocks: " + positions.size());
		Object[] temp = positions.keySet().toArray();		
			
		ArrayList<Stock> entries = null;
		try {
			entries = stockChecker.analysis(Arrays.asList(temp).toArray(new String[temp.length]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		DecimalFormat form = new DecimalFormat("0.00");
		for(Stock entry: entries) {
			model.addRow(new Object[] {entry.getTicker(), form.format(entry.getPeRatio()), form.format(entry.getPerVolume()), 
					form.format(entry.getPrice()), form.format(entry.getPrevClose()), form.format(entry.getPerToday())});
		}
		add(tb);

	}

	public static void main(String[] args, Hashtable<String, String> positions) {
		TabbedPane tb = new TabbedPane(positions);
		tb.setSize(1000, 1000);
		UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab.contentMargins", new Insets(10, 100, 0, 0));
		tb.setVisible(true);
	}
}