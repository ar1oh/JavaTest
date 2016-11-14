package net.arioh;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class DBInteraction {


    private Connection connect() {
        String url = "jdbc:sqlite:jdbc_test.s3db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

     String name = null;
     String dividend = null;
     String divider = null;
     String quotient = null;
     int count = 0;


    public void selectAllCases(){
        String sql = "SELECT id, dividend, divider, quotient FROM testcases";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

             while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("dividend") + "\t" +
                        rs.getString("divider") + "\t" +
                        rs.getString("quotient"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int selectCaseCount(){
        String sql = "SELECT COUNT (*) AS MYCOUNT FROM testcases";
        int res = 0;
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql)){

            ResultSet rs  = pstmt.executeQuery();
            res = rs.getInt("MYCOUNT");
        } catch (SQLException e) {
             System.out.println(e.getMessage());
        }
        return res;
    }

    public void selectCase(int id){
        String sql = "SELECT name, dividend, divider, quotient FROM testcases WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,id);
            ResultSet rs  = pstmt.executeQuery();

            name = rs.getString("name");
            dividend = rs.getString("dividend");
            divider = rs.getString("divider");
            quotient = rs.getString("quotient");
            } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clearReport() {
        String sql = "DELETE FROM report";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertReport(String name, String dividend, String divider, String expected,
                             String received, String result) {
        String sql = "INSERT INTO report(name,dividend,divider,expected,received,result) VALUES(?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setString(1, name);
             pstmt.setString(2, dividend);
             pstmt.setString(3, divider);
             pstmt.setString(4, expected);
             pstmt.setString(5, received);
             pstmt.setString(6, result);
             pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getReport(){
        String sql = "SELECT id, name, dividend, divider, expected, received, result FROM report";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             int pass= 0;
             int fail = 0;
             int cases = 0;
             while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\n" + "Dividend: " +
                        rs.getString("dividend") + "\n" + "Divider: " +
                        rs.getString("divider") + "\n" + "Expected: " +
                        rs.getString("expected") + "\n" + "Received: " +
                        rs.getString("received") + "\n" + "Result: " +
                        rs.getString("result") + "\n");
                 cases++;
                 if (rs.getString("result").equals("Pass")) {pass++;}
                 else fail++;
            }
            System.out.println("Pass: "+pass+"\t"+"Fail: "+fail+"\t"+"Number of test cases: "+cases+
                        "\n"+"Test successful coverage: "+pass*100/cases+"%");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getReportToFile(){
        String sql = "SELECT id, name, dividend, divider, expected, received, result FROM report";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            FileWriter wr = new FileWriter("report.txt", false);
            int pass= 0;
            int fail = 0;
            int cases = 0;
            while (rs.next()) {
                wr.write("\n"+rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\n" + "Dividend: " +
                        rs.getString("dividend") + "\n" + "Divider: " +
                        rs.getString("divider") + "\n" + "Expected: " +
                        rs.getString("expected") + "\n" + "Received: " +
                        rs.getString("received") + "\n" + "Result: " +
                        rs.getString("result") + "\n");
                cases++;
                if (rs.getString("result").equals("Pass")) {pass++;}
                else fail++;
            }
            wr.write("\n"+"Pass: "+pass+"\t"+"Fail: "+fail+"\t"+"Number of test cases: "+cases+
                    "\n"+"Test successful coverage: "+pass*100/cases+"%");
            wr.flush();
        } catch (IOException|SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

