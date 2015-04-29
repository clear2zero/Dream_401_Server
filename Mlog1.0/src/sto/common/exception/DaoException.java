package sto.common.exception;


import org.apache.log4j.Logger;

/**
 * Dao层异常
 * @date 2011/04/07
 */
public class DaoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8801718407102159973L;

	private Logger log = null;

	@SuppressWarnings("unchecked")
	public DaoException(Class clazz, String message) {
		super(message);
		log = Logger.getLogger(clazz);
		log.error(message);
	}
	
	@SuppressWarnings("unchecked")
	public DaoException(Class clazz, Throwable throwable) {
		super(throwable);
		log = Logger.getLogger(clazz);
		log.error(throwable.getMessage());
	}
}
