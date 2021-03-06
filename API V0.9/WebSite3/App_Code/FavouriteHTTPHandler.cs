﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;

/// <summary>
/// Summary description for FavouriteHandler
/// </summary>
public class FavouriteHandler : IHttpHandler
{
    private DatabaseLayer db = new DatabaseLayer();

	public FavouriteHandler()
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
            default:
                break;
        }
    }

    public void ProcessGET(HttpContext context)
    {
        string paramID;

        paramID = context.Request.QueryString["id"];
        List<Place> favouriteList = new List<Place>();
        favouriteList = db.getFavourites(paramID);

        DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(List<Place>));
        serializer.WriteObject(context.Response.OutputStream, favouriteList);
    }

    public void ProcessPOST(HttpContext context)
    {
        FavouriteObject newFavourite;
        DataContractJsonSerializer deserializer = new DataContractJsonSerializer(typeof(FavouriteObject));
        newFavourite = (FavouriteObject)deserializer.ReadObject(context.Request.InputStream);

        db.addNewPlace(newFavourite.favouritedPlace);
        db.addNewFavourite(newFavourite.UserID, newFavourite.favouritedPlace.ID);

        DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(FavouriteObject));
        deserializer.WriteObject(context.Response.OutputStream, newFavourite);

        DataContractJsonSerializer placeSerializer = new DataContractJsonSerializer(typeof(Place));
        placeSerializer.WriteObject(context.Response.OutputStream, newFavourite.favouritedPlace);

        context.Response.StatusCode = 201;
        context.Response.Write("Done");
        
        //Do response stuff here
    }

    public void ProcessDELETE(HttpContext context)
    {
        string paramPID;
        string paramUID;
        bool duplicate = false;

        paramPID = context.Request.QueryString["pid"];
        paramUID = context.Request.QueryString["uid"];

        db.deleteFavourite(paramUID, paramPID);
        db.deleteReview(paramPID, paramUID);
        duplicate = db.checkRemainingPlaceFavourite(paramPID);
        if (duplicate == false)
        {
            db.deletePlace(paramPID);
        }


    }

    public bool IsReusable { get { return true; } }

    [DataContract]
    private class FavouriteObject
    {
        [DataMember(Name="user_id")]
        public string UserID { get; set; }
        [DataMember(Name = "place")]
        public Place favouritedPlace { get; set; }

        public FavouriteObject(string newUID, Place newPlace)
        {
            UserID = newUID;
            favouritedPlace = newPlace;
        }
    }
}