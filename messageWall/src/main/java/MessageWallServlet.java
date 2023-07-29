import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Message{
    public String from;
    public String to;
    public String msg;
}
@WebServlet("/message")
public class MessageWallServlet extends HttpServlet{


    //暂时用这个变量保存所有数据,后续可能会用数据库保存数据
    private List<Message> messageList=new ArrayList<>();
    private ObjectMapper objectMapper=new ObjectMapper();


    //客户端(浏览器)向服务器发送请求,请求服务器储存数据
    //服务器将请求解析,将数据储存下来
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Message msg=objectMapper.readValue(req.getInputStream(),Message.class);

        System.out.println(msg.from+msg.to+msg.from);

        save(msg);
        resp.setStatus(200);
    }

    //客户端(浏览器)向服务器发送请求,请求从服务器获取数据
    //将服务器储存的数据转化成相应的格式然后发送响应回浏览器
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        messageList=load();
        objectMapper.writeValue(resp.getWriter(),messageList);
    }

    //往数据库里面储存信息
    Connection connection=null;
    PreparedStatement statement=null;

    ResultSet resultSet=null;
    protected void save(Message message){
        try {
            //1.建立连接
           connection= DBUtil.getConnection();
            //2.写陈述语句
            String sql="insert into message values(?,?,?);";
             statement=connection.prepareStatement(sql);
            statement.setString(1,message.from);
            statement.setString(2,message.to);
            statement.setString(3,message.msg);
            //3.执行sql语句
            statement.executeUpdate();
        } catch (SQLException e) {
         e.printStackTrace();
        }finally {
            //4.断开连接
            DBUtil.close(connection,statement,null);
        }
    }

    //从数据库获得信息
    protected List<Message> load(){
        try {
            //1.建立连接
            connection=DBUtil.getConnection();
            //2.sql语句
            String sql="select * from message;";
            statement=connection.prepareStatement(sql);
            //3.执行sql语句
           resultSet=statement.executeQuery();
           //4.将resultSet里面的数据拿出来存到数组里面
            List<Message> list=new ArrayList<>();
            Message message=new Message();
            while(resultSet.next()){
                message.from=resultSet.getString("from");
                message.to=resultSet.getString("to");
                message.msg=resultSet.getString("msg");
                list.add(message);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }
    }
}
