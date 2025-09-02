package bg.sofia.uni.fmi.mjt.splitwise.app.client.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EchoClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 1024;
    private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(BUFFER_SIZE);

    private static final String QUIT_COMMAND = "quit";
    private static final String CONNECT_COMMAND = "connect";
    private static final String DISCONNECT_COMMAND = "disconnect";

    private static final String JSON_STATUS = "status";
    private static final String JSON_MESSAGE = "data";
    private static final String JSON_SEPARATOR = ": ";

    private static final String USER_INPUT_PROMPT = "$ ";
    private static final String USER_INPUT_TO_SERVER_PROMPT = "Server~ $ ";

    private static final Gson GSON = new Gson();

    private static String unJsonify(JsonObject jsonObject) {
        String status = jsonObject.get(JSON_STATUS).getAsString();
        String data = jsonObject.get(JSON_MESSAGE).getAsString();

        return status + JSON_SEPARATOR + data;
    }

    private static void runClient() {
        try (Scanner scanner = new Scanner(System.in)) {
            String message = serverCommunication(scanner);

            while (!QUIT_COMMAND.equals(message)) {
                printPrompt(null);
                message = scanner.nextLine();

                if (CONNECT_COMMAND.equals(message)) {
                    message = serverCommunication(scanner);
                }
            }
        } catch (IOException e) {
            System.err.printf("There is a problem with the user input: %s%n", e.getMessage());
        }
    }

    private static String serverCommunication(Scanner scanner) throws IOException {
        String message = "";

        try (SocketChannel socketChannel = SocketChannel.open()) {
            addShutdownHook(socketChannel); // Add shutdown hook for graceful termination
            connectToServer(socketChannel);

            while (socketChannel.isConnected()) {
                printPrompt(socketChannel);

                message = scanner.nextLine(); // read a line from the console

                if (QUIT_COMMAND.equals(message) || DISCONNECT_COMMAND.equals(message)) {
                    break;
                }

                sendMessage(socketChannel, message);
                String reply = receiveMessage(socketChannel);

                printResponse(reply); // display reply
            }
        } catch (Exception e) {
            System.err.printf("There is a problem with the network communication: %s%n", e.getMessage());
        }

        return message;
    }

    private static void printPrompt(SocketChannel socketChannel) {
        if (socketChannel == null) {
            System.out.print(USER_INPUT_PROMPT);
        } else {
            System.out.print(USER_INPUT_TO_SERVER_PROMPT);
        }
    }

    private static void printResponse(String json) {
        JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);

        System.out.println(unJsonify(jsonObject));
    }

    private static void connectToServer(SocketChannel socketChannel) throws IOException {
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        System.out.println("Successfully connected to the server.");
    }

    private static void sendMessage(SocketChannel socketChannel, String message) throws IOException {
        BUFFER.clear(); // switch to writing mode
        BUFFER.put(message.getBytes()); // buffer fill
        BUFFER.flip(); // switch to reading mode
        socketChannel.write(BUFFER); // buffer drain
    }

    private static String receiveMessage(SocketChannel socketChannel) throws IOException {
        BUFFER.clear(); // switch to writing mode
        socketChannel.read(BUFFER); // buffer fill
        BUFFER.flip(); // switch to reading mode

        byte[] byteArray = new byte[BUFFER.remaining()];
        BUFFER.get(byteArray); // buffer drain
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    private static void addShutdownHook(SocketChannel socketChannel) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down client...");
            try {
                if (socketChannel != null && socketChannel.isOpen()) {
                    socketChannel.close();
                    System.out.println("Client disconnected.");
                }
            } catch (IOException e) {
                System.err.printf("Error closing socket: %s%n", e.getMessage());
            }
            System.out.println("Client stopped.");
        }));
    }

    public static void main(String[] args) {
        runClient();
    }
}
