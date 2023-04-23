package carsharing;



import java.util.ArrayList;
import java.util.Scanner;



public class Menu {
    static Scanner scanner = new Scanner(System.in);

    public static ArrayList<Company> companies = new ArrayList<Company>();

    public static void start(){
            mainMenu();
    }
    public static void mainMenu(){

        String input="";
        while(!input.equals("Exit")){
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");

            input = scanner.nextLine();
            if(input.equals("1")){
                loginManager();
            }
            if(input.equals("2")){
                Customer.loginCustomer();
            }
            if(input.equals("3")){
                Customer.createCustomer();
            }
            if(input.equals("0")){
                System.exit(0);
            }

        }
    }
    public static void loginManager(){

        String input = "";
        while(!input.equals("0")){
            System.out.println();
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            input = scanner.nextLine();
            if(input.equals("1")){
                System.out.println();

                Company.companyList();

            }
            if(input.equals("2")){
                Company.createACompany();
            }

            System.out.println();
        }
    }









}

