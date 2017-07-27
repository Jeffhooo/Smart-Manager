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
public class RDManager {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_manager";
    private static final String USER = "root";
    private static final String PASS = "01165418";
    private static final Date today = new Date(2017, 7, 22);

    private ArrayList<Feature> popularFeatures;
    private ArrayList<RDSchedule> rdSchedules;

    public RDManager() {
        popularFeatures = new ArrayList<>();
        rdSchedules = new ArrayList<>();
    };

    public void setPopularFeatures(ArrayList<Feature> popularFeatures) {
        this.popularFeatures = popularFeatures;
    }

    public void getRdSchedulesFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "select * from rd_plan";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String productName = rs.getString("product_name");
                int id = rs.getInt("product_id");
                ArrayList<String> features = new ArrayList<>();
                features.add(rs.getString("feature_1"));
                features.add(rs.getString("feature_2"));
                features.add(rs.getString("feature_3"));
                Date beginDate = new Date(rs.getString("develop_begin_time"));
                Date endDate = new Date(rs.getString("develop_finish_time"));
                RDSchedule rdSchedule = new RDSchedule(productName, id, features, beginDate, endDate);
                this.rdSchedules.add(rdSchedule);
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

    public void showRDSchedule() {
        String[][] table = new String[3][7];
        int counter = 0;
        for(RDSchedule rdSchedule : rdSchedules) {
            table[counter][0] = rdSchedule.getProductName();
            table[counter][1] = new Integer(rdSchedule.getId()).toString();
            ArrayList<String> features = rdSchedule.getFeatures();
            table[counter][2] = features.get(0);
            table[counter][3] = features.get(1);
            table[counter][4] = features.get(2);
            table[counter][5] = rdSchedule.getBeginDate().toString();
            table[counter][6] = rdSchedule.getEndDate().toString();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        String[] title = {"Product Name", "Product ID", "Feature 1", "Feature 2", "Feature 3" , "Develop Begin Date", "Develop Finish Date"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(900, 75));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Develop Schedule");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void showPopularFeatures() {
        String[][] table = new String[6][2];
        int counter = 0;
        for(Feature feature : popularFeatures) {
            table[counter][0] = feature.getName();
            table[counter][1] = new Integer(feature.getScore()).toString();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        jFrame.setBounds(0, 150, 400, 200);
        String[] title = {"Feature Name", "Score"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Popular Features");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    private void checkSchedule() {
        for(RDSchedule rdSchedule : rdSchedules) {
            ArrayList<String> features = rdSchedule.getFeatures();
            StringBuilder stringBuilder = new StringBuilder();
            int matchNum = 0;
            for(String feature : features) {
                for(Feature popFeature : popularFeatures) {
                    if(feature.equals(popFeature.getName())) {
                        if(rdSchedule.getBeginDate().getDay() > today.getDay()+7) {
                            ++matchNum;
                            if(matchNum == 1) {
                                stringBuilder.append(feature);
                            } else {
                                stringBuilder.append(", " + feature);
                            }
                        }
                    }
                }
            }
            if(matchNum != 0) {
                sendOptimizeAdvise(rdSchedule, stringBuilder.toString());
            }
        }
    }

    public void sendOptimizeAdvise(RDSchedule rdSchedule, String features) {
        String massageBasic = "Your development of '%s'is highly matched the popular feature: '%s'. " +
                "But your begin date is a week later. Do you want to change this plan?";
        String massage = String.format(massageBasic, rdSchedule.getProductName(), features);
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

    public void updateComplete(){
        JPanel jPanel = new JPanel();
        JOptionPane.showMessageDialog(jPanel, "Update complete!", "massage",JOptionPane.INFORMATION_MESSAGE);
    }

    public void showUpdateResult() {
        String[][] table = new String[1][7];
        table[0][0] = "pink_storm_coffee";
        table[0][1] = "3582";
        table[0][2] = "strawberry";
        table[0][3] = "ice_cream";
        table[0][4] = "cold";
        table[0][5] = "2017-7-26";
        table[0][6] = "2017-8-10";

        JFrame jFrame = new JFrame();
        String[] title = {"Product Name", "Product ID", "Feature 1", "Feature 2", "Feature 3" , "Develop Begin Date", "Develop Finish Date"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(900, 75));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Develop Schedule of pink_storm_coffee");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        RDManager rdManager = new RDManager();
        rdManager.getRdSchedulesFromDB();
        rdManager.showRDSchedule();
    }
}
