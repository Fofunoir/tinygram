package tinygram;

import com.googlecode.objectify.annotation.*;

/*
 * Format du message que l'on stockera dans le datastore.
 */

@Entity
@Cache
public class Message {

	@Id Long id;
	String body;
	String image;
	
	public Message() {}
	
	public Message(String body, String image) {
		this.body = body;
		this.image = image;
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
