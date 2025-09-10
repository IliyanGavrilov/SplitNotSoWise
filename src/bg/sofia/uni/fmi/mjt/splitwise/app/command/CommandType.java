package bg.sofia.uni.fmi.mjt.splitwise.app.command;

import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.api.ListCurrenciesCommand;
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
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.general.WhoAmICommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group.AddFriendCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.group.CreateGroupCommand;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositoryManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.math.BigDecimal;
import java.nio.channels.SocketChannel;
import java.util.Set;

public enum CommandType {
    LOGIN(new String[] {"login"}, new String[] {"<username>", "<password>"}, CommandArgumentsType.EXACT),
    LOGOUT(new String[] {"logout"}, new String[] {}, CommandArgumentsType.EXACT),
    REGISTER(new String[] {"register"}, new String[] {"<username>", "<password>", "<nickname>"},
        CommandArgumentsType.AT_LEAST),
    ADD_FRIEND(new String[] {"add-friend", "add-user"}, new String[] {"<username>"}, CommandArgumentsType.EXACT),
    CREATE_GROUP(new String[] {"create-group", "add-group"},
        new String[] {"<group_name>", "<username>", "<username> ..."}, CommandArgumentsType.AT_LEAST),
    SPLIT(new String[] {"split", "split-friend", "split-user"},
        new String[] {"<amount>", "<username>", "<reason_for_payment>"}, CommandArgumentsType.AT_LEAST),
    SPLIT_GROUP(new String[] {"split-group"}, new String[] {"<amount>", "<group_name>", "<reason_for_payment>"},
        CommandArgumentsType.AT_LEAST),
    GET_STATUS(new String[] {"get-status", "status"}, new String[] {}, CommandArgumentsType.EXACT),
    PAID(new String[] {"paid"}, new String[] {"<amount>", "<username>"}, CommandArgumentsType.EXACT),
    PAYMENT_HISTORY(new String[] {"payment-history", "payments", "history", "transactions"}, new String[] {},
        CommandArgumentsType.EXACT),
    SWITCH_CURRENCY(new String[] {"switch-currency", "change-currency", "set-currency"},
        new String[] {"<currency_code>"}, CommandArgumentsType.EXACT),
    LIST_CURRENCIES(new String[] {"list-currencies", "currencies"}, new String[] {}, CommandArgumentsType.EXACT),
    WHOAMI(new String[] {"whoami", "me", "profile", "account"}, new String[] {}, CommandArgumentsType.EXACT),
    HELP(new String[] {"help", "info", "commands", "manual", "man"}, new String[] {},
        CommandArgumentsType.EXACT),
    DISCONNECT(new String[] {"disconnect"}, new String[] {}, CommandArgumentsType.EXACT),
    UNKNOWN(new String[] {""}, new String[] {}, CommandArgumentsType.AT_LEAST);

    private static final String INVALID_ARGS =
        "Invalid count of arguments: \"%s\" expects EXACTLY %d arguments. Example: \"%s\"";
    private static final String INSUFFICIENT_ARGS =
        "Invalid count of arguments: \"%s\" expects AT LEAST %d arguments. Example: \"%s\"";
    private static final Set<String> NUMBER_ARGS = Set.of("<amount>");
    private static final String COMMAND_DELIMITER = " ";

    private final String command;
    private final Set<String> aliases;
    private final String[] args;
    private final CommandArgumentsType argsCntType;

    CommandType(String[] aliases, String[] args, CommandArgumentsType argsCntType) {
        this.command = aliases[0];
        this.aliases = Set.of(aliases);
        this.args = args;
        this.argsCntType = argsCntType;
    }

    public static CommandType fromString(String command) {
        for (CommandType type : values()) {
            if (type.aliases.contains(command.toLowerCase())) {
                return type;
            }
        }

        return null;
    }

    public static Command getCommand(CommandDefinition command, SocketChannel channel,
                                     RepositoryManager repositoryManager) {
        CommandType commandType = fromString(command.name());

        if (commandType == null) {
            return new UnknownCommand(command.args());
        }

        return switch (commandType) {
            case LOGIN -> new LoginCommand(command.args(), channel, repositoryManager);
            case LOGOUT -> new LogoutCommand(command.args(), channel, repositoryManager);
            case REGISTER -> new RegisterCommand(command.args(), channel, repositoryManager);
            case ADD_FRIEND -> new AddFriendCommand(command.args(), channel, repositoryManager);
            case CREATE_GROUP -> new CreateGroupCommand(command.args(), channel, repositoryManager);
            case SPLIT -> new SplitFriendshipCommand(command.args(), channel, repositoryManager);
            case SPLIT_GROUP -> new SplitGroupCommand(command.args(), channel, repositoryManager);
            case GET_STATUS -> new StatusCommand(command.args(), channel, repositoryManager);
            case PAID -> new PaidCommand(command.args(), channel, repositoryManager);
            case PAYMENT_HISTORY -> new PaymentHistoryCommand(command.args(), channel, repositoryManager);
            case SWITCH_CURRENCY -> new SwitchCurrencyCommand(command.args(), channel, repositoryManager);
            case LIST_CURRENCIES -> new ListCurrenciesCommand(command.args(), repositoryManager);
            case WHOAMI -> new WhoAmICommand(command.args(), channel, repositoryManager);
            case HELP -> new HelpCommand(command.args());
            case DISCONNECT -> new DisconnectCommand(command.args());
            default -> new UnknownCommand(command.args());
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
                    if (new BigDecimal(inputArgs[i]).compareTo(BigDecimal.ZERO) < 0) {
                        throw new ExpenseSplitException(
                            String.format("Given number \"%s\" < 0! Negative numbers are not allowed!", inputArgs[i]));
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new ExpenseSplitException(String.format("\"%s\" is not a valid number!", inputArgs[i]), e);
        }
    }

    @Override
    public String toString() {
        return command + COMMAND_DELIMITER + String.join(COMMAND_DELIMITER, args);
    }
}
