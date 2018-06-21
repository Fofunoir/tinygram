package tinygram;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class PopulateServlet extends HttpServlet {

	String NOM_TEST = "HeraChat";
	
	// SERVLET SERVANT POUR LES TESTS.
	static {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Message.class);
        ObjectifyService.register(BigIndex.class);
    }

	// On lance la création d'entités qui seront followers du user rentré. On crée nb (argument "nb") followers, et ils suivront  NOM_TEST+nb
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		BigIndex BI = ofy().load().type(BigIndex.class).id("BIGINDEX").now();
		resp.setContentType("text/html");
		long t1=System.currentTimeMillis();
		
		String nbParam = req.getParameter("nb");
		int nb = Integer.parseInt(nbParam);
		
		User u = new User(NOM_TEST+nb);
		ofy().save().entity(u).now();
		
		// Permet la création des entités followers
		for (int i = 1; i < nb; i++)
		{
			User tmp = new User("friend"+i);
			ofy().save().entity(tmp).now();
			BI.addFollower(u.name, tmp.name);
		}
		
		// Décommenter le bloc pour la réaction en chaine, lançant la création de nb messages pour l'entité NOM_TEST+nb
		/*RequestDispatcher rd = req.getRequestDispatcher("/message?test="+nb+"&message=QLE&name="+NOM_TEST+nb);
		try {
			rd.forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
