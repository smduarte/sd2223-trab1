package sd2223.trab1.clients.rest;

import static sd2223.trab1.api.java.Result.error;
import static sd2223.trab1.api.java.Result.ErrorCode.NOT_IMPLEMENTED;

import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sd2223.trab1.api.User;
import sd2223.trab1.api.java.Result;
import sd2223.trab1.api.java.Users;
import sd2223.trab1.api.rest.UsersService;


public class RestUsersClient extends RestClient implements Users {

	final WebTarget target;
	
	public RestUsersClient( String serverURI ) {
		super( serverURI );
		target = client.target( serverURI ).path( UsersService.PATH );
	}
	
	private Result<User> clt_getUser(String name, String pwd) {

		Response r = target.path( name )
				.queryParam(UsersService.PWD, pwd).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		return super.toJavaResult(r, User.class);
	}

	private Result<String> clt_createUser(User user) {
		Response r = target
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity( user, MediaType.APPLICATION_JSON));

		return super.toJavaResult(r, String.class);
	}
	
	@Override
	public Result<User> getUser(String name, String pwd) {
		return super.reTry(() -> clt_getUser(name, pwd));
	}
	
	@Override
	public Result<String> createUser(User user) {
		return error(NOT_IMPLEMENTED);
	}
	
	@Override
	public Result<User> updateUser(String userId, String password, User user) {
		return error(NOT_IMPLEMENTED);
	}

	@Override
	public Result<User> deleteUser(String userId, String password) {
		return error(NOT_IMPLEMENTED);
	}

	@Override
	public Result<List<User>> searchUsers(String pattern) {
		return error(NOT_IMPLEMENTED);
	}	
}
