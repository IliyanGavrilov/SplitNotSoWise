//package bg.sofia.uni.fmi.mjt.splitwise.app.server;
//
//import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExchangeAPIException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class ExpenseServerTest {
//    private ExpenseServer pollServer;
//
//    @BeforeEach
//    public void setup() throws ExchangeAPIException {
//        pollServer = new ExpenseServer(8080);
//    }
//
//    @Test
//    public void testServerStartAndStop() throws InterruptedException {
//        Thread serverThread = new Thread(() -> pollServer.start());
//        serverThread.start();
//
//        Thread.sleep(100);
//        assertTrue(pollServer.isServerWorking());
//
//        pollServer.stop();
//        assertFalse(pollServer.isServerWorking());
//    }
//}
