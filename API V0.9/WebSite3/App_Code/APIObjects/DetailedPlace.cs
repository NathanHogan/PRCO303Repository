using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for DetailedPlace
/// </summary>
/// 
[DataContract]
public class DetailedPlace
{
    [DataMember(IsRequired=false)]
    public string formattedAddress;

    [DataMember(IsRequired = false)]
    public string formattedPhone;

    [DataMember(IsRequired = false)]
    public List<String> weekTimes;

    [DataMember(IsRequired = false)]
    public bool openNow;


	public DetailedPlace()
	{

	}

    public DetailedPlace(GoogleDetailResponse detailedResponse)
    {
        if(detailedResponse.detailedResult.formattedAddress != null)
        formattedAddress = detailedResponse.detailedResult.formattedAddress;

        if (detailedResponse.detailedResult.formattedTelephone != null)
        formattedPhone = detailedResponse.detailedResult.formattedTelephone;

        if (detailedResponse.detailedResult.openingInformation != null)
        {
            weekTimes = detailedResponse.detailedResult.openingInformation.openingHours;
            openNow = detailedResponse.detailedResult.openingInformation.openNow;
        }

        

    }
}