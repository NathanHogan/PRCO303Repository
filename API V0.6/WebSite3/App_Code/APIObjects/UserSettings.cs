using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for UserSettings
/// </summary>
/// 
[DataContract]
public class UserSettings
{
    [DataMember (Name="Types")]
    public List<String> types { get; set; }
    [DataMember (Name="Radius")]
    public int radius { get; set; }

	public UserSettings(List<String> newTypes, int newRadius)
	{
        types = newTypes;
        radius = newRadius;
	}

    public UserSettings()
    {
        types = new List<String>();
        radius = 0;
    }
}