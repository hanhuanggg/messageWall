import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        System.out.println(msg.from+msg.msg+msg.to);

        messageList.add(msg);
        resp.setStatus(200);
    }

    //客户端(浏览器)向服务器发送请求,请求从服务器获取数据
    //将服务器储存的数据转化成相应的格式然后发送响应回浏览器
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        objectMapper.writeValue(resp.getWriter(),messageList);
    }

}
