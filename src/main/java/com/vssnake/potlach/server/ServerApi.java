package com.vssnake.potlach.server;

public class ServerApi {
	
		public static final String BEARER_TOKEN ="bearer_token";
	    public static final String HEADER_EMAIL = "email";
	    public static final String HEADER_START = "start";
	    public static final String HEADER_INNA = "inappropriate";
	    public static final String HEADER_GIFT_CHAIN = "idChain";
	    public static final String HEADER_GIFT_ID = "giftID";
	    public static final String HEADER_GIFT_TITLE = "giftTitle";
	    public static final String HEADER_GCM_CLIENT_KEY = "gcmKey";

	    public static final String HEADER_GC_LATITUDE = "gcLatitude";
	    public static final String HEADER_GC_LONGITUDE = "gcLongitude";
	    public static final String HEADER_GC_PRECISION = "gcPrecision";
	    public static final String HEADER_GC_TITLE = "gcTitle";
	    public static final String HEADER_GC_DESCRIPTION = "gcDescription";
	    public static final String GC_MULTI_IMAGE = "gcMultiImage";
	    public static final String GC_MULTI_IMAGE_THUMB = "gcMultiImageThumb";

	    public static final String REGISTER = "/register";
	    public static final String LOGIN = "/login";
	    public static final String LOGOUT = "/logout";
	    public static final String GET_USER = "/user";
	    public static final String CURRENT_USER_MODIF_INA = "/user/inna"; //Modify inappropriate
	    public static final String USER_GIFTS = "/user/gifts";
	    public static final String SEARCH_USER = "/user/{email}";
	    public static final String GIFT_CREATE = "/gift/create/";
	    public static final String GIFT_SHOW = "/gift/";
	    public static final String GIFTS_SHOW = "/gift";
	    public static final String GIFT_LIKE = "/gift/like";
	    public static final String GIFT_OBSCENE = "/gift/obscene";
	    public static final String GIFT_DELETE = "/gift/delete";
	    public static final String GIFT_SEARCH = "/gift/search";
	    public static final String GIFT_CHAIN = "/gift/chain";
	    public static final String SPECIAL_INFO = "/special";

	    public static final String TITLE_PARAMETER = "title";
	    public static final String START_PARAMETER = "start";

	    public static final String LINK_IMAGE = "/photo";

	    public static final String PATH_IMAGE = "image";
	    public static final String PATH_GIFT_ID = "giftID";

	    public static final String PART_GIFT = "giftPart";

}
