package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.api.SwitchCurrencyCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.auth.LoginCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.auth.LogoutCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.auth.RegisterCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense.PaidCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense.SplitFriendshipCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense.SplitGroupCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense.StatusCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense.history.PaymentHistoryCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general.DisconnectCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general.HelpCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general.UnknownCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group.AddFriendCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group.CreateGroupCommand;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.math.BigDecimal;
import java.nio.channels.SocketChannel;
import java.util.Set;

public enum CommandType {
    LOGIN("login", new String[] {"<username>", "<password>"}, CommandArgumentsType.EXACT),
    LOGOUT("logout", new String[] {}, CommandArgumentsType.EXACT),
    REGISTER("register", new String[] {"<username>", "<password>", "<nickname>"}, CommandArgumentsType.AT_LEAST),
    ADD_FRIEND("add-friend", new String[] {"<username>"}, CommandArgumentsType.EXACT),
    CREATE_GROUP("create-group", new String[] {"<group_name>", "<username> <username> ..."},
        CommandArgumentsType.AT_LEAST),
    SPLIT("split", new String[] {"<amount>", "<username>", "<reason_for_payment>"}, CommandArgumentsType.AT_LEAST),
    SPLIT_GROUP("split-group", new String[] {"<amount>", "<group_name>", "<reason_for_payment>"},
        CommandArgumentsType.AT_LEAST),
    GET_STATUS("get-status", new String[] {}, CommandArgumentsType.EXACT),
    PAID("paid", new String[] {"<amount>", "<username>"}, CommandArgumentsType.EXACT),
    PAYMENT_HISTORY("payment-history", new String[] {}, CommandArgumentsType.EXACT),
    SWITCH_CURRENCY("switch-currency", new String[] {"<currency_code>"}, CommandArgumentsType.EXACT),
    HELP("help", new String[] {}, CommandArgumentsType.EXACT),
    DISCONNECT("disconnect", new String[] {}, CommandArgumentsType.EXACT),
    UNKNOWN("", new String[] {}, CommandArgumentsType.EXACT);

    private static final String INVALID_ARGS =
        "Invalid count of arguments: \"%s\" expects %d arguments. Example: \"%s\"";
    private static final String INSUFFICIENT_ARGS =
        "Invalid count of arguments: \"%s\" expects at least %d arguments. Example: \"%s\"";
    private static final Set<String> NUMBER_ARGS = Set.of("<amount>");
    private static final String COMMAND_DELIMITER = " ";

    private final String command;
    private final String[] args;
    private final CommandArgumentsType argsCntType;

    CommandType(String command, String[] args, CommandArgumentsType argsCntType) {
        this.command = command;
        this.args = args;
        this.argsCntType = argsCntType;
    }

    public static CommandType fromString(String command) {
        for (CommandType type : values()) {
            if (type.command.equalsIgnoreCase(command)) {
                return type;
            }
        }
        return null;
    }

    public static Command getCommand(String command, SocketChannel channel,
                                     String[] args) {
        CommandType commandType = fromString(command);

        if (commandType == null) {
            return new UnknownCommand(args);
        }

        return switch (commandType) {
            case LOGIN -> new LoginCommand(args, channel);
            case LOGOUT -> new LogoutCommand(args, channel);
            case REGISTER -> new RegisterCommand(args);
            case ADD_FRIEND -> new AddFriendCommand(args, channel);
            case CREATE_GROUP -> new CreateGroupCommand(args, channel);
            case SPLIT -> new SplitFriendshipCommand(args, channel);
            case SPLIT_GROUP -> new SplitGroupCommand(args, channel);
            case GET_STATUS -> new StatusCommand(args, channel);
            case PAID -> new PaidCommand(args, channel);
            case PAYMENT_HISTORY -> new PaymentHistoryCommand(args, channel);
            case SWITCH_CURRENCY -> new SwitchCurrencyCommand(args, channel);
            case HELP -> new HelpCommand(args);
            case DISCONNECT -> new DisconnectCommand(args);
            default -> new UnknownCommand(args);
        };
    }

    public void validateArguments(String[] inputArgs) throws ExpenseSplitException {
        int argCnt = inputArgs.length;

        // Check arguments count
        if (!((argsCntType == CommandArgumentsType.EXACT && argCnt == args.length) ||
            (argsCntType == CommandArgumentsType.AT_LEAST && argCnt >= args.length))) {
            throw new ExpenseSplitException(String.format((argsCntType == CommandArgumentsType.EXACT ?
                INVALID_ARGS : INSUFFICIENT_ARGS), command, args.length, this));
        }

        // Check arguments type
        int i = 0;

        try {
            for (; i < args.length; i++) {
                if (NUMBER_ARGS.contains(args[i])) {
                    new BigDecimal(inputArgs[i]);
                }
            }
        } catch (NumberFormatException e) {
            throw new ExpenseSplitException(inputArgs[i] + " is not a valid number!", e);
        }
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return command + COMMAND_DELIMITER + String.join(COMMAND_DELIMITER, args);
    }
}
