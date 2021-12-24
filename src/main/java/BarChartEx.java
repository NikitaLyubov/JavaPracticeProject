import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;

import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChartEx extends JFrame {

    public BarChartEx() {
        initUI();
    }

    private void initUI() {

        CategoryDataset dataset = createDataset();

        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        pack();
        setTitle("Bar chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CategoryDataset createDataset() {

        var dataset = new DefaultCategoryDataset();
        var data = new Parser("./src/main/resources/data.csv");
        data.parse();
        for (var countryStat : data.getStatistics()) {
            dataset.setValue(countryStat.generosity, "Generosity", countryStat.country);
        }
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {

        CategoryAxis categoryAxis = new CategoryAxis("Страны");
        categoryAxis.setLowerMargin(.01);
        categoryAxis.setCategoryMargin(.01);
        categoryAxis.setUpperMargin(.01);
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);


        ValueAxis valueAxis = new NumberAxis("");

        StackedBarRenderer renderer = new StackedBarRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        CategoryPlot plot = new CategoryPlot(dataset,
                categoryAxis,
                valueAxis,
                renderer);

        plot.setOrientation(PlotOrientation.VERTICAL);


        return new JFreeChart("Показатели уровня щедрости всех стран",
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true);

    }
}
