using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Threading;

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
        UserSettings searchParams;
        string paramUID;
        string paramLocation;
        string token = "no token";
        paramUID = context.Request.QueryString["uid"];
        paramLocation = context.Request.QueryString["location"];
        try
        {
            token = context.Request.QueryString["token"];
        }
        catch (NullReferenceException e)
        {
            token = "no token";
        }

        if(token == null){
            token = "no token";
        }
        searchParams = db.getSettings(paramUID);
        Place newPlace;
        GoogleSearchResponse searchResponse = new GoogleSearchResponse();
        List<String> tokenList = new List<String>();


        while (token != null)
        {
            int index = 0;
            searchResponse = apiCall.CallSearchAPI(searchParams, token, paramLocation);
            for (int i = 0; i < searchResponse.placeList.Count; i++)
            {
                newPlace = new Place(searchResponse.placeList[i]);
                placeList.Add(newPlace);
            }
            token = searchResponse.token;
            index++;
            if(index <2)
            Thread.Sleep(2000);
        }

        DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(List<Place>));
        serializer.WriteObject(context.Response.OutputStream, placeList); 
    }

    public bool IsReusable { get { return true; } }
}