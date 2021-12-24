import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private final ArrayList<Statistics> statistics;
    private BufferedReader reader;

    public Parser(String path) {
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        statistics = new ArrayList<>();
    }

    public void parse() {
        var line = "";
        var count = 0;
        while (true) {
            try {
                if ((line = reader.readLine()) == null)
                    break;
                count++;
                if (count == 1)
                    continue;
                var stats = line.split(",");
                statistics.add(new Statistics(stats[0], stats[1], Integer.parseInt(stats[2]), Double.parseDouble(stats[3]),
                        Double.parseDouble(stats[4]), Double.parseDouble(stats[5]), Double.parseDouble(stats[6]),
                        Double.parseDouble(stats[7]), Double.parseDouble(stats[8]), Double.parseDouble(stats[9]),
                        Double.parseDouble(stats[10]), Double.parseDouble(stats[11]), Double.parseDouble(stats[12])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Statistics> getStatistics() {
        return statistics;
    }
}
