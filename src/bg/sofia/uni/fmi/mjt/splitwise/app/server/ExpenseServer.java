package bg.sofia.uni.fmi.mjt.splitwise.app.server;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ExpenseServer {
    private static ExpenseServer instance;

    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_HOST = "localhost";
    private static final String DISCONNECT_COMMAND = "disconnect";

    private final CommandExecutor commandExecutor;
    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    private ExpenseServer(int port) throws ExchangeAPIException {
        this.port = port;
        commandExecutor = new CommandExecutor();
        ExchangeRatesClient.getInstance(HttpClient.newBuilder().build());
        ExchangeRatesClient.getInstance().getExchangeRates();
    }

    private void startupSetup(ServerSocketChannel serverSocketChannel) throws IOException {
        selector = Selector.open();

        // Configure server socket channel
        serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, this.port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        isServerWorking = true;
    }

    private void checkForDisconnect(SocketChannel clientChannel, String message) throws IOException {
        if (message.equals(DISCONNECT_COMMAND)) {
            clientChannel.close();
        }
    }

    private void processClientRequest() throws IOException {
        int readyChannels = selector.select();
        if (readyChannels == 0) {
            return;
        }

        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if (key.isReadable()) {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                String clientInput = getClientInput(clientChannel);

                if (clientInput == null) {
                    continue;
                }

                String output = commandExecutor.execute(clientInput, clientChannel);

                writeClientOutput(clientChannel, output);
                checkForDisconnect(clientChannel, clientInput);
            } else if (key.isAcceptable()) {
                accept(selector, key);
            }

            keyIterator.remove();
        }
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    public static ExpenseServer getInstance(int port) throws ExchangeAPIException {
        if (instance == null) {
            instance = new ExpenseServer(port);
        }

        return instance;
    }

    public static ExpenseServer getInstance() {
        return instance;
    }

    public void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            startupSetup(serverSocketChannel);

            while (isServerWorking) {
                try {
                    processClientRequest();
                } catch (Exception e) {
                    System.err.printf("Error occurred while processing client request: %s\n", e.getMessage());
                    stop();
                    break;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to start server", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;

        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    public boolean isServerWorking() {
        return isServerWorking;
    }
}
