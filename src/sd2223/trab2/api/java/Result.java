package sd2223.trab2.api.java;

/**
 * 
 * Represents the result of an operation, either wrapping a result of the given type,
 * or an error.
 * 
 * @author smduarte
 *
 * @param <T> type of the result value associated with success
 */
public interface Result<T> {

	/**
	 * 
	 * @author smd
	 *
	 * Service errors:
	 * OK - no error, implies a non-null result of type T, except for for Void operations
	 * CONFLICT - something is being created but already exists
	 * NOT_FOUND - an access occurred to something that does not exist
	 * INTERNAL_ERROR - something unexpected happened
	 */
	enum ErrorCode{ OK, CONFLICT, NOT_FOUND, BAD_REQUEST, FORBIDDEN, INTERNAL_ERROR, REDIRECTED, NOT_IMPLEMENTED, TIMEOUT, NO_CONTENT};
	
	/**
	 * Tests if the result is an error.
	 */
	boolean isOK();
	
	/**
	 * obtains the payload value of this result
	 * @return the value of this result.
	 */
	T value();

	/**
	 *
	 * obtains the error code of this result
	 * @return the error code
	 * 
	 */
	ErrorCode error();
	
	/**
	 * obtains the payload value of this result
	 * @return the value of this result.
	 */
	<P> P errorValue();
	
	/**
	 * Convenience method for returning non error results of the given type
	 * @param Class of value of the result
	 * @return the value of the result
	 */
	static <T> Result<T> ok( T result ) {
		return new OkResult<>(result);
	}

	/**
	 * Convenience method for returning non error results without a value
	 * @return non-error result
	 */
	static <T> Result<T> ok() {
		return new OkResult<>(null);	
	}
	
	/**
	 * Convenience method used to return an error 
	 * @return
	 */
	static <T> Result<T> error(ErrorCode error) {
		return new ErrorResult<>(error);		
	}
	
	/**
	 * Convenience method used to return an error 
	 * @return
	 */
	static <T> Result<T> error(ErrorCode error, Object errorValue) {
		return new ErrorResult<>(error, errorValue);		
	}
	
	/**
	 * Convenience method used to return an redirect result 
	 * @return
	 */
	static <T> Result<T> redirected(Result<T> res) {
		System.err.println(">>>>>>>>>>>" + res );
		if( res.isOK())
			return error(ErrorCode.REDIRECTED, res.value());		
		else
			return res;
	}
}

/*
 * 
 */
class OkResult<T> implements Result<T> {

	final T result;
	
	OkResult(T result) {
		this.result = result;
	}
	
	@Override
	public boolean isOK() {
		return true;
	}

	@Override
	public T value() {
		return result;
	}

	@Override
	public ErrorCode error() {
		return ErrorCode.OK;
	}
	
	public String toString() {
		return "(OK, " + value() + ")";
	}

	@Override
	public <P> P errorValue() {
		throw new RuntimeException("Attempting to extract the error value of an OK result: " + error());
	}
}

class ErrorResult<T> implements Result<T> {

	final Object errorValue;
	final ErrorCode error;
	
	ErrorResult(ErrorCode error) {
		this.error = error;
		this.errorValue = null;
	}
	
	ErrorResult(ErrorCode error, Object errorValue) {
		this.error = error;
		this.errorValue = errorValue;
	}
	@Override
	public boolean isOK() {
		return false;
	}

	@Override
	public T value() {
		if( error == ErrorCode.REDIRECTED)
			return errorValue();
		throw new RuntimeException("Attempting to extract the value of an Error: " + error());
	}

	@SuppressWarnings("unchecked")
	public <Q> Q errorValue() {
		return (Q)errorValue; 
	}

	
	@Override
	public ErrorCode error() {
		return error;
	}
	
	public String toString() {
		return "(" + error() + ")";		
	}
}