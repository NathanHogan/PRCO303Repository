using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Odbc;
using System.Web.Configuration;

/// <summary>
/// Summary description for DatabaseLayer
/// </summary>
/// 

//NEED DATABASE VALIDATION. REMEMBER.
public class DatabaseLayer
{
    private string _DatabaseConnection = WebConfigurationManager.ConnectionStrings["DatabaseConnection"].ConnectionString;
	public DatabaseLayer()
	{
	}

    public void DatabaseTestConnection()
    {
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT * FROM Place;", conn);

        using (conn)
        {
            conn.Open();
            com.ExecuteReader();
            conn.Close();
        }
    }

    public string addNewPlace(Place newPlace)
    {
        if (!checkDuplicatePlace(newPlace.ID))
        {
            OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
            OdbcCommand com = new OdbcCommand("INSERT INTO place (PlaceID, Name, Location)" + 
                "VALUES ( ?, ?, ?);", conn);
            using (conn)
            {
                using (com)
                {
                    conn.Open();
                    com.Parameters.AddWithValue("@pid", newPlace.ID);
                    com.Parameters.AddWithValue("@name", newPlace.Name);
                    com.Parameters.AddWithValue("@location", newPlace.Location);
                    com.ExecuteNonQuery();
                    conn.Close();
                }
            } 
        }

        return checkDuplicatePlace(newPlace.ID).ToString();
    }

    public bool checkDuplicatePlace(string PID)
    {
        bool duplicate = false;
        string result = null;

        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT * FROM place WHERE PlaceID=?;", conn);
        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@PID", PID);
                OdbcDataReader reader = com.ExecuteReader();
                while (reader.Read())
                {
                    result = (string)reader[0];
                }
                conn.Close();
            }
        }

        if (result == null)
        {
            duplicate = false;
        }
        else
        {
            duplicate = true;
        }

        return duplicate;
    }

    public void addNewFavourite(string UID, string PID)
    {
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("INSERT INTO favourites (PlaceID, FBID)" +
            "VALUES(?, ?);", conn);

        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@PID", PID);
                com.Parameters.AddWithValue("@UID", UID);
                com.ExecuteReader();
                conn.Close();
            }
        }
    }

    public UserSettings getSettings(string UID)
    {
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT Types, Radius FROM person WHERE FBID=?;", conn);
        UserSettings retrievedSettings = new UserSettings();
        string types = "";

        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@UID", UID);
                OdbcDataReader reader = com.ExecuteReader();
                while (reader.Read())
                {
                    types = (string)reader["Types"];
                    retrievedSettings.radius = (int)reader["Radius"];
                }
                conn.Close();
            }
        }

        string[] typesArray = types.Split('|');
        for (int i = 0; i < typesArray.Length; i++)
        {
            retrievedSettings.types.Add(typesArray[i]);
        }

        return retrievedSettings;
    }

    public void addNewUser()
    {

    }
}