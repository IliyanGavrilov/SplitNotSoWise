package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;
import bg.sofia.uni.fmi.mjt.splitwise.util.json.GsonSingleton;
import bg.sofia.uni.fmi.mjt.splitwise.util.json.JsonResponse;

import java.nio.channels.SocketChannel;

public abstract class Command {
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    protected final String[] args;
    protected final CommandType type;
    protected final SocketChannel socket;

    protected String jsonifyArguments(String status, String data) {
        JsonResponse<String> response = new JsonResponse<>(status, data);
        return GsonSingleton.getInstance().toJson(response);
    }

    public Command(String[] args, CommandType type, SocketChannel socket) {
        this.args = args;
        this.type = type;
        this.socket = socket;
    }

    public String command() {
        return type.getCommand();
    }

    public String[] arguments() {
        return args;
    }

    protected abstract String executeCommand() throws ExpenseSplitException;

    public String execute() {
        try {
            type.validateArguments(args);

            return jsonifyArguments(OK, executeCommand());
        } catch (ExpenseSplitException e) {
            return jsonifyArguments(ERROR, e.getMessage());
        }
    }
}
