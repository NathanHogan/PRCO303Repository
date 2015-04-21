using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;
/// <summary>
/// Summary description for Person
/// </summary>
/// 
[DataContract]
public class Person
{
    [DataMember (Name="name")]
    public string name;
    [DataMember (Name="fbid")]
    public string uid;
    [DataMember (Name="age")]
    public string age;
    [DataMember (Name="dateJoined")]
    public DateTime dateJoined;
    [DataMember(Name = "Settings")]
    public UserSettings settings;

	public Person()
	{

	}
}