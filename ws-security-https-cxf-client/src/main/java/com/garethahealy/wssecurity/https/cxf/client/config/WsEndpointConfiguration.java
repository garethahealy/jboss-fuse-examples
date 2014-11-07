package com.garethahealy.wssecurity.https.cxf.client.config;

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
	private String passwordCallbackClass;
	private String signatureKeystoreFilename;
	private String signatureKeystorePassword;

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

	public String getPasswordCallbackClass() {
		return passwordCallbackClass;
	}

	public void setPasswordCallbackClass(String passwordCallbackClass) {
		this.passwordCallbackClass = passwordCallbackClass;
	}

	public String getSignatureKeystoreFilename() {
		return signatureKeystoreFilename;
	}

	public void setSignatureKeystoreFilename(String signatureKeystoreFilename) {
		this.signatureKeystoreFilename = signatureKeystoreFilename;
	}

	public String getSignatureKeystorePassword() {
		return signatureKeystorePassword;
	}

	public void setSignatureKeystorePassword(String signatureKeystorePassword) {
		this.signatureKeystorePassword = signatureKeystorePassword;
	}

	@Override
	public String toString() {
		return "WsEndpointConfiguration [isCxfDebug=" + isCxfDebug
				+ ", certifactionAlias=" + certifactionAlias + ", wsAddress="
				+ wsAddress + ", serviceClass=" + serviceClass
				+ ", pathToKeystore=" + pathToKeystore + ", pathToTruststore="
				+ pathToTruststore + ", keystoreFilename=" + keystoreFilename
				+ ", truststoreFilename=" + truststoreFilename
				+ ", keystorePassword=" + keystorePassword
				+ ", truststorePassword=" + truststorePassword
				+ ", keyManagerPassword=" + keyManagerPassword
				+ ", passwordCallbackClass=" + passwordCallbackClass
				+ ", signatureKeystoreFilename=" + signatureKeystoreFilename
				+ ", signatureKeystorePassword=" + signatureKeystorePassword
				+ "]";
	}
}
