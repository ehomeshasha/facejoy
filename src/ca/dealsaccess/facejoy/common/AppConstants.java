package ca.dealsaccess.facejoy.common;

public class AppConstants {
	
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE"; 
	
	public static final String CLICK_ID = "CLICK_ID";
	
	public static final String CLICK_PATH = "CLICK_PATH";

	public static final String FACE_CHECKED_LIST = "FACE_CHECKED_LIST";

	public static final String PERSON_NAME = "PERSON_NAME";
	
	public static final String PERSON_ID = "PERSON_ID";

	public static final String FACE_ID = "FACE_ID";
	
	public static final String FACE_IMG_PATH = "FACE_IMG_PATH";

	public static final String API_KEY = "3807aeb4a0b911495fdf0c946d006251";
	
	public static final String API_SECRET = "DQOkhWeHweMMItMjaIGwqG0Nns8JNj1E";

	public static class TRAIN_STATE_STRING {
		public static final String NOT_START = "NOT_START";
		public static final String INQUEUE = "INQUEUE";
		public static final String FAILED = "FAILED";
		public static final String SUCC = "SUCC";
	}
	
	
	public static class TRAIN_STATE {
		public static final int NOT_START = 0;
		public static final int INQUEUE = 1;
		public static final int FAILED = 2;
		public static final int SUCC = 3;
	}
	
}
