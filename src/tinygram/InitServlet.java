package tinygram;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class InitServlet extends HttpServlet {

	static {
		ObjectifyService.register(User.class);
        ObjectifyService.register(Message.class);
        ObjectifyService.register(BigIndex.class);
    }
	
	// Servlet à lancer au démarrage initial (une seule fois, sinon écrase les données présentes ...)
	// Permet l'initialisation de l'indexe principal. 
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
		
		resp.getWriter().println("Initialisation ...");
		BigIndex BI = new BigIndex("BIGINDEX");
		ofy().save().entity(BI).now();
		resp.getWriter().println("Done !");
		}
		catch(Exception e)
		{
			resp.getWriter().println(e.getMessage());
		}
	}
}
