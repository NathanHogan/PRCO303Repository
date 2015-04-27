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
        switch (context.Request.HttpMethod)
        {
            case "GET":
                ProcessGET(context);
                break;
            case "POST":
                ProcessPOST(context);
                break;
            case "DELETE":
                ProcessDELETE(context);
                break;
            case "PUT":
                ProcessPUT(context);
                break;
            default:
                break;
        }
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
        string paramID;
        UserSettings updatedSettings;

        paramID = context.Request.QueryString["id"];

        DataContractJsonSerializer deserializer = new DataContractJsonSerializer(typeof(UserSettings));
        updatedSettings = (UserSettings)deserializer.ReadObject(context.Request.InputStream);

        db.updateSettings(paramID, updatedSettings);
    }

    public void ProcessPOST(HttpContext context)
    {

    }

    public void ProcessDELETE(HttpContext context){

    }

    public bool IsReusable { get { return true; } }
}