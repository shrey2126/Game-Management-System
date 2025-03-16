import java.sql.*;
import java.util.*;
import java.util.Stack;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class GameManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gamemanagementdb";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    static String emailId, pwd;
    static Scanner sc = new Scanner(System.in);
    static {
        DATE_FORMAT.setLenient(false); // Strictly parse the date
        TIME_FORMAT.setLenient(false); // Strictly parse the time
    }
    static GameManagementSystem gms = new GameManagementSystem();
    static Stack<String> operations = new Stack<>();

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private GameLinkedList recentGames = new GameLinkedList();

    public void registerUser(String username, String password, String email, String profile) {
        String query = "INSERT INTO users (username, password, email, profile) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            //pstmt.setInt(1,id);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, profile);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGame(int id, String title, String type, int price, boolean availability) {
        String query = "INSERT INTO Games1(id,title, type ,price, availability) VALUES (?,?, ?, ?,?)";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, type);
            pstmt.setInt(4, price);
            pstmt.setBoolean(5, availability);
            pstmt.execute();
            // Add to the recent games linked list
            recentGames.add(new Game(id, title, type, price, availability)); // Assuming 0 as a placeholder ID
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Game> getAllGames() {
        LinkedList<Game> games = new LinkedList<>();
        String query = "SELECT * FROM Games1";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                games.add(new Game(rs.getInt("id"), rs.getString("title"), rs.getString("type"), rs.getInt("price"),
                        rs.getBoolean("availability")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public void bookGameSession(String user_name, String game_name, String bookingDate, String bookingTime) {
        String query = "INSERT INTO Bookings (user_name, game_name, booking_date, booking_time) VALUES (?, ?, ?, ?)";

        while (true) {
            boolean validDate = false;
            boolean validTime = false;
            Scanner scanner = new Scanner(System.in);

            // Validate date format
            while (!validDate) {
                try {
                    DATE_FORMAT.parse(bookingDate);
                    validDate = true;
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use DD-MM-YYYY.");
                    System.out.print("Enter booking date (DD-MM-YYYY): ");
                    bookingDate = scanner.nextLine();
                }
            }

            // Validate time format
            while (!validTime) {
                try {
                    TIME_FORMAT.parse(bookingTime);
                    validTime = true;
                } catch (ParseException e) {
                    System.out.println("Invalid time format. Please use HH:MM:SS.");
                    System.out.print("Enter booking time (HH:MM:SS): ");
                    bookingTime = scanner.nextLine();
                }
            }

            // If both date and time are valid, break the loop
            if (validDate && validTime) {
                break;
            }
        }

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user_name);
            pstmt.setString(2, game_name);
            pstmt.setString(3, bookingDate);
            pstmt.setString(4, bookingTime);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLeaderboardEntry(String user_name, String game_name, int score, String achievements) {
        String query = "INSERT INTO Leaderboard (user_name, game_name, score, achievements) VALUES (?,?,?,?)";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user_name);
            pstmt.setString(2, game_name);
            pstmt.setInt(3, score);
            pstmt.setString(4, achievements);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByEmail(String email) {
        String query = "{call getUserByEmail(?)}";
        try (Connection conn = connect();
                CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setString(1, email);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("profile"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found
    }

    public void updateGameAvailability(int gameId, boolean newAvailability) {
        String query = "{call updateGameAvailability(?, ?)}";
        try (Connection conn = connect();
                CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setInt(1, gameId);
            cstmt.setBoolean(2, newAvailability);
            cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String game_name, String message) {
        String query = "INSERT INTO Notifications (user_name, message) VALUES (?, ?)";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, game_name);
            pstmt.setString(2, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayRecentGames() {
        System.out.println("Recently Added Games:");
        recentGames.display();
    }

    public GameManagementSystem() {

    }

    public GameManagementSystem(int n) {
        try {
            boolean flag = true;
            while (flag) {
                System.out.println(
                        """
                                ------------------------------------------------------------------------------------------------
                                ==================================Welcome to The GameZONE=======================================
                                ------------------------------------------------------------------------------------------------
                                |                                         Homepage                                             |
                                ------------------------------------------------------------------------------------------------
                                |                                  Press 1 For Login                                           |
                                |                                  Press 2 For Registration                                    |
                                |                                  Press 3 For Exit                                            |
                                ------------------------------------------------------------------------------------------------
                                |                                  Enter from Above Options                                    |
                                ------------------------------------------------------------------------------------------------""");
                if (sc.hasNextInt()) {
                    int choice = sc.nextInt();
                    switch (choice) {
                        case 1: {
                            boolean isValidUser = isUser();
                            if (isValidUser) {
                                System.out.println("Login Successfully");
                                handleUserOperations();
                            }
                            break;
                        }
                        case 2: {
                            System.out.print("Enter username: ");
                            String username = sc.nextLine();
                            sc.nextLine();
                            System.out.print("Enter password: ");
                            String password = sc.nextLine();
                            System.out.print("Enter email: ");
                            String email = sc.nextLine();
                            System.out.print("Enter profile: ");
                            String profile = sc.nextLine();
                            System.out.println("User registered successfully!!");
                            gms.registerUser(username, password, email, profile);
                            operations.push("Registered User: " + username);
                            break;
                        }
                        case 3: {
                            flag = false;
                            break;
                        }
                        default: {
                            System.out.println("Enter Valid Option");
                        }
                    }
                } else {
                    System.out.println("Please enter a valid integer option.");
                    sc.next(); // Consume the invalid input
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sc.nextLine();
            new GameManagementSystem(1);
        }
    }

    public static void handleUserOperations() throws Exception {
        GameLinkedList gll = new GameLinkedList();
        GameManagementSystem gms = new GameManagementSystem();
        Scanner scanner = new Scanner(System.in);
        Stack<String> operations = new Stack<>();
        boolean exit = false;

        while (!exit) {
            System.out.println();
            System.out.println("********  GAME MANAGEMENT SYSTEM  ********");
            System.out.println("--> 1. Register User");
            System.out.println("--> 2. Add Game ");
            System.out.println("--> 3. Payment ");
            System.out.println("--> 4. Book Game Session");
            System.out.println("--> 5. Add Leaderboard Entry");
            System.out.println("--> 6. Send Notification");
            System.out.println("--> 7. View Recently Added Game");
            System.out.println("--> 8. Retrive User by Email");
            System.out.println("--> 9. Search Game by Title");
            System.out.println("--> 10.Exit");
            System.out.println("*******************************************");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter profile: ");
                    String profile = scanner.nextLine();
                    System.out.println("User registerd successfully!! ");
                    gms.registerUser(username, password, email, profile);
                    operations.push("Registered User: " + username);
                    break;

                case 2:
                    boolean addMoreGames = true;

                    while (addMoreGames) {
                        System.out.println("Choose a game to add:");
                        System.out.println("1. Chess");
                        System.out.println("2. Monopoly");
                        System.out.println("3. Scrabble");
                        System.out.println("4. Carrom");
                        System.out.println("5. Custom Game");
                        System.out.println("Enter choice:");
                        int gameChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        String title = "";
                        String type = "";
                        int price = 0;
                        boolean availability = false;

                        switch (gameChoice) {
                            case 1:
                                title = "Chess";
                                type = "Board Game";
                                price = 500;
                                availability = true;
                                gll.add(new Game(1, title, type, price, availability));
                                gms.addGame(1, title, type, price, availability); // Add to the database
                                break;
                            case 2:
                                title = "Monopoly";
                                type = "Board Game";
                                price = 800;
                                availability = true;
                                gll.add(new Game(2, title, type, price, availability));
                                gms.addGame(2, title, type, price, availability); // Add to the database
                                break;
                            case 3:
                                title = "Scrabble";
                                type = "Word Game";
                                price = 600;
                                availability = true;
                                gll.add(new Game(3, title, type, price, availability));
                                gms.addGame(3, title, type, price, availability); // Add to the database
                                break;
                            case 4:
                                title = "Carrom";
                                type = "Board Game";
                                price = 300;
                                availability = true;
                                gll.add(new Game(4, title, type, price, availability));
                                gms.addGame(4, title, type, price, availability); // Add to the database
                                break;
                            case 5:
                                int id;
                                while (true) {
                                    System.out.println("Enter game Id (must be greater than 4): ");
                                    id = scanner.nextInt();
                                    scanner.nextLine(); // Consume newline

                                    if (id > 4) {
                                        break; // Exit loop if the ID is valid
                                    } else {
                                        System.out.println("Invalid ID! Please enter a number greater than 4.");
                                    }
                                }

                                System.out.print("Enter game title: ");
                                title = scanner.nextLine();
                                System.out.print("Enter game type: ");
                                type = scanner.nextLine();
                                System.out.print("Enter game price: ");
                                price = scanner.nextInt();
                                scanner.nextLine(); // Consume newline

                                // Input validation for availability
                                while (true) {
                                    System.out.print("Is the game available? (true/false): ");
                                    try {
                                        availability = scanner.nextBoolean();
                                        scanner.nextLine(); // Consume newline
                                        break; // Exit loop if valid input
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid input! Please enter 'true' or 'false'.");
                                        scanner.nextLine(); // Clear the invalid input
                                    }
                                }
                                gms.addGame(id, title, type, price, availability);
                                break;
                            default:
                                System.out.println("Invalid choice! No game added.");
                                break;
                            // Skip the payment prompt and continue adding games
                        }

                        System.out.println("Do you want to add another game? (yes/no):");
                        String response = scanner.nextLine();
                        addMoreGames = response.equalsIgnoreCase("yes");

                    }
                    break;

                case 3:
                    LinkedList<Game> games = gms.getAllGames();
                    for (Game game : games) {

                        System.out.println("Payment Process:");
                        Game g1 = new Game(game.id, game.title, game.type, game.price, game.availability);
                        g1.payment();
                    }
                    break;

                case 4:
                    System.out.print("Enter user name: ");
                    String user_name = scanner.nextLine();
                    System.out.print("Enter game name: ");
                    String game_name = scanner.nextLine();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter booking date (YYYY-MM-DD): ");
                    String bookingDate = scanner.nextLine();
                    System.out.print("Enter booking time (HH:MM:SS): ");
                    String bookingTime = scanner.nextLine();
                    gms.bookGameSession(user_name, game_name, bookingDate, bookingTime);
                    operations.push("Booked Game Session for User name: " +user_name);
                    break;

                case 5:
                    System.out.print("Enter user name: ");
                    String u_name = scanner.nextLine();
                    System.out.print("Enter game name: ");
                    String g_name = scanner.nextLine();
                    System.out.print("Enter score: ");
                    int score = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter achievements: ");
                    String achievements = scanner.nextLine();
                    gms.addLeaderboardEntry(u_name, g_name, score, achievements);
                    operations.push("Added Leaderboard Entry for User name: " + u_name);
                    break;

                case 6:
                    System.out.print("Enter user name: ");
                    String nuser_name = scanner.nextLine();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter message: ");
                    String message = scanner.nextLine();
                    gms.sendNotification(nuser_name, message);
                    operations.push("Sent Notification to User name: " + nuser_name);
                    break;

                case 7:

                    gll.getRecent();
                    break;
                case 8:
                    // Scanner scanner = new Scanner(System.in);

                    // Example: Get user by email
                    System.out.print("Enter user email to retrieve: ");
                    String email1 = scanner.nextLine();
                    User user = gms.getUserByEmail(email1);
                    if (user != null) {
                        System.out.println("User found: " + user.username);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case 9: {
                    System.out.print("Enter game title to search: ");
                    String searchTitle = scanner.nextLine();
                    Game foundGame = gms.recentGames.searchGameByTitle(searchTitle);
                    if (foundGame != null) {
                        System.out.println("Game found: " + foundGame.title + ", Type: " + foundGame.type +
                                ", Price: " + foundGame.price + ", Available: " + foundGame.availability);
                    } else {
                        System.out.println("Game not found.");
                    }
                    operations.push("Searched for Game: " + searchTitle);
                    break;
                }
                case 10:
                    // exit = true;
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("Operations performed:");
        while (!operations.isEmpty()) {
            System.out.println(operations.pop());
        }

        scanner.close();

    }

    public static void main(String[] args) throws Exception {
        try {
            new GameManagementSystem(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void validateEmailId() throws Exception {
        sc.nextLine();
        while (true) {
            System.out.println("Enter Emaild Id: ");
            emailId = sc.nextLine();
            boolean isExists = isEmailidExists(emailId);
            if (!isExists) {
                if (emailId.contains("@gmail.com") || emailId.contains("@hotmail.com") || emailId.contains("@yahoo.com")
                        || emailId.contains("@outlook.com")) {
                    return;
                } else {
                    System.out.println("Enter valid E-Mail id");
                    continue;
                }
            } else {
                System.out.println("You've already logged in");
                throw new Exception("EmailId already exists");
            }
        }
    }

    public boolean isEmailidExists(String emailId) throws Exception {
        String query = "Select email from users where email=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, emailId);
        ResultSet rs = pst.executeQuery();
        return rs.next();
    }

    public boolean ispwdCorrect(String emailid, String pwd) throws Exception {
        String query = "Select password from users where email = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, emailid);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            if (rs.getString(1).equals(pwd)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void validatepwd() {
        // sc.nextLine();
        while (true) {
            System.out.println("Enter New Password: ");
            pwd = sc.nextLine();
            if (checkpwd(pwd)) {
                System.out.println("Enter Confirm Password: ");
                String cp = sc.nextLine();
                if (cp.equals(pwd)) {
                    System.out.println("Password verifed");
                    return;
                } else {
                    System.out.println("Failed to match confirm password with given pwd");
                    System.out.println("Please enter again");
                    continue;
                }
            } else {
                System.out.println("Please Enter Password again");
            }
        }
    }

    public boolean checkpwd(String pwd) {
        if (pwd.length() != 10) {
            System.out.println("Password must be of 10 characters");
            return false;
        }

        if (!(pwd.contains("@") || pwd.contains("_") || pwd.contains("#") || pwd.contains("$"))) {
            System.out.println("Password must contain at least one special character: @, _, #, $");
            return false;
        }

        boolean flag = true;
        int specialCharCount = 0;

        for (int i = 0; i < pwd.length(); i++) {
            char c = pwd.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
            } else if (c == '@' || c == '_' || c == '#' || c == '$') {
                specialCharCount++;
                if (specialCharCount > 1) {
                    System.out.println("Special characters more than once are not allowed");
                    flag = false;
                    break;
                }
            } else {
                System.out.println(
                        "Characters of the Password must be between a to z, 0 to 9, and one special character (@, _, #, $)");
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isUser() throws Exception {
        sc.nextLine();
        System.out.println("Enter E-Mail Id: ");
        emailId = sc.nextLine();
        System.out.println("Enter Password: ");
        pwd = sc.nextLine();

        String query = "select * from users where email=? and password=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, emailId);
        pst.setString(2, pwd);
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
            throw new Exception("Not a valid customer\nIf you didn't registered, then register yourself first");
        } else {
            return true;
        }
    }
}