package tinygram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.*;

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
public class UNUSED_TinyGramIndexQuery extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");

		String user = req.getParameter("user");
		if (user == null) {
			user = "u172";
		}
		resp.getWriter().println("getting index timeline for:" + user);

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Filter pf = new FilterPredicate("followers", FilterOperator.EQUAL, user);
		Query q = new Query("MessIndex").setFilter(pf);
		q.setKeysOnly();

		PreparedQuery pq = ds.prepare(q);

		resp.getWriter().println("<h1>query</h1>");
		long t1 = System.currentTimeMillis();
		List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(100));
		List<Key> pk = new ArrayList<Key>();
		for (Entity r : results) {
			pk.add(r.getParent());
		}
		
		Map<Key, Entity> hm = new HashMap<Key, Entity>();
		hm = ds.get(pk);

		for (Entity ki : hm.values()) {
			resp.getWriter().println("<li>:"+ki.getProperty("body"));
		}

		resp.getWriter()
				.println("done in " + (System.currentTimeMillis() - t1));

	}
}
