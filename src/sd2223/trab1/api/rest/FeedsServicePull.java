package sd2223.trab1.api.rest;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import sd2223.trab1.api.Message;

public interface FeedsServicePull extends FeedsService {

	@GET
	@Path("/personal/{" + USER + "}")
	@Produces(MediaType.APPLICATION_JSON)
	List<Message> pull_getTimeFilteredPersonalFeed(@PathParam(USER) String user, @QueryParam(TIME) long time);
}
