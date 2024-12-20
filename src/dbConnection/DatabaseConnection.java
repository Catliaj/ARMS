package dbConnection;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException; // Add this import
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Apartment;
import model.LedgerRecord;
import model.Tenant;
import model.TenantDetails;

import model.TenantModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/arafbsdb";
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = ""; // Replace with your DB password

    // Singleton instance
    private static DatabaseConnection instance = null;
    private static Connection connection = null;

    // Private constructor to prevent instantiation
    public DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create database connection.");
        }
    }

    // Method to get the singleton instance
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Method to get the connection
    public Connection getConnection() {
        return connection;
    }
    
    
    

    // Method to close the connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to close database connection.");
            }
        }
    }
    
    
    public int getTotalApartments() {
        int total = 0;
        String query = "SELECT COUNT(*) AS total FROM apartment";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt("total"); // Get total count
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to fetch total apartments.");
        }
        return total;
        
    }
    
    public int getTotalOccupants() {
        int totalOccupants = 0;
        String query = "SELECT SUM(occupants) AS total_occupants FROM apartment";
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalOccupants = rs.getInt("total_occupants");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalOccupants;
    }
    
    public void populateAvailableUnitCodes(JComboBox<String> comboBoxUnitCode) {
        String query = "SELECT unitCode, status FROM apartment";  // Query to fetch units and their statuses

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Clear the combo box before adding new items
            comboBoxUnitCode.removeAllItems();

            // Add an initial empty item (optional)
            comboBoxUnitCode.addItem("");  // Placeholder item

            // Loop through the result set to add available units
            while (rs.next()) {
                String unitCode = rs.getString("unitCode");
                String status = rs.getString("status");

                // Add to combo box only if the unit is available
                if ("Available".equals(status)) {
                    comboBoxUnitCode.addItem(unitCode);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
   

    public String addTenant(String firstName, String lastName, String contactNo, String email, String unitCode,
            String strOccupants, String additionalInfo, String strRentStartDate, String strRentEndDate) {
			try {
			// 1. Parse occupants to integer
			int occupants;
			try {
			occupants = Integer.parseInt(strOccupants);
			} catch (NumberFormatException e) {
			return "Invalid occupants value. Please enter a valid number.";
			}
			
			// 2. Validate and parse dates
			java.sql.Date rentStartDate, rentEndDate;
			try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			rentStartDate = new java.sql.Date(sdf.parse(strRentStartDate).getTime());
			rentEndDate = new java.sql.Date(sdf.parse(strRentEndDate).getTime());
			} catch (ParseException e) {
			return "Invalid date format. Please use 'yyyy-MM-dd'.";
			}
			
			// 3. Get unitID and rentAmount based on unitCode
			String unitQuery = "SELECT unitID, rentAmount FROM apartment WHERE unitCode = ?";
			PreparedStatement unitStmt = connection.prepareStatement(unitQuery);
			unitStmt.setString(1, unitCode);
			ResultSet unitRS = unitStmt.executeQuery();
			
			int unitID = 0;
			double rentAmount = 0.0;
			if (unitRS.next()) {
			unitID = unitRS.getInt("unitID");
			rentAmount = unitRS.getDouble("rentAmount");
			} else {
			return "Unit code not found. Please check and try again.";
			}
			
		//	System.out.println("Retrieved unitID: " + unitID);
		//	System.out.println("Retrieved rentAmount: " + rentAmount);
			
			// 4. Update apartment occupants and status
			String updateApartmentQuery = "UPDATE apartment SET occupants = ?, status = 'Occupied' WHERE unitCode = ?";
			PreparedStatement updateApartmentStmt = connection.prepareStatement(updateApartmentQuery);
			updateApartmentStmt.setInt(1, occupants);
			updateApartmentStmt.setString(2, unitCode);
			updateApartmentStmt.executeUpdate();
			
			// 5. Insert tenant data into tenant table
			String tenantQuery = "INSERT INTO tenant (firstName, lastName, contactNum, email, additionalInfo, unitID) " +
			                 "VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement tenantStmt = connection.prepareStatement(tenantQuery, Statement.RETURN_GENERATED_KEYS);
			tenantStmt.setString(1, firstName);
			tenantStmt.setString(2, lastName);
			tenantStmt.setString(3, contactNo);
			tenantStmt.setString(4, email);
			tenantStmt.setString(5, additionalInfo);
			tenantStmt.setInt(6, unitID);
			int tenantRowsAffected = tenantStmt.executeUpdate();
			
			if (tenantRowsAffected == 0) {
			return "Failed to add tenant details.";
			}
			
			ResultSet tenantRS = tenantStmt.getGeneratedKeys();
			int tenantID = 0;
			if (tenantRS.next()) {
			tenantID = tenantRS.getInt(1);  // Get generated tenantID
			}
			
			// 6. Insert into rentContract table
			String rentContractQuery = "INSERT INTO rentalcontract (tenantID, unitID, rentStart, rentEnd) " +
			                        "VALUES (?, ?, ?, ?)";
			PreparedStatement rentContractStmt = connection.prepareStatement(rentContractQuery, Statement.RETURN_GENERATED_KEYS);
			rentContractStmt.setInt(1, tenantID);
			rentContractStmt.setInt(2, unitID);
			rentContractStmt.setDate(3, rentStartDate);
			rentContractStmt.setDate(4, rentEndDate);
			int rentContractRowsAffected = rentContractStmt.executeUpdate();
			
			if (rentContractRowsAffected == 0) {
			return "Failed to create rent contract.";
			}
			
			ResultSet rentContractRS = rentContractStmt.getGeneratedKeys();
			int rentalContractID = 0;
			if (rentContractRS.next()) {
			rentalContractID = rentContractRS.getInt(1);  // Get generated rentContractID
			}
			
			// 7. Insert into rentalHistory table
			String rentalHistoryQuery = "INSERT INTO rentalhistory (tenantID, unitID, rentalContractID) " +
			                        "VALUES (?, ?, ?)";
			PreparedStatement rentalHistoryStmt = connection.prepareStatement(rentalHistoryQuery);
			rentalHistoryStmt.setInt(1, tenantID);
			rentalHistoryStmt.setInt(2, unitID);
			rentalHistoryStmt.setInt(3, rentalContractID);
			rentalHistoryStmt.executeUpdate();
			
			// 8. Insert into bills table
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			//currentTime + 1 month
		//	String dueDate = sdf.format(new java.util.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));  // Add 1 month to rent date
			// Use Calendar to add 1 month to rentStartDate
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(rentStartDate);
	        calendar.add(Calendar.MONTH, 1); // Add one month

	        // Get the dueDate after adding one month
	        java.sql.Date dueDate = new java.sql.Date(calendar.getTimeInMillis());

			String billsQuery = "INSERT INTO bills (tenantID, unitID, totalAmount, totalBalance, dueDate, status) " +
			                "VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement billsStmt = connection.prepareStatement(billsQuery);
			billsStmt.setInt(1, tenantID);
			billsStmt.setInt(2, unitID);
			billsStmt.setDouble(3, rentAmount);
			billsStmt.setDouble(4, rentAmount);
			billsStmt.setDate(5, dueDate);
			billsStmt.setString(6, "Unpaid");
			billsStmt.executeUpdate();
			
			return "Tenant and related data saved successfully.";
			
			} catch (SQLException e) {
			e.printStackTrace();
			return "Error while adding tenant and related data: " + e.getMessage();
			}
			}
    
    
    public List<TenantModel> fetchTenants() {
        List<TenantModel> tenants = new ArrayList<>();
        String sql = "SELECT t.tenantID, CONCAT(t.firstName, ' ', t.lastName) AS tenantName, a.unitCode " +
                     "FROM tenant t " +
                     "JOIN apartment a ON t.unitID = a.unitID";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TenantModel tenant = new TenantModel(
                        rs.getInt("tenantID"),
                        rs.getString("tenantName"),
                        rs.getString("unitCode")
                );
                tenants.add(tenant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenants;
    }

    public TenantDetails fetchTenantDetails(int tenantID) {
    	String sql = "SELECT t.tenantID, CONCAT(t.firstName, ' ', t.lastName) AS tenantName, " +
                "t.contactNum, t.email, t.additionalInfo, a.unitCode, r.rentStart " +
                "FROM tenant t " +
                "JOIN apartment a ON t.unitID = a.unitID " +
                "JOIN rentalContract r ON t.tenantID = r.tenantID " +
                "WHERE t.tenantID = ?";


        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, tenantID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TenantDetails(
                            rs.getInt("tenantID"),
                            rs.getString("tenantName"),
                            rs.getString("contactNum"),
                            rs.getString("email"),
                            rs.getString("additionalInfo"),
                            rs.getString("unitCode"),
                            rs.getDate("rentStart")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
	public boolean deleteTenantAndUpdateUnitStatus(int tenantID) {
        String deleteTenantQuery = "DELETE FROM tenant WHERE tenantID = ?";
        String getUnitIdQuery = "SELECT unitID FROM tenant WHERE tenantID = ?";
        String updateUnitStatusQuery = "UPDATE apartment SET status = 'Available', occupants = NULL WHERE unitID = ?";

        // Using transaction for ensuring atomicity
        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Get the unit ID associated with the tenant
            int unitID = -1;
            try (PreparedStatement getUnitStmt = connection.prepareStatement(getUnitIdQuery)) {
                getUnitStmt.setInt(1, tenantID);
                ResultSet rs = getUnitStmt.executeQuery();
                if (rs.next()) {
                    unitID = rs.getInt("unitID");
                }
            }

            // If unitID is found, proceed with deletion and update
            if (unitID != -1) {
                // Delete tenant
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteTenantQuery)) {
                    deleteStmt.setInt(1, tenantID);
                    int rowsDeleted = deleteStmt.executeUpdate();

                    // If tenant was deleted, update the apartment status
                    if (rowsDeleted > 0) {
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateUnitStatusQuery)) {
                            updateStmt.setInt(1, unitID);
                            int rowsUpdated = updateStmt.executeUpdate();

                            // If the apartment status is updated, commit the transaction
                            if (rowsUpdated > 0) {
                                connection.commit();  // Commit both delete and update
                                return true;
                            }
                        }
                    }
                }
            }

            // If any of the operations fail, rollback the transaction
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();  // In case of exception, rollback changes
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);  // Restore default commit behavior
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;  // Return false if the operation failed
    }
	

	
	public void populateTable(JTable tableRentBills) {
	    String updateQuery = """
	        UPDATE bills b
	        JOIN apartment a ON b.unitID = a.unitID
	        LEFT JOIN facility f ON b.facilityID = f.facilityID
	        SET 
	            b.totalAmount = (
	                a.rentAmount +
	                IFNULL(b.electricityBill, 0) +
	                IFNULL(b.waterBill, 0) +
	                IFNULL(f.facilityBill, 0)
	            )
	        """;

	    String updateBalanceQuery = """
	    	    UPDATE bills b
	    	    LEFT JOIN payment p ON b.billID = p.billID
	    	    SET b.totalBalance = (
	    	        b.totalAmount - IFNULL((SELECT SUM(p.paymentAmount) FROM payment p WHERE p.billID = b.billID), 0) - b.advancePayment
	    	    );
	    	""";


	    String selectQuery = """
	        SELECT 
	            b.billID, 
	            CONCAT(t.firstName, ' ', t.lastName) AS tenantName, 
	            CONCAT('₱', FORMAT(a.rentAmount, 2)) AS rentAmount, 
	            IFNULL(CONCAT('₱', FORMAT(b.electricityBill, 2)), 'Awaiting Data') AS electricityBill, 
	            IFNULL(CONCAT('₱', FORMAT(b.waterBill, 2)), 'Awaiting Data') AS waterBill, 
	            IFNULL(CONCAT('₱', FORMAT(f.facilityBill, 2)), 'Awaiting Data') AS facilityBill, 
	            IFNULL(CONCAT('₱', FORMAT(b.advancePayment, 2)), 'No Advance Payment') AS advancePayment,
	            CONCAT('₱', FORMAT(b.totalAmount, 2)) AS totalAmount, 
	            CONCAT('₱', FORMAT(b.totalBalance, 2)) AS totalBalance,
	            b.dueDate, 
	            b.status 
	        FROM bills b 
	        JOIN tenant t ON b.tenantID = t.tenantID 
	        JOIN apartment a ON b.unitID = a.unitID 
	        LEFT JOIN facility f ON b.facilityID = f.facilityID
	        GROUP BY b.billID;
	    """;

	    try {
	        connection.prepareStatement(updateQuery).executeUpdate();
	        connection.prepareStatement(updateBalanceQuery).executeUpdate();

	        PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
	        ResultSet rs = selectStmt.executeQuery();
	        DefaultTableModel model = (DefaultTableModel) tableRentBills.getModel();
	        model.setRowCount(0);

	        while (rs.next()) {
	            Object[] row = {
	                rs.getInt("billID"),
	                rs.getString("tenantName"),
	                rs.getString("rentAmount"),
	                rs.getString("electricityBill"),
	                rs.getString("waterBill"),
	                rs.getString("facilityBill"),
	                rs.getString("advancePayment"),
	                rs.getString("totalAmount"),
	                rs.getString("totalBalance"),
	                rs.getDate("dueDate"),
	                rs.getString("status")
	            };
	            model.addRow(row);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public boolean insertFacilityAndUpdateBill(String billIDStr, String electricityBillStr, String waterBillStr, 
            String advancePaymentStr, String facilityName, String facilityBillStr) {
int billID = Integer.parseInt(billIDStr);
double electricityBill = Double.parseDouble(electricityBillStr);
double waterBill = Double.parseDouble(waterBillStr);
double advancePayment = Double.parseDouble(advancePaymentStr);
double facilityBill = Double.parseDouble(facilityBillStr);

String insertFacilitySQL = "INSERT INTO facility (facilityName, facilityBill) VALUES (?, ?)";
String updateBillSQL = "UPDATE bills SET electricityBill = ?, waterBill = ?, advancePayment = ?, facilityID = ? WHERE billID = ?";

boolean success = false;
try {
connection.setAutoCommit(false);

PreparedStatement stmt = connection.prepareStatement(insertFacilitySQL, Statement.RETURN_GENERATED_KEYS);
stmt.setString(1, facilityName);
stmt.setDouble(2, facilityBill);
stmt.executeUpdate();

ResultSet keys = stmt.getGeneratedKeys();
int facilityID = keys.next() ? keys.getInt(1) : -1;

PreparedStatement updateStmt = connection.prepareStatement(updateBillSQL);
updateStmt.setDouble(1, electricityBill);
updateStmt.setDouble(2, waterBill);
updateStmt.setDouble(3, advancePayment);
updateStmt.setInt(4, facilityID);
updateStmt.setInt(5, billID);
success = updateStmt.executeUpdate() > 0;

connection.commit();
} catch (SQLException e) {
e.printStackTrace();
try {
connection.rollback();
} catch (SQLException ex) {
ex.printStackTrace();
}
} finally {
try {
connection.setAutoCommit(true);
} catch (SQLException e) {
e.printStackTrace();
}
}

return success;
}

	
	
	public List<Object[]> fetchTenantBillDetails() {
	    List<Object[]> tenantBillDetails = new ArrayList<>();
	    String query = """
	        SELECT t.tenantID, CONCAT(t.firstName, ' ', t.lastName) AS tenantName, 
	               b.billID, CONCAT('₱', FORMAT(b.totalAmount, 2)) AS formatTotal,
	               CONCAT('₱', FORMAT(b.totalBalance, 2)) AS formatBalance
	        FROM tenant t
	        JOIN bills b ON t.tenantID = b.tenantID
	        WHERE b.totalBalance > 0
	    """;

	    try (PreparedStatement stmt = connection.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Object[] row = new Object[5];  // Increase size of the array to accommodate totalBalance
	            row[0] = rs.getInt("tenantID");
	            row[1] = rs.getString("tenantName");
	            row[2] = rs.getInt("billID");
	            row[3] = rs.getString("formatTotal");
	            row[4] = rs.getString("formatBalance");  // Fetch the formatted totalBalance as a string
	            tenantBillDetails.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return tenantBillDetails;
	}
	

	
	public String processPayment(int billID, double paymentAmount, java.sql.Date paymentDate) {
	    String resultMessage = "";
	    Connection conn = getConnection();
	    try {
	        conn.setAutoCommit(false); // Start transaction

	        // Fetch tenantID and totalBalance before payment
	        String fetchDetailsQuery = "SELECT tenantID, totalBalance FROM bills WHERE billID = ?";
	        int tenantID = 0;
	        double totalBalance = 0.0;
	        try (PreparedStatement fetchStmt = conn.prepareStatement(fetchDetailsQuery)) {
	            fetchStmt.setInt(1, billID);
	            ResultSet rs = fetchStmt.executeQuery();
	            if (rs.next()) {
	                tenantID = rs.getInt("tenantID");
	                totalBalance = rs.getDouble("totalBalance");
	            } else {
	                throw new SQLException("Bill ID not found.");
	            }
	        }

	        // Insert payment and retrieve paymentID
	        int paymentID;
	        String insertPaymentQuery = "INSERT INTO payment (billID, tenantID, paymentAmount, paymentDate) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement paymentStmt = conn.prepareStatement(insertPaymentQuery, Statement.RETURN_GENERATED_KEYS)) {
	            paymentStmt.setInt(1, billID);
	            paymentStmt.setInt(2, tenantID);
	            paymentStmt.setDouble(3, paymentAmount);
	            paymentStmt.setDate(4, paymentDate);
	            paymentStmt.executeUpdate();
	            ResultSet rs = paymentStmt.getGeneratedKeys();
	            if (rs.next()) {
	                paymentID = rs.getInt(1);
	            } else {
	                throw new SQLException("Failed to retrieve paymentID.");
	            }
	        }

	        // Update the totalBalance for the bill after payment
	        String updateBalanceQuery = """
	            UPDATE bills b
	            LEFT JOIN payment p ON b.billID = p.billID
	            SET b.totalBalance = (
	                b.totalAmount - IFNULL((SELECT SUM(p.paymentAmount) FROM payment p WHERE p.billID = b.billID), 0)
	            )
	            WHERE b.billID = ?;
	        """;
	        try (PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery)) {
	            updateBalanceStmt.setInt(1, billID);
	            updateBalanceStmt.executeUpdate();
	        }

	        // Retrieve the updated totalBalance from the bills table
	        String getUpdatedBalanceQuery = "SELECT totalBalance FROM bills WHERE billID = ?";
	        double updatedTotalBalance = 0.0;
	        try (PreparedStatement getBalanceStmt = conn.prepareStatement(getUpdatedBalanceQuery)) {
	            getBalanceStmt.setInt(1, billID);
	            ResultSet rs = getBalanceStmt.executeQuery();
	            if (rs.next()) {
	                updatedTotalBalance = rs.getDouble("totalBalance");
	            } else {
	                throw new SQLException("Failed to retrieve updated totalBalance.");
	            }
	        }

	        // Update bill status
	        String status = updatedTotalBalance <= 0 ? "Paid" : "Partially Paid";
	        String updateBillQuery = "UPDATE bills SET status = ? WHERE billID = ?";
	        try (PreparedStatement updateStmt = conn.prepareStatement(updateBillQuery)) {
	            updateStmt.setString(1, status);
	            updateStmt.setInt(2, billID);
	            updateStmt.executeUpdate();
	        }

	        // Insert into ledger with the updated totalBalance after payment
	        String insertLedgerQuery = "INSERT INTO ledger (tenantID, billID, paymentID, balanceAfterPayment) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement ledgerStmt = conn.prepareStatement(insertLedgerQuery)) {
	            ledgerStmt.setInt(1, tenantID);
	            ledgerStmt.setInt(2, billID);
	            ledgerStmt.setInt(3, paymentID);
	            ledgerStmt.setDouble(4, updatedTotalBalance); // Insert the updated totalBalance into ledger
	            ledgerStmt.executeUpdate();  //
	        }

	        conn.commit(); // Commit transaction
	        resultMessage = "Payment processed successfully. Payment ID: " + paymentID + ", Status: " + status;

	    } catch (SQLException ex) {
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch (SQLException rollbackEx) {
	                resultMessage = "Error rolling back transaction: " + rollbackEx.getMessage();
	            }
	        }
	        resultMessage = "Error processing payment: " + ex.getMessage();
	    } finally {
	        try {
	            conn.setAutoCommit(true);
	        } catch (SQLException ex) {
	            resultMessage += "\nError resetting auto-commit: " + ex.getMessage();
	        }
	    }
	    return resultMessage;
	}
	
	public List<Tenant> getTenants() {
        List<Tenant> tenants = new ArrayList<>();
        String query = "SELECT tenantID, firstName, lastName FROM tenant";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int tenantID = rs.getInt("tenantID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                tenants.add(new Tenant(tenantID, firstName, lastName)); // Concatenate in the constructor
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenants;
    }
	
	
	public List<LedgerRecord> getLedgerData(int tenantID) {
	    List<LedgerRecord> ledgerData = new ArrayList<>();
	    String query = "SELECT p.paymentDate, l.billID, l.paymentID, " +
	                   "       CONCAT('₱', FORMAT(p.paymentAmount, 2)) AS formattedPaymentAmount, " +
	                   "       CONCAT('₱', FORMAT(l.balanceAfterPayment, 2)) AS formattedBalanceAfterPayment " +
	                   "FROM ledger l " +
	                   "JOIN payment p ON l.paymentID = p.paymentID " +
	                   "WHERE l.tenantID = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, tenantID);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            Date paymentDate = rs.getDate("paymentDate");
	            int billID = rs.getInt("billID");
	            int paymentID = rs.getInt("paymentID");
	            String paymentAmount = rs.getString("formattedPaymentAmount"); // Updated to String
	            String balanceAfterPayment = rs.getString("formattedBalanceAfterPayment"); // Updated to String
	            ledgerData.add(new LedgerRecord(paymentDate, billID, paymentID, paymentAmount, balanceAfterPayment));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return ledgerData;
	}
	
	public List<Apartment> getAllApartments() {
        List<Apartment> apartments = new ArrayList<>();
        String query = "SELECT unitID, unitCode, unitType, description, rentAmount, status, bedspace FROM apartment";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int unitID = rs.getInt("unitID");
                String unitCode = rs.getString("unitCode");
                String unitType = rs.getString("unitType");
                String description = rs.getString("description");
                double rentAmount = rs.getDouble("rentAmount");
                String status = rs.getString("status");
                int bedspace = rs.getInt("bedspace");

                apartments.add(new Apartment(unitID, unitCode, unitType, description, rentAmount, status,bedspace));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return apartments;
    }
	
			public String addTenanthisotry(String firstName, String lastName, String contactNo, String email, String unitCode, 
		            String occupants, String additionalInfo, String rentStartDate, String rentEndDate) {
		String sqlInsertTenant = """
		INSERT INTO tenanthistory (firstName, lastName, contactNum, email, additionalInfo, unitID)
		VALUES (?, ?, ?, ?, ?, (SELECT unitID FROM apartment WHERE unitCode = ?))
		""";
		
		String sqlInsertRentalContract = """
		INSERT INTO rentalContract (tenantID, rentStart, rentEnd, occupants)
		VALUES ((SELECT tenantID FROM tenant WHERE contactNum = ? AND email = ?), ?, ?, ?)
		""";
		
		String sqlUpdateUnitStatus = """
		UPDATE apartment SET status = 'Occupied' WHERE unitCode = ?
		""";
		
		try (PreparedStatement psTenant = connection.prepareStatement(sqlInsertTenant);
		PreparedStatement psRental = connection.prepareStatement(sqlInsertRentalContract);
		PreparedStatement psUpdateUnit = connection.prepareStatement(sqlUpdateUnitStatus)) {
		
		// Insert tenant data
		psTenant.setString(1, firstName);
		psTenant.setString(2, lastName);
		psTenant.setString(3, contactNo);
		psTenant.setString(4, email);
		psTenant.setString(5, additionalInfo);
		psTenant.setString(6, unitCode);
		psTenant.executeUpdate();
		
		// Insert rental contract data
		psRental.setString(1, contactNo);
		psRental.setString(2, email);
		psRental.setString(3, rentStartDate);
		psRental.setString(4, rentEndDate);
		psRental.setString(5, occupants);
		psRental.executeUpdate();
		
		// Update apartment unit status
		psUpdateUnit.setString(1, unitCode);
		psUpdateUnit.executeUpdate();
		
		return "Tenant and related data saved successfully.";
		} catch (SQLException e) {
		e.printStackTrace();
		return "Failed to add tenant: " + e.getMessage();
		}
}

			

	


	

	
	
	
	
	
	

	
}
