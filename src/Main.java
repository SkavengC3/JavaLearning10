import java.util.Scanner;

public class Main {
    public static final int MAX_USERS = 15;
    public static String[] bannedPasswords = {"admin", "pass", "password", "qwerty", "ytrewq"};
    public static String[] usernames = new String[MAX_USERS];
    public static String[] passwords = new String[MAX_USERS];
    public static boolean[] isActive = new boolean[MAX_USERS];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            showMenu();
            System.out.print("Ваш вибір: ");
            int choice = readIntInput(scanner);
            
            try {
                switch (choice) {
                    case 1:
                        registerUser(scanner);
                        break;
                    case 2:
                        deleteUser(scanner);
                        break;
                    case 3:
                        authenticateUser(scanner);
                        break;
                    case 4:
                        addForbiddenPassword(scanner);
                        break;
                    case 5:
                        showUsers();
                        break;
                    case 0:
                        isRunning = false;
                        System.out.println("Вихід з програми. Бувай!");
                        break;
                    default:
                        System.out.println("Гей, такого пункту немає! Спробуй ще раз.");
                }
            } catch (Exception e) {
                System.out.println("Помилка: " + e.getMessage());
            }

            if (isRunning) {
                System.out.println("\nНатисни Enter, щоб продовжити...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    public static void showMenu() {
        System.out.println("\n======= МЕНЮ =======");
        System.out.println("1. Зареєструвати нового користувача");
        System.out.println("2. Видалити користувача");
        System.out.println("3. Увійти в систему");
        System.out.println("4. Додати заборонений пароль");
        System.out.println("5. Показати список користувачів");
        System.out.println("0. Вихід");
        System.out.println("====================");
    }

    public static int readIntInput(Scanner scanner) {
        int value = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.print("Це не число! Давай ще раз: ");
            }
        }
        return value;
    }

    public static void registerUser(Scanner scanner) {
        System.out.println("\n--- Реєстрація нового користувача ---");
        System.out.print("Введи логін (мінімум 5 символів, без пробілів): ");
        String username = scanner.nextLine();

        if (username == null || username.trim().length() < 5) {
            throw new RuntimeException("Логін має бути не менше 5 символів!");
        }

        if (username.contains(" ")) {
            throw new RuntimeException("У логіні не повинно бути пробілів!");
        }

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && isActive[i] && usernames[i].equals(username)) {
                throw new RuntimeException("Такий користувач вже є!");
            }
        }

        System.out.print("Введи пароль (мінімум 10 символів, мінімум 3 цифри і 1 спецсимвол): ");
        String password = scanner.nextLine();

        if (password == null || password.length() < 10) {
            throw new RuntimeException("Пароль має бути не менше 10 символів!");
        }

        if (password.contains(" ")) {
            throw new RuntimeException("У паролі не повинно бути пробілів!");
        }

        int digitCount = 0;
        int specialCharCount = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            } else if (!Character.isLetterOrDigit(c)) {
                specialCharCount++;
            }
        }

        if (digitCount < 3) {
            throw new RuntimeException("У паролі має бути мінімум 3 цифри!");
        }

        if (specialCharCount < 1) {
            throw new RuntimeException("У паролі має бути хоча б 1 спецсимвол!");
        }

        for (String restricted : bannedPasswords) {
            if (restricted.equals(password)) {
                throw new RuntimeException("Цей пароль заборонений! Вибери інший.");
            }
        }

        boolean userAdded = false;
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] == null || !isActive[i]) {
                usernames[i] = username;
                passwords[i] = password;
                isActive[i] = true;
                System.out.println("Ура! Користувач " + username + " успішно зареєстрований!");
                userAdded = true;
                break;
            }
        }

        if (!userAdded) {
            throw new RuntimeException("Місця немає! У нас вже максимум користувачів (15)!");
        }
    }

    public static void deleteUser(Scanner scanner) {
        System.out.println("\n--- Видалення користувача ---");
        System.out.print("Введи логін для видалення: ");
        String username = scanner.nextLine();

        boolean userFound = false;
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && isActive[i] && usernames[i].equals(username)) {
                isActive[i] = false;
                System.out.println("Користувач " + username + " видалений!");
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            throw new RuntimeException("Такого користувача немає в системі!");
        }
    }

    public static void authenticateUser(Scanner scanner) {
        System.out.println("\n--- Вхід в систему ---");
        System.out.print("Логін: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        boolean authenticated = false;
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && isActive[i] && usernames[i].equals(username) && passwords[i].equals(password)) {
                System.out.println("Привіт, " + username + "! Ти успішно увійшов в систему.");
                authenticated = true;
                break;
            }
        }

        if (!authenticated) {
            throw new RuntimeException("Невірний логін або пароль! Спробуй ще раз.");
        }
    }

    public static void addForbiddenPassword(Scanner scanner) {
        System.out.println("\n--- Додавання забороненого пароля ---");
        System.out.print("Введи пароль для заборони: ");
        String password = scanner.nextLine();

        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Порожній пароль не можна заборонити!");
        }

        for (String restricted : bannedPasswords) {
            if (restricted.equals(password)) {
                throw new RuntimeException("Цей пароль вже заборонений!");
            }
        }

        String[] newBannedPasswords = new String[bannedPasswords.length + 1];
        System.arraycopy(bannedPasswords, 0, newBannedPasswords, 0, bannedPasswords.length);
        newBannedPasswords[bannedPasswords.length] = password;
        bannedPasswords = newBannedPasswords;
        System.out.println("Пароль додано до чорного списку!");
    }

    public static void showUsers() {
        System.out.println("\n--- Список активних користувачів ---");
        boolean hasUsers = false;

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && isActive[i]) {
                System.out.println("- " + usernames[i]);
                hasUsers = true;
            }
        }

        if (!hasUsers) {
            throw new RuntimeException("Немає активних користувачів.");
        }
    }
}