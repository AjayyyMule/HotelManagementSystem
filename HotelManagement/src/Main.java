
import java.sql.*;
import java.util.Scanner;

public class Main {


    public static void reserveRoom(Connection connection,Scanner sc){
        try{
            System.out.println("Enter guest name: ");
            String guestName = sc.next();

            System.out.println("Enter room number: ");
            int roomNumber = sc.nextInt();

            System.out.println("Enter contact number: ");
            String contactNumber = sc.next();

            String sql = "INSERT INTO Reservations(guest_name, room_number, contact_number) VALUES('" + guestName + "', " + roomNumber + ", '" + contactNumber + "');";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if(affectedRows>0){
                    System.out.println("Reservation successful");
                }else{
                    System.out.println("Reservation failed ");
                }

            }catch(SQLException e){
                e.printStackTrace();
            }

        }catch(Exception e){
            System.out.println("Some exception");

        }

    }

    private static void viewReservations(Connection connection) throws SQLException{

        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM Reservations; ";

        try(Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql)) {

            System.out.println("Current Reservations ");

            System.out.println("================================================================================================= ");
            System.out.println("| Reservation ID | Guest           |  Room Number       | Contact Number      | Reservation Date  ");
            System.out.println("==================================================================================================");

            while (rs.next()){
                int reservationId = rs.getInt("reservation_id") ;
                String guestName = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_number");
                String contactNumber = rs.getString("contact_number");
                String reservationDate = rs.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %13d | %-20s | %-19s |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);


            }
            System.out.println("--------------------------------------------------------------------------------------------------------");

        }



    }


    private static void getRoomNumber(Connection connection, Scanner sc){

        try{
            System.out.println("Enter reservation ID: ");
            int reservationId = sc.nextInt();
            System.out.println("Enter guest name: ");
            String guestName = sc.next();

            String sql = "SELECT room_number FROM Reservations WHERE reservation_id = " + reservationId + " AND guest_name = '" + guestName + "';";


            try{
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);

                if(rs.next()){
                    int roomNumber = rs.getInt("room_number");
                    System.out.println("Room number for Reservation ID "+ reservationId + " and Guest "+ guestName + " is "+ roomNumber);
                }else{
                    System.out.println("Reservation not found for the given ID and guest name. " );
                }

            }catch(SQLException sqe){
                sqe.printStackTrace();

            }

        }catch (Exception e){
            e.printStackTrace();

        }

    }


    private static void updateReservations(Connection connection,Scanner sc){

        try{
            System.out.println("Enter reservation ID to update");
            int reservationId = sc.nextInt();

            if(!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found on given ID ");
                return;

            }

            System.out.println("Enter new guest name: ");
            String newGuestName = sc.nextLine();

            System.out.println("Enter new room number: ");
            int newRoomNumber = sc.nextInt();

            System.out.println("Enter new contact number");
            String newContactNumber = sc.nextLine();

            String sql = "UPDATE Reservations SET guest_name = '" + newGuestName + "', room_number = " + newRoomNumber + ", contact_number = '" + newContactNumber + "' WHERE reservation_id = " + reservationId;


            try (Statement statement = connection.createStatement()) {
                    int affectedRows = statement.executeUpdate(sql);

                    if (affectedRows>0){
                        System.out.println("Reservation updated successfully ");
                    }else{
                        System.out.println("Reservation failed successfully ");

                    }
                }
            }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();


        }


    }


    private static void deleteReservations(Connection connection,Scanner sc){

        try{
            System.out.println("Enter reservation ID to Delete: ");
            int reservationId = sc.nextInt();

            if(!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found on given ID ");
                return;
            }

            String sql = "DELETE FROM Reservations WHERE reservation_id = " + reservationId;

            try(Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows>0){
                    System.out.println("Reservation deleted successfully");
                }else{
                    System.out.println("Reservation deletion failed ");
                }

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }



    private static boolean reservationExists(Connection connection , int reservationId){
        try {

            String sql = "SELECT reservation_id FROM Reservations WHERE reservation_id = " + reservationId;


            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(sql);

                return rs.next();  // If there's result, the reservation exists

            }

        }catch (SQLException sqe){
            sqe.printStackTrace();
            return false;

        }
    }



    public static void exit() throws InterruptedException{
        System.out.print("Exiting System");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou for using Hotel Reservation System!!!!");

    }




    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "root";


    public static void main(String[] args) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("Drivers loaded successfully ");
        }catch(ClassNotFoundException cnfe){
            System.out.println(cnfe.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
//            System.out.println("Connection Established Successfully ");
            while(true){
                System.out.println();
                System.out.println("Hotel Management System");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations ");
                System.out.println("3.Get Room Number");
                System.out.println("4. Update Reservations ");
                System.out.println("5. Delete Reservations ");
                System.out.println("0. Exit");
                System.out.println("Choose an Option: ");
                int option = sc.nextInt();

                switch(option){
                    case 1:
                        reserveRoom(connection, sc);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, sc);
                        break;
                    case 4:
                        updateReservations(connection, sc);
                        break;
                    case 5:
                        deleteReservations(connection, sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice, Try again !!!");

                }



            }

        }catch (SQLException sqe) {
            System.out.println(sqe.getMessage());
        }
        catch(InterruptedException ie){
            throw new RuntimeException(ie);
        }



    }
}