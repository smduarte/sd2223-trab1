package sd2223.trab2.servers.rest;


import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable ex) {

		if (ex instanceof WebApplicationException) {
			return ((WebApplicationException) ex).getResponse();
		}

		ex.printStackTrace();

		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).type(MediaType.APPLICATION_JSON).build();
	}
}