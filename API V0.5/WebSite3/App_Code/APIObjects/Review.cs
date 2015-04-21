using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for Review
/// </summary>
/// 
[DataContract]
public class Review
{
    [DataMember (Name="user_id")]
    public string UID { get; set; }

    [DataMember (Name="place_id")]
    public string PID { get; set; }

    [DataMember (Name="rating")]
    public int rating { get; set; }

    [DataMember (Name="review")]
    public string reviewText {get; set;}

	public Review()
	{

	}

    public Review(string newUID, string newPID, int newRate, string newReview)
    {
        UID = newUID;
        PID = newPID;
        rating = newRate;
        reviewText = newReview;
    }
}