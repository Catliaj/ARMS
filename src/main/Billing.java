package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import componentsUI.Header;
import componentsUI.RoundedButton;
import componentsUI.RoundedPanel;
import componentsUI.SidebarPanel;
import dbConnection.DatabaseConnection;

public class Billing extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableRentBills;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Billing frame = new Billing();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public Billing() {
        setTitle("Apartment Rentals and Facilities Billing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1300, 800);
        setLocationRelativeTo(null);
        setUndecorated(true);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 238, 226));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Header header = new Header(this);
        getContentPane().add(header);

        SidebarPanel sidebar = new SidebarPanel(this, "Rent Bills");
        getContentPane().add(sidebar);

        RoundedPanel mainPanel = new RoundedPanel(30);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(301, 76, 949, 724);
        mainPanel.setLayout(null);
        contentPane.add(mainPanel);

        JPanel stPanel = new JPanel();
        stPanel.setBackground(Color.LIGHT_GRAY);
        stPanel.setBounds(0, 61, 949, 663);
        mainPanel.add(stPanel);
        stPanel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
        scrollPane.setBounds(0, 0, 949, 663);
        stPanel.add(scrollPane);

        tableRentBills = new JTable();
        tableRentBills.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Bill ID", "Tenant Name", "Rent Amount", "Electricity Bill", 
                "Water Bill", "Facility Bill", "Advance Payment", 
                "Total", "Balance", "Due Date", "Status"
            }
        ));
        tableRentBills.setSelectionBackground(new Color(255, 230, 150));
        tableRentBills.setRowHeight(30);
        tableRentBills.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tableRentBills.setShowHorizontalLines(false);
        tableRentBills.setShowVerticalLines(false);
        scrollPane.setViewportView(tableRentBills);

        // Populate table with data
        DatabaseConnection dbc = new DatabaseConnection();
        dbc.populateTable(tableRentBills);

        // Custom table renderer for alternate row colors and text alignment
        tableRentBills.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 238, 226));
                }
                setHorizontalAlignment(SwingConstants.CENTER); // Center align text
                return c;
            }
        });

        JTableHeader tblHeader = tableRentBills.getTableHeader();
        tblHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblHeader.setBackground(new Color(247, 247, 231));
        tblHeader.setForeground(Color.black);
        tblHeader.setReorderingAllowed(false);

        JLabel lblRentBills = new JLabel("Rent Bills");
        lblRentBills.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblRentBills.setBounds(15, 11, 265, 35);
        mainPanel.add(lblRentBills);

        // Insert Charges Button
        RoundedButton btnInsertCharges = new RoundedButton("Insert Charges", 15);
        btnInsertCharges.addActionListener(e -> {
            // Ensure a row is selected
            int selectedRow = tableRentBills.getSelectedRow();
            if (selectedRow != -1) {
                // Retrieve and clean the data
                String billID = tableRentBills.getValueAt(selectedRow, 0).toString();
                String electricity = cleanNumericValue(tableRentBills.getValueAt(selectedRow, 3).toString());
                String water = cleanNumericValue(tableRentBills.getValueAt(selectedRow, 4).toString());
                String facilityName = tableRentBills.getValueAt(selectedRow, 5).toString();
                String facilityBill = cleanNumericValue(tableRentBills.getValueAt(selectedRow, 5).toString());
                String advancePayment = cleanNumericValue(tableRentBills.getValueAt(selectedRow, 6).toString());

                // Open InsertChargesDialog and populate fields
                InsertChargesDialog insertDialog = new InsertChargesDialog();
                insertDialog.setChargesData(billID, electricity, water, facilityBill, advancePayment);
                insertDialog.setModal(true);
                insertDialog.setVisible(true);
            } else {
                // Show an error message if no row is selected
                JOptionPane.showMessageDialog(this, "Please select a row to insert charges.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    

        btnInsertCharges.setForeground(Color.WHITE);
        btnInsertCharges.setBorderPainted(false);
        btnInsertCharges.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnInsertCharges.setBackground(new Color(183, 183, 47));
        btnInsertCharges.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnInsertCharges.setBounds(714, 15, 115, 30);
        mainPanel.add(btnInsertCharges);

        // Refresh Button
        RoundedButton btnRefresh = new RoundedButton("Refresh", 15);
        btnRefresh.addActionListener(e -> dbc.populateTable(tableRentBills));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(183, 183, 47));
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setBounds(840, 15, 80, 30);
        mainPanel.add(btnRefresh);
    }
 // Helper method to clean numeric values
    private String cleanNumericValue(String value) {
        return value.replaceAll("[^\\d.]", ""); // Remove any character that's not a digit or decimal point
    }
}