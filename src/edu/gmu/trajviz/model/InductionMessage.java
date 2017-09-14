package edu.gmu.trajviz.model;


public class InductionMessage {

	public static final String DATA_FNAME = "data_file_name";
	public static final String STATUS_MESSAGE = "status_message";
	public static final String TIME_SERIES_MESSAGE =  "time_series_message";
	public static final String CHART_MESSAGE = "chart_message";
	/** The key storage. */
	private String type;

		private Object payload,payload1,payload2,payload3;
	 	public Object getPayload3() {
	 		return payload3;
	 	}
	 	
	 	
	/**
	   * Constructor.
	   * 
	   * @param messageType set the message type.
	   * @param payload set the payload.
	   */
	  public InductionMessage(String messageType, Object payload) {
	    this.type = messageType;
	    this.payload = payload;
	  }
	  /**
	   * Constructor.
	   * 
	   * @param messageType set the message type.
	   * @param payload set the payload.
	   */
	  public InductionMessage(String messageType, Object payload, Object payload1) {
		    this.type = messageType;
		    this.payload = payload;
		    this.payload1 = payload1;
		//	System.out.println("SequiturMessagePayload1::::::::::::::::"+this.payload1);
		    
		  }
	  public InductionMessage(String messageType, Object payload, Object payload1, Object payload2) {
		    this.type = messageType;
		    this.payload = payload;
		    this.payload1 = payload1;
		    this.payload2 = payload2;
		//	System.out.println("SequiturMessagePayload1::::::::::::::::"+this.payload1);
		    
		  }
	  public Object getPayload() {
		    return payload;
		  }
	  public Object getPayload1() {
		    return payload1;
		  }
	 
	  public Object getPayload2() {
		    return payload2;
		  }
	  public void setPayload(Object payload) {
		    this.payload = payload;
		  }
	  public void setPayload1(Object payload1) {
		    this.payload1 = payload1;
		  }
	  public void setPayload2(Object payload2) {
		    this.payload2 = payload2;
		  }
	  public String getType() {
		    return type;
		  }
	  public void setType(String type) {
		    this.type = type;
		  }
	
}
