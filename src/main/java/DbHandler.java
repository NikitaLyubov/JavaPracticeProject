import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class DbHandler {
    // Константа, в которой хранится адрес подключения
    private static final String CON_STR = "jdbc:sqlite:./src/main/resources/countryStatistics.db";

    // Используем шаблон одиночка, чтобы не плодить множество
    // экземпляров класса DbHandler
    private static DbHandler instance = null;

    public static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DbHandler();
        return instance;
    }

    private final Connection connection;

    private DbHandler() throws SQLException {
        // Регистрируем драйвер, с которым будем работать
        // в нашем случае Sqlite
        DriverManager.registerDriver(new JDBC());
        // Выполняем подключение к базе данных
        this.connection = DriverManager.getConnection(CON_STR);
    }

    public List<Statistics> getAllStatistics() {

        // Statement используется для того, чтобы выполнить sql-запрос
        try (Statement statement = this.connection.createStatement()) {
            // В данный список будем загружать наши продукты, полученные из БД
            List<Statistics> products = new ArrayList<>();
            // В resultSet будет храниться результат нашего запроса,
            // который выполняется командой statement.executeQuery()
            ResultSet resultSet = statement.executeQuery("SELECT Country, Region, HappinessRank, HappinessScore, LowerConfidenceInterval, UpperConfidenceInterval, Economy, Family, Health, Freedom, Trust, Generosity, DystopiaResidual FROM countries;");
            // Проходимся по нашему resultSet и заносим данные в products

            // orderby sqlite
            while (resultSet.next()) {
                products.add(new Statistics(resultSet.getString("Country"),
                        resultSet.getString("Region"),
                        resultSet.getInt("HappinessRank"),
                        resultSet.getDouble("HappinessScore"),
                        resultSet.getDouble("LowerConfidenceInterval"),
                        resultSet.getDouble("UpperConfidenceInterval"),
                        resultSet.getDouble("Economy"),
                        resultSet.getDouble("Family"),
                        resultSet.getDouble("Health"),
                        resultSet.getDouble("Freedom"),
                        resultSet.getDouble("Trust"),
                        resultSet.getDouble("Generosity"),
                        resultSet.getDouble("DystopiaResidual")));
            }
            // Возвращаем наш список
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустую коллекцию
            return Collections.emptyList();
        }
    }

    // Добавление статистики в БД
    public void addStats(Statistics statistics) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO countries('Country', 'Region', 'HappinessRank', 'HappinessScore', 'LowerConfidenceInterval'," +
                        " 'UpperConfidenceInterval', 'Economy', 'Family', 'Health', " +
                        "'Freedom', 'Trust', 'Generosity', 'DystopiaResidual') " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setObject(1, statistics.country);
            statement.setObject(2, statistics.region);
            statement.setObject(3, statistics.happinessRank);
            statement.setObject(4, statistics.happinessScore);
            statement.setObject(5, statistics.lowerConfInterval);
            statement.setObject(6, statistics.upperConfInterval);
            statement.setObject(7, statistics.economy);
            statement.setObject(8, statistics.family);
            statement.setObject(9, statistics.health);
            statement.setObject(10, statistics.freedom);
            statement.setObject(11, statistics.trust);
            statement.setObject(12, statistics.generosity);
            statement.setObject(13, statistics.dystopiaResidual);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public double getLowestGenerosity(List<Statistics> statistics) {

        var lowest = 100d;
        for (var w : statistics) {
            if (w.region.equals("Middle East and Northern Africa") || w.region.equals("Central and Eastern Europe"))
                if (w.generosity < lowest)
                    lowest = w.generosity;
        }
        return lowest;
    }

    public String getCountryWithMostAverageStats(List<Statistics> statistics) {
        var middle = 0d;
        var country = "";
        var min = 100d;
        for (var a : statistics) {
            middle += a.happinessRank + a.happinessScore + a.lowerConfInterval + a.upperConfInterval + a.economy + a.family + a.health + a.freedom + a.trust + a.generosity + a.dystopiaResidual;
        }
        middle /= statistics.size();
        for (var c : statistics) {
            var sum = 0d;
            if (Objects.equals(c.region, "Southeastern Asia")
                    || Objects.equals(c.region, "Sub-Saharan Africa")) {
                sum = c.happinessRank + c.happinessScore + c.lowerConfInterval + c.upperConfInterval + c.economy + c.family + c.health + c.freedom + c.trust + c.generosity + c.dystopiaResidual;
                if (min > Math.abs(middle - sum)) {
                    min = Math.abs(middle - sum);
                    country = c.country;
                }
            }
        }
        return country;
    }


    public void cleanDb() {
        try {
            this.connection.prepareStatement("DELETE FROM countries;").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
