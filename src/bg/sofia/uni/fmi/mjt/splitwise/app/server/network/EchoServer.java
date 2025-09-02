package bg.sofia.uni.fmi.mjt.splitwise.app.server.network;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.ExpenseServer;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;

public class EchoServer {
    private static final int SERVER_PORT = 7777;
    private static ExpenseServer server;

    private static void addShutdownHook(ExpenseServer server) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop(); // Ensure resources are freed
            System.out.println("Server stopped.");
        }));
    }

    private static void runServer() {
        try {
            server = ExpenseServer.getInstance(SERVER_PORT);
        } catch (ExchangeAPIException e) {
            System.err.printf("Error: %s%n", e.getMessage());
        }

        addShutdownHook(server); // Add shutdown hook for graceful termination

        try {
            server.start(); // Blocking call
        } catch (Exception e) {
            System.err.printf("Server encountered an error: %s%n", e.getMessage());
        }
    }

    public static void main(String[] args) {
        runServer();
    }
}
