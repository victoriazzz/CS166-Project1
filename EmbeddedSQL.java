/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class EmbeddedSQL {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of EmbeddedSQL
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public EmbeddedSQL (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end EmbeddedSQL

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            EmbeddedSQL.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      EmbeddedSQL esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the EmbeddedSQL object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new EmbeddedSQL (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Mechanic");
            System.out.println("3. Add Car");
            System.out.println("4. Start Service Request");
            System.out.println("5. Close Service Request"); 
            System.out.println("6. List date, comment, and bill for all closed requests with bill lower than 100");
            System.out.println("7. List First Name and Last Name of Customers having more than 20 different cars");
            System.out.println("8. List Make, Model, and Year of all cars build before 1995 having less than 50000 miles");
            System.out.println("9. List the Make, Model and number of Service Requests for the first k Cars with the highest number of Service Requests");
            System.out.println("10. List the first name, last name and total bill of customers in descending order of their total bill for all cars brought to the mechanic.");
            System.out.println("11. Update the mechanic for an existing service request");
            System.out.println("12. List all available mechanics (i.e., mechanics that are not currently working on a service request)");
            System.out.println("13. List all open service requests");
            System.out.println("14. List the average repair costs per car make");
            System.out.println("15. List all customers with an open service request");
            System.out.println("16. < EXIT");

            switch (readChoice()){
               case 1: Query1(esql); break;
               case 2: Query2(esql); break;
               case 3: Query3(esql); break;
               case 4: Query4(esql); break;
               case 5: Query5(esql); break;
               case 6: Query6(esql); break;
               case 7: Query7(esql); break;
               case 8: Query8(esql); break;
               case 9: Query9(esql); break;
               case 10: Query10(esql); break;
               case 11: Query11(esql); break;
               case 12: Query12(esql); break;
               case 13: Query13(esql); break;
               case 14: Query14(esql); break;
               case 15: Query15(esql); break;
               case 16: keepon = false; break;


               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main
   
   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              R&V Mechanic Shop Database     	              \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Welcome to R&V Mechanic Shop Database! Please make a selection of the following queries: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   // adding new customer with unused cust_id tested successfully
   public static void Query1(EmbeddedSQL esql){ 
      try {
         String query = "INSERT INTO Customer VALUES (";

         String custID = "";

         while (true) { 
            System.out.print("\tEnter 5-digit Customer ID: ");
            custID = in.readLine();

            if(custID.length() != 5){ // validate length of ID is exactly 5 
               System.out.println("\tCustomer ID must be exactly 5 digits.");
               continue;
            }

            try{ // check that input data type is numeric
               Integer.parseInt(custID);
            }
            catch(Exception e){
               System.out.println("\tCustomer ID must be numeric.");
               continue;
            }

            String checkCustIDQuery = "SELECT cust_id FROM Customer WHERE cust_id = " + custID + ";";

            int custIDExists = esql.executeQuery(checkCustIDQuery);
            
            if (custIDExists == 0) { // check if customer ID is unusued
               break;
            } 
            else { // if it's used, choose a different unused ID
               System.out.println("\tCustomer ID already exists. Please enter another unused Customer ID.");
            }
         }

         query += custID + ',';

         String firstName = "";
         while(true){
            System.out.print("\tEnter First Name: ");
            firstName = in.readLine();

            if(firstName.trim().length() == 0) {  // check that first name is NOT empty
               System.out.println("\tFirst name cannot be empty.");
               continue;
            }
            break;
         }
         query += "'" + firstName + "'" +  ',';


         String lastName = "";
         while(true){
            System.out.print("\tEnter Last Name: ");
            lastName = in.readLine();

            if(lastName.trim().length() == 0) { // check that last name is NOT empty
               System.out.println("\tLast name cannot be empty.");
               continue;
            }
            break;
         }
         query += "'" + lastName + "'" +  ',';

         String input3 = "";

         while(true){
            System.out.print("\tEnter 10-digit Phone Number: ");
            input3 = in.readLine();

            if(input3.length() != 10){ // check phone number has length 10
               System.out.println("\tPhone number must be exactly 10 digits.");
               continue;
            }

            try{
               Long.parseLong(input3);    // check phone number is completely numeric
               break;
            }
            catch(Exception e){
               System.out.println("\tPhone number must be numeric.");
            }
         }

         query += input3 + ',';

         String addrID = "";

         while(true){

            System.out.print("\tEnter 5-digit Address ID: ");
            addrID = in.readLine();

            if(addrID.length() != 5){  // check addrID is length 5
               System.out.println("\tAddress ID must be exactly 5 digits.");
               continue;
            }

            try{
               Integer.parseInt(addrID);  // check addrID is numeric
            }
            catch(Exception e){
               System.out.println("\tAddress ID must be numeric.");
               continue;
            }

            String checkAddrIDQuery =
               "SELECT addr_id FROM Address WHERE addr_id = " + addrID + ";";

            int addrExists = esql.executeQuery(checkAddrIDQuery);

            if(addrExists > 0){
               break; // address exists
            }

            System.out.println("\n \tAddress does not exist in database. Read the options below:");
            System.out.println("\t1 - Create new address");
            System.out.println("\t2 - Enter different address ID");  // goes back to beginning of while loop

            String choice = in.readLine();

            if(choice.equals("1")){ // create a new address

               String addrQuery = "INSERT INTO Address VALUES (" + addrID + ",";

               String street = "";
               while(true){   // continue asking for street until entered
                  System.out.print("\tEnter Street: ");
                  street = in.readLine();

                  if(street.trim().length() == 0){    // check a street is entered
                     System.out.println("\tStreet cannot be empty.");
                     continue;
                  }
                  break;
               }
               addrQuery += "'" + street + "',";

               String city = "";
               while(true){   // continue asking for city until entered
                  System.out.print("\tEnter City: ");
                  city = in.readLine();

                  if(city.trim().length() == 0){   // check city is entered
                     System.out.println("\tCity cannot be empty.");
                     continue;
                  }
                  break;
               }
               addrQuery += "'" + city + "',";

               String state = "";
               while(true){   // continue asking for state until entered
                  System.out.print("\tEnter State: ");   // check state is entered
                  state = in.readLine();

                  if(state.trim().length() == 0){
                     System.out.println("\tState cannot be empty.");
                     continue;
                  }
                  break;
               }
               addrQuery += "'" + state + "',";

               String zipcode = "";
               while(true){   // continue asking for zipcode until valid one is entered
                  System.out.print("\tEnter Zipcode: ");
                  zipcode = in.readLine();

                  if(zipcode.length() != 5){ // check zipcode is length 5
                     System.out.println("\tZipcode must be exactly 5 digits.");
                     continue;
                  }

                  try{
                     Integer.parseInt(zipcode); // check zipcode is numeric 
                     break;
                  }
                  catch(Exception e){
                     System.out.println("\tZipcode must be numeric.");
                  }
               }

               addrQuery += "'" + zipcode + "');";

               esql.executeUpdate(addrQuery);

               System.out.println("Address added successfully!");
               break;
            }
         }

         query += addrID + ")";


         esql.executeUpdate(query);

         System.out.println ("Customer added successfully!");
      } catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query1


   public static void Query2(EmbeddedSQL esql){
      try {
         String query = "INSERT INTO Mechanic VALUES (";

         String mechID = "";
         while (true) { 
            System.out.print("\tEnter 5-digit Mechanic ID: ");
            mechID = in.readLine();

            if(mechID.length() != 5){ // validate length of ID is exactly 5 
               System.out.println("\tMechanic ID must be exactly 5 digits.");
               continue;
            }

            try{ // check that input data type is numeric
               Integer.parseInt(mechID);
            }
            catch(Exception e){
               System.out.println("\tMechanic ID must be numeric.");
               continue;
            }

            String checkMechIDQuery = "SELECT mech_id FROM Mechanic WHERE mech_id = " + mechID + ";";
            
            int mechIDExists = esql.executeQuery(checkMechIDQuery);
            
            if (mechIDExists == 0) { // check if the mechanic ID is unused
               break;
            } 
            else { // if its used, choose a different unused ID
               System.out.println("\tMechanic ID already exists. Please enter another unused Mechanic ID.");
            }
         }

         query += mechID + ',';

         String firstName = "";
         while(true){
            System.out.print("\tEnter First Name: ");
            firstName = in.readLine();

            if(firstName.trim().length() == 0) {  // check that first name is NOT empty
               System.out.println("\tFirst name cannot be empty.");
               continue;
            }
            break;
         }
         query += "'" + firstName + "'" +  ',';


         String lastName = "";

         while(true){
            System.out.print("\tEnter Last Name: ");
            lastName = in.readLine();

            if(lastName.trim().length() == 0) { // check that last name is NOT empty
               System.out.println("\tLast name cannot be empty.");
               continue;
            }
            break;
         }
         query += "'" + lastName + "'" +  ',';

         String yearsOfExp = "";

         while(true){
            System.out.print("\tEnter Years of Experience (0-99): ");
            yearsOfExp = in.readLine();

            try{
               int yearsNum = Integer.parseInt(yearsOfExp);

               if(yearsNum < 0 || yearsNum > 99) { // validate the realistic # of years of experience
                  System.out.println("\tExperience must be between 0 and 99.");
                  continue;
               }

               break;
            }
            catch(Exception e){
               System.out.println("\tExperience must be a number between 0 and 99.");
            }
         }
         query += yearsOfExp + ")";

         esql.executeUpdate(query);

         System.out.println("Mechanic added successfully!");

      } catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query2


   public static void Query3(EmbeddedSQL esql){
     try{
         String vin = "";
         while(true){ // check that service request is unused
            System.out.print("\tEnter 5-digit VIN number: ");
            vin = in.readLine();

            if(vin.length() != 5){ // validate length of vin is exactly 5 
               System.out.println("\tVIN must be exactly 5 digits.");
               continue;
            }

            try{ // check that input data type is numeric
               Integer.parseInt(vin);
            }
            catch(Exception e){
               System.out.println("\tVIN must be numeric.");
               continue;
            }

            String checkVIN = "SELECT vin FROM Cars WHERE vin = " + vin + ";";
            int count = esql.executeQuery(checkVIN);

            if(count == 0){
               break; // car does NOT exist in DB yet -> so add
            }

            System.out.println("\tVIN number already exists in database. Please enter a different VIN.");
         }

         String year = "";
         while(true){
            System.out.print("\tEnter year of vehicle: ");
            year = in.readLine();

            try{
               int y = Integer.parseInt(year);  // check year is numeric 

               if(y < 1886 || y > 2026){  // check valid year is entered
                  System.out.println("\tEnter a valid vehicle year.");
                  continue;
               }

               break;
            }
            catch(Exception e){
               System.out.println("\tYear must be numeric.");
            }
         }

         String make = "";

         while(true){
            System.out.print("\tEnter make: ");
            make = in.readLine();

            if(make.trim().length() == 0){   // check make is entered
               System.out.println("\tMake cannot be empty.");
               continue;
            }
            break;
         }


         String model = "";         
         while(true){
            System.out.print("\tEnter model: ");
            model = in.readLine();

            if(model.trim().length() == 0){  // check model is entered
               System.out.println("\tModel cannot be empty.");
               continue;
            }
            break;
         }

         String cust = "";
         while(true) {
            System.out.print("\tEnter 5-digit customer ID of owner: ");
            cust = in.readLine();

            if(cust.length() != 5){ // check cust_id is length 5
               System.out.println("\tCustomer ID must be 5 digits.");
               continue;
            }

            try{
               Integer.parseInt(cust);    // check cust_id is numeric
            } 
            catch(Exception e){
               System.out.println("\tCustomer ID must be numeric.");
               continue;
            }

            String checkReqID = "SELECT cust_id FROM Cars WHERE cust_id = " + cust + ";";
            int count = esql.executeQuery(checkReqID);

            if(count > 0){
               break;   // there is a customer in the db with the entered cust_id
            }

            System.out.println("\n \tCustomer does not exist in database. Read the options below:");
            System.out.println("\t1 - Create new Customer");
            System.out.println("\t2 - Enter different Customer ID");
            String input = in.readLine();


            if(input.equals("1")) {
                Query1(esql); //create a new customer
                continue;
            }
            else if(input.equals("2")) {
               continue;   // loops back to prompt for cust_id again
            }
            else {
               System.out.println("\t Invalid option.");
            }
         }

         String query = "INSERT INTO Cars VALUES (" + vin + ", " + year + ", '" + make + "', '" + model + "', " + cust + ");";

         esql.executeUpdate(query);
         System.out.println("Car added successfully!");

     }catch(Exception e){
        System.err.println (e.getMessage());
     }
   }//end Query3


   public static void Query4(EmbeddedSQL esql){
   try{
      String custLasttName = "";

      while (true) { 
         System.out.print("\tEnter last name of the Customer of the Service Request: ");
         custLasttName = in.readLine();

         if(custLasttName.trim().length() == 0) {  // check that last name is NOT empty
            System.out.println("\tLast name cannot be empty.");
            continue;
         }

         String checkLastNameQuery = "SELECT lastName FROM Customer WHERE lastName = '" + custLasttName + "';";

         int custExists = esql.executeQuery(checkLastNameQuery);
         
         if(custExists > 0){
            break;   // customer is in database --> can continue with initiating service request
         }

         System.out.println("\tCustomer does not exist in database. Enter '1' to create a new customer or '2' to enter a different customer id.");
         String input = in.readLine();


         if(input.equals("1")) {
               Query1(esql); //create a new customer
               continue;
         }
         else if(input.equals("2")) {
            continue;   // loop back to enter cust_id again
         }
         else {
            System.out.println("\t Invalid option.");
         }
      }

      String query = "SELECT cust_id, firstName, lastName FROM Customer WHERE lastName = '" + custLasttName + "';";
      int rowCount = esql.executeQuery(query);

      String custIDInput = "";

      if(rowCount > 1){    // if multiple customers have the inputted last name, select specific cust_id
         System.out.print("\tMultiple Customers found. Select a Customer ID from the list above: ");
         custIDInput = in.readLine();
      }

      else if(rowCount == 1){    // only one customer found
         String custIDQuery = "SELECT cust_id FROM Customer WHERE lastName = '" + custLasttName + "';";
         Statement stmt = esql._connection.createStatement();
         ResultSet rs = stmt.executeQuery(custIDQuery);

         if(rs.next()){
            custIDInput = rs.getString("cust_id"); // get cust_id from query
         }

         rs.close();
         stmt.close();

         System.out.println("\tOne customer found. Using Customer ID: " + custIDInput);
      }

      else{
         System.out.println("\tNo customers found with last name '" + custLasttName + "'");
         System.out.println("\tPlease create a new Customer profile.");

         Query1(esql);  // create a new customer

         System.out.print("\tEnter the new Customer's ID: ");
         custIDInput = in.readLine();  // get cust_id of newly created customer
      }

      String carQuery = "SELECT vin, year, make, model FROM Cars WHERE cust_id = " + custIDInput + ";";
      int carCount = esql.executeQuery(carQuery);

      if(carCount == 0){   // selected customer does not own a car
         System.out.println("\tNo cars found for this customer. Please add a car.");
         Query3(esql);  // add a car to the db
      }

      String carVinInput = "";
      while(true) {  // check entered VIN is valid
         System.out.print("\tSelect the VIN of the car for the Service Request: ");
         carVinInput = in.readLine();

         try{ // check that input data type is numeric
               Integer.parseInt(carVinInput);
            }
            catch(Exception e){
               System.out.println("\tVIN number must be numeric.");
               continue;
            }

         if (carVinInput.length() == 5) {  // check VIN length is 5
            break;
         }
         else {
            System.out.print("\tVIN number must be 5 digits long.");
         }
      }


      System.out.println("\nAvailable Mechanics:");

      String mechQuery = "SELECT * FROM Mechanic WHERE mech_id NOT IN (SELECT mech_id FROM Service_Request WHERE closeDate IS NULL);";

      int mechCount = esql.executeQuery(mechQuery);

      if(mechCount == 0){  // no available mechanics, cannot initiate service request
         System.out.println("\tNo mechanics available right now.");
         return;
      }

      String srMechID = "";

      while(true){   // validate inputted mechanic
         System.out.print("\tEnter Mechanic ID from the list above: ");
         srMechID = in.readLine();

         String checkMech = "SELECT mech_id FROM Service_Request WHERE mech_id = " + srMechID + "  IS NULL;";

         int valid = esql.executeQuery(checkMech);

         if(valid != 1){   // mechanic exists and is not working on an open service request already
            break;   
         }

         System.out.println("\tMechanic unavailable or invalid. Try again.");
      }


      String srReqID = "";

      while(true){ // check that service request is unused
         System.out.print("Enter Service Request ID: ");
         srReqID = in.readLine();

         String checkReqID =
            "SELECT req_id FROM Service_Request WHERE req_id = " + srReqID + ";";

         int count = esql.executeQuery(checkReqID);

         if(count == 0){
            break; // ID is unique
         }

         System.out.println("\tService Request ID already exists. Please enter a different ID.");
      }

      System.out.print("Enter the date car was brought in (yymmdd): ");
      String srInDate = in.readLine();

      System.out.print("Enter the reason for visit: ");
      String srProblem = in.readLine();

      System.out.print("If applicable, Enter start date (yymmd) or press ENTER if none: ");
      String srStartDate = in.readLine();

      System.out.print("If applicable, Enter close date (yymmd) or press ENTER if none: ");
      String srEndDate = in.readLine();

      System.out.print("Enter comments or press ENTER if none: ");
      String srComments = in.readLine();

      System.out.print("Enter bill amount: ");
      String srBill = in.readLine();

      System.out.print("Enter odometer reading: ");
      String srOdometer = in.readLine();


      // Convert empty inputs to NULL
      if(srStartDate.trim().isEmpty()) srStartDate = "NULL";
      if(srEndDate.trim().isEmpty()) srEndDate = "NULL";
      if(srComments.trim().isEmpty()) srComments = "NULL";
      else srComments = "'" + srComments + "'";

      String addSRQuery = "INSERT INTO Service_Request VALUES(" + srReqID + "," + srMechID + "," + carVinInput + "," + srInDate + "," + "'" + srProblem + "'," + 
                           srStartDate + "," + srEndDate + "," + srComments + "," + srBill + "," + srOdometer + ");";

      esql.executeUpdate(addSRQuery);

      System.out.println("\nService Request added successfully!");
   }

      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }


   public static void Query5(EmbeddedSQL esql){
      try{
         String request = "";

         while(true){ // check that service request exists and is open
            System.out.print("\tEnter service request number: ");
            request = in.readLine();
            String checkReqID = "SELECT req_id FROM Service_Request WHERE req_id = " + request + " AND closeDate IS NULL;";
            int count = esql.executeQuery(checkReqID);


            if(count != 0){
               break; // ID is in requests and is open
            }

            System.out.println("\tService Request ID is already closed or does not exist. Please enter a different ID.");
         }


         String mechanic = "";
         while(true){   // validate entered mechanic id
            System.out.print("\tEnter mechanic ID: ");
            mechanic = in.readLine();
            if(mechanic.length() != 5){ // check mechanic id has length 5
               System.out.println("\tMechanic id must be exactly length 5.");
               continue;
            }

            try{
               Integer.parseInt(mechanic);    // check mechanic id is completely numeric
               break;
            }
            catch(Exception e){
               System.out.println("\tMechanic id must be numeric.");
            }

            // check that mechanic is linked to inputted service request
            String checkReqID = "SELECT mech_id FROM Service_Request WHERE req_id = " + request + " AND closeDate IS NULL AND mech_id = " + mechanic + ";";
            int count = esql.executeQuery(checkReqID);

            if(count != 0){
               break; // mechanic matches request id
            }

            System.out.println("\tMechanic is not linked to the request. Please enter a different mechanic ID.");
         }

         String query = "UPDATE Service_Request SET closeDate = ";

         String input = "";
         while(true){
            System.out.print("\tEnter Service Request's close date (yymmdd): ");
            input = in.readLine();
            
            if(input.length() != 6){ // check mechanic id has length 5
               System.out.println("\tDate must be exactly length 5.");
               continue;
            }

            try{
               Integer.parseInt(input);    // check mechanic id is completely numeric
               break;
            }
            catch(Exception e){
               System.out.println("\tDate must be numeric.");
            }

            String checkValidCloseQuery = "SELECT req_id FROM Service_Request WHERE req_id = " + request + " AND openDate IS NOT NULL AND openDate <= " + input + ";";
            int validCloseDate = esql.executeQuery(checkValidCloseQuery);

            if(validCloseDate == 1){
               break; // closeDate is valid
            }

            System.out.println("\tClose date cannot be earlier than the open date. Please enter a valid date (yymmdd).");
         }

         query += input + ", comments = '";

         while(true){
            System.out.print("\tIf applicable, enter any additional comments. Must include 'Done' before enter: ");
            input = in.readLine();

            if(input.contains("Done")){
               break;
            }

            System.out.println("\tComments must include the word 'Done' before closing the request.");
         }

         query += input + "', bill = ";

         while(true) {
            System.out.print("\tEnter total bill amount: ");
            input = in.readLine();

            try{
               Integer.parseInt(input);    // check bill is completely numeric
               break;
            }
            catch(Exception e){
               System.out.println("\tBill amount must be numeric.");
            }
         }

         query += input + " WHERE req_id = " + request + " AND mech_id = " + mechanic + " AND closeDate IS NULL;";
         esql.executeUpdate(query);

         System.out.println("Service Request closed successfully!");
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query5

   public static void Query6(EmbeddedSQL esql){
      try{
         String query = "SELECT S.closeDate, S.comments, S.bill FROM Service_Request S WHERE S.closeDate IS NOT NULL AND S.bill < 100;";
         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      } 
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query6


   public static void Query7(EmbeddedSQL esql){
      try{
         String query = "SELECT Cu.firstName, Cu.lastName FROM Customer Cu, Cars Ca WHERE Cu.cust_id = Ca.cust_id GROUP BY Cu.firstName, Cu.lastName HAVING COUNT(Ca.cust_id) > 20;";
         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      } 
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query7


   public static void Query8(EmbeddedSQL esql){
     try{
        String query = "SELECT C.make, C.model, C.year FROM Cars C, Service_Request S WHERE C.vin = S.vin AND C.year < 1995 AND S.odometer < 50000";
        int rowCount = esql.executeQuery(query);
        System.out.println ("total row(s): " + rowCount);
     }
     catch(Exception e){
        System.err.println (e.getMessage());
     }
   }//end Query8


   public static void Query9(EmbeddedSQL esql){
      try{
         String query = "SELECT Ca.make, Ca.model, COUNT(*) AS num_service_requests " +
                        "FROM Cars Ca, Service_Request SR " +
                        "WHERE Ca.vin = SR.vin " +
                        "GROUP BY Ca.vin, Ca.make, Ca.model " +
                        "ORDER BY num_service_requests DESC LIMIT ";        

          int k = 0;
            while(true) {
               System.out.print("\tEnter k (positive integer): ");
               String input = in.readLine();
               try {
                  k = Integer.parseInt(input);
                  if(k > 0) { // check k is valid
                     break;
                  } else {
                     System.out.println("\tk must be greater than 0.");
                  }
               } catch(Exception e) {
                  System.out.println("\tk must be a positive integer.");
               }
            }
            query += k + ";";

            int rowCount = esql.executeQuery(query);
            System.out.println ("total row(s): " + rowCount);

      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query9


   public static void Query10(EmbeddedSQL esql){
      try{
         String query = "SELECT T.cust_id, T.firstName, T.lastName, SUM(S.bill) as customerTotalBill FROM Customer T, Service_Request S, Cars C WHERE T.cust_id = C.cust_id AND C.vin = S.vin GROUP BY T.cust_id ORDER BY customerTotalBill DESC";
         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      } 
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query10

   public static void Query11(EmbeddedSQL esql){
     try{
      String req_id = "";
      while(true){ // check that service request is unused
         System.out.println ("Enter 5-digit service request id");
         req_id = in.readLine();
         String checkReqID = "SELECT req_id FROM Service_Request WHERE req_id = " + req_id + " AND closeDate IS NULL;";
         int count = esql.executeQuery(checkReqID);

         if(count != 0){
            break; // ID is in requests and open
         }

         System.out.println("\tService Request ID does not exist or is not open. Please enter a different ID.");
      }


      String mechanic = "";
      while(true){ // check that mechanic is in db 
         System.out.println ("Enter 5-digit mechanic id");
         mechanic = in.readLine();
         String checkMechanic = "SELECT mech_id FROM Mechanic WHERE mech_id = " + mechanic + ";";
         int countMechanic = esql.executeQuery(checkMechanic);

         if(countMechanic == 0){
            System.out.println("\tMechanic does not exist. Please enter a different ID.");
            continue;
         }

         String checkMechanicInSR = "SELECT * FROM Service_Request WHERE closeDate IS NULL AND mech_id = " + mechanic + ";";
         int countSR = esql.executeQuery(checkMechanicInSR);

         if(countSR != 0) {   // check mechanic is available for work
            System.out.println("\tMechanic is already working on another service request. Please enter a different ID.");
            continue;
         }

         break;
      }

      String query = "UPDATE Service_Request SET mech_id = " + mechanic + " WHERE req_id = " + req_id + ";";
      esql.executeUpdate(query);
      System.out.println ("Service Request's Mechanic Updated Successfully!");
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query 11


   public static void Query12(EmbeddedSQL esql){
     try{
         String query = "SELECT * FROM Mechanic WHERE mech_id NOT IN (SELECT mech_id FROM Service_Request WHERE closeDate IS NULL);";
         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query12


   public static void Query13(EmbeddedSQL esql){
     try{
         String query = "SELECT req_id, vin, mech_id, openDate, problem FROM Service_Request WHERE closeDate IS NULL;";
         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query13


   public static void Query14(EmbeddedSQL esql){
     try{
         String query = "SELECT Ca.make, AVG(SR.bill) AS avg_repair_cost FROM Cars Ca, Service_Request SR WHERE Ca.vin = SR.vin AND SR.bill IS NOT NULL GROUP BY Ca.make;";
         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query14


   public static void Query15(EmbeddedSQL esql){
     try{
         String query = "SELECT C.firstName, C.lastName, SR.req_id, Ca.make, Ca.model FROM Customer C, Cars Ca, Service_Request SR WHERE C.cust_id = Ca.cust_id AND Ca.vin = SR.vin AND SR.closeDate IS NULL;";
         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query15

}//end EmbeddedSQL
