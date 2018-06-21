package tinygram;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.StringTokenizer;

import com.googlecode.objectify.annotation.*;

/*
 * Format de l'utilisateur que l'on stockera dans le datastore.
 * Prévu pour améliorations potentielles. 
 */

@Entity
public class User {
	
	@Id String name;
	
	public User() {}
	
	public User(String name) 
	{
		this.name = name;
	}
	
	public User(String name, String ListeMessages, String ListeFollowers) 
	{
		this.name = name;
	}
}
