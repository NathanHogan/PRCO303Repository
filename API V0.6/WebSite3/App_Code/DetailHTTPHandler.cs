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
        string placeID = null;

        try
        {
            placeID = context.Request.QueryString["pid"];
        } catch (Exception e)
        {
            placeID = null;
        }

        if (placeID != null)
        {
            DetailedPlace requestedPlace;
            GoogleAPICalls apiCall = new GoogleAPICalls();
            try
            {
                requestedPlace = apiCall.GoogleDetailCall(placeID);
            }
            catch (NullReferenceException e)
            {
                requestedPlace = null;
            }

            if (requestedPlace != null)
            {
                context.Response.StatusCode = 200;
                DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(DetailedPlace));
                serializer.WriteObject(context.Response.OutputStream, requestedPlace);
            }
            else
            {
                context.Response.StatusCode = 400;  
            }
        }
        else
        {
            context.Response.StatusCode = 400;
        }

    }

    public void ProcessGET(HttpContext context)
    {

    }

    public bool IsReusable { get { return true; } }
}