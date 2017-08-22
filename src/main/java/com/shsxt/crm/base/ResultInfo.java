package com.shsxt.crm.base;

public class ResultInfo {
	
	private Integer resultCode;
	private String resultMessage;
	private Object result;
	
	public ResultInfo() {
		
	}
	
	public ResultInfo(Integer resultCode, String resultMessage, Object result) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.result = result;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
