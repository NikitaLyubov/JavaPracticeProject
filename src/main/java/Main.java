import java.awt.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        var parser = new Parser("./src/main/resources/data.csv");
        parser.parse();
        DbHandler dbHandler = DbHandler.getInstance();

        // Заполнение таблицы в БД из csv файла
//        for (var stat: parser.getStatistics()) {
//            dbHandler.addStats(stat);
//        }

        var allStatistics = dbHandler.getAllStatistics();

        System.out.println("Самый низкий показатель щедрости среди  \"Middle East and Northern Africa\" и \"Central and Eastern Europe\": "+ dbHandler.getLowestGenerosity(allStatistics));

        System.out.println("Страна с самыми средними показателями среди \"Southeastern Asia\" и \"Sub-Saharan Africa\": " + dbHandler.getCountryWithMostAverageStats(allStatistics));




        // Вывод графика
        EventQueue.invokeLater(() -> {
            var ex = new BarChartEx();
            ex.setVisible(true);
        });
    }
}