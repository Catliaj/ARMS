package main;

import java.awt.Color;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import componentsUI.Header;
import componentsUI.RoundedButton;
import componentsUI.FrameDragUtility;
import componentsUI.RoundedPanel;
import componentsUI.SidebarPanel;
import dbConnection.DatabaseConnection;
import componentsUI.BackgroundPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;



public class Pay extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private BackgroundPanel photoPanel;
	private JTable tablePayBills;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pay frame = new Pay();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Pay() {
		setTitle("Apartment Rentals and Facilities Billing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1300, 800); // Fixed size
        setLocationRelativeTo(null); // Center the window
        setUndecorated(true); // Remove the border
        
        new FrameDragUtility(this);
        
    

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 238, 226));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        Header header = new Header(this);
        getContentPane().add(header);
        
        SidebarPanel sidebar = new SidebarPanel(this, "Pay");
        getContentPane().add(sidebar);
        
        RoundedPanel mainPanel = new RoundedPanel(30);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(301, 76, 949, 724);
        mainPanel.setLayout(null); // Absolute positioning
        contentPane.add(mainPanel);
        
        photoPanel = new BackgroundPanel("/images/interior2.png");
        photoPanel.setBackground(Color.RED);
        photoPanel.setBounds(251, 26, 1049, 774);
        photoPanel.setLayout(null); // Absolute positioning
        contentPane.add(photoPanel);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameHeight = getHeight(); // Get the new frame height
                int frameWidth = getWidth();
                photoPanel.setBounds(300, 26, frameWidth - 300, frameHeight - 26); // Adjust sidebarPanel height

            }
        });  
        
        
       
        JPanel stPanel = new JPanel();
        stPanel.setBackground(Color.LIGHT_GRAY);
        stPanel.setBounds(0, 61, 949, 663);
        mainPanel.add(stPanel);
        stPanel.setLayout(null);
        
        
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // for empty border
        scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
        scrollPane.setBounds(0, 0, 949, 663);
        stPanel.add(scrollPane);
        
        tablePayBills = new JTable();
        tablePayBills.setRowHeight(30);
        tablePayBills.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tablePayBills.setSelectionBackground(new Color(255, 230, 150));
        tablePayBills.setShowVerticalLines(false);
        tablePayBills.setShowHorizontalLines(false);
        tablePayBills.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Tenant ID", "Tenant Name", "Bill ID", "Total Bill", "Unpaid Balance"
        	}
        ));
        
        scrollPane.setViewportView(tablePayBills);
        
        tablePayBills.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 238, 226) );
                }
                setHorizontalAlignment(SwingConstants.CENTER); // Center align text
                return c;
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameHeight = getHeight(); // Get the new frame height
                int frameWidth = getWidth();
                stPanel.setBounds(0, 76, frameWidth - 401, frameHeight - 76);
                scrollPane.setBounds(0, 0, frameWidth - 401, frameHeight - 76); 
                tablePayBills.setBounds(0, 0, frameWidth - 401, frameHeight - 76); 

            }
        });
        
        JTableHeader tblHeader = tablePayBills.getTableHeader();
        tblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tblHeader.setBackground(new Color(247, 247, 231));
        tblHeader.setForeground(Color.black);
        tblHeader.setReorderingAllowed(false);  
        
        RoundedButton btnRefresh = new RoundedButton("Refresh", 15);
        btnRefresh.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		loadTenantBillDetails();
        		
        		//dbConnection.populateTenantBillTable(tablePayBills);
        	
        	   
        	}
        });
     // Refresh the table programmatically after payment

        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(183, 183, 47));
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setBounds(840, 15, 80, 30);
        mainPanel.add(btnRefresh);
        
        RoundedButton btnMakePayment = new RoundedButton("Make Payment", 15);
        btnMakePayment.addActionListener(e -> {
            int selectedRow = tablePayBills.getSelectedRow();

            if (selectedRow != -1) {
                // Get the data from the selected row
                String billID = tablePayBills.getValueAt(selectedRow, 2).toString(); // Assuming Bill ID is in column 2
               

                // Open the payment dialog and pass the data
                MakePaymentDialog paymentDialog = new MakePaymentDialog();
                paymentDialog.setPaymentData(billID);
                paymentDialog.setModal(true);
                paymentDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to make a payment.");
            }
        });

        btnMakePayment.setForeground(Color.WHITE);
        btnMakePayment.setBorderPainted(false);
        btnMakePayment.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMakePayment.setBackground(new Color(183, 183, 47));
        btnMakePayment.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMakePayment.setBounds(712, 15, 118, 30);
        mainPanel.add(btnMakePayment);
        
        
        
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameWidth = mainPanel.getWidth();
                btnMakePayment.setBounds(frameWidth - 240, 20, 118, 40); 
                  btnRefresh.setBounds(frameWidth - 100, 20, 80, 40); // 


            }
        });  
        
        
        JLabel lblPayBills = new JLabel("Pay Bills");
        lblPayBills.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblPayBills.setBounds(15, 11, 265, 35);
        mainPanel.add(lblPayBills);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameHeight = getHeight(); // Get the new frame height
                int frameWidth = getWidth();
                mainPanel.setBounds(351, 76, frameWidth - 401, frameHeight - 76); // Adjust sidebarPanel height

            }
        });
        
       loadTenantBillDetails();
        
       
      
       
	}
	
		public void loadTenantBillDetails() {
	    DatabaseConnection dbc = DatabaseConnection.getInstance();
	    List<Object[]> tenantBillDetails = dbc.fetchTenantBillDetails();

	    // Get the table model
	    DefaultTableModel model = (DefaultTableModel) tablePayBills.getModel();
	    model.setRowCount(0); // Clear existing data
	    model.fireTableDataChanged();  // Refresh the table v
	    tablePayBills.repaint();  // Forces a repaint of the table


	    // Populate the table with data
	    for (Object[] row : tenantBillDetails) {
	        model.addRow(row);
	    }
	} 
	
	

}
