import java.util.Scanner;

public class Main {
    private static final int MAX_USERS = 15;
    private static final String[] restrictedPasswords = {"admin", "pass", "password", "qwerty", "ytrewq"};
    private static final String[] usernames = new String[MAX_USERS];
    private static final String[] passwords = new String[MAX_USERS];
    private static final boolean[] isActive = new boolean[MAX_USERS];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("    Система автентифікації користувачів    ");

        while (isRunning) {
            showMenu();
            System.out.print("Оберіть опцію: ");
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
                        System.out.println("Програму зупинено.");
                        break;
                    default:
                        System.out.println("Некоректний вибір. Спробуйте ще раз.");
                }
            } catch (Exception e) {
                System.out.println("Сталася помилка: " + e.getMessage());
            }

            if (isRunning) {
                System.out.println("\nНатисніть Enter для продовження...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Реєстрація нового користувача");
        System.out.println("2. Видалити користувача");
        System.out.println("3. Автентифікація користувача");
        System.out.println("4. Додати заборонений пароль");
        System.out.println("5. Показати список користувачів");
        System.out.println("0. Завершити програму");
    }

    private static int readIntInput(Scanner scanner) {
        int value = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.print("Некоректне число! Введіть ціле число: ");
            }
        }
        return value;
    }

    private static void registerUser(Scanner scanner) throws Exception {
        System.out.println("\n=== Реєстрація нового користувача ===");
        System.out.print("Введіть логін (мінімум 5 символів, без пробілів): ");
        String username = scanner.nextLine();

        validateUsername(username);

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && isActive[i] && usernames[i].equals(username)) {
                throw new Exception("Користувач з таким ім'ям вже існує!");
            }
        }

        System.out.print("Введіть пароль (мінімум 10 символів, з яких хоча б 3 цифри і 1 спецсимвол): ");
        String password = scanner.nextLine();

        validatePassword(password);

        for (String restricted : restrictedPasswords) {
            if (restricted.equals(password)) {
                throw new Exception("Пароль знаходиться у списку заборонених!");
            }
        }

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] == null || !isActive[i]) {
                usernames[i] = username;
                passwords[i] = password;
                isActive[i] = true;
                System.out.println("Користувача " + username + " успішно зареєстровано.");
                return;
            }
        }
        throw new Exception("Досягнуто максимальної кількості користувачів (15)!");
    }

    private static void deleteUser(Scanner scanner) throws Exception {
        System.out.println("\n=== Видалення користувача ===");
        System.out.print("Введіть логін для видалення: ");
        String username = scanner.nextLine();

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && isActive[i] && usernames[i].equals(username)) {
                isActive[i] = false;
                System.out.println("Користувача " + username + " успішно видалено.");
                return;
            }
        }
        throw new Exception("Користувача з ім'ям " + username + " не знайдено!");
    }

    private static void authenticateUser(Scanner scanner) throws Exception {
        System.out.println("\n=== Автентифікація користувача ===");
        System.out.print("Введіть логін: ");
        String username = scanner.nextLine();
        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && isActive[i] && usernames[i].equals(username) && passwords[i].equals(password)) {
                System.out.println("Користувача " + username + " успішно авторизовано.");
                return;
            }
        }
        throw new Exception("Невірне ім'я користувача або пароль!");
    }

    private static void addForbiddenPassword(Scanner scanner) throws Exception {
        System.out.println("\n=== Додавання забороненого паролю ===");
        System.out.print("Введіть пароль для заборони: ");
        String password = scanner.nextLine();

        for (String restricted : restrictedPasswords) {
            if (restricted.equals(password)) {
                throw new Exception("Пароль вже знаходиться у списку заборонених!");
            }
        }

        String[] newRestrictedPasswords = new String[restrictedPasswords.length + 1];
        System.arraycopy(restrictedPasswords, 0, newRestrictedPasswords, 0, restrictedPasswords.length);
        newRestrictedPasswords[restrictedPasswords.length] = password;
        System.out.println("Пароль успішно додано до списку заборонених.");
    }

    private static void showUsers() {
        System.out.println("\n--- Список активних користувачів ---");
        boolean hasUsers = false;

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && isActive[i]) {
                System.out.println("- " + usernames[i]);
                hasUsers = true;
            }
        }

        if (!hasUsers) {
            System.out.println("Немає активних користувачів.");
        }
    }

    private static void validateUsername(String username) throws Exception {
        if (username == null || username.trim().length() < 5) {
            throw new Exception("Некоректне ім'я користувача! Воно повинно містити щонайменше 5 символів.");
        }

        if (username.contains(" ")) {
            throw new Exception("Некоректне ім'я користувача! Воно не повинно містити пробілів.");
        }
    }

    private static void validatePassword(String password) throws Exception {
        if (password == null || password.length() < 10) {
            throw new Exception("Некоректний пароль! Повинен бути мінімум 10 символів.");
        }

        if (password.contains(" ")) {
            throw new Exception("Некоректний пароль! Не повинен містити пробілів.");
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
            throw new Exception("Некоректний пароль! Повинен містити щонайменше 3 цифри.");
        }

        if (specialCharCount < 1) {
            throw new Exception("Некоректний пароль! Повинен містити щонайменше 1 спеціальний символ.");
        }
    }
}