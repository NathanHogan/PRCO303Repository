using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for GooglePlaceResult
/// </summary>
/// 
[DataContract]
public class GooglePlaceResult
{
    [DataMember(Name = "place_id")]
    public string ID { get; set; }

    [DataMember(Name = "name")]
    public string Name { get; set; }

    [DataMember(Name = "geometry")]
    public GoogleGeo Geometry { get; set; }

	public GooglePlaceResult()
	{
	}
}