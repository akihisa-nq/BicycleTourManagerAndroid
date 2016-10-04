package net.nqlab.btmw.model;

public class WearProtocol {
    // constans for data
	public  static final int REQUEST_POINT_PARAM_POINT_TYPE_START = 0;
    public  static final int REQUEST_POINT_PARAM_POINT_TYPE_GOAL = 1;
    public  static final int REQUEST_POINT_PARAM_POINT_TYPE_CONTROL_POINT_START = 2;
    public  static final int REQUEST_POINT_PARAM_POINT_TYPE_CONTROL_POINT_GOAL = 3;
    public  static final int REQUEST_POINT_PARAM_POINT_TYPE_PASS = 4;
    public  static final int REQUEST_POINT_PARAM_POINT_TYPE_BOTTOM = 5;
    public  static final int REQUEST_POINT_PARAM_POINT_TYPE_WAY = 6;

    // Handheld to Wear : Data : point to display
    public static final String REQUEST_POINT = "/point";
	public static final String REQUEST_POINT_PARAM_DATA = "data";
	public static final String REQUEST_POINT_PARAM_PREVIOUS = "previous_data";
	public static final String REQUEST_POINT_PARAM_BASE_DATE = "base_date";
	public static final String REQUEST_POINT_PARAM_START_DATE = "start_date";
    public static final String REQUEST_POINT_PARAM_POINT_TYPE = "point_type";

    // Wear to Handheld : Data : sound
    public static final String REQUEST_SOUND = "/sound";
    public static final String REQUEST_SOUND_PARAM_DATA = "sound_data";
    public static final String REQUEST_SOUND_PARAM_DATE = "date_recorded";

    // Wear to Handheld : Message : action go next
	public static final String REQUEST_NEXT = "/tour/go_next";
}