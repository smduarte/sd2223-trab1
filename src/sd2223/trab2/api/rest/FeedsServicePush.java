package sd2223.trab2.api.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import sd2223.trab2.api.PushMessage;

public interface FeedsServicePush extends sd2223.trab2.api.rest.FeedsService {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	void push_PushMessage(PushMessage msg);

	@PUT
	@Path("/followers/{" + USERSUB + "}/{" + USER + "}")
	@Consumes(MediaType.APPLICATION_JSON)
	void push_updateFollowers(@PathParam(USERSUB) String user, @PathParam(USER) String follower, boolean following);
}
