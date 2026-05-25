import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipientNumber;
    private String messageText;
    private String messageHash;

    // Shared across all messages
    private static int totalMessagesSent = 0;
    private static ArrayList<Message> sentMessages = new ArrayList<>();

    // Constructor
    public Message(int messageNumber, String recipientNumber, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipientNumber = recipientNumber;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Generate a random 10-digit message ID
    private String generateMessageID() {
        Random random = new Random();
        long id = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    // Check that message ID is 10 characters or fewer
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Validate recipient cell number
    // Must contain international code (+) and be no longer than 10 characters
    public boolean checkRecipientCell() {
        if (recipientNumber == null || recipientNumber.isEmpty()) {
            return false;
        }
        if (!recipientNumber.startsWith("+")) {
            System.out.println("Cell number is incorrectly formatted or does not contain an international code.");
            return false;
        }
        if (recipientNumber.length() > 10) {
            System.out.println("Cell number is too long. Must be no longer than 10 characters.");
            return false;
        }
        return true;
    }

    // Create message hash: first 2 digits of ID : message number : first and last word (uppercase)
    public String createMessageHash() {
        String idPart = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();
        return (idPart + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Let user send, store, or disregard the message
    public String SentMessage(String action) {
        switch (action) {
            case "1": // Send
                totalMessagesSent++;
                sentMessages.add(this);
                return "Message successfully sent";
            case "2": // Disregard
                return "Press 0 to delete the message";
            case "3": // Store
                storeMessage();
                return "Message successfully stored";
            default:
                return "Invalid action.";
        }
    }

    // Print all sent messages
    public static void printMessages() {
        if (sentMessages.isEmpty()) {
            System.out.println("No messages sent yet.");
            return;
        }
        System.out.println("\n--- Sent Messages ---");
        for (Message m : sentMessages) {
            System.out.println("Message ID   : " + m.messageID);
            System.out.println("Message Hash : " + m.messageHash);
            System.out.println("Recipient    : " + m.recipientNumber);
            System.out.println("Message      : " + m.messageText);
            System.out.println("---------------------");
        }
        System.out.println("Total messages sent: " + totalMessagesSent);
    }

    // Return total number of messages sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Store message in a JSON file (no external library required)
    public void storeMessage() {
        // Build JSON manually
        String json = "{\n" +
                "  \"messageID\": \"" + messageID + "\",\n" +
                "  \"messageNumber\": " + messageNumber + ",\n" +
                "  \"recipientNumber\": \"" + recipientNumber + "\",\n" +
                "  \"messageText\": \"" + messageText.replace("\"", "\\\"") + "\",\n" +
                "  \"messageHash\": \"" + messageHash + "\"\n" +
                "}";

        try (FileWriter file = new FileWriter("stored_messages.json", true)) {
            file.write(json + "\n");
            sentMessages.add(this);
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    // Getters
    public String getMessageID()   { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient()   { return recipientNumber; }
    public String getMessageText() { return messageText; }
}
