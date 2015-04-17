using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;

/// <summary>
/// Summary description for AndroidHTTPHandler
/// </summary>
public class PlaceHTTPHandler : IHttpHandler
{
    private DatabaseLayer db = new DatabaseLayer();
	public PlaceHTTPHandler()
	{
	}

    public void ProcessRequest(HttpContext context)
    {
        if (context.Request.HttpMethod == "GET")
            ProcessGET(context);
        else
        {
            context.Response.Write("Method not supported");
        }
    }

    public void ProcessGET(HttpContext context)
    {
        GoogleAPICalls apiCall = new GoogleAPICalls();
        List<Place> placeList = new List<Place>();
        Place newPlace;
        string token = "first";
        //Need to do - add a way to retrieve more results
        GoogleSearchResponse searchResponse;

        searchResponse = apiCall.CallSearchAPI();
        token = searchResponse.token;

        for (int i = 0; i < searchResponse.placeList.Count; i++)
        {
            newPlace = new Place(searchResponse.placeList[i]);
            placeList.Add(newPlace);
        }

        DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(List<Place>));
        serializer.WriteObject(context.Response.OutputStream, placeList);
    }

    public bool IsReusable { get { return true; } }
}