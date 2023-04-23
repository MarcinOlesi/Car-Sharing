package carsharing;

import carsharing.db.Car;

import java.sql.*;
import java.util.Scanner;

import static carsharing.Main.DB_URL;
import static carsharing.Main.JDBC_DRIVER;
import static carsharing.Menu.companies;

public class Company {
    String companyName;

   public Company(String companyName){
        this.companyName = companyName;
    }

    static Scanner scanner = new Scanner(System.in);
    static Connection conn = null;
    static Statement stmt = null;

    public static void createACompany(){
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        Company company = new Company(name);
        companies.add(company);
        addIntoTableCompany(name);
        System.out.println("The company was created!");
    }

    public static void companyList(){
        System.out.println();


        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);

            stmt = conn.createStatement();

            String sql = "SELECT count(*) AS recordCount FROM Company;";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int count = rs.getInt("recordCount");


            if(count == 0){
                System.out.println();
                System.out.println("The company list is empty!");
            }
            else {
                System.out.println("Choose the company:");

                stmt = conn.createStatement();
                sql = "SELECT id, name FROM Company ;";

                rs = stmt.executeQuery(sql);
                // STEP 4: Extract data from result set
                //System.out.println("Company list:");
                String name= "";
                while (rs.next()) {
                    // Retrieve by column name
                    int index = rs.getInt(1);
                    name = rs.getString(2);
                    System.out.println(index +". " + name);

                }
                System.out.println("0. Back");

           int index = Integer.parseInt(scanner.nextLine());
            System.out.println();

            if(index != 0)
            companyCarOption(index,name);
            }
            // STEP 5: Clean-up environment
            rs.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException ignored) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try

    }
    public static void companyCarOption(int index, String name){


        System.out.println(name + " company");
        String input = "";
        while(!input.equals("0")) {
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            input = scanner.nextLine();
            if(input.equals("1")){
                Car.carList(index);

            }
            if(input.equals("2"))
                Car.createACar(index);
        }
    }
    public static void addIntoTableCompany(String name){
        Connection conn = null;
        Statement stmt = null;
        try{
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            // STEP 2: Open a connection
            //System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL);


            // STEP 3: Execute a query
            stmt = conn.createStatement();
            String sql = "INSERT INTO company(name)  VALUES('" + name + "');";

            stmt.executeUpdate(sql);

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException ignored) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try
        // System.out.println("Goodbye!");
    }

    public static int companyListForCustomer(){
        System.out.println();
        System.out.println("Choose the company:");

        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);

            stmt = conn.createStatement();

            String sql = "SELECT count(*) AS recordCount FROM Company;";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int count = rs.getInt("recordCount");


            if(count == 0){
                System.out.println();
                System.out.println("The company list is empty!");

            }
            else {
                stmt = conn.createStatement();
                sql = "SELECT id, name FROM Company ;";

                rs = stmt.executeQuery(sql);
                // STEP 4: Extract data from result set
                //System.out.println("Company list:");

                while (rs.next()) {
                    // Retrieve by column name
                    int index = rs.getInt(1);
                    String name = rs.getString(2);
                    System.out.println(index +". " + name);

                }
                System.out.println("0. Back");
            }

            int index = Integer.parseInt(scanner.nextLine());
            System.out.println();


            // STEP 5: Clean-up environment
            rs.close();
            return index;
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException ignored) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try


        return 0;
    }
}
