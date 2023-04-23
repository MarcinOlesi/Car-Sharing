package carsharing;

import carsharing.db.Car;

import java.sql.*;
import java.util.Scanner;

import static carsharing.Main.DB_URL;
import static carsharing.Main.JDBC_DRIVER;

public class Customer {
    static Connection conn = null;
    static Statement stmt = null;
    static Scanner scanner = new Scanner(System.in);

    public static void loginCustomer(){
        int indexCustomer = customerList();
        if(indexCustomer !=0) {
            customerCar(indexCustomer);
        }
    }

    public static int customerList(){
        System.out.println();
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);

            stmt = conn.createStatement();
            int indexId =0;
                    String sql = "SELECT count(*) AS recordCount FROM Customer " ;

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int count = rs.getInt("recordCount");


            if(count == 0){
                System.out.println();
                System.out.println("The customer list is empty!");

            }
            else {
                stmt = conn.createStatement();
                sql = "SELECT id, name FROM Customer " ;

                rs = stmt.executeQuery(sql);

                // STEP 4: Extract data from result set
                System.out.println("Customer list:");
                int index = 1;
                while (rs.next()) {
                    // Retrieve by column name

                    String name = rs.getString(2);
                    System.out.println(index++ +". " + name);

                }
                System.out.println("0. Back");

                indexId = Integer.parseInt(scanner.nextLine());
            //Choose customer

                return indexId;

            }

            System.out.println();
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
return 0;
    }

    public static void createCustomer(){
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        try{
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            // STEP 2: Open a connection
            //System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            String sql = "INSERT INTO customer VALUES( NULL  ,'" + name + "' , NULL, NULL)";

            stmt.executeUpdate(sql);
            System.out.println("The customer was added!");
            System.out.println();
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

    }



    public static void customerCar(int indexCustomer){
        String input = "";
        while(!input.equals("0")) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
        input = scanner.nextLine();

            if(input.equals("1")){
                rentACar(indexCustomer);
            }
            if(input.equals("2")){
                returnRentedCar(indexCustomer);
            }
            if(input.equals("3")){
                rentedCarList(indexCustomer);
            }
        }
    }

    public static void rentACar(int indexCustomer) {
        //System.out.println("Choose a company:");

        if (rentACarCheck(indexCustomer)) {
            System.out.println("You've already rented a car!");
        } else {
            int indexCompany = Company.companyListForCustomer();
            if(indexCompany == 0)
                return;

            System.out.println("Choose a car:");
            //Car.carList(indexCompany);
            Car.carListWithoutRented(indexCompany);
            int indexCar = Integer.parseInt(scanner.nextLine());
            String car = addCarToCustomer(indexCar, indexCompany, indexCustomer);
            //int indexCarDB = Car.carList(indexCompany,indexCar);


            System.out.println("You rented '" + car + "'");
            System.out.println();
        }
    }
    public static void returnRentedCar(int indexCustomer) {
        if (rentACarCheck(indexCustomer)) {
            try {

                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL);
                stmt = conn.createStatement();


                String sql = "SELECT id, name, RENTED_CAR_ID,  COMPANY_ID From CUSTOMER " +
                        "WHERE id =" + indexCustomer + ";";



                stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    if (rs.getInt(1) == indexCustomer) {
                        sql = "UPDATE Customer " +
                                "SET RENTED_CAR_ID =  NULL " +
                                ", COMPANY_ID=  NULL " +
                                "WHERE ID =" + indexCustomer + ";";
                    }
                }

                stmt.executeUpdate(sql);
                System.out.println("You've returned a rented car!");
                // STEP 5: Clean-up environment
                rs.close();

            } catch (SQLException se) {
                // Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                // Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                // finally block used to close resources
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException ignored) {
                } // nothing we can do
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                } // end finally try
            } // end try

        }
        else
            System.out.println("You didn't rent a car!");
    }

    public static void rentedCarList(int indexCustomer ){
            Connection conn = null;
            Statement stmt = null;
        try {

        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL);

        if(rentACarCheck(indexCustomer)) {

            stmt = conn.createStatement();
           String sql = "SELECT RENTED_CAR_ID FROM CUSTOMER " +
                   "WHERE ID =" + indexCustomer + ";";

            ResultSet rs2 = stmt.executeQuery(sql);
            int carId = 0;
            while (rs2.next()) {
                carId = rs2.getInt(1);
            }

            stmt = conn.createStatement();
             sql = "SELECT ID , NAME , COMPANY_ID FROM CAR" +
                    " WHERE ID=" + carId +";";
             rs2 = stmt.executeQuery(sql);
             String carName="";
            String carCompanyId="";
            while (rs2.next()) {
                carName = rs2.getString(2);
                carCompanyId = rs2.getString(3);
            }
            sql = "SELECT name FROM COMPANY" +
                    " WHERE ID ="+ carCompanyId+ ";";
             rs2 = stmt.executeQuery(sql);
            String companyName="";
            while (rs2.next()) {
                companyName = rs2.getString(1);

            }
            System.out.println("Your rented car:");
            System.out.println(carName);
            System.out.println("Company:");
            System.out.println(companyName);

            rs2.close();
           }
        else
        {
            System.out.println("You didn't rent a car!");
        }

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


    public static String addCarToCustomer(int indexCar, int indexCompany ,int  indexCustomer){
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();




           String  sql = "SELECT a.id, a.name FROM Car as A " +
                    "INNER JOIN Company as B  ON (b.id=a.company_id) " +
                    "WHERE b.id= " + indexCompany + "; ";

            ResultSet  rs = stmt.executeQuery(sql);


                int index = 1;
                int rented_car_id=0;
                while (rs.next()) {

                    if(index == indexCar){
                        rented_car_id =  rs.getInt(1);
                        stmt = conn.createStatement();
                        sql ="UPDATE Customer " +
                                "SET RENTED_CAR_ID="+ rented_car_id +
                                 ", COMPANY_ID="+ indexCompany +
                                  " WHERE ID =" + indexCustomer +";";

                        stmt.executeUpdate(sql);
                       }
                    index++;
                }
//System.out.println("rented car id " +rented_car_id+ " indexCompany " + indexCompany + " index customer " + indexCustomer);
            stmt = conn.createStatement();

            sql = "SELECT id , name FROM Car "+
            "WHERE id ="+ rented_car_id +";";
            rs = stmt.executeQuery(sql);
            String rentedCarName="error";
            while (rs.next())
                rentedCarName=rs.getString(2);



            // STEP 5: Clean-up environment
            rs.close();
            return rentedCarName;
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
    return null;
    }

    public static boolean rentACarCheck(int indexCustomer){
        Connection conn = null;
        Statement stmt = null;

        boolean rented = false;
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();

            String  sql ="SELECT count(*) AS recordCount  FROM Customer LEFT JOIN car " +
                    "ON Customer.RENTED_CAR_ID = car.id " +
                    " WHERE Customer.id= " + indexCustomer +
                    " AND car.id > 0;";



            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int count = rs.getInt("recordCount");


        rs.close();
            rented = count != 0;
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
        return rented;
    }

}
