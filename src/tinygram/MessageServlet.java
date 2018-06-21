package tinygram;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class MessageServlet extends HttpServlet {

	static {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Message.class);
        ObjectifyService.register(BigIndex.class);
    }
	
	//Permet de récupérer les données d'un message en fournissant son ID (argument "id");
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		BigIndex BI = ofy().load().type(BigIndex.class).id("BIGINDEX").now();
		String idParam = req.getParameter("id");
		Long id = Long.parseLong(idParam);
		
		resp.setContentType("text/html");
		
		Message target = ofy().load().type(Message.class).id(id).now();
		resp.getWriter().println("Corps du message : "+target.body);
		resp.getWriter().println("URL image : "+target.image);
		
	}
	
	//Permet à un user (argument "name") de poster un message (argument "message"), à stocker l'URL de l'image (argument "image")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		BigIndex BI = ofy().load().type(BigIndex.class).id("BIGINDEX").now();
		resp.setContentType("text/html");
		long t1=System.currentTimeMillis();
		String name = req.getParameter("name");
		String msg = URLDecoder.decode(req.getParameter("message"),"UTF-8");
		String image = req.getParameter("image");
		
		//////// Ne sert qu'au cas de test. ///////////
		if(req.getParameter("test") != null)
		{
			for(int i = 0; i < Integer.parseInt(req.getParameter("test")); i++)
			{

				Message m = new Message (msg+i,image);
				ofy().save().entity(m).now();
				BI.writeMessage(name, m.id);
			}
			
			resp.getWriter().println("done in "+(System.currentTimeMillis()-t1));
			resp.sendRedirect("http://1-dot-woven-amulet-194010.appspot.com/timeline?name="+name);
		}
		///////////////////////////////////////////////
		
		// Crée le message, le sauvegarde dans le datastore et alimente l'indexe. 
		else {
			User u = ofy().load().type(User.class).id(name).now();
			Message m = new Message (msg,image);
			ofy().save().entity(m).now();
			BI.writeMessage(name, m.id);
			
		}
		ofy().save().entity(BI).now();
		
	}
	
	
	
	
}
