import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Jeff on 2017/7/23.
 */
public class ShopManager {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_manager";
    private static final String USER = "root";
    private static final String PASS = "01165418";

    private String shopName;
    private ArrayList<Product> shopSales;
    private String[][] salesRecord;
    private String[][] feedbackRecord;
    private String[][] ingredientStorageRecord;

    public ShopManager(String shopName) {
        this.shopName = shopName;
        salesRecord = new String[20][4];
        feedbackRecord = new String[4][3];
        ingredientStorageRecord = new String[5][2];
    }

    public void getSalesRecordFromDB(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sqlBase = "select * from sales_record where shop_name='%s'";
            String sql = String.format(sqlBase, shopName);
            rs = stmt.executeQuery(sql);
            int counter = 0;
            while(rs.next() && counter < 20) {
                String productName = rs.getString("product_name");
                String productId = rs.getString("product_id");
                String sellNum = rs.getString("sell_number");
                Date sellDate = new Date(rs.getString("sell_date"));
                String[] record = {productName, productId, sellNum, sellDate.toString()};
                salesRecord[counter] = record;
                ++counter;
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void getFeedBackRecordFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sqlBase = "select * from customers_feedback where feedback_shop='%s'";
            String sql = String.format(sqlBase, shopName);
            rs = stmt.executeQuery(sql);
            int counter = 0;
            while(rs.next() && counter < 4) {
                String feature = rs.getString("feature");
                String score = rs.getString("score");
                Date feedbackDate = new Date(rs.getString("feedback_date"));
                String[] record = {feature, score, feedbackDate.toString()};
                feedbackRecord[counter] = record;
                ++counter;
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void getIngredientStorageRecordFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sqlBase = "select * from storage_record where shop_name='%s'";
            String sql = String.format(sqlBase, shopName);
            rs = stmt.executeQuery(sql);
            int counter = 0;
            while(rs.next() && counter < 5) {
                String ingredientName = rs.getString("ingredient_name");
                String storeNum = rs.getString("store_number");
                String[] record = {ingredientName, storeNum};
                ingredientStorageRecord[counter] = record;
                ++counter;
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void getShopSales(ArrayList<Product> shopSales) {
        this.shopSales = shopSales;
    }

    public void showSalesRecord() {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(0, 0, 500, 350);
        String[] title = {"Product Name", "Product ID", "Sold Number", "Sold Date"};
        JTable jTable = new JTable(salesRecord, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(500, 350));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Sales Record");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void showFeedBackRecord() {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(0, 350, 500, 100);
        String[] title = {"Feature", "Score", "Feedback Date"};
        JTable jTable = new JTable(feedbackRecord, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Feedback Record");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void showIngredientStorageRecord() {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(0, 500, 500, 100);
        String[] title = {"Ingredient Name", "Store Number"};
        JTable jTable = new JTable(ingredientStorageRecord, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Ingredient Storage Record");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void showShopSales() {
        String[][] table = new String[3][2];
        int counter = 0;
        for(Product product : shopSales) {
            table[counter][0] = product.getName();
            table[counter][1] = new Integer(product.getWeekSales()).toString();
            ++counter;
            if(counter == 3) break;
        }

        JFrame jFrame = new JFrame();
        jFrame.setBounds(500, 0, 700, 100);
        String[] title = {"Product Name", "Sold Number"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(400, 100));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Shop Top Sales Products");
        jFrame.pack();
        jFrame.setVisible(true);

        DefaultPieDataset dataset = new DefaultPieDataset();
        for(int i = 0; i < 3; i++) {
            dataset.setValue(table[i][0], Integer.parseInt(table[i][1]));
        }
        JFreeChart jFreeChart =
                ChartFactory.createPieChart3D
                        ("Shop Top Sales Products", dataset, true, false, false);
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        JPanel jPanel = new JPanel();
        jPanel.add(chartPanel, BorderLayout.CENTER);
        JFrame jFrameChart = new JFrame();
        jFrameChart.add(jPanel);
        jFrameChart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrameChart.setBounds(500, 100, 700, 500);
        jFrameChart.setVisible(true);
    }





    public static void main(String[] args) {
        ShopManager shopManager = new ShopManager("shop_A");
        shopManager.getSalesRecordFromDB();
        shopManager.showSalesRecord();
        shopManager.getFeedBackRecordFromDB();
        shopManager.showFeedBackRecord();
    }
}
