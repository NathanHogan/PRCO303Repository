using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

/// <summary>
/// Summary description for Place
/// </summary>
/// 
[DataContract]
public class Place
{
    private string _Name;
    [DataMember (Name="name")]
    public string Name { get { return _Name; } set { _Name = value; } }

    private string _ID;
    [DataMember (Name="placeid")]
    public string ID { get { return _ID; } set { _ID = value; } }

    private string _Location;
    [DataMember(Name = "location")]
    public string Location { get { return _Location; } set { _Location = value; } }

    private string _vicinity;
    [DataMember(Name = "vicinity")]
    public string vicinity { get { return _vicinity; } set { _vicinity = value; } }

    private List<String> _types;
    [DataMember(Name="types")]
    public List<String> types { get { return _types; } set { _types = value; } }

	public Place()
	{
	}

    public Place(GooglePlaceResult passedPlace)
    {
        _Name = passedPlace.Name;
        _ID = passedPlace.ID;
        _Location = passedPlace.Geometry.location.lat + "," + passedPlace.Geometry.location.lng;
        _types = passedPlace.typeList;
        _vicinity = passedPlace.Vicinity;

    }

    public Place(string newName, string newLocation, string newID, string newVic, List<String>newTypes)
    {
        _Name = newName;
        _ID = newID;
        _Location = newLocation;
        _types = newTypes;
        _vicinity = newVic;
    }

    public Place(string newName, string newLocation, string newID)
    {
        _Name = newName;
        _ID = newID;
        _Location = newLocation;
        _types = null;
        _vicinity = null;
    }

    
}