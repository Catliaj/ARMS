package model;

public class Apartment {
    private int unitID;
    private String unitCode;
    private String unitType;
    private String description;
    private double rentAmount;
    private String status;
    private int bedspace;

    

	public Apartment(int unitID, String unitCode, String unitType, String description, double rentAmount, String status,int bedspace) {
        this.unitID = unitID;
        this.unitCode = unitCode;
        this.unitType = unitType;
        this.description = description;
        this.rentAmount = rentAmount;
        this.status = status;
        this.bedspace = bedspace;
    }

    public int getUnitID() {
        return unitID;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getUnitType() {
        return unitType;
    }

    public String getDescription() {
        return description;
    }

    public double getRentAmount() {
        return rentAmount;
    }

    public String getStatus() {
        return status;
    }
    public int getBedspace() {
		return bedspace;
	}

	public void setBedspace(int bedspace) {
		this.bedspace = bedspace;
	}
	public Apartment() {}
}
