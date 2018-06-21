package tinygram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.datanucleus.annotations.Unowned;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;

@Api(name = "tinygram")
public class UNUSED_MessageEP{
	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	int MAX_TIMELINE = 5;
	@Inject private HttpServletResponse resp;
	
	@ApiMethod(
			name = "postMessage",
	        path = "postMessage/{name}/",
	        httpMethod = HttpMethod.POST
	    )
    public Entity postMessage(@Named("name") String name, @Named("msg") String body, @Named("img") String img){
		Query q = new Query("Membre").setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name));
		List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
		
		
		try {
			Entity user = datastore.get(results.get(0).getKey());
			
			if(body != null)
			{
				//Message msg = new Message(body, img);
				Entity m = new Entity("Message");
				m.setProperty("body",body);
				m.setProperty("image",img);
				datastore.put(m);
				
				m.setProperty("IDMessage",m.getKey());
				datastore.put(m);
				
				UNUSED_MessageWrapper message = new UNUSED_MessageWrapper(user,m);
				
				addMessageToUser(message);
				//addMessageToFollowers(message);
				
				
				return m;
			}
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	public void addMessageToUser(UNUSED_MessageWrapper msg)
	{
		Entity user = msg.user;
		Entity m = msg.message;
		StringBuilder liste = new StringBuilder("");
		liste.append(user.getProperty("listeMessages").toString());
		liste.append(":");
		liste.append(m.getKey());	
		if(liste.charAt(0) ==':')
			liste.deleteCharAt(0);
		
		user.setProperty("listeMessages", liste.toString());
		datastore.put(user);
	}
	
	public void addMessageToFollowers(UNUSED_MessageWrapper msg)
	{
		Entity user = msg.user;
		Entity m = msg.message;
		String followers = user.getProperty("listeFollowers").toString();
		StringTokenizer tk = new StringTokenizer(followers,":");
		while(tk.hasMoreTokens())
		{
			Key key = KeyFactory.createKey("Membre", tk.nextToken());
			Entity e;
			try {
				e = datastore.get(key);
				UNUSED_MessageWrapper messageCheck = new UNUSED_MessageWrapper(e,m);
				
				addMessageToUser(messageCheck);
			} catch (EntityNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace() ;
			}	
		}
	}
	
	@ApiMethod(
			name = "getTimeline",
	        path = "getTimeline/{name}",
	        httpMethod = HttpMethod.GET
	    )
	public List<Message> getTimeline(@Named("name") String name) throws EntityNotFoundException, IOException{
		List<Entity> listeMsg = new ArrayList<Entity>();
		List<Message> result = new ArrayList<Message>();
		Query q = new Query("Membre").setFilter(new Query.FilterPredicate("name",Query.FilterOperator.EQUAL, name));
		List<Entity> iUser = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
		Key u = KeyFactory.createKey("Membre", name);
		Entity user = iUser.get(0);
		
		String listeMessages = user.getProperty("listeMessages").toString();
		StringTokenizer tk = new StringTokenizer(listeMessages,":");
		int count = MAX_TIMELINE;
		//Key key = KeyFactory.createKey("Message", "707702298738688");
		
		/*Query qq = new Query("Message").setFilter(new Query.FilterPredicate("body",Query.FilterOperator.EQUAL, "coucou!"));
		List<Entity> msgLIST = datastore.prepare(qq).asList(FetchOptions.Builder.withDefaults());
		listeMsg.add(msgLIST.get(0));*/
		
		while(tk.hasMoreTokens() && count >= 0 )
		{
			Key key = KeyFactory.createKey("Message", tk.nextToken());
			try {
				Entity msgEntity = datastore.get(key);
				result.add(new Message(msgEntity.getProperty("body").toString(), msgEntity.getProperty("image").toString()));
				//listeMsg.add(msgEntity);
				//result.add(msgEntity.getProperty("body").toString());
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//result.add(datastore.get(key))
			count--;
		}
		return result;
		/*for (Entity e : listeMsg)
		{
			try {
				resp.getWriter().println("<li>:" + e.getProperty("body").toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}*/
	}
}
