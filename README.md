# SplitNotSoWise

A client-server expense-splitting app in Java, inspired by Splitwise. Users register, add friends, create groups, split bills and settle debts from a terminal client, and the server keeps everyone's balances, payment history and notifications. Amounts can be shown in any currency using live exchange rates.

Built as the course project for **Modern Java Technologies** at FMI, Sofia University (2025).

## Features

- **Accounts:** register / login / logout. Passwords are hashed with PBKDF2 (HMAC-SHA256, 65,536 iterations).
- **Friends and groups:** add friends and create groups with any number of members.
- **Splitting:** split an amount with a friend or equally across a group, with a reason for each payment.
- **Settling up:** mark debts as paid and see who owes whom (`status`) and your full payment history.
- **Notifications:** online users get pushed new expenses and payments instantly; offline users get them queued and shown on their next login.
- **Currencies:** switch your display currency. Rates come live from [ExchangeRate-API](https://www.exchangerate-api.com/), with the base currency BGN.
- **Persistence:** all data (users, groups, expenses, balances, notifications) is saved as JSON and survives restarts.
- **Help:** every command has aliases, and the server validates arguments with a usage example on mistakes.

## Architecture

```
EchoClient  ──TCP :7777-->  ExpenseServer (Java NIO, non-blocking, single Selector)
                                  │
                         CommandExecutor -> CommandFactory -> Command (sealed hierarchy)
                                  │
                         RepositoryManager
                         ├─ users / groups / expenses / notifications (in-memory)
                         ├─ UserSessionManager (who is logged in on which socket)
                         ├─ RepositorySaver --> resources/*.json
                         └─ ExchangeRatesClient ──HTTPS--> ExchangeRate-API
```

- **Networking:** one server thread handles all clients through `java.nio` channels and a `Selector`.
- **Commands:** each command is its own class built by a factory from the `CommandType` enum, which holds the aliases, the expected arguments and the validation.
- **Error handling:** HTTP and API errors are mapped to a typed exception hierarchy, so the client gets a clear message instead of a crash.
- **Tests:** 38 test classes with JUnit 5 and Mockito, covering the commands, models, repositories, the API client, the `.env` loader and encryption.

## Commands

| Command (aliases) | Arguments |
|---|---|
| `register` | `<username> <password> <nickname>` |
| `login` | `<username> <password>` |
| `logout` | - |
| `add-friend` (`add-user`) | `<username>` |
| `create-group` (`add-group`) | `<group_name> <username> <username> ...` |
| `split` (`split-friend`, `split-user`) | `<amount> <username> <reason>` |
| `split-group` | `<amount> <group_name> <reason>` |
| `paid` | `<amount> <username>` |
| `status` (`get-status`) | - |
| `payment-history` (`history`, `payments`, `transactions`) | - |
| `switch-currency` (`set-currency`) | `<currency_code>` |
| `list-currencies` (`currencies`) | - |
| `whoami` (`me`, `profile`, `account`) | - |
| `help` (`commands`, `man`) | - |
| `disconnect` | - |

## Running it

**Requirements:** JDK 22+ and [Gson](https://github.com/google/gson). JUnit 5 and Mockito are needed only for the tests.

1. Get a free API key from [exchangerate-api.com](https://www.exchangerate-api.com/).
2. Create `resources/.env` (it's in `.gitignore`, so it never gets committed):
   ```
   EXCHANGE_API_KEY=your_key_here
   ```
3. Open the project in IntelliJ IDEA (or add Gson to the classpath) and run:
   - the server: `bg.sofia.uni.fmi.mjt.splitwise.app.server.network.EchoServer`
   - one or more clients: `bg.sofia.uni.fmi.mjt.splitwise.app.client.network.EchoClient`
4. Type `help` in the client to see all commands.

## Tech

Java 22 (records, sealed classes, switch expressions, unnamed variables), java.nio, java.net.http, Gson, JUnit 5, Mockito
