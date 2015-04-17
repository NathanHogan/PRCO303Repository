using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;

/// <summary>
/// Summary description for GoogleAPICalls
/// </summary>
public class GoogleAPICalls
{


	public GoogleAPICalls()
	{

	}


    public GoogleSearchResponse CallSearchAPI()
    {
 
        string URI = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=50.3748,-4.13787&radius=200&key=AIzaSyAlzKIkI5OOJEH9iZnrpgbz77hxwiyk2XM";

        var webRequest = (HttpWebRequest)WebRequest.Create(URI);
        webRequest.Method = "GET";
        HttpWebResponse response = (HttpWebResponse)webRequest.GetResponse();
        Stream responseStream = response.GetResponseStream();

        DataContractJsonSerializer deserializer = new DataContractJsonSerializer(typeof(GoogleSearchResponse));
        var responseObject = (GoogleSearchResponse)deserializer.ReadObject(responseStream);

        return responseObject;



    }

    public DetailedPlace GoogleDetailCall(string placeID)
    {
        DetailedPlace placeToCall = new DetailedPlace();
        string URI = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key=AIzaSyAlzKIkI5OOJEH9iZnrpgbz77hxwiyk2XM";

        var webRequest = (HttpWebRequest)WebRequest.Create(URI);
        webRequest.Method = "GET";
        HttpWebResponse response = (HttpWebResponse)webRequest.GetResponse();
        Stream responseStream = response.GetResponseStream();

        DataContractJsonSerializer deserializer = new DataContractJsonSerializer(typeof(GoogleDetailResponse));
        var responseObject = (GoogleDetailResponse)deserializer.ReadObject(responseStream);

        placeToCall = new DetailedPlace(responseObject); 
        return placeToCall;

    }
    
}