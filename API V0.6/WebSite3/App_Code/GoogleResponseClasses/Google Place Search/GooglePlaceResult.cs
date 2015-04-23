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

    [DataMember(Name="types")]
    public List<String> typeList { get; set;}

    [DataMember(Name = "vicinity")]
    public string Vicinity { get; set; }

	public GooglePlaceResult()
	{
	}
}