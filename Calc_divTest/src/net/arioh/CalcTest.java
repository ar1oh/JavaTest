package net.arioh;

import java.io.*;

public class CalcTest{
    public static void main (String[] args) {
        try{
            DBInteraction dbi = new DBInteraction();
            dbi.clearReport();
            int count = dbi.selectCaseCount();


            for (int step=0; step<count; step++){
                 OutputStream stdin = null;
                 InputStream stderr = null;
                 InputStream stdout = null;
                 String line = null;

                 Process pr = Runtime.getRuntime().exec("calc_div.exe");
                 stdin = pr.getOutputStream ();
                 stdout = pr.getInputStream();

                 dbi.selectCase(step+1);

                 line = dbi.dividend + "\n";
                 stdin.write(line.getBytes() );
                 stdin.flush();
                 line = dbi.divider + "\n";
                 stdin.write(line.getBytes() );
                 stdin.flush();

                 String res = "Fail";
                 String expected = null;
                 BufferedReader brCleanUp =
                     new BufferedReader (new InputStreamReader (stdout));
                 while ((line = brCleanUp.readLine ()) != null) {
                     expected = line;
                 }
                 if (dbi.quotient.equals(expected)) {res = "Pass";}
                 dbi.insertReport(dbi.name,dbi.dividend,dbi.divider,dbi.quotient, expected, res);
                 brCleanUp.close();
            }
            dbi.getReport();
            dbi.getReportToFile();
            }catch (Exception e){
            System.out.println(e);
        }
    }
}