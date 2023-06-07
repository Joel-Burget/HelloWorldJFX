package C195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDate;

public class Customers {
    private int customerId;
    private String customerName;
    private String address;
    private String zip;
    private String phoneNumber;

    public Customers(int customerId, String customerName, String address, String zip, String phoneNumber){
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
    }

    public static ObservableList<Customers> getAllCustomers(){
        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
        String statement = "SELECT * FROM Customers";
        int custID;
        String custName;
        String custAdd;
        String custZip;
        String custPhone;

        try{
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();
            while(rs.next()){
                custID = rs.getInt(1);
                custName = rs.getString(2);
                custAdd = rs.getString(3);
                custZip = rs.getString(4);
                custPhone = rs.getString(5);

                Customers temp = new Customers(custID, custName, custAdd, custZip, custPhone);
                allCustomers.add(temp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return allCustomers;
    }


    public static void createCustomer(String name, String address, String postal, int phoneNumber, LocalDate createdDate, int divisonID){
        try{
            String statement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Division_ID) VALUES('"
                    + name + "', '" + address + "', '" + postal + "', '" + phoneNumber + "', '" + createdDate + "', '" + divisonID + "');";

            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            JDBC.getPreparedStatement().executeUpdate();
            System.out.println("Customer created successfully.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void updateCustomer(int CustomerID, String name, String address, String postal, int phoneNumber){
        String statement = "UPDATE customers SET Customer_Name = '" + name + "', Address =" + "'" + address + "', " + "Postal_Code = '" + postal + "', "
                + "Phone ='" + phoneNumber + "' " + "WHERE Customer_ID = '" + CustomerID + "'";
        System.out.println(statement);

        try{
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            JDBC.getPreparedStatement().executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void deleteCustomer(int customerID){
        int count;
        String statement = "SELECT COUNT(Appointment_ID) FROM appointments WHERE Customer_ID='" + customerID + "'";
        try{
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();
            System.out.println(statement);
            while(rs.next()){
                count = rs.getInt(1);
                if(count > 0){
                    System.out.println("Please delete all appointments before deleting a customer profile.");
                } else {
                    statement = "DELETE FROM customers WHERE Customer_ID='" + customerID + "'";
                    JDBC.makePreparedStatement(statement, JDBC.getConnection());
                    JDBC.getPreparedStatement().executeUpdate();
                    System.out.println("Customer Deleted");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
