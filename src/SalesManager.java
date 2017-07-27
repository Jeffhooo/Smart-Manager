/**
 * Created by Jeff on 2017/7/23.
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class SalesManager {
    private String areaName;
    private ArrayList<Product> areaSales;
    private ArrayList<Integer> productWeeklySale;

    public SalesManager(String areaName) {
        this.areaName = areaName;
        productWeeklySale = new ArrayList<>();
    }

    public void getAreaSales(ArrayList<Product> areaSales) {
        this.areaSales = areaSales;
    }

    public void showAreaSales() {
        String[][] table = new String[10][2];
        int counter = 0;
        for(Product product : areaSales) {
            table[counter][0] = product.getName();
            table[counter][1] = new Integer(product.getWeekSales()).toString();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        String[] title = {"Product Name", "Sold Number"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Area Top Sales Products");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void getProductWeeklySales(ArrayList<Integer> productWeeklySale) {
        this.productWeeklySale = productWeeklySale;
    }

    public void showProductWeeklySales() {
        String[][] table = new String[1][7];
        int counter = 0;
        for(Integer sellNum : productWeeklySale) {
            table[0][counter] = new Integer(sellNum).toString();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        jFrame.setBounds(400, 0, 600, 50);
        String[] title = {"2017-7-16", "2017-7-17", "2017-7-18", "2017-7-19",
                "2017-7-20", "2017-7-21", "2017-7-22"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(600, 30));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Weekly sales of black storm coffee");
        jFrame.pack();
        jFrame.setVisible(true);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 0; i < 7; i++) {
            dataset.setValue(Integer.parseInt(table[0][i]), "number", title[i]);
        }
        JFreeChart jFreeChart =
                ChartFactory.createBarChart3D
                        ("Weekly sales of black storm coffee", "Date", "Sale", dataset, PlotOrientation.VERTICAL, true, false, false);
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        JPanel jPanel = new JPanel();
        jPanel.add(chartPanel, BorderLayout.CENTER);
        JFrame jFrameChart = new JFrame();
        jFrameChart.add(jPanel);
        jFrameChart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrameChart.setBounds(400, 100, 700, 500);
        jFrameChart.setVisible(true);
    }

    public static void main(String[] args) {
        SalesManager salesManager = new SalesManager("area_A");
    }

}
