package ca.dealsaccess.facejoy.sqlite;

import ca.dealsaccess.util.StringUtils;


public class PersonTrainVerify {

	public int _id;
	public String personId;
	public String personName;
	public int status;
	public int startTime;
	public int createTime;
	public int finishTime;
	public String sessionId;
	public String result;
	
	public PersonTrainVerify() {
	}
	
	public PersonTrainVerify(String personId, String personName, int status, int startTime,
			String sessionId) {
		this(personId, personName, status, startTime, 0, 0, sessionId, StringUtils.EMPTY);
	}
	
	
	
	

	public PersonTrainVerify(String personId, String personName, int status, int startTime,
			int createTime, int finishTime, String sessionId, String result) {
		this.personId = personId;
		this.personName = personName;
		this.status = status;
		this.startTime = startTime;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.sessionId = sessionId;
		this.result = result;
	}

}
