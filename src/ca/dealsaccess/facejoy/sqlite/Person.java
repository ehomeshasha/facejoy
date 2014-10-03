package ca.dealsaccess.facejoy.sqlite;

public class Person {

	public int _id;
	public String personId;
	public String personName;
	public int isTrain;
	public int startTime;
	public int createTime;
	public int finishTime;
	public String sessionId;
	
	public Person() {
	}

	public Person(String personId, String personName, int isTrain, int startTime,
			int createTime, int finishTime, String sessionId) {
		this.personId = personId;
		this.personName = personName;
		this.isTrain = isTrain;
		this.startTime = startTime;
		this.createTime = createTime;
		this.finishTime = finishTime;
		this.sessionId = sessionId;
	}

}
