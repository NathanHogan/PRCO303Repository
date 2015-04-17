using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for GoogleSearchResponse
/// </summary>
/// 
[DataContract]
public class GoogleSearchResponse
{
    [DataMember(Name = "results")]
    public List<GooglePlaceResult> placeList { get; set; }

    [DataMember(Name = "next_page_token", IsRequired=false)]
    public string token { get; set; }

	public GoogleSearchResponse()
	{
	}
}