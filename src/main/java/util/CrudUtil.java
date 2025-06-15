package util;

import db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CrudUtil {
    public static <X> X execute(String sql,Object... args) throws SQLException {
        PreparedStatement psTm = DBConnection.getInstance().getConnection().prepareStatement(sql);

        for(int i = 0; i<args.length; i++){
            psTm.setObject((i+1),args[i]);
        }
        if(sql.startsWith("SELECT")||sql.startsWith("select")){
           return (X) psTm.executeQuery();
        }
        return (X) (Boolean) (psTm.executeUpdate()>0);

    }

//    public static void addNumber(Object... numbers){
//        for (int i = 0; i < numbers.length; i++) {
//            System.out.println(numbers[i]);
//        }
//    }

}
