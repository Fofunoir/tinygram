package tinygram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.*;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class UNUSED_TinyGramTimeline extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		

		String user=req.getParameter("user");
		if (user==null) {user="u416";}
		resp.getWriter().println("getting timeline for:"+user);

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		
		Filter pf = new FilterPredicate("followers", FilterOperator.EQUAL,user);
		Query q = new Query("Message").setFilter(pf);

		PreparedQuery pq = ds.prepare(q);
		
		long t1=System.currentTimeMillis();
		
		List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(100));

		resp.getWriter().println("<h1>query</h1>");

		for (Entity r : results) {
			resp.getWriter().println("<li>:" + r.getProperty("body"));
		}

		resp.getWriter().println("done in "+(System.currentTimeMillis()-t1));

	}
}
