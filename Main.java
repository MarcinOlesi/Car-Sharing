package carsharing;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    // JDBC driver name and database URL
    public static final String JDBC_DRIVER = "org.h2.Driver";
    public static String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    private static String formatPath (String[] args) {
        boolean found = false;
        StringBuilder dbPath = new StringBuilder("jdbc:h2:file:../task/src/carsharing/db/");
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-databaseFileName")) {
                if (i + 1 < args.length) {
                    dbPath.append(args[i + 1]);
                    found = true;
                }
            }
        }
        if (!found) {
            dbPath.append("anyPath");
        }
        return dbPath.toString();// + ".mv.db";
    }


    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //STEP 2: Open a connection
           // System.out.println("Connecting to database...");
            DB_URL = formatPath(args);
            //System.out.println(DB_URL);
            conn = DriverManager.getConnection( DB_URL );

            //STEP 3: Execute a query
            stmt = conn.createStatement();
            String sql = "";

/*
            stmt = conn.createStatement();
            sql = "DROP TABLE if exists company cascade;";
            stmt.executeUpdate(sql);

            stmt = conn.createStatement();
            sql = "DROP TABLE if exists car cascade;";
            stmt.executeUpdate(sql);

            stmt = conn.createStatement();
            sql = "DROP TABLE if exists customer cascade;";
            stmt.executeUpdate(sql);
*/


                //System.out.println("Creating table in given database...");
              stmt = conn.createStatement();
                 sql = "CREATE TABLE  IF NOT EXISTS  COMPANY " +
                        "(ID INTEGER PRIMARY KEY  AUTO_INCREMENT     , " +
                        " NAME VARCHAR(255) NOT NULL UNIQUE)" ;

                stmt.executeUpdate(sql);

                stmt = conn.createStatement();
                sql = "CREATE TABLE  IF NOT EXISTS  CAR  " +
                        "(ID INTEGER PRIMARY KEY  AUTO_INCREMENT     , " +
                        " NAME VARCHAR(255) NOT NULL UNIQUE ," +
                        " COMPANY_ID INTEGER NOT NULL, " +
                        " FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";

                stmt.executeUpdate(sql);


               stmt = conn.createStatement();
                 sql = "CREATE TABLE  IF NOT EXISTS  CUSTOMER  " +
                    "(ID INTEGER  PRIMARY KEY  AUTO_INCREMENT     , " +
                    " NAME VARCHAR(255) NOT NULL UNIQUE ," +
                    "RENTED_CAR_ID INTEGER ," +
                     "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID) ," +
                    " COMPANY_ID INTEGER , " +
                    " FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";

                 stmt.executeUpdate(sql);

                 Menu.start();

            conn.setAutoCommit(true);
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException ignored) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        } //end try




       // System.out.println("Goodbye!");
    }
}
