using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
/// <summary>
/// Summary description for ReviewHTTPHandler
/// </summary>
public class ReviewHTTPHandler : IHttpHandler
{
    private DatabaseLayer db = new DatabaseLayer();
	public ReviewHTTPHandler()
	{

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
        string paramUID = context.Request.QueryString["uid"];
        string paramPID = context.Request.QueryString["pid"];
        Review newReview;
        List<Review> reviewList = new List<Review>();

        if (paramUID != "none")
        {
            newReview = db.getSingleReview(paramPID, paramUID);
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(Review));
            serializer.WriteObject(context.Response.OutputStream, newReview);
        }
        else
        {
            reviewList = db.getReviewList(paramPID);
                DataContractJsonSerializer listSerializer = new DataContractJsonSerializer(typeof(List<Review>));
                listSerializer.WriteObject(context.Response.OutputStream, reviewList);
            

        }

    }

    public void ProcessPOST(HttpContext context)
    {
        Review newReview;
        DataContractJsonSerializer deserializer = new DataContractJsonSerializer(typeof(Review));

        newReview = (Review)deserializer.ReadObject(context.Request.InputStream);
        db.postReview(newReview);
    }

    public void ProcessPUT(HttpContext context)
    {
        Review updateReview;
        DataContractJsonSerializer deserializer = new DataContractJsonSerializer(typeof(Review));

        updateReview = (Review)deserializer.ReadObject(context.Request.InputStream);
        db.updateReview(updateReview);
    }

    public void ProcessDELETE(HttpContext context)
    {
        string paramUID = context.Request.QueryString["uid"];
        string paramPID = context.Request.QueryString["pid"];

        db.deleteReview(paramPID, paramUID);
    }

    public bool IsReusable { get { return true; } }
}