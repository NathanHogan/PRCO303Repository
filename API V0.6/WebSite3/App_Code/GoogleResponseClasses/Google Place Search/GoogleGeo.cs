using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for GoogleGeo
/// </summary>
/// 
[DataContract]
public class GoogleGeo
{

    [DataMember (Name="location")]
    public GoogleLocation location { get; set;}

	public GoogleGeo()
	{

	}
}