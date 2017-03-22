package com.evensel.swyftr.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author prishanm 02/14/2017
 *
 */
public class JsonRequestManager {

	private static JsonRequestManager mInstance;
	private RequestQueue mRequestQueue;
	private static Context mCtx;

	/**
	 * Log or request TAG
	 */
	public static final String TAG = "VolleyInstance";
	private final String tag_json_arry = "json_array_req";

	/* Volley */
	public static synchronized JsonRequestManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new JsonRequestManager(context);
		}
		return mInstance;
	}

	private JsonRequestManager(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();
	}

	private RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley
					.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	/******************************************************************************************************************************************/

	/**
	 * Register User
	 **/
	public interface registerUser{
		void onSuccess(ResponseModel model);

		void onError(String status);

		void onError(ResponseModel model);


	}

	public void registerUserRequest(String url, final String email,String password,String conPassword,String phoneNo,
							   final registerUser callback) {

		//Log.d("test Request", image);
		String finalUrl = url;
		HashMap<String, String> params = new HashMap<>();
		params.put("email",email);
		params.put("password",password);
		params.put("password_confirmation",conPassword);
		params.put("phone_no",phoneNo);

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				finalUrl, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ObjectMapper mapper = new ObjectMapper();

						try {
							if(response!=null){
								ResponseModel responseModel = mapper.readValue(response.toString(), ResponseModel.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Login User
	 **/
	public interface loginUser{
		void onSuccess(LoginResponse model);

		void onError(LoginResponse model);

		void onError(String s);
	}

	public void loginUserRequest(String url, final String email,String password,
									final loginUser callback) {

		//Log.d("test Request", image);
		String finalUrl = url;
		HashMap<String, String> params = new HashMap<>();
		params.put("email",email);
		params.put("password",password);
		params.put("user","user");

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				finalUrl, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ObjectMapper mapper = new ObjectMapper();

						try {
							if(response!=null){
								LoginResponse responseModel = mapper.readValue(response.toString(), LoginResponse.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorLoginResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Update User Info
	 **/
	public interface updateUser{
		void onSuccess(ResponseModel model);

		void onError(String status);

		void onError(ResponseModel model);


	}

	public void updateUserRequest(String url, String token, String name,String email,String mobile ,String home_address,String office_address,
									final updateUser callback) {

		//Log.d("test Request", image);
		String finalUrl = url+"?token="+token;
		HashMap<String, String> params = new HashMap<>();
		params.put("email",email);
		params.put("name",name);
		params.put("phone_no",mobile);
		params.put("home_address",home_address);
		params.put("office_address",office_address);

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				finalUrl, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ObjectMapper mapper = new ObjectMapper();

						try {
							if(response!=null){
								ResponseModel responseModel = mapper.readValue(response.toString(), ResponseModel.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}


	/******************************************************************************************************************************************/

	/**
	 * Send password reset code
	 **/
	public interface sendResetCode{
		void onSuccess(ResponseModel model);

		void onError(String status);

		void onError(ResponseModel model);


	}

	public void sendResetCodeRequest(String url,String email,
								  final sendResetCode callback) {

		//Log.d("test Request", image);
		String finalUrl = url;
		HashMap<String, String> params = new HashMap<>();
		params.put("email",email);
		params.put("user","user");

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				finalUrl, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ObjectMapper mapper = new ObjectMapper();

						try {
							if(response!=null){
								ResponseModel responseModel = mapper.readValue(response.toString(), ResponseModel.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Validate reset code
	 **/
	public interface validateResetCode{
		void onSuccess(ResponseModel model);

		void onError(String status);

		void onError(ResponseModel model);


	}

	public void validateResetCodeRequest(String url,String code,
									 final validateResetCode callback) {

		//Log.d("test Request", image);
		String finalUrl = url;
		HashMap<String, String> params = new HashMap<>();
		params.put("code",code);

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				finalUrl, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ObjectMapper mapper = new ObjectMapper();

						try {
							if(response!=null){
								ResponseModel responseModel = mapper.readValue(response.toString(), ResponseModel.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Reset password
	 **/
	public interface resetPassword{
		void onSuccess(ResponseModel model);

		void onError(String status);

		void onError(ResponseModel model);


	}

	public void resetPasswordRequest(String url,String password,
										 final resetPassword callback) {

		//Log.d("test Request", image);
		String finalUrl = url;
		HashMap<String, String> params = new HashMap<>();
		params.put("email","prishanmaduka@outlook.com");
		params.put("password","Prishan123");
		params.put("password_confirmation","Prishan123");
		params.put("user","user");

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				finalUrl, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ObjectMapper mapper = new ObjectMapper();

						try {
							if(response!=null){
								ResponseModel responseModel = mapper.readValue(response.toString(), ResponseModel.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Get Category List
	 **/
	public interface getCategoriesRequest{
		void onSuccess(CategoriesResponse model);

		void onError(String status);

		void onError(CategoriesResponse model);
	}

	public void getCategories(String url,String token,
						  final getCategoriesRequest callback) {

		url = url+"&token="+token;

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
				url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							if(response!=null){
								CategoriesResponse responseModel = mapper.readValue(response.toString(), CategoriesResponse.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorCatResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Product Search Home
	 **/
	public interface productSearchHomeRequest{
		void onSuccess(CategoriesResponse model);

		void onError(String status);

		void onError(CategoriesResponse model);
	}

	public void productSearchHome(String url,String token,
							  final productSearchHomeRequest callback) {

		url = url+"&token="+token;

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
				url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							if(response!=null){
								CategoriesResponse responseModel = mapper.readValue(response.toString(), CategoriesResponse.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorCatResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Favourite Product
	 **/
	public interface favouriteProductRequest{
		void onSuccess(ResponseModel model);

		void onError(String status);

		void onError(ResponseModel model);
	}

	public void favouriteProduct(String url,String token,int productId,int status,
								  final favouriteProductRequest callback) {

		url = url+"?token="+token;
		HashMap<String, String> params = new HashMap<>();
		params.put("product_id",String.valueOf(productId));
		params.put("status",String.valueOf(status));
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				url, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							if(response!=null){
								ResponseModel responseModel = mapper.readValue(response.toString(), ResponseModel.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}



	/******************************************************************************************************************************************/

	/**
	 * Get Category Product List
	 **/
	public interface categoryProductRequest{
		void onSuccess(CategoriesResponse model);

		void onError(String status);

		void onError(CategoriesResponse model);
	}

	public void categoryProducts(String url,String token,
								 final categoryProductRequest callback) {

		url = url+"&token="+token;

		Log.d("XXXXXXXXXXXX",url);


		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
				url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							if(response!=null){
								CategoriesResponse responseModel = mapper.readValue(response.toString(), CategoriesResponse.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorCatResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Get Brand List
	 **/
	public interface getBrandListRequest{
		void onSuccess(CategoriesResponse model);

		void onError(String status);

		void onError(CategoriesResponse model);
	}

	public void getBrandList(String url,
								 final getBrandListRequest callback) {

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
				url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							if(response!=null){
								CategoriesResponse responseModel = mapper.readValue(response.toString(), CategoriesResponse.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorCatResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}

	/******************************************************************************************************************************************/

	/**
	 * Get Category base product search list
	 **/
	public interface categoryBasedSearchRequest{
		void onSuccess(CategoriesResponse model);

		void onError(String status);

		void onError(CategoriesResponse model);
	}

	public void categoryBasedSearch(String url,String token,
							 final categoryBasedSearchRequest callback) {

		url = url+"&token="+token;

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
				url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							if(response!=null){
								CategoriesResponse responseModel = mapper.readValue(response.toString(), CategoriesResponse.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorCatResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}



	/******************************************************************************************************************************************/

	/**
	 * Social Login
	 **/
	public interface socialLogin{
		void onSuccess(LoginResponse model);

		void onError(String status);

		void onError(LoginResponse model);


	}

	public void socialLoginRequest(String url,String token,String media,
								   final socialLogin callback) {

		//Log.d("test Request", image);
		String finalUrl = url+"?token="+token;
		HashMap<String, String> params = new HashMap<>();
		params.put("media",media);

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				finalUrl, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						ObjectMapper mapper = new ObjectMapper();

						try {
							if(response!=null){
								LoginResponse responseModel = mapper.readValue(response.toString(), LoginResponse.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError(errorLoginResponse(error.networkResponse.data,HttpHeaderParser.parseCharset(error.networkResponse.headers)));
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}


	/******************************************************************************************************************************************/

	/**
	 * Update Location
	 **/
	public interface updateLocationRequest{
		void onSuccess(LocationResponseModel model);

		void onError(String status);
	}

	public void updateLocation(String url,String token,double longitude,double latitude,
								 final updateLocationRequest callback) {

		url = url+"?token="+token;
		HashMap<String, Double> params = new HashMap<>();
		params.put("lon",longitude);
		params.put("lat",latitude);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				url, new JSONObject(params),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							if(response!=null){
								LocationResponseModel responseModel = mapper.readValue(response.toString(), LocationResponseModel.class);
								callback.onSuccess(responseModel);
							}else{
								callback.onError("Error occured");
							}

						} catch (Exception e) {
							e.printStackTrace();
							callback.onError("Error occured");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				callback.onError("Error occured");
			}
		});

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq,
				tag_json_arry);

	}


	/******************************************************************************************************************************************/


	/**
	 * Method to convert 400,401,500 error to response model class
	 * @param bytes
	 * @param charset
     * @return ResponseModel
     */
	private ResponseModel errorResponse(byte[] bytes,String charset){
		ResponseModel responseModel = new ResponseModel();
		responseModel.setMessage("Error Occured.");
		responseModel.setStatus("error");
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = new String(bytes, charset);
			JSONObject errorResponse = new JSONObject(json);
			if(errorResponse!=null){
				responseModel = mapper.readValue(errorResponse.toString(),ResponseModel.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseModel;
	}

	/**
	 * Method to convert 400,401,500 error to response model class
	 * @param bytes
	 * @param charset
	 * @return ResponseModel
	 */
	private LoginResponse errorLoginResponse(byte[] bytes,String charset){
		LoginResponse responseModel = new LoginResponse();
		responseModel.setMessage("Error Occured.");
		responseModel.setStatus("error");
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = new String(bytes, charset);
			JSONObject errorResponse = new JSONObject(json);
			if(errorResponse!=null){
				responseModel = mapper.readValue(errorResponse.toString(),LoginResponse.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseModel;
	}

	/**
	 * Method to convert 400,401,500 error to response model class
	 * @param bytes
	 * @param charset
	 * @return ResponseModel
	 */
	private CategoriesResponse errorCatResponse(byte[] bytes,String charset){
		CategoriesResponse responseModel = new CategoriesResponse();
		responseModel.setMessage("Error Occured.");
		responseModel.setStatus("error");
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = new String(bytes, charset);
			JSONObject errorResponse = new JSONObject(json);
			if(errorResponse!=null){
				responseModel = mapper.readValue(errorResponse.toString(),CategoriesResponse.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseModel;
	}



	/******************************************************************************************************************************************/


}
