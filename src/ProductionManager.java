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
public class ProductionManager {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_manager";
    private static final String USER = "root";
    private static final String PASS = "01165418";

    private String area;
    private ArrayList<ProductionSchedule> productionSchedules;
    private ArrayList<Ingredient> ingredientStorageList;

    public ProductionManager(String area) {
        this.area = area;
        this.productionSchedules = new ArrayList<>();
        this.ingredientStorageList = new ArrayList<>();
    }

    public void getProductionScheduleFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "select * from production_plan";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String ingredientName = rs.getString("ingredient_name");
                System.out.println(ingredientName);
                int produceNumber = rs.getInt("produce_number");
                Date beginDate = new Date(rs.getString("produce_begin_time"));
                Date endDate = new Date(rs.getString("produce_finish_time"));
                ProductionSchedule productionSchedule = new ProductionSchedule(ingredientName, produceNumber, beginDate, endDate);
                this.productionSchedules.add(productionSchedule);
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

    public void getIngredientStorageList(ArrayList<Ingredient> IngredientStorageList) {
        this.ingredientStorageList = IngredientStorageList;
    }

    public void showProductionSchedule() {
        String[][] table = new String[3][4];
        int counter = 0;
        for(ProductionSchedule productionSchedule : productionSchedules) {
            table[counter][0] = productionSchedule.getIngredientName();
            table[counter][1] = new Integer(productionSchedule.getProduceNumber()).toString();
            table[counter][2] = productionSchedule.getBeginDate().toString();
            table[counter][3] = productionSchedule.getEndDate().toString();
            ++counter;
        }

        JFrame jFrame = new JFrame();
        String[] title = {"Ingredient Name", "Produce Number", "Produce Begin Date", "Produce End Date"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(900, 75));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Production Schedule");
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
        jFrame.setTitle("Ingredient Storage of Area_A");
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
        for(ProductionSchedule productionSchedule : productionSchedules) {
            for(Ingredient ingredient : ingredientStorageList) {
                if(productionSchedule.getIngredientName().equals(ingredient.getIngredientName())
                        && productionSchedule.getProduceNumber() < 150)
                sendOptimizeAdvise(productionSchedule);
            }
        }
    }

    public void sendOptimizeAdvise(ProductionSchedule productionSchedule) {
        String massageBasic = "The ingredient '%s'is in shortage. " +
                "But your produce plan is less than 150. Do you want to change this plan?";
        String massage = String.format(massageBasic, productionSchedule.getIngredientName());
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
        JLabel label1 = new JLabel("Produce Number: ");
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
        String[][] table = new String[1][4];
        table[0][0] = "chocolate";
        table[0][1] = "200";
        table[0][2] = "2017-7-23";
        table[0][3] = "2017-7-27";

        JFrame jFrame = new JFrame();
        String[] title = {"Ingredient Name", "Produce Number", "Produce Begin Date", "Produce End Date"};
        JTable jTable = new JTable(table, title);
        jTable.setPreferredScrollableViewportSize(new Dimension(900, 75));
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        jFrame.setTitle("Production Schedule of chocolate");
        jFrame.pack();
        jFrame.setVisible(true);
    }
    public static void main(String[] args) {
        ProductionManager productionManager = new ProductionManager("area_A");
        productionManager.getProductionScheduleFromDB();
        productionManager.showProductionSchedule();
    }
}
