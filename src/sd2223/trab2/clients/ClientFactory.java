package sd2223.trab2.clients;

import java.net.URI;
import java.util.function.Function;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import sd2223.trab2.api.java.Result;
import sd2223.trab2.discovery.Discovery;


public class ClientFactory<T> {

	private static final String REST = "/rest";
	private static final String SOAP = "/soap";

	private final String serviceName;
	private final Function<String, T> restClientFunc;
	private final Function<String, T> soapClientFunc;
	
	ClientFactory( String serviceName, Function<String, T> restClientFunc, Function<String, T> soapClientFunc) {
		this.restClientFunc = restClientFunc;
		this.soapClientFunc = soapClientFunc;
		this.serviceName = serviceName;
	}
	
	private T newClient( String serverURI ) {
		if (serverURI.endsWith(REST))
			return restClientFunc.apply( serverURI );
		else if (serverURI.endsWith(SOAP))
			return soapClientFunc.apply( serverURI );
		else
			throw new RuntimeException("Unknown service type..." + serverURI);	
	}
	
	LoadingCache<URI, T> clients = CacheBuilder.newBuilder()
			.build(new CacheLoader<>() {
				@Override
				public T load(URI uri) throws Exception {
					return newClient( uri.toString() );
				}
			});
	
	
	public T get(String domain) {
		var uris = Discovery.getInstance().knownUrisOf(domain + ":" + serviceName, 1);
		return get(uris[0]);
	}
	
	public T get(URI uri) {
		try {
			return clients.get(uri);
		} catch (Exception x) {
			x.printStackTrace();
			throw new RuntimeException( Result.ErrorCode.INTERNAL_ERROR.toString());
		}
	}	
}
