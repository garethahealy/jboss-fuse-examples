package com.garethahealy.wssecurity.https.cxf.client.config.impl;


public class WsEndpointConfiguration<T> {

	private boolean isCxfDebug;
	private String certifactionAlias;
	private String wsAddress;
	private Class<T> serviceClass;
	private String pathToKeystore; 
	private String pathToTruststore;
	private String keystoreFilename; 
	private String truststoreFilename; 
	private String keystorePassword; 
	private String truststorePassword;
	private String keyManagerPassword;

	public boolean isCxfDebug() {
		return isCxfDebug;
	}

	public void setCxfDebug(boolean isCxfDebug) {
		this.isCxfDebug = isCxfDebug;
	}

	public String getCertifactionAlias() {
		return certifactionAlias;
	}

	public void setCertifactionAlias(String certifactionAlias) {
		this.certifactionAlias = certifactionAlias;
	}

	public String getWsAddress() {
		return wsAddress;
	}

	public void setWsAddress(String wsAddress) {
		this.wsAddress = wsAddress;
	}

	public Class<T> getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(Class<T> serviceClass) {
		this.serviceClass = serviceClass;
	}

	public void setPathToKeystore(String pathToKeystore) {
		this.pathToKeystore = pathToKeystore;
	}

	public void setPathToTruststore(String pathToTruststore) {
		this.pathToTruststore = pathToTruststore;
	}

	public void setKeystoreFilename(String keystoreFilename) {
		this.keystoreFilename = keystoreFilename;
	}

	public void setTruststoreFilename(String truststoreFilename) {
		this.truststoreFilename = truststoreFilename;
	}
	
	public String getKeystorePath() {
		return pathToKeystore + "/" + keystoreFilename;
	}
	
	public String getTruststorePath() {
		return pathToTruststore + "/" + truststoreFilename;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}

	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}

	public String getKeyManagerPassword() {
		return keyManagerPassword;
	}

	public void setKeyManagerPassword(String keyManagerPassword) {
		this.keyManagerPassword = keyManagerPassword;
	}
}
