package other;

import java.io.Serializable;

/**
 * @author Umut Gokbayrak
 *
 */
public class ConnectionProfile implements Serializable{
	private String shortName;
	private String fetchServer;
	private String fetchPort;
	private String protocol;
	private String smtpServer;
	private String smtpPort;
	private String smtpAuthenticated;
    
    /**
	 * Default constructor
	 */
    public ConnectionProfile() {
        super();
    }

    /**
     * @return Returns the fetchPort.
     */
    public int getIFetchPort() {
        return Integer.parseInt(fetchPort);
    }

    /**
     * @return Returns the fetchServer.
     */
    public String getFetchServer() {
        return fetchServer;
    }
    /**
     * @param fetchServer The fetchServer to set.
     */
    public void setFetchServer(String fetchServer) {
        this.fetchServer = fetchServer;
    }
    /**
     * @return Returns the smtpPort.
     */
    public int getISmtpPort() {
        return Integer.parseInt(smtpPort);
    }

    /**
     * @return Returns the smtpServer.
     */
    public String getSmtpServer() {
        return smtpServer;
    }
    /**
     * @param smtpServer The smtpServer to set.
     */
    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }
    /**
     * @return Returns the protocol.
     */
    public String getProtocol() {
        return protocol;
    }
    /**
     * @param protocol The protocol to set.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
	/**
	 * @return
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param string
	 */
	public void setShortName(String string) {
		shortName = string;
	}

	/**
	 * @return
	 */
	public String getFetchPort() {
		return fetchPort;
	}

	/**
	 * @return
	 */
	public String getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param string
	 */
	public void setFetchPort(String string) {
		fetchPort = string;
	}

	/**
	 * @param string
	 */
	public void setSmtpPort(String string) {
		smtpPort = string;
	}

	/**
	 * @return
	 */
	public String getSmtpAuthenticated() {
		return smtpAuthenticated;
	}

	/**
	 * @param string
	 */
	public void setSmtpAuthenticated(String string) {
		smtpAuthenticated = string;
	}

}
