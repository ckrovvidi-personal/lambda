package com.amazonaws.lambda.demo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class ExchangeRates implements Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	private String							base;
	private String							disclaimer;
	private String							license;
	private Map<String, Double>	rates;
	private Date								timestamp;

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Map<String, Double> getRates() {
		return rates;
	}

	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
