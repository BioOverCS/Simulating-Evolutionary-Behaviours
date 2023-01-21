import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * A class that displays all the charts. These charts are powered by JFreeChart,
 * an external library for graphs and charts in Swing. This class only exists
 * because JFreeChart does not work well will a null layout manager, which was
 * used in the controls class.
 * 
 * @see Controls
 */
public class Charts extends JPanel {
    private static final int WIDTH = 500, HEIGHT = 500;
    /* Series of data for charts, these need to global for update method. My needs
     * just happen to all require XY series. */
    private XYSeries growthSeries = new XYSeries("Growth Rate");
    private XYSeries popSeries = new XYSeries("Population");
    private XYSeries scatterSeries = new XYSeries("Physical Characteristics");

    public Charts() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Scatter plot: Speed VS Sight
        XYSeriesCollection scatterSet = new XYSeriesCollection(scatterSeries);
        JFreeChart scatterChart = ChartFactory.createScatterPlot(
            "Physical Characteristics", "Speed", "Sight",
            scatterSet, PlotOrientation.VERTICAL,
            false, true, false);
        /* I'm setting defined ranges for both axes because auto ranging for a scatter
         * plot is terrible idea. I turn of the tick labels because those numbers are
         * meaningless for the user. */
        scatterChart.getXYPlot().getDomainAxis().setRange(2, 16);
        scatterChart.getXYPlot().getRangeAxis().setRange(5, 30);
        scatterChart.getXYPlot().getDomainAxis().setTickLabelsVisible(false);
        scatterChart.getXYPlot().getRangeAxis().setTickLabelsVisible(false);
        ChartPanel scatterPlot = new ChartPanel(scatterChart);
        scatterPlot.setPreferredSize(new Dimension(500, 255));
        add(scatterPlot);

        // Population chart
        XYSeriesCollection popSet = new XYSeriesCollection(popSeries);
        JFreeChart popChart = ChartFactory.createXYAreaChart(
            "Population", "Round No.", "Population",
            popSet, PlotOrientation.VERTICAL,
            false, true, false);
        ChartPanel good_var_name = new ChartPanel(popChart);
        good_var_name.setPreferredSize(new Dimension(240, 240));
        add(good_var_name);

        // Growth rate chart
        XYSeriesCollection growthSet = new XYSeriesCollection(growthSeries);
        JFreeChart growthChart = ChartFactory.createXYLineChart(
            "Growth Rate", "Round No.", "Creatures Added",
            growthSet, PlotOrientation.VERTICAL,
            false, true, false);
        ChartPanel chp = new ChartPanel(growthChart);
        chp.setPreferredSize(new Dimension(240, 240));
        add(chp);
    }

    /** Updates all the series, which update all the charts. */
    public void update(int rounds, int growth, ArrayList<Creature> guys) {
        growthSeries.add(rounds, growth);
        popSeries.add(rounds, guys.size());
        scatterSeries.clear();
        for (Creature guy : guys) {
            scatterSeries.add(guy.speed(), guy.sight());
        }
    }

    /** Clears the series, which clears all the charts. */
    public void clear() {
        growthSeries.clear();
        popSeries.clear();
        scatterSeries.clear();
    }

}