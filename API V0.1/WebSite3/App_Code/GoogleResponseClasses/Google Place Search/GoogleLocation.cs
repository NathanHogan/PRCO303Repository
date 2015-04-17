using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for GoogleLocation
/// </summary>
/// 
[DataContract]
public class GoogleLocation
{
    [DataMember(Name = "lat")]
    public string lat { get; set; }

    [DataMember(Name = "lng")]
    public string lng { get; set; }

	public GoogleLocation()
	{

	}
}