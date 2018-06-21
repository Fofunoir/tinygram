package tinygram;

import java.util.List;

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
import com.google.api.server.spi.config.Named;

@Api(name = "tinygram")
public class UNUSED_Users {
    
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // Méthode de l'API
	@ApiMethod(
			name = "getMembre",
	        path = "isMembre/{name}",
	        httpMethod = HttpMethod.GET
	    )
    public Entity getMembre(@Named("name") String name) throws EntityNotFoundException {
		Query q = new Query("Membre").setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name));
		List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        Entity membre = datastore.get(results.get(0).getKey());
        return membre;
    }
	
	@ApiMethod(
			name = "setMembre",
	        path = "membre/{name}",
	        httpMethod = HttpMethod.POST
	    )
    public void setMembre(@Named("name") String name) {
		Query q = new Query("Membre").setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name));
		List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
		if(results.isEmpty())
		{
			 Entity e = new Entity("Membre");
				e.setProperty("name", name );
				
				e.setProperty("listeMessages", "");
				e.setProperty("listeFollowers", "");
				datastore.put(e);
				
				e.setProperty("IDUser", e.getKey());
				datastore.put(e);
		}
       
    }
}
