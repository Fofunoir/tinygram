package tinygram;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Api(name = "tinygram")
public class UNUSED_Followers {

DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@ApiMethod(
	        path = "addFollower/{name}/{follower}",
	        httpMethod = HttpMethod.POST
	    )
	
	public Entity addFollower(@Named("name") String name, @Named("follower") String follower){
		Key k = KeyFactory.createKey("UserID",follower);
		try {
			@SuppressWarnings("unused")
			Entity user = datastore.get(k);
			
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	@ApiMethod(
	        path = "getFollowers/{name}",
	        httpMethod = HttpMethod.GET
	    )
	public Entity getFollowers(@Named("userID") String userID){
		Key k = KeyFactory.createKey("UserID",userID);
		try {
			@SuppressWarnings("unused")
			Entity user = datastore.get(k);
			
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}