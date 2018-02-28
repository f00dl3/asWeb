/*
by Anthony Stump
Created: 10 Feb 2018
Updated: 18 Feb 2018
For legacy support of non-RESTful API calls
*/

package asWeb.servlet;

import asWebRest.action.GetWebAccessLogAction;
import asWebRest.action.GetWebUIserAuthAction;
import asWebRest.dao.WebAccessLogDAO;
import asWebRest.dao.WebUIserAuthDAO;
import asWebRest.shared.WebCommon;
import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.json.JSONArray;

public class Controller extends HttpServlet {
    
    private DataSource dataSource;
    
    @Override
    public void init() {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Core");
        } catch (NamingException ne) {
            ne.printStackTrace();
        }
    }
    
    @Override
    public void doGet(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    public void doPost(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String uri = request.getRequestURI();
        int lastIndex = uri.lastIndexOf("/");
        String action = uri.substring(lastIndex + 1);
        
        String userName = "";
        String hashWord = "";
        String dispatchUrl = null;
        
        boolean redirect = false;
        boolean loginCheck = false;
        
        switch(action) {
           
            case "Anthony":
                dispatchUrl = "/Anthony.jsp";
                break;

            default:
                dispatchUrl = "/login.jsp";
                try { userName = request.getParameter("asUser"); } catch (Exception e) { e.printStackTrace(); }
                if(WebCommon.isSet(hashWord)) { try { hashWord = WebCommon.cryptIt(request.getParameter("asPass")); } catch (Exception e) { e.printStackTrace(); } }
                GetWebUIserAuthAction getWebUIserAuthAction = new GetWebUIserAuthAction(new WebUIserAuthDAO());
                GetWebAccessLogAction getWebAccessLogAction = new GetWebAccessLogAction(new WebAccessLogDAO());
                String webUIserAuth = getWebUIserAuthAction.getWebUIserAuth(userName);
                if(webUIserAuth.equals(hashWord) && WebCommon.isSet(hashWord)) { loginCheck = true; }
                JSONArray webAccessLogs = getWebAccessLogAction.getWebAccessLogs();
                request.setAttribute("webAccessLogs", webAccessLogs.toString());               
                request.setAttribute("loginCheck", loginCheck);
                request.setAttribute("userCrypt", hashWord);
                break;

        }

        if (dispatchUrl != null) {
                RequestDispatcher rd = request.getRequestDispatcher(dispatchUrl);
                rd.forward(request, response);
        }
            
    }
}
