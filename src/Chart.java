import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart extends JFrame {

	
	public XYSeriesCollection dataset;
	public XYSeries series;
	
	public Chart(String name) {
		super("XY Line Chart Example with JFreechart");
		dataset = new XYSeriesCollection();
		series = new XYSeries(name);
	}
	
	public void afficheChart(){
		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);
		
		setSize(640, 480);
		setLocationRelativeTo(null);
		
	}

	public JPanel createChartPanel() {
		String chartTitle = "MOGPL";
		String xAxisLabel = "X";
		String yAxisLabel = "Y";

		dataset.addSeries(series);
		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset);

		XYPlot plot = chart.getXYPlot();

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		// sets paint color for each series
		renderer.setSeriesPaint(0, Color.RED);

		// sets thickness for series (using strokes)
		renderer.setSeriesStroke(0, new BasicStroke(4.0f));
		plot.setRenderer(renderer);

		return new ChartPanel(chart);
	}

	public void createDataset(Integer abs, Double ord) {
			
		series.add(abs,ord);

	}
}