package tinygram;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class TimelineServlet extends HttpServlet{
	int MAX_TIMELINE = 100;
	
	static {
		ObjectifyService.register(User.class);
        ObjectifyService.register(Message.class);
        ObjectifyService.register(BigIndex.class);
    }
	
	// Permet de récupérer les messages de l'user (argument "name"). On peut ajouter l'argument "max" pour décider du nombre voulu. Par défaut, MAX_TIMELINE 
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		BigIndex BI = ofy().load().type(BigIndex.class).id("BIGINDEX").now();
		long t1=System.currentTimeMillis();

		resp.setContentType("text/html");
		
		String name = req.getParameter("name");

		HashSet<Long> lMessage = BI.getListMessage(name);
		// Dans le cas où la timeline est vide : l'utilisateur n'a jamais posté de message.
		if(lMessage == null)
		{
			resp.getWriter().println("Timeline : " + name);
			resp.getWriter().println("Empty...");
		}
		else
		{
			int count = 0;
			if(req.getParameter("max")==null)
			{
				count = MAX_TIMELINE;
			}
			else
			{
				count = Integer.parseInt(req.getParameter("max"));
			}
			resp.getWriter().println("Timeline : " + name);
			
			//Deux variables utilisées pour récupérer les derniers messages (simulation des messages "les plus récents").
			// Il y'aura des problèmes de scalabilité ici, il faut envisager de traiter autrement les messages ...
			int index = lMessage.size();
			long result[] = new long[count];
			for(Long id : lMessage)
			{
				if(index<=count)
				{
					result[index-1] = id;
				}
					index--;
								
			}
			
			// On affichera les derniers messages.
			for(int i = 0; i<result.length;i++)
			{
				Message m = ofy().load().type(Message.class).id(result[i]).now();
				resp.getWriter().println("<message>:" + m.body + "   <image>:"+m.image);
			}
		}
		
		
		resp.getWriter().println("done in "+(System.currentTimeMillis()-t1));
	}
}
