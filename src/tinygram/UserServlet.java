package tinygram;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {

	static {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Message.class);
        ObjectifyService.register(BigIndex.class);
    }
	
	
	//Permet de récupérer les informations relatives à un user (argument "name").
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		BigIndex BI = ofy().load().type(BigIndex.class).id("BIGINDEX").now();
		String name = req.getParameter("name");
		resp.setContentType("text/html");
		
		
		User target = ofy().load().type(User.class).id(name).now();
		// Pour éviter de retourner une erreur.
		if(target==null)
		{
			resp.getWriter().println("This ID doesn't exist : "+name);
		}
		else 
		{
			// Ici, on fait appel aux fonctions de BigIndexe
			resp.getWriter().println("ID : "+target.name);
			resp.getWriter().println("ListeMessage : "+BI.toStringMessage(target.name));
			resp.getWriter().println("ListeFollowers : "+BI.toStringFollowers(target.name));
		}
	}
	
	// On ajouter une entité dans le datastore (argument "name").
	// Pas grande utilité pour l'instant
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String name = req.getParameter("name");
		User u = new User(name);
		ofy().save().entity(u).now();
		
	}
}
