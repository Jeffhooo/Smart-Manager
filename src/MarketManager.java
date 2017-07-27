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

/**
 * Created by Jeff on 2017/7/23.
 */
public class MarketManager {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_manager";
    private static final String USER = "root";
    private static final String PASS = "01165418";
    private static final Date today = new Date(2017, 7, 22);

    private ArrayList<MarketSchedule> marketSchedules;
    private ArrayList<Product> topSalesLastWeek;

    public MarketManager() {
        marketSchedules = new ArrayList<>();
        topSalesLastWeek = new ArrayList<>();
    };

    public void getMarketSchedulesFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "select * from market_plan";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String planName = rs.getString("plan_name");
                System.out.println(planName);
                String productName = rs.getString("product_name");
                String promoteMethod = rs.getString("promote_method");
                Date beginDate = new Date(rs.getString("promote_begin_date"));
                Date endDate = new Date(rs.getString("promote_end_date"));
                String promoteArea = rs.getString("promote_area");
                MarketSchedule marketSchedule = new MarketSchedule(planName, productName, promoteMethod, beginDate, endDate, promoteArea);
                this.marketSchedules.add(marketSchedule);
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

    public void getTopSalesLastWeek(ArrayList<Product> topSalesLastWeek) {
        this.topSalesLastWeek = topSalesLastWeek;
    }

    public void OptimizeSchedule() {
        for(MarketSchedule marketSchedule : marketSchedules) {
            String productName = marketSchedule.getProductName();
            for(Product product : topSalesLastWeek) {
                if(productName.equals(product.getName())) {
                    if(marketSchedule.getBeginDate().getDay() > today.getDay()) {
                            //optimize
                    }
                }
            }
        }
    }

    public void showMarketSchedule() {
        String[][] table = new String[3][6];
        int counter = 0;
        for(MarketSchedule marketSchedule : marketSchedules) {
            table[counter][0] = marketSchedule.getPlanName();
            table[counter][1] = marketSchedule.getProductName();
            table[counter][2] = marketSchedule.getPromoteMethod();
            table[counter][3] = marketSchedule.getBeginDate().toString();
            table[counter][4] = marketSchedule.getEndDate().toString();
            table[counter][5] = marketSchedule.getPromoteArea();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        String[] title = {"Plan Name", "Product Name", "Promote Method", "Begin Date", "End Date" , "Promote Area"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(900, 75));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Market Schedule");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void showTopSales() {
        String[][] table = new String[10][2];
        int counter = 0;
        for(Product product : topSalesLastWeek) {
            table[counter][0] = product.getName();
            table[counter][1] = new Integer(product.getWeekSales()).toString();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        jFrame.setBounds(0, 150, 400, 200);
        String[] title = {"Product Name", "Sold Number"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Area Top Sales Products");
        jFrame.pack();
        jFrame.setVisible(true);

        DefaultPieDataset dataset = new DefaultPieDataset();
        for(int i = 0; i < 10; i++) {
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
        jFrameChart.setBounds(400, 100, 700, 500);
        jFrameChart.setVisible(true);
    }

    public void receiveMessage() {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(0, 400, 400, 100);
        JButton checkSchedule = new JButton("New Message(1)");
        checkSchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkSchedule();
            }
        });
        JPanel jPanel = new JPanel();
        jFrame.getContentPane().add(jPanel);
        jPanel.add(checkSchedule);
        jFrame.setVisible(true);
    }

    private void checkSchedule() {
        for(MarketSchedule marketSchedule : marketSchedules) {
            String productName = marketSchedule.getProductName();
            System.out.println(productName);
            for(Product topProduct : topSalesLastWeek) {
                System.out.println(topProduct.getName());
                if(topProduct.getName().equals(productName)) {
                    if(marketSchedule.getBeginDate().getDay() > today.getDay()+7) {
                        sendOptimizeAdvise(marketSchedule);
                    }
                }
            }
        }
    }

    public void sendOptimizeAdvise(MarketSchedule marketSchedule) {
        String massageBasic = "Your marketing plan of '%s'is highly matched the good sales product: '%s'. " +
                "But your begin date is a week later. Do you want to change this plan?";
        String massage = String.format(massageBasic, marketSchedule.getPlanName(), marketSchedule.getProductName());
        int n = JOptionPane.showConfirmDialog(null, massage, "Schedule Advise",JOptionPane.YES_NO_OPTION);
        if(n == 0) {
            changeSchedule();
        }
    }

    private void changeSchedule() {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("Change Schedule");
        jFrame.setBounds(200, 100, 500, 200);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        jFrame.setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(3,1,5,5));
        JPanel pane1 = new JPanel();
        contentPane.add(pane1);
        JPanel pane2 = new JPanel();
        contentPane.add(pane2);
        JPanel pane3 = new JPanel();
        contentPane.add(pane3);
        JPanel pane4 = new JPanel();
        contentPane.add(pane4);
        JLabel label1 = new JLabel("Begin Date: ");
        JTextField textField1 = new JTextField();
        textField1.setColumns(10);
        pane1.add(label1);
        pane1.add(textField1);
        JLabel label2 = new JLabel("End Date: ");
        JTextField textField2=new JTextField();
        textField2.setColumns(10);
        pane2.add(label2);
        pane2.add(textField2);
        JButton optimizeForMe = new JButton("Optimize for Me");
        optimizeForMe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateComplete();
                showUpdateResult();
                jFrame.dispose();
            }
        });
        pane3.add(optimizeForMe);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateComplete();
                showUpdateResult();
                jFrame.dispose();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        });
        pane4.add(okButton);
        pane4.add(cancelButton);
        jFrame.setVisible(true);
    }

    public void updateComplete(){
        JPanel jPanel = new JPanel();
        JOptionPane.showMessageDialog(jPanel, "Update complete!", "massage",JOptionPane.INFORMATION_MESSAGE);
    }

    public void showUpdateResult() {
        String[][] table = new String[1][6];
        table[0][0] = "black_storm_party";
        table[0][1] = "black_storm_coffee";
        table[0][2] = "on_line_advertising";
        table[0][3] = "2017-7-25";
        table[0][4] = "2017-8-25";
        table[0][5] = "area_A";

        JFrame jFrame = new JFrame();
        String[] title = {"Plan Name", "Product Name", "Promote Method", "Begin Date", "End Date" , "Promote Area"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(900, 75));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Market Schedule of black_storm_party");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        MarketManager marketManager = new MarketManager();
        marketManager.getMarketSchedulesFromDB();
        marketManager.showMarketSchedule();
    }
}
