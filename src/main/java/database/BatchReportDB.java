package database;

import domain.BatchReport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class BatchReportDB {
    private static BatchReport batchReport;
    private ResultSet rs;

    private DatabaseConnection dbConnection;
    private Connection connection;



    public BatchReportDB() {
        dbConnection = new DatabaseConnection();
        connection = dbConnection.getConnection();

    }

    public void createBatchReport(String company, float amountProduced, float amountToProduce, String productType, float speed,
                                  float accepted, float defected, String idleTime, String timeOn, String startTime) {
        try {
            Statement st = connection.createStatement();
            String sql = "INSERT INTO batchreport "
                    + "(company, amountProduced, amountToProduce, productType, speed, accepted, defected, idletime,"
                    + "timeon, starttime) "
                    + "VALUES ('" + company + "'," + amountProduced + "," + amountToProduce + ",'" + productType
                    + "'," + speed + "," + accepted + "," + defected + ",'" + idleTime + "','" + timeOn + "','"
                    + startTime + "')";
            st.executeQuery(sql);
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void deleteaReportinDB(int batchID) {
        
        try {
            Statement st = connection.createStatement();
            String sql = "DELETE FROM batchreport WHERE batchid= " + batchID;
            st.executeUpdate(sql);
            st.close();
            System.out.println("Batch ID: " + batchID + "deleted");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int getBatchID() {
        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                String sql = "SELECT * FROM batchreport";
                rs = st.getGeneratedKeys();
                rs =  st.executeQuery(sql);

                int batchid = 0;
                while (rs.next()) {
                    batchid = rs.getInt("batchid");
                }
                st.close();
                return batchid;

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("BatchReportDB getBatchID(): no connection to database");
        }

        return 0;
    }

    /*
        OEE = (Good Count * Ideal Cycle Time) / Planned Production Time

        Good Count = Total Count - Rejected Count
        Ideal Cycle Time = "... is the theoretical minimum time to produce one piece.", in our case: speed
        Planned Production Time = 8 hours = 28800 seconds

     */

    public int getProducedAmount(int batchID) {
        try {
            Statement st = connection.createStatement();
            String sql = "SELECT amountproduced FROM batchreport WHERE batchid = " + batchID;
            rs = st.executeQuery(sql);
            int amountProduced = 0;
            while (rs.next()) {
                amountProduced = rs.getInt("amountproduced");
            }
            st.close();
            return amountProduced;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    public int getDefective(int batchID) {
        try {
            Statement st = connection.createStatement();
            String sql = "SELECT defected FROM batchreport WHERE batchid = " + batchID;
            rs = st.executeQuery(sql);
            int defective = 0;
            while (rs.next()) {
                defective = rs.getInt("defected");
            }
            st.close();
            return defective;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    public int getSpeed(int batchID) {
        try {
            Statement st = connection.createStatement();
            String sql = "SELECT speed FROM batchreport WHERE batchid = " + batchID;
            rs = st.executeQuery(sql);
            int speed = 0;
            while (rs.next()) {
            // batchID = rs.getInt("batchid");
                speed = rs.getInt("speed");
            }
            st.close();
            return speed;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    public List getReportInfo() {
        List<BatchReport> brList  = new ArrayList<>();
        if (connection != null) {
            try {
                Statement st = connection.createStatement();
                rs = st.executeQuery("SELECT * FROM batchreport");
                while (rs.next()) {
                    brList.add(new BatchReport(rs.getString("company"), rs.getInt("batchid"),
                            rs.getInt("amountproduced"), rs.getInt("amounttoproduce"),
                            rs.getString("producttype"), rs.getInt("speed"),
                            rs.getInt("accepted"), rs.getInt("defected"),
                            rs.getString("idletime"), rs.getString("timeon"),
                            rs.getString("starttime")));
                }
                st.close();

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("No connection to database");
        }
        return brList;

    }

}