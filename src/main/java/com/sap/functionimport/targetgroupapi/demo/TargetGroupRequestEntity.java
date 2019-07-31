package com.sap.functionimport.targetgroupapi.demo;

public class TargetGroupRequestEntity {
	private String systemURL;
	private String targetGroupUUID;
	
	public TargetGroupRequestEntity(String systemURL, String targetGroupUUID) {
		super();
		this.systemURL = systemURL;
		this.targetGroupUUID = targetGroupUUID;
	}

	public String getSystemURL() {
		return systemURL;
	}

	public void setSystemURL(String systemURL) {
		this.systemURL = systemURL;
	}

	public String getTargetGroupUUID() {
		return targetGroupUUID;
	}

	public void setTargetGroupUUID(String targetGroupUUID) {
		this.targetGroupUUID = targetGroupUUID;
	}
	
	

}
