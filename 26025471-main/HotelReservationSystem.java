package demo;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "user";
    private static final String password = "mini@5";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println("1. Reservation Management");
                System.out.println("2. Room Management");
                System.out.println("3. Customer Management");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reservationManagement(connection, scanner);
                        break;
                    case 2:
                        roomManagement(connection, scanner);
                        break;
                    case 3:
                        customerManagement(connection, scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void reservationManagement(Connection connection, Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("RESERVATION MANAGEMENT");
            System.out.println("1. Reserve a room");
            System.out.println("2. View Reservations");
            System.out.println("3. Get Room Number");
            System.out.println("4. Update Reservations");
            System.out.println("5. Delete Reservations");
            System.out.println("0. Back to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    reserveRoom(connection, scanner);
                    break;
                case 2:
                    viewReservations(connection);
                    break;
                case 3:
                    getRoomNumber(connection, scanner);
                    break;
                case 4:
                    updateReservation(connection, scanner);
                    break;
                case 5:
                    deleteReservation(connection, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                    "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation successful!");
                } else {
                    System.out.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection connection) {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number       | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID: ");
            int reservationId = scanner.nextInt();
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();

            String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = " + reservationId +
                    " AND guest_name = '" + guestName + "'";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId +
                            " and Guest " + guestName + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }

    private static void roomManagement(Connection connection, Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("ROOM MANAGEMENT");
            System.out.println("1. Add a new room");
            System.out.println("2. View room details");
            System.out.println("3. Update room information");
            System.out.println("4. Delete a room");
            System.out.println("0. Back to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addRoom(connection, scanner);
                    break;
                case 2:
                    viewRoomDetails(connection);
                    break;
                case 3:
                    updateRoom(connection, scanner);
                    break;
                case 4:
                    deleteRoom(connection, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addRoom(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            System.out.print("Enter room type: ");
            String roomType = scanner.nextLine();
            System.out.print("Enter room price: ");
            double roomPrice = scanner.nextDouble();

            String sql = "INSERT INTO rooms (room_number, room_type, room_price) " +
                    "VALUES (" + roomNumber + ", '" + roomType + "', " + roomPrice + ")";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Room added successfully!");
                } else {
                    System.out.println("Failed to add room.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewRoomDetails(Connection connection) {
        String sql = "SELECT room_number, room_type, room_price FROM rooms";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Room Details:");
            System.out.println("+------------+---------------+------------+");
            System.out.println("| Room Number| Room Type     | Room Price |");
            System.out.println("+------------+---------------+------------+");

            while (resultSet.next()) {
                int roomNumber = resultSet.getInt("room_number");
                String roomType = resultSet.getString("room_type");
                double roomPrice = resultSet.getDouble("room_price");

                System.out.printf("| %-11d | %-13s | %-10.2f |\n", roomNumber, roomType, roomPrice);
            }

            System.out.println("+------------+---------------+------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateRoom(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter room number to update: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            if (!roomExists(connection, roomNumber)) {
                System.out.println("Room not found for the given number.");
                return;
            }

            System.out.print("Enter new room type: ");
            String newRoomType = scanner.nextLine();
            System.out.print("Enter new room price: ");
            double newRoomPrice = scanner.nextDouble();

            String sql = "UPDATE rooms SET room_type = '" + newRoomType + "', room_price = " + newRoomPrice +
                    " WHERE room_number = " + roomNumber;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Room updated successfully!");
                } else {
                    System.out.println("Room update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRoom(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter room number to delete: ");
            int roomNumber = scanner.nextInt();

            if (!roomExists(connection, roomNumber)) {
                System.out.println("Room not found for the given number.");
                return;
            }

            String sql = "DELETE FROM rooms WHERE room_number = " + roomNumber;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Room deleted successfully!");
                } else {
                    System.out.println("Room deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean roomExists(Connection connection, int roomNumber) {
        try {
            String sql = "SELECT room_number FROM rooms WHERE room_number = " + roomNumber;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the room exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }

    private static void customerManagement(Connection connection, Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("CUSTOMER MANAGEMENT");
            System.out.println("1. Register a new customer");
            System.out.println("2. View customer details");
            System.out.println("3. Update customer information");
            System.out.println("4. Delete a customer");
            System.out.println("0. Back to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    registerCustomer(connection, scanner);
                    break;
                case 2:
                    viewCustomerDetails(connection);
                    break;
                case 3:
                    updateCustomer(connection, scanner);
                    break;
                case 4:
                    deleteCustomer(connection, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void registerCustomer(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter customer name: ");
            String customerName = scanner.next();
            scanner.nextLine(); // Consume newline character
            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO customers (customer_name, contact_number) " +
                    "VALUES ('" + customerName + "', '" + contactNumber + "')";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Customer registered successfully!");
                } else {
                    System.out.println("Customer registration failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewCustomerDetails(Connection connection) {
        String sql = "SELECT customer_id, customer_name, contact_number FROM customers";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Customer Details:");
            System.out.println("+-------------+-----------------+------------------+");
            System.out.println("| Customer ID | Customer Name   | Contact Number   |");
            System.out.println("+-------------+-----------------+------------------+");

            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String customerName = resultSet.getString("customer_name");
                String contactNumber = resultSet.getString("contact_number");

                System.out.printf("| %-11d | %-15s | %-16s |\n", customerId, customerName, contactNumber);
            }

            System.out.println("+-------------+-----------------+------------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateCustomer(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter customer ID to update: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            if (!customerExists(connection, customerId)) {
                System.out.println("Customer not found for the given ID.");
                return;
            }

            System.out.print("Enter new customer name: ");
            String newCustomerName = scanner.nextLine();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE customers SET customer_name = '" + newCustomerName + "', " +
                    "contact_number = '" + newContactNumber + "' WHERE customer_id = " + customerId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Customer updated successfully!");
                } else {
                    System.out.println("Customer update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteCustomer(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter customer ID to delete: ");
            int customerId = scanner.nextInt();

            if (!customerExists(connection, customerId)) {
                System.out.println("Customer not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM customers WHERE customer_id = " + customerId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Customer deleted successfully!");
                } else {
                    System.out.println("Customer deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean customerExists(Connection connection, int customerId) {
        try {
            String sql = "SELECT customer_id FROM customers WHERE customer_id = " + customerId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the customer exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }

    public static void exit() {
        System.out.println("Exiting Hotel Management System. Goodbye!");
    }
}

