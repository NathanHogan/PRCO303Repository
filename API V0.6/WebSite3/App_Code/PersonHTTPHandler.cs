using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;

/// <summary>
/// Summary description for PersonHTTPHandler
/// </summary>
public class PersonHTTPHandler : IHttpHandler
{
    private DatabaseLayer db = new DatabaseLayer();

	public PersonHTTPHandler()
	{
		//
		// TODO: Add constructor logic here
		//
	}

    public void ProcessRequest(HttpContext context)
    {
        ProcessGET(context);
    }

    public void ProcessGET(HttpContext context)
    {
        string paramID;
        UserSettings requestedSettings;
        paramID = context.Request.QueryString["id"];
        requestedSettings = db.getSettings(paramID);

        DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(UserSettings));
        serializer.WriteObject(context.Response.OutputStream, requestedSettings);
    }

    public void ProcessPUT(HttpContext context)
    {

    }

    public void ProcessPOST(HttpContext context)
    {

    }

    public void ProcessDELETE(HttpContext context){

    }

    public bool IsReusable { get { return true; } }
}