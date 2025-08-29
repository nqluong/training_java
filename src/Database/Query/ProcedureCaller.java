package Database.Query;

import java.sql.*;

public class ProcedureCaller {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/db_training";
        String user = "nqluong";
        String password = "NguyenLuong1405";

        try(Connection conn = DriverManager.getConnection(url, user, password)){
            CallableStatement stmt = conn.prepareCall("call increase_salary_by_dept2(?, ?)");

            int deptId = 3;
            int percentIncrease = 10;
            stmt.setInt(1, deptId);
            stmt.setInt(2, percentIncrease);
            //stmt.registerOutParameter(3, Types.DECIMAL);

            stmt.execute();


        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
