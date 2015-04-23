using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
/// <summary>
/// Summary description for GoogleDetailResponse
/// </summary>
/// 
[DataContract]
public class GoogleDetailResponse
{
    [DataMember (Name="result")]
    public GoogleDetailResult detailedResult;

	public GoogleDetailResponse()
	{

	}
}