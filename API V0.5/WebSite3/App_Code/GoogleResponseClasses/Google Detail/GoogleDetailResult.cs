using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
/// <summary>
/// Summary description for GoogleDetailResult
/// </summary>
/// 
[DataContract]
public class GoogleDetailResult
{
    [DataMember(Name = "formatted_address", IsRequired=false)]
    public string formattedAddress;

    [DataMember(Name = "formatted_phone_number", IsRequired=false)]
    public string formattedTelephone;

    [DataMember (Name="opening_hours", IsRequired=false)]
    public GoogleDetailOpening openingInformation;

	public GoogleDetailResult()
	{
	}
}