import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Test")
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 5589752892736045780L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = "Meu deus socorr";
        req.setAttribute("message", message);        
        req.getRequestDispatcher("hello.jsp").forward(req, resp);
    }
    
}
