import java.util.Scanner;

public class QuickChatApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        boolean isLoggedIn = false;
        boolean isRunning = true;
        int maxMessagesAllowed = 0;
        int messagesSentCount = 0;

        // Welcome message
        System.out.println("Welcome to QuickChat.");

        // Ask user for max number of messages
        while (true) {
            System.out.print("Enter the maximum number of messages you want to send this session: ");

            if (scanner.hasNextInt()) {
                maxMessagesAllowed = scanner.nextInt();
                scanner.nextLine();

                if (maxMessagesAllowed > 0) {
                    break;
                } else {
                    System.out.println("Please enter a number greater than 0.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }

        // Login
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!username.isEmpty() && !password.isEmpty()) {
            isLoggedIn = true;
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. Username and password cannot be empty.");
            scanner.close();
            return;
        }

        // Main loop
        while (isRunning) {

            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1":

                    if (!isLoggedIn) {
                        System.out.println("You must log in first.");
                        break;
                    }

                    if (messagesSentCount >= maxMessagesAllowed) {
                        System.out.println("You have reached your message limit.");
                        break;
                    }

                    // Get recipient
                    String recipientNumber;
                    while (true) {
                        System.out.print("Enter recipient number (with international code, max 10 chars): ");
                        recipientNumber = scanner.nextLine().trim();

                        // Temporarily create a dummy message just to validate recipient
                        Message tempCheck = new Message(messagesSentCount + 1, recipientNumber, "placeholder text");
                        if (tempCheck.checkRecipientCell()) {
                            break;
                        }
                    }

                    // Get message text
                    String messageText;
                    while (true) {
                        System.out.print("Enter your message (max 250 characters): ");
                        messageText = scanner.nextLine();

                        if (messageText.trim().isEmpty()) {
                            System.out.println("Message cannot be empty.");
                        } else if (messageText.length() > 250) {
                            System.out.println("Please enter a message of less than 250 characters.");
                        } else {
                            System.out.println("Message ready.");
                            break;
                        }
                    }

                    // Create the message
                    Message message = new Message(messagesSentCount + 1, recipientNumber, messageText);

                    // Show message details before action
                    System.out.println("\n--- Message Preview ---");
                    System.out.println("Message ID   : " + message.getMessageID());
                    System.out.println("Message Hash : " + message.getMessageHash());
                    System.out.println("Recipient    : " + message.getRecipient());
                    System.out.println("Message      : " + message.getMessageText());

                    // Choose action
                    System.out.println("\nWhat would you like to do?");
                    System.out.println("1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message for Later");
                    System.out.print("Choose an option: ");
                    String action = scanner.nextLine();

                    String result = message.SentMessage(action);
                    System.out.println(result);

                    // Only count toward limit if sent
                    if (action.equals("1")) {
                        messagesSentCount++;
                        System.out.println("Messages sent: " + messagesSentCount + "/" + maxMessagesAllowed);
                    }

                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    isRunning = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }
}
