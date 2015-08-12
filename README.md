Simple Search

Getting started with Android

Bharat Singhvi
12 August 2015


* Concepts

The following concepts were touched while creating this application:

- Adapters
- Broadcast Receiver
- Notification
- Storage
- WebView
- Third Party Libraries

* Adapters

* Adapters

`Adapter` is an interface which serves as the bridge between an `AdapterView` and underlying data for that view. 

*Task* 

To add an `EditText` in the application which provides autocomplete suggestions from web as well as local search history.

*Components*

- AutoCompleteTextView
- SuggestionsAdapter

* AutoCompleteTextView

- `android:completionThreshold`: Refers to number of characters to be typed before displaying autocomplete suggestions.
- `setAdapter()`: Sets the list of data to be used for autocomplete suggestions.

* SuggestionsAdapter

This is a custom Adapter class used for preparing the list of data which would serve as autocomplete suggestions. 

- Class signature: 
	public class SuggestionsAdapter extends ArrayAdapter<String> implements Filterable
- Methods used:
	void getCount()
	String getItem(int index)
	Filter getFilter()
		FilterResults performFiltering()
		void publishResults()

* Broadcast Receiver

* Broadcast Receiver

In Android, it is the base class for the code which receive intents sent by `sendBroadcast()`. 

*Task*

To listen to changes in network connectivity of the device and display the state through a `Switch`. This requires us to use the followin permission in our application:

	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

*Components*

- ConnectivityManager
- ConnectivityChangeReceiver

* ConnectivityManager

`ConnectivityManager` is a class in Android which keeps track of network connectivity. It sends broadcast intents whenever there is a change in network connectivity. 

To accomplish the task, the following method was used:

	registerReceiver( ... , new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

This ensures that our application listens to the broadcast intents sent out by `ConnectivityManager` whenever there is a change in network connectivity. 

We use the `getActiveNetworkInfo()` method of `ConnectivityManager` to get current network state. 

* ConnectivityChangeReceiver

This is a custom class which extends `BroadcastReceiver`. The purpose of this class is to listen to change in network connectivity and reflect the same on main UI. The following method is used:

	void onReceive(Context context, Intent intent)

* Notification

* Notification

Notification is an object in Android which can be displayed on the device outside the application UI. It is presented on the device using `NotificationManager` which is a class responsible for notifying the user about events that have happened. 

*Task*

To display a sticky notification to the user which opens the search activity of the application. 

*Components*

- Notification
- NotificationManager

* Notification

Notification is created using the default constructor

	new Notification(R.mipmap.ic_launcher, null, System.currentTimeMillis()) 

The ticker text is set to null. The notification layout is defined using a custom layout file through RemoteViews. The following attributes are defined 

	.contentView: To display the custom layout for notification.
	.flags: Notification.FLAG_NO_CLEAR to make the notification sticky.
	.contentIntent: To launch the application's search activity when the notification is clicked.

* NotificationManager

`NotificationManager` is used to post the notification on the device. The following methods are used:

	getSystemService(NOTIFICATION_SERVICE)
	.notify(int id, Notification notification)

* Storage

* Storage

Android provides several options to save the persistent application data. These methods include `SharedPreferences`, `SQLiteDatabase` and saving data on internal, external or network storage.

*Task* 

To store the keywords searched by user. These keywords should be used while providing autocomplete suggestions.

*Components*

- SQLiteDatabase
- SQLiteOpenHelper

* SQLiteDatabase

This class provides the API to manage a SQLite database and perform common database management tasks.

We use the `insert()` and `query()` methods provided by `SQLiteDatabase` in order to insert the keywords search by the user and retrieve the previous keywords from the database. 

* SQLiteOpenHelper

This is a helper class provided by Android which can be used to manage creation and upgradation of SQLiteDatabase. 

The following custom subclass uses SQLiteOpenHelper

	public class MySQLiteDb extends SQLiteOpenHelper

To create the database, we implement the following methods in `MySQLiteDb`:

	onCreate(SQLiteDatabase database)
	onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) 

In order to insert and retrieve keywords from the database, we use the `getWritableDatabase()` method of `SQLiteOpenHelper`.  

* WebView

* WebView

Android provides a `View` for displaying web pages through `WebView` which can be imagined to be a web browser which can be used to display online content. 

*Task*

To open google search results corresponding the the search query of the user within the application. This requires us to use the following permission in our application:

	<uses-permission android:name="android.permission.INTERNET" />

We achieve this using the `loadURL()` method of `WebView`.

* Third Party Libraries: Retrofit

* Retrofit

This library provides a Java interface for making web based queries. 

*Task*

In our application, we are required to fetch autocomplete suggestions from web.

*Components*

- HTTPSuggestionsFetcher
- HTTPSuggestions

* HTTPSuggestionsFetcher

This is a custom interface which uses @GET and @Query annotations of Retrofit library and defines the method to perform a web query. The interface looks like follows:

	public interface HTTPSuggestionsFetcher {
	    @GET("/sugg")
	    ArrayList<String> getHTTPSuggestions(@Query("nResults") int numResults, 
	    									 @Query("command") String keyword);
	} 

* HTTPSuggestions

This is a custom class which is used to perform the web query defined in `HTTPSuggestionsFetcher`. 

We use the `RestAdapter` provided by Retrofit library to create a callable object and perform the web query. Since we execute the query during `filtering` operations (which is executed asyncronously), we make a synchronous retrofit call to get autocomplete suggestions. 

The class looks like follows:

	private static final String BASE_URL = "https://complete.reports.mn/";
	public static ArrayList<String> retrieveHTTPSuggestions(String keyword) throws IOException {
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build();
		HTTPSuggestionsFetcher httpSF = restAdapter.create(HTTPSuggestionsFetcher.class);
		return httpSF.getHTTPSuggestions(5, keyword);
	}

* Third Party Libraries: ButterKnife

* ButterKnife

`ButterKnife` is a field and method binding library which helps to reduce boilerplate code while working with Android Views. 

In this application, the following annotations were used:

	@Bind
	@BindString
	@OnClick
