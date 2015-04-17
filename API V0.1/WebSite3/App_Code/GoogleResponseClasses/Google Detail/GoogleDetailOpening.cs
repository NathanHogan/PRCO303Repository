using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
/// <summary>
/// Summary description for GoogleDetailOpening
/// </summary>
/// 
[DataContract]
public class GoogleDetailOpening
{
    [DataMember(Name = "open_now")]
    public bool openNow;

    [DataMember(Name="weekday_text")]
    public List<String> openingHours;


   //ublic GoogleDetailOpeningHours openingHours;

	public GoogleDetailOpening()
	{

	}
}