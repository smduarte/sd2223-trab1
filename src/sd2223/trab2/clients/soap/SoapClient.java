package sd2223.trab2.clients.soap;

import static sd2223.trab2.api.java.Result.error;
import static sd2223.trab2.api.java.Result.ok;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import com.sun.xml.ws.client.BindingProviderProperties;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.WebServiceException;
import sd2223.trab2.api.java.Result;
import sd2223.trab2.api.java.Result.ErrorCode;
import utils.Sleep;

/**
* 
* Shared behavior among SOAP clients.
* 
* Holds endpoint information.
* 
* Translates soap responses/exceptions to Result<T> for interoperability.
*  
* @author smduarte
*
*/
abstract public class SoapClient {
	protected static final int READ_TIMEOUT = 10000;
	protected static final int CONNECT_TIMEOUT = 10000;

	protected static final int MAX_RETRIES = 3;
	protected static final int RETRY_SLEEP = 1000;

	private static Logger Log = Logger.getLogger(SoapClient.class.getName());

	protected static final String WSDL = "?wsdl";

	protected final String uri;
	
	public SoapClient(String uri) {
		this.uri = uri;
	}

	protected void setTimeouts(BindingProvider port ) {
		port.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
		port.getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);		
	}

	public <T> Result<T> reTry(ResultSupplier<Result<T>> func) {
		for (int i = 0; i < MAX_RETRIES; i++)
			try {
				return func.get();
			} catch (WebServiceException x) {
				x.printStackTrace();
				Log.fine("Timeout: " + x.getMessage());
				Sleep.ms(RETRY_SLEEP);
			} 	
			catch (Exception x) {
				x.printStackTrace();
				return Result.error(ErrorCode.INTERNAL_ERROR);
			}
		return Result.error(ErrorCode.TIMEOUT);
	}
	
	protected <R> Result<R> toJavaResult(ResultSupplier<R> supplier) {
		try {
			return ok( supplier.get());	
		} 
		catch (WebServiceException x) {
			throw x;
		}
		catch (Exception e) {
			return error(getErrorCodeFrom(e));
		}
	}

	protected <R> Result<R> toJavaResult( VoidSupplier r) {
		try {
			r.run();
			return ok();
		}
		catch (WebServiceException x) {
			throw x;
		}
		catch (Exception e) {
			return error(getErrorCodeFrom(e));
		}
	}

	static private ErrorCode getErrorCodeFrom(Exception e) {
		try {
			return ErrorCode.valueOf( e.getMessage() );			
		} catch( IllegalArgumentException x) {			
			return ErrorCode.INTERNAL_ERROR ;			
		}
	}

	protected static interface ResultSupplier<T> {
		T get() throws Exception;
	}

	static interface VoidSupplier {
		void run() throws Exception;
	}
	
	@Override
	public String toString() {
		return uri.toString();
	}	
	
	public static URL toURL( String url ) {
		try {
			return new URL( url );
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void sleep_ms(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
