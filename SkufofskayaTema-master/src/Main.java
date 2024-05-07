import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class Main {
    public static void main(String[] args)  throws ClassNotFoundException {
        System.out.println("Запуск программы...");
        String url = "jdbc:mysql://localhost:3306/SkufofskayaTema";
        String username = "root";
        String password = "";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Добро пожаловать в Скуфуслуги!\nВведите 1, чтобы зарегистрироваться\nВведите 2, чтобы найти пользователя\nВведите 3, чтобы поменять данные\nВведите 4, чтобы удалить пользователя:");
            switch (scanner.nextLine()){
                case "1":
                    try{
                        System.out.println("Введите ваше фамилию:");
                        String clientLastName = scanner.nextLine();
                        System.out.println("Введите ваше имя:");
                        String clientFirstName = scanner.nextLine();
                        System.out.println("Введите ваше отчество:");
                        String clientPatronymic = scanner.nextLine();
                        System.out.println("Введите ваш пол:");
                        String clientGender = scanner.nextLine();
                        System.out.println("Введите вашу дату рождения в формате гггг-мм-дд:");
                        String clientDateOfBirth = scanner.nextLine();
                        System.out.println("Введите ваше место рождения:");
                        String clientPlaceOfBirth = scanner.nextLine();
                        System.out.println("Введите кем выдан ваш паспорт:");
                        String passportIssued = scanner.nextLine();
                        System.out.println("Введите дату выдачи вашего паспорта в формате гггг-мм-дд:");
                        String passportDateOfIssue = scanner.nextLine();
                        System.out.println("Введите код подразделения вашего паспорта:");
                        String passportUnitCode = scanner.nextLine();
                        System.out.println("Введите серию вашего паспорта (4 цифры слитно):");
                        String passportSeries = scanner.nextLine();
                        System.out.println("Введите номер вашего паспорта (6 цифр слитно):");
                        String passportNumber = scanner.nextLine();
                        System.out.println("Введите придуманный пароль (4 цифры):");
                        String registrationPassword = scanner.nextLine();
                        Connection connection = DriverManager.getConnection(url, username, password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("""
                                INSERT INTO Passports (PassportIssued, PassportDateOfIssue, PassportUnitCode, PassportSeries, PassportNumber) VALUES
                                ('%s', '%s', '%s', '%s', '%s')
                                """.formatted(passportIssued, passportDateOfIssue, passportUnitCode, passportSeries, passportNumber));
                        ResultSet resultSetPassport = statement.executeQuery("""
                                SELECT PassportID FROM Passports
                                WHERE PassportIssued = '%s'
                                AND PassportDateOfIssue = '%s'
                                AND PassportUnitCode = '%s'
                                AND PassportSeries = '%s'
                                AND PassportNumber = '%s'
                                """.formatted(passportIssued, passportDateOfIssue, passportUnitCode, passportSeries, passportNumber));
                        int clientPassportID = 1;
                        if(resultSetPassport.next()){
                            clientPassportID = resultSetPassport.getInt(1);
                        }
                        statement.executeUpdate("""
                                INSERT INTO Registration (RegistrationPassword) VALUES
                                ('%s')
                                """.formatted(registrationPassword));
                        ResultSet resultSetRegistration = statement.executeQuery("""
                                SELECT RegistrationID FROM Registration
                                WHERE RegistrationPassword = '%s'
                                """.formatted(registrationPassword));
                        int clientRegistrationID = 1;
                        if (resultSetRegistration.next()){
                            clientRegistrationID = resultSetRegistration.getInt(1);
                        }
                        statement.executeUpdate("""
                                INSERT INTO Clients (ClientLastName, ClientFirstName, ClientPatronymic, ClientGender, ClientDateOfBirth, ClientPlaceOfBirth, ClientPassportID, ClientRegistrationID) VALUES
                                ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')
                                """.formatted(clientLastName, clientFirstName, clientPatronymic, clientGender, clientDateOfBirth, clientPlaceOfBirth, clientPassportID, clientRegistrationID));
                        System.out.println("Вы успешно зарегистрировались!");
                    }
                    catch (Exception e){
                        System.out.println("Ошибка: " + e);
                    }
                    break;

                case "2":
                    System.out.println("Введите фамилию пользователя, о котором вы хотите посмотреть инофрмацию:");
                    String clientLastNameSelect = scanner.nextLine();
                    try {
                        Connection connection = DriverManager.getConnection(url, username, password);
                        Statement statement = connection.createStatement();
                        ResultSet resultSetClients = statement.executeQuery("SELECT * FROM Clients WHERE ClientLastName = '" + clientLastNameSelect + "'");
                        if (resultSetClients.next()){
                            String registrationID = resultSetClients.getString("ClientRegistrationID");
                            String registrationPassword = "";
                            ResultSet resultSetRegistration = statement.executeQuery("SELECT * FROM Registration WHERE RegistrationID = '" + registrationID + "'");
                            if (resultSetRegistration.next()){
                                registrationPassword = resultSetRegistration.getString("RegistrationPassword");
                            }
                            ResultSet resultSetClient = statement.executeQuery("SELECT * FROM Clients WHERE ClientLastName = '" + clientLastNameSelect + "'");
                            if (resultSetClient.next()){
                                System.out.println("Введите пароль для того, чтобы просмотреть все данные о пользователе:");
                                if (scanner.nextLine().equals(registrationPassword)){
                                    String clientLastName = resultSetClient.getString("ClientLastName");
                                    String clientFirstName = resultSetClient.getString("ClientFirstName");
                                    String clientPatronymic = resultSetClient.getString("ClientPatronymic");
                                    String clientGender = resultSetClient.getString("ClientGender");
                                    String clientDateOfBirth = resultSetClient.getString("ClientDateOfBirth");
                                    String clientPlaceOfBirth = resultSetClient.getString("ClientPlaceOfBirth");
                                    String passportIssued = "";
                                    String passportDateOfIssue = "";
                                    String passportUnitCode = "";
                                    String passportSeries = "";
                                    String passportNumber = "";
                                    String clientPassportID = resultSetClient.getString("ClientPassportID");
                                    ResultSet resultSetPassport = statement.executeQuery("SELECT * FROM Passports WHERE PassportID = '" + clientPassportID + "'");
                                    if (resultSetPassport.next()){
                                        passportIssued = resultSetPassport.getString("PassportIssued");
                                        passportDateOfIssue = resultSetPassport.getString("PassportDateOfIssue");
                                        passportUnitCode = resultSetPassport.getString("PassportUnitCode");
                                        passportSeries = resultSetPassport.getString("PassportSeries");
                                        passportNumber = resultSetPassport.getString("PassportNumber");
                                    }
                                    System.out.println("""
                                            Фамилия: %s
                                            Имя: %s
                                            Отчество: %s
                                            Пол: %s
                                            Дата рождения: %s
                                            Место рождения: %s
                                            Кем выдан: %s
                                            Дата выдачи: %s
                                            Код подразделения: %s
                                            Серия паспорта: %s
                                            Номер паспорта: %s
                                            """.formatted(clientLastName, clientFirstName, clientPatronymic, clientGender, clientDateOfBirth, clientPlaceOfBirth, passportIssued, passportDateOfIssue, passportUnitCode, passportSeries, passportNumber));
                                }
                                else{
                                    System.out.println("Пароль неверный!");
                                    break;
                                }
                            }
                        }
                        else{
                            System.out.println("Такого пользователя не существует!");
                            break;
                        }
                    }
                    catch (Exception e){
                        System.out.println("Ошибка: " + e);
                    }
                    break;

                case "3":
                    System.out.println("Введите фамилию пользователя у которого вы хотите поменять данные:");
                    String clientLastNameChange = scanner.nextLine();
                    try {
                        Connection connection = DriverManager.getConnection(url, username, password);
                        Statement statement = connection.createStatement();
                        ResultSet resultSetClients = statement.executeQuery("SELECT * FROM Clients WHERE ClientLastName = '" + clientLastNameChange + "'");
                        if (resultSetClients.next()) {
                            String registrationID = resultSetClients.getString("ClientRegistrationID");
                            String registrationPassword = "";
                            ResultSet resultSetRegistration = statement.executeQuery("SELECT * FROM Registration WHERE RegistrationID = '" + registrationID + "'");
                            if (resultSetRegistration.next()) {
                                registrationPassword = resultSetRegistration.getString("RegistrationPassword");
                            }
                            ResultSet resultSetClient = statement.executeQuery("SELECT * FROM Clients WHERE ClientLastName = '" + clientLastNameChange + "'");
                            if (resultSetClient.next()) {
                                System.out.println("Введите пароль для того, чтобы поменять данные о пользователе:");
                                if (scanner.nextLine().equals(registrationPassword)) {
                                    System.out.println("Введите 1, чтобы поменять кем выдан паспорт\nВведите 2, чтобы поменять дату выдачи паспорта\nВведите 3, чтобы поменять код подразделения паспорта\nВведите 4, чтобы поменять серию паспорта\nВведите 5, чтобы поменять номер паспорта\nВведите 6, чтобы поменять пароль от аккаунта\nВведите 7, чтобы поменять фамилию\nВведите 8, чтобы поменять имя\nВведите 9, чтобы поменять отчество\nВведите 10, чтобы поменять пол\nВведите 11, чтобы поменять дату рождения\nВведите 12, чтобы поменять место рождения:");
                                    switch (scanner.nextLine()){
                                        case "1":
                                            System.out.println("Введите новый кем выдан паспорт:");
                                            statement.executeUpdate("UPDATE Passports SET PassportIssued = '" + scanner.nextLine() + "' WHERE PassportID = " + resultSetClient.getString("ClientPassportID"));
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "2":
                                            System.out.println("Введите новую дату выдачи паспорта:");
                                            statement.executeUpdate("UPDATE Passports SET PassportDateOfIssue = '" + scanner.nextLine() + "' WHERE PassportID = " + resultSetClient.getString("ClientPassportID"));
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "3":
                                            System.out.println("Введите новый код подразделения паспорта:");
                                            statement.executeUpdate("UPDATE Passports SET PassportUnitCode = '" + scanner.nextLine() + "' WHERE PassportID = " + resultSetClient.getString("ClientPassportID"));
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "4":
                                            System.out.println("Введите новую серию паспорта");
                                            statement.executeUpdate("UPDATE Passports SET PassportSeries = '" + scanner.nextLine() + "' WHERE PassportID = " + resultSetClient.getString("ClientPassportID"));
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "5":
                                            System.out.println("Введите новый номер паспорта");
                                            statement.executeUpdate("UPDATE Passports SET PassportNumber = '" + scanner.nextLine() + "' WHERE PassportID = " + resultSetClient.getString("ClientPassportID"));
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "6":
                                            System.out.println("Введите новый пароль");
                                            statement.executeUpdate("UPDATE Registration SET RegistrationPassword = '" + scanner.nextLine() + "' WHERE RegistrationID = " + resultSetClient.getString("ClientRegistrationID"));
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "7":
                                            System.out.println("Введите новую фамилию");
                                            statement.executeUpdate("UPDATE Clients SET ClientLastName = '" + scanner.nextLine() + "' WHERE ClientLastName = " + clientLastNameChange);
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "8":
                                            System.out.println("Введите новое имя");
                                            statement.executeUpdate("UPDATE Clients SET ClientFirstName = '" + scanner.nextLine() + "' WHERE ClientLastName = " + clientLastNameChange);
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "9":
                                            System.out.println("Введите новое отчество");
                                            statement.executeUpdate("UPDATE Clients SET ClientPatronymic = '" + scanner.nextLine() + "' WHERE ClientLastName = " + clientLastNameChange);
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "10":
                                            System.out.println("Введите новый пол");
                                            statement.executeUpdate("UPDATE Clients SET ClientGender = '" + scanner.nextLine() + "' WHERE ClientLastName = " + clientLastNameChange);
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "11":
                                            System.out.println("Введите новую дату рождения");
                                            statement.executeUpdate("UPDATE Clients SET ClientDateOfBirth = '" + scanner.nextLine() + "' WHERE ClientLastName = " + clientLastNameChange);
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        case "12":
                                            System.out.println("Введите новое место рождения");
                                            statement.executeUpdate("UPDATE Clients SET ClientPlaceOfBirth = '" + scanner.nextLine() + "' WHERE ClientLastName = " + clientLastNameChange);
                                            System.out.println("Операция выполнена успешно!");
                                            break;
                                        default:
                                            System.out.println("Такой команды не существует!");
                                            statement.executeUpdate("UPDATE Clients SET ClientLastName = '" + scanner.nextLine() + "' WHERE ClientLastName = " + clientLastNameChange);
                                            break;
                                    }
                                } else {
                                    System.out.println("Пароль неверный!");
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Такого пользователя не существует!");
                            break;
                        }
                    }
                    catch (Exception e){
                        System.out.println("Ошибка: " + e);
                    }
                    break;


                case "4":
                    System.out.println("Введите фамилию пользователя, которого вы хотите удалить:");
                    String clientLastNameDelete = scanner.nextLine();
                    try {
                        Connection connection = DriverManager.getConnection(url, username, password);
                        Statement statement = connection.createStatement();
                        ResultSet resultSetClients = statement.executeQuery("SELECT * FROM Clients WHERE ClientLastName = '" + clientLastNameDelete + "'");
                        if (resultSetClients.next()){
                            String registrationID = resultSetClients.getString("ClientRegistrationID");
                            String registrationPassword = "";
                            ResultSet resultSetRegistration = statement.executeQuery("SELECT * FROM Registration WHERE RegistrationID = '" + registrationID + "'");
                            if (resultSetRegistration.next()){
                                registrationPassword = resultSetRegistration.getString("RegistrationPassword");
                            }
                            ResultSet resultSetClient = statement.executeQuery("SELECT * FROM Clients WHERE ClientLastName = '" + clientLastNameDelete + "'");
                            if (resultSetClient.next()) {
                                System.out.println("Введите пароль для того, чтобы удалить все данные о пользователе:");
                                if (scanner.nextLine().equals(registrationPassword)) {
                                    String clientPassportID = resultSetClient.getString("ClientPassportID");
                                    String clientRegistrationID = resultSetClient.getString("ClientRegistrationID");
                                    statement.executeUpdate("DELETE FROM Clients WHERE ClientLastName = '" + clientLastNameDelete + "'");
                                    statement.executeUpdate("DELETE FROM Passports WHERE PassportID = '" + clientPassportID + "'");
                                    statement.executeUpdate("DELETE FROM Registration WHERE RegistrationID = '" + clientRegistrationID + "'");
                                    System.out.println("Клиент успешно удален!");
                                } else {
                                    System.out.println("Пароль неверный!");
                                    break;
                                }
                            }
                        }
                        else{
                            System.out.println("Такого пользователя не существует!");
                            break;
                        }
                    }
                    catch (Exception e){
                        System.out.println("Ошибка: " + e);
                    }
                    break;

                default:
                    System.out.println("Такой команды не существует!");
                    break;
            }
        }
    }
}