
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Jedis jedis = new Jedis("localhost");
        System.out.println(jedis.getClient().getPort());
        System.out.println("连接本地的Redis服务器成功");
        //查看服务是否运行
        System.out.println("服务正在运行：" + jedis.ping());
        String url = "jdbc:mysql://localhost:3306/sys";

        String username = "root";
        String password = "";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);

        Statement statement = connection.createStatement();
        String sql = "select * from session;" ;
        ResultSet resultSet = statement.executeQuery(sql);
        List<Map> maps = ResultSetToMap(resultSet);
        System.out.println(maps);
        resultSet.close();
        statement.close();
        connection.close();


    }
    public static List<Map> ResultSetToMap(ResultSet resultSet) throws SQLException {
        List<Map> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> jsonMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnTypeName = metaData.getColumnTypeName(i);
                String columnName = metaData.getColumnName(i);
                if ("INT".equals(columnTypeName)) {
                    int anInt = resultSet.getInt(columnName);
                    jsonMap.put(columnName, anInt);
                } else {
                    String s = resultSet.getString(columnName);
                    jsonMap.put(columnName, s);
                }
            }
            list.add(jsonMap);
        }
        return list;
    }
}
