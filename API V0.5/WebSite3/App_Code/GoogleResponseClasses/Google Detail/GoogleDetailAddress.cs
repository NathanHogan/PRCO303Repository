using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for GoogleDetailAddress
/// </summary>
/// 
[DataContract]
public class GoogleDetailAddress
{
    [DataMember (Name="formatted_address")]
    public string formattedAddress;
    [DataMember (Name="formatted_phone_number")]
    public string formattedTelephone;

	public GoogleDetailAddress()
	{

	}
}