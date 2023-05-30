package sd2223.trab2.api.rest;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import sd2223.trab2.api.User;

@Path(UsersService.PATH)
public interface UsersService {

	String PWD = "pwd";
	String NAME = "name";
	String QUERY = "query";
	String PATH = "/users";
	
	/**
	 * Creates a new user in the local domain.
	 * @param user User to be created
	 * @return 200 the address of the user (name@domain). 
	 * 		409 if the name already exists. 
	 * 		400 otherwise.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	String createUser(User user);
	
	/**
	 * Obtains the information on the user identified by name
	 * @param name the name of the user
	 * @param pwd password of the user
	 * @return 200 and the user object, if the userId exists and password matches the
	 *         existing password; 
	 *         403 if the password is incorrect; 
	 *         404 if no user exists with the provided name
	 */
	@GET
	@Path("/{" + NAME+ "}")
	@Produces(MediaType.APPLICATION_JSON)
	User getUser(@PathParam(NAME) String name, @QueryParam( PWD ) String pwd);
	
	/**
	 * Modifies the information of a user. Values of null in any field of the user will be 
	 * considered as if the the fields is not to be modified (the name cannot be modified).
	 * @param name the name of the user
	 * @param pwd password of the user
	 * @param user Updated information
	 * @return 200 the updated user object, if the name exists and password matches
	 *         the existing password 
	 *         403 if the password is incorrect 
	 *         404 if no user exists with the provided name 
	 *         400 otherwise.
	 */
	@PUT
	@Path("/{" + NAME+ "}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	User updateUser(@PathParam( NAME ) String name, @QueryParam( PWD ) String pwd, User user);
	
	/**
	 * Deletes the user identified by name
	 * @param name the name of the user
	 * @param pwd password of the user
	 * @return 200 the deleted user object, if the name exists and pwd matches the
	 *         existing password 
	 *         403 if the password is incorrect 
	 *         404 if no user exists with the provided name
	 *         409 otherwise
	 */
	@DELETE
	@Path("/{" + NAME+ "}")
	@Produces(MediaType.APPLICATION_JSON)
	User deleteUser(@PathParam(NAME) String name, @QueryParam(PWD) String pwd);
	
	/**
	 * Returns the list of users for which the pattern is a substring of the name
	 * (of the user), case-insensitive. The password of the users returned by the
	 * query must be set to the empty string "".
	 * 
	 * @param pattern substring to search
	 * @return 200 when the search was successful, regardless of the number of hits
	 *         (including 0 hits). 400 otherwise.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<User> searchUsers(@QueryParam(QUERY) String pattern);
}
