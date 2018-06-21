package tinygram;

import com.google.appengine.api.datastore.Entity;

public class UNUSED_MessageWrapper {

	public Entity user;
	public Entity message;
	
	UNUSED_MessageWrapper(Entity u, Entity m)
	{
		this.user = u;
		this.message = m;
	}
}
