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
public class DistributionManager {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_manager";
    private static final String USER = "root";
    private static final String PASS = "01165418";

    private String areaName;
    private ArrayList<DistributionSchedule> distributionSchedules;
    private ArrayList<Ingredient> ingredientStorageList;

    public DistributionManager(String areaName) {
        this.areaName = areaName;
        this.distributionSchedules = new ArrayList<>();
        this.ingredientStorageList = new ArrayList<>();
    }

    public void getDistributionSchedulesFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "select * from distribution_plan";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String ingredientName = rs.getString("ingredient_name");
                int storeNumber = rs.getInt("store_number");
                Date shipInTime = new Date(rs.getString("ship_in_time"));
                Date shipOutTime = new Date(rs.getString("ship_out_time"));
                String destination = rs.getString("destination");
                DistributionSchedule distributionSchedule
                        = new DistributionSchedule(ingredientName, storeNumber, shipInTime, shipOutTime, destination);
                this.distributionSchedules.add(distributionSchedule);
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

    public void setIngredientStorageList(ArrayList<Ingredient> ingredientStorageList) {
        this.ingredientStorageList = ingredientStorageList;
    }

    public void showDistributionSchedule() {
        String[][] table = new String[12][5];
        int counter = 0;
        for(DistributionSchedule distributionSchedule : distributionSchedules) {
            table[counter][0] = distributionSchedule.getIngredientName();
            table[counter][1] = new Integer(distributionSchedule.getStoreNumber()).toString();
            table[counter][2] = distributionSchedule.getShipInDate().toString();
            table[counter][3] = distributionSchedule.getShipOutDate().toString();
            table[counter][4] = distributionSchedule.getDestination();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        String[] title = {"Ingredient Name", "Store Number", "Ship In Date", "Ship Out Date", "Destination"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(900, 200));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Distribution Schedule");
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void showIngredientStorage() {
        String[][] table = new String[5][2];
        int counter = 0;
        for(Ingredient ingredient : ingredientStorageList) {
            table[counter][0] = ingredient.getIngredientName();
            table[counter][1] = new Integer(ingredient.getStore_number()).toString();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        jFrame.setBounds(0, 150, 400, 200);
        String[] title = {"Ingredient Name", "Store Number"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Ingredient Storage of Shop_J");
        jFrame.pack();
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

    public void checkSchedule() {
        for(DistributionSchedule distributionSchedule : distributionSchedules) {
            for(Ingredient ingredient : ingredientStorageList) {
                if(distributionSchedule.getIngredientName().equals(ingredient.getIngredientName())
                        && distributionSchedule.getStoreNumber() < 100)
                    sendOptimizeAdvise(distributionSchedule);
            }
        }
    }

    public void sendOptimizeAdvise(DistributionSchedule distributionSchedule) {
        String massageBasic = "The ingredient '%s'is in shortage in '%s'. " +
                "But your distribution plan is less than 100. Do you want to change this plan?";
        String massage = String.format(massageBasic, distributionSchedule.getIngredientName(), distributionSchedule.getDestination());
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
        JPanel pane5 = new JPanel();
        contentPane.add(pane5);
        JLabel label1 = new JLabel("Number: ");
        JTextField textField1 = new JTextField();
        textField1.setColumns(10);
        pane1.add(label1);
        pane1.add(textField1);
        JLabel label2 = new JLabel("Begin Date: ");
        JTextField textField2=new JTextField();
        textField2.setColumns(10);
        pane2.add(label2);
        pane2.add(textField2);
        JLabel label3 = new JLabel("Finish Date: ");
        JTextField textField3=new JTextField();
        textField3.setColumns(10);
        pane3.add(label3);
        pane3.add(textField3);
        JButton optimizeForMe = new JButton("Optimize for Me");
        optimizeForMe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateComplete();
                showUpdateResult();
                jFrame.dispose();
            }
        });
        pane4.add(optimizeForMe);
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
        pane5.add(okButton);
        pane5.add(cancelButton);
        jFrame.setVisible(true);
    }

    public void updateComplete(){
        JPanel jPanel = new JPanel();
        JOptionPane.showMessageDialog(jPanel, "Update complete!", "massage",JOptionPane.INFORMATION_MESSAGE);
    }

    public void showUpdateResult() {
        String[][] table = new String[1][5];
        table[0][0] = "chocolate";
        table[0][1] = "150";
        table[0][2] = "2017/7/23";
        table[0][3] = "2017-7-27";
        table[0][4] = "shop_J";

        JFrame jFrame = new JFrame();
        String[] title = {"Ingredient Name", "Store Number", "Ship In Date", "Ship Out Date", "Destination"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(900, 200));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Distribution Schedule of chocolate for shop_J");
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
