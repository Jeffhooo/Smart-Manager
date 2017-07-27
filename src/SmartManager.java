import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jeff on 2017/7/23.
 */
public class SmartManager {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_manager";
    private static final String USER = "root";
    private static final String PASS = "01165418";
    private static final String[] productList = {
            "black_storm_coffee",
            "blue_sky_coffee",
            "home_make_coffee",
            "sun_shine_coffee",
            "sweet_dream_coffee",
            "Americano_coffee",
            "colombian_coffee",
            "blue_mountain_coffee",
            "lavender_cappuccino",
            "hazelnut_cappuccino"
    };

    private static final String[] featureList = {
            "ice_cream",
            "cold",
            "chocolate",
            "strawberry",
            "milk",
            "foam"
    };

    private static final String[] ingredientList = {
            "ice_cream",
            "chocolate",
            "ice",
            "coffee_bean",
            "milk"
    };

    private ArrayList<Product> sendAreaSales(String areaName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ArrayList<Product> areaSales = new ArrayList<>();
            for(String product_name : productList) {
                String sql_base = "select sell_number from sales_record where area_name='%s' and product_name='%s'";
                String sql = String.format(sql_base, areaName, product_name);
                rs = stmt.executeQuery(sql);
                Integer counter = 0;
                while(rs.next()) {
                    Integer sellNumber = rs.getInt("sell_number");
                    counter += sellNumber;
                }
                Product product = new Product(product_name);
                product.setWeekSales(counter);
                areaSales.add(product);
                ProductWeekSalesComparator pc = new ProductWeekSalesComparator();
                Collections.sort(areaSales, pc);
            }
            return areaSales;
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
        return null;
    }

    private ArrayList<Product> sendShopSales(String shopName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ArrayList<Product> shopSales = new ArrayList<>();
            for(String product_name : productList) {
                String sql_base = "select sell_number from sales_record where shop_name='%s' and product_name='%s'";
                String sql = String.format(sql_base, shopName, product_name);
                rs = stmt.executeQuery(sql);
                Integer counter = 0;
                while(rs.next()) {
                    Integer sellNumber = rs.getInt("sell_number");
                    counter += sellNumber;
                }
                Product product = new Product(product_name);
                product.setWeekSales(counter);
                shopSales.add(product);
                ProductWeekSalesComparator pc = new ProductWeekSalesComparator();
                Collections.sort(shopSales, pc);
            }
            return shopSales;
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
        return null;
    }

