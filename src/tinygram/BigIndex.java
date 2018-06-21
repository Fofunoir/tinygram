package tinygram;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Map;


import com.googlecode.objectify.annotation.*;

@Entity
@Cache
public class BigIndex implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id String id;
	@Serialize Map<String, LinkedHashSet<Long>> mapMessages;
	@Serialize Map<String, LinkedHashSet<String>> mapFollowers;
	
	public BigIndex()
	{
		
	}
	
	public BigIndex(String id)
	{
		this.id = id;
		// LinkedHashSet permet une plus grande réactivité. Les maps permettent de stocker l'indexe. 
		this.mapMessages = new HashMap<String, LinkedHashSet<Long>>(10000);
		this.mapFollowers = new HashMap<String, LinkedHashSet<String>>(10000);
	}
	
	// Fonction d'ajout de message en base ainsi qu'auprès des followers.
	public void writeMessage(String user,Long id)
	{
		//Si l'utilisateur n'à jamais posté de message
		if(mapMessages.get(user)==null)
		{
			//Si l'utilisateur n'a pas de followers
			if(mapFollowers.get(user)==null)
			{
				LinkedHashSet<Long> m = new LinkedHashSet<Long>();
				m.add(id);
				this.mapMessages.put(user, m);
			}
			//Si l'utilisateur a des followers, on itère sur la liste de followers
			else
			{
				LinkedHashSet<Long> m = new LinkedHashSet<Long>();
				m.add(id);
				this.mapMessages.put(user, m);
				
				LinkedHashSet<String> fol = mapFollowers.get(user);
				Iterator<String> t = fol.iterator();
				while(t.hasNext())
				{
					
					String key = t.next().toString();
					LinkedHashSet<Long> tmp = mapMessages.get(key);
					// Si le follower n'a jamais posté de message
					if(tmp==null)
					{
						tmp = new LinkedHashSet<Long>();
						tmp.add(id);
						mapMessages.put(key, tmp);
					}
					// Si le follower a déjà posté un message.
					else
					{
						tmp.add(id);
						mapMessages.put(key, tmp);
					}
				}
				
			}
			
		}
		//Sinon, si l'utilisateur a déjà posté un message
		else 
		{
			//Si l'utilisateur n'a pas de followers
			if(mapFollowers.get(user)==null)
			{
				LinkedHashSet<Long> l = mapMessages.get(user);
				l.add(id);
				mapMessages.put(user, l);
				
			}
			//Si l'utilisateur a des followers, il faut écrire les messages dans leurs indexes.
			else 
			{
				LinkedHashSet<Long> l = mapMessages.get(user);
				l.add(id);
				mapMessages.put(user, l);
				
				LinkedHashSet<String> fol = mapFollowers.get(user);
				Iterator<String> t = fol.iterator();
				while(t.hasNext())
				{
					String key = t.next().toString();
					LinkedHashSet<Long> tmp = mapMessages.get(key);
					// Si le follower n'a jamais posté de message
					if(tmp==null)
					{
						tmp = new LinkedHashSet<Long>();
						tmp.add(id);
						mapMessages.put(key, tmp);
					}
					// Si le follower a déjà posté un message.
					else
					{
						tmp.add(id);
						mapMessages.put(key, tmp);
					}
				}
			}
			
		}
		
	}
	
	// Fonction d'ajout d'un follower. User 
	public void addFollower(String user,String followed)
	{
		if(mapFollowers.get(user)==null)
		{
			LinkedHashSet<String> l = new LinkedHashSet<String>();
			l.add(followed);
			mapFollowers.put(user, l);
		}
		else 
		{
			LinkedHashSet<String> l = mapFollowers.get(user);
			l.add(followed);
			mapFollowers.put(user, l);
		}
		ofy().save().entity(this).now();
	}
	
	// Dans le cas où récupérer la liste est nécessaire
	public LinkedHashSet<Long> getListMessage(String user)
	{
		return mapMessages.get(user);

	}
	
	// Dans le cas où récupérer la liste est nécessaire
	public LinkedHashSet<String> getListFollowers(String user)
	{
		return mapFollowers.get(user);

	}
	
	// Permet d'afficher la liste des messages d'un utilisateur
	public String toStringMessage(String user)
	{
		try{
		StringBuilder liste = new StringBuilder("");
		Iterator<Long> it = this.mapMessages.get(user).iterator();
		while(it.hasNext())
		{
			liste.append(":");
			liste.append(it.next().toString());
			
		}
		if(liste.charAt(0) ==':')
			liste.deleteCharAt(0);
		
		return liste.toString();
		}
		
		catch(Exception e){return e.getMessage();}
	}
	
	// Permet d'afficher la liste des followers d'un utilisateur
	public String toStringFollowers(String user)
	{
		try 
		{
		StringBuilder liste = new StringBuilder("");
		Iterator<String> it = this.mapFollowers.get(user).iterator();
		while(it.hasNext())
		{
			liste.append(":");
			liste.append(it.next().toString());
			
		}
		if(liste.charAt(0) ==':')
			liste.deleteCharAt(0);
		return liste.toString();
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
	}
}