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
public class FollowerServlet extends HttpServlet {

	static {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Message.class);
        ObjectifyService.register(BigIndex.class);
    }
	
	//Permet de récupérer la liste des followers d'un user en fonction d'un argument "name"
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// BI est l'indexe principal. Il est nécessaire de l'actualiser à chaque appel. 
		BigIndex BI = ofy().load().type(BigIndex.class).id("BIGINDEX").now();
		String name = req.getParameter("name");
		resp.setContentType("text/html");
		
		User target = ofy().load().type(User.class).id(name).now();
		resp.getWriter().println("Coucou "+target.name);
		resp.getWriter().println("ListeFollowers : "+BI.toStringFollowers(name));
		
	}
	
	//Permet d'ajouter un follower (argument follower) à un user (argument "name")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		BigIndex BI = ofy().load().type(BigIndex.class).id("BIGINDEX").now();
		String name = req.getParameter("name");
		String followed = req.getParameter("follower");
		
		User u = ofy().load().type(User.class).id(followed).now();
		BI.addFollower(name, followed);
		ofy().save().entity(u).now();
		resp.setContentType("text/html");	
	}
}