    public ArrayList<Integer> sendProductWeekSales(String productName, Date beginDate) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Date date = beginDate;
        int beginDay = beginDate.getDay();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ArrayList<Integer> weekSales = new ArrayList<>();
            for(int i = 0; i < 7; i++) {
                date.setDay(beginDay+i);
                String sql_base
                        = "select sell_number from sales_record where product_name='%s' and sell_date='%s'";
                String sql = String.format(sql_base, productName, date);
                rs = stmt.executeQuery(sql);
                Integer counter = 0;
                while(rs.next()) {
                    Integer sellNumber = rs.getInt("sell_number");
                    counter += sellNumber;
                }
                weekSales.add(counter);
            }
            date.setDay(beginDay);
            return weekSales;
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
        return null;
    }

    public ArrayList<Integer> sendProductWeekSales(String productName, Date beginDate, String areaName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Date date = beginDate;
        int beginDay = beginDate.getDay();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ArrayList<Integer> weekSales = new ArrayList<>();
            for(int i = 0; i < 7; i++) {
                date.setDay(beginDay+i);
                String sql_base
                        = "select sell_number from sales_record where product_name='%s' and sell_date='%s' and area_name='%s'";
                String sql = String.format(sql_base, productName, date, areaName);
                rs = stmt.executeQuery(sql);
                Integer counter = 0;
                while(rs.next()) {
                    Integer sellNumber = rs.getInt("sell_number");
                    counter += sellNumber;
                }
                weekSales.add(counter);
            }
            date.setDay(beginDay);
            return weekSales;
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
        return null;
    }

    public ArrayList<Product> sendTopSalesLastWeek(Date beginDay) {
        ArrayList<Product> topTen = new ArrayList<>();
        for(String productName : productList) {
            ArrayList<Integer> weekSales
                    = sendProductWeekSales(productName, beginDay);
            Integer counter = 0;
            for(Integer daySales : weekSales) {
                counter += daySales;
            }
            Product product = new Product(productName);
            product.setWeekSales(counter);
            topTen.add(product);
        }
        ProductWeekSalesComparator productWeekSalesComparator = new ProductWeekSalesComparator();
        Collections.sort(topTen, productWeekSalesComparator);
        return topTen;
    }

    public ArrayList<Feature> sendTopFearuesLastWeek(Date beginDate) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Date endDay
                = new Date(beginDate.getYear(), beginDate.getMonth(),beginDate.getDay()+7);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ArrayList<Feature> topFeatures = new ArrayList<>();
            for(String feature_name : featureList) {
                String sql_base
                        = "select * from customers_feedback where feedback_date>'%s' and feedback_date<'%s' and feature='%s'";
                String sql = String.format(sql_base, beginDate.toString(), endDay.toString(), feature_name);
                rs = stmt.executeQuery(sql);
                Integer counter = 0;
                while(rs.next()) {
                    Integer score = rs.getInt("score");
                    counter += score;
                }
                Feature feature = new Feature(feature_name);
                feature.setScore(counter);
                topFeatures.add(feature);
            }
            FeatureWeekScoreComparator featureWeekScoreComparator = new FeatureWeekScoreComparator();
            Collections.sort(topFeatures, featureWeekScoreComparator);
            return topFeatures;
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
        return null;
    }

    public ArrayList<Ingredient> sendIngredientStorage() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ArrayList<Ingredient> storeList = new ArrayList<>();
            for(String ingredient_name : ingredientList) {
                String sql_base
                        = "select store_number from storage_record where ingredient_name='%s'";
                String sql = String.format(sql_base, ingredient_name);
                rs = stmt.executeQuery(sql);
                Integer counter = 0;
                while(rs.next()) {
                    Integer storeNumber = rs.getInt("store_number");
                    counter += storeNumber;
                }
                Ingredient ingredient = new Ingredient(ingredient_name, counter);
                storeList.add(ingredient);
            }
            return storeList;
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
        return null;
    }

    public ArrayList<Ingredient> sendIngredientStorageOfShop(String shopName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ArrayList<Ingredient> storeList = new ArrayList<>();
            for(String ingredient_name : ingredientList) {
                String sql_base
                        = "select store_number from storage_record where ingredient_name='%s' and shop_name='%s'";
                String sql = String.format(sql_base, ingredient_name, shopName);
                rs = stmt.executeQuery(sql);
                Integer counter = 0;
                while(rs.next()) {
                    Integer storeNumber = rs.getInt("store_number");
                    counter += storeNumber;
                }
                Ingredient ingredient = new Ingredient(ingredient_name, counter);
                storeList.add(ingredient);
            }
            return storeList;
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
        return null;
    }

    public void showPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("apple", 100);
        dataset.setValue("orange" , 50);
        JFreeChart jFreeChart =
                ChartFactory.createPieChart3D
                        ("fruit", dataset, true, false, false);
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        JPanel jPanel = new JPanel();
        jPanel.add(chartPanel, BorderLayout.CENTER);
        JFrame jFrame = new JFrame();
        jFrame.add(jPanel);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setBounds(100, 100, 700, 500);
        jFrame.setVisible(true);
    }

    public void showDemo() {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("Smart Manager Demo");
        jFrame.setBounds(100, 100, 1000, 100);
        JPanel contentPane = new JPanel();
//        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        jFrame.setContentPane(contentPane);
//        contentPane.setLayout(new GridLayout(3,1,5,5));
        JPanel pane1 = new JPanel();
        contentPane.add(pane1);
        JPanel pane2 = new JPanel();
        contentPane.add(pane2);
        JPanel pane3 = new JPanel();
        contentPane.add(pane3);
        JPanel pane4 = new JPanel();
        contentPane.add(pane4);
        JPanel pane5 = new JPanel();
        contentPane.add(pane5);
        JPanel pane6 = new JPanel();
        contentPane.add(pane6);
        JButton shopManager = new JButton("Shop Manager");
        shopManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SmartManager smartManager = new SmartManager();
                Date beginDate = new Date(2017, 7, 16);
                ShopManager shopManager_A = new ShopManager("shop_A");
                shopManager_A.getSalesRecordFromDB();
                shopManager_A.showSalesRecord();
                shopManager_A.getFeedBackRecordFromDB();
                shopManager_A.showFeedBackRecord();
                shopManager_A.getIngredientStorageRecordFromDB();
                shopManager_A.showIngredientStorageRecord();
                shopManager_A.getShopSales(smartManager.sendShopSales("shop_A"));
                shopManager_A.showShopSales();
            }
        });
        JButton salesManager = new JButton("Sales Manager");
        salesManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SmartManager smartManager = new SmartManager();
                Date beginDate = new Date(2017, 7, 16);
                SalesManager salesManager_A = new SalesManager("area_A");
                salesManager_A.getAreaSales(smartManager.sendAreaSales("area_A"));
                salesManager_A.showAreaSales();
                salesManager_A.getProductWeeklySales(smartManager.sendProductWeekSales(productList[0], beginDate, "area_A"));
                salesManager_A.showProductWeeklySales();
            }
        });
        JButton marketManager = new JButton("Market Manager");
        marketManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SmartManager smartManager = new SmartManager();
                Date beginDate = new Date(2017, 7, 16);
                MarketManager marketManager = new MarketManager();
                marketManager.getMarketSchedulesFromDB();
                marketManager.showMarketSchedule();
                marketManager.getTopSalesLastWeek(smartManager.sendTopSalesLastWeek(beginDate));
                marketManager.showTopSales();
                marketManager.receiveMessage();
            }
        });
        JButton rdManager = new JButton("R&D Manager");
        rdManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SmartManager smartManager = new SmartManager();
                Date beginDate = new Date(2017, 7, 16);
                RDManager rdManager = new RDManager();
                rdManager.getRdSchedulesFromDB();
                rdManager.showRDSchedule();
                rdManager.setPopularFeatures(smartManager.sendTopFearuesLastWeek(beginDate));
                rdManager.showPopularFeatures();
                rdManager.receiveMessage();
            }
        });
        JButton productionManager = new JButton("Production Manager");
        productionManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SmartManager smartManager = new SmartManager();
                Date beginDate = new Date(2017, 7, 16);
                ProductionManager productionManager = new ProductionManager("area_A");
                productionManager.getProductionScheduleFromDB();
                productionManager.showProductionSchedule();
                productionManager.getIngredientStorageList(smartManager.sendIngredientStorage());
                productionManager.showIngredientStorage();
                productionManager.receiveMessage();
            }
        });
        JButton distributionManager = new JButton("Distribution Manager");
        distributionManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SmartManager smartManager = new SmartManager();
                Date beginDate = new Date(2017, 7, 16);
                DistributionManager distributionManager = new DistributionManager("area_A");
                distributionManager.getDistributionSchedulesFromDB();
                distributionManager.showDistributionSchedule();
                distributionManager.setIngredientStorageList(smartManager.sendIngredientStorageOfShop("shop_J"));
                distributionManager.showIngredientStorage();
                distributionManager.receiveMessage();
            }
        });
        pane1.add(shopManager);
        pane2.add(salesManager);
        pane3.add(marketManager);
        pane4.add(rdManager);
        pane5.add(productionManager);
        pane6.add(distributionManager);
        jFrame.setVisible(true);

    }

    public static void main(String[] args) {
        SmartManager smartManager = new SmartManager();
        smartManager.showDemo();
    }
}
