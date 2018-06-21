package tinygram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


@SuppressWarnings("serial")
public class UNUSED_TinyGramServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("populating store...");
		
		Random r=new Random();


		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		int maxmessage=250;
		int maxuser=1000;
		for (int i = 0; i < maxmessage; i++) {
			Entity e = new Entity("Message", "m" + i);
			e.setProperty("body", "Hello " + i );
			ArrayList<String> followers = new ArrayList<String>();
			for (int j = 0; j < 100; j++) {
				followers.add("u"+r.nextInt(maxuser+1));
			}
			e.setProperty("followers", followers);
			datastore.put(e);
			resp.getWriter().println("wrote:"+e.getKey());
		}
		resp.getWriter().println("done");
		
	}
}
