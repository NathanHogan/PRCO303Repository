using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;

/// <summary>
/// Summary description for DetailHTTPHandler
/// </summary>
public class DetailHTTPHandler : IHttpHandler
{
	public DetailHTTPHandler()
	{
	}

    public void ProcessRequest(HttpContext context)
    {
        string placeID;

        placeID = context.Request.QueryString["id"];
        DetailedPlace requestedPlace;
        GoogleAPICalls apiCall = new GoogleAPICalls();
        requestedPlace = apiCall.GoogleDetailCall(placeID);

        DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(DetailedPlace));
        serializer.WriteObject(context.Response.OutputStream, requestedPlace);

    }

    public void ProcessGET(HttpContext context)
    {

    }

    public bool IsReusable { get { return true; } }
}