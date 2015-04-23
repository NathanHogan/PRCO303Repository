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

    public bool checkDuplicateReview(string UID, string PID)
    {
        bool duplicate = true;
        string retrievedReview = null;

        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT * FROM reviews WHERE FBID=? AND PlaceID=?;", conn);

        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@FBID", UID);
                com.Parameters.AddWithValue("@PlaceID", PID);
                OdbcDataReader reader = com.ExecuteReader();
                while (reader.Read())
                {
                    retrievedReview = (string)reader["FBID"];
                }
                conn.Close();
            }
        }

        if (retrievedReview == null)
        {
            duplicate = false;
        }
        else
        {
            duplicate = true;
        }


        return duplicate;
    }

    public bool checkDuplicatePerson(string UID)
    {
        bool duplicate = true;

        return duplicate;
    }

    public bool checkDuplicateFavourite(string UID, string PID)
    {
        bool duplicate = false;
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT * FROM favourites WHERE FBID=? AND PlaceID=?;", conn);
        string returnedStr = null;

        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@FBID", UID);
                com.Parameters.AddWithValue("@PID", PID);
                OdbcDataReader reader = com.ExecuteReader();
                while (reader.Read())
                {
                    returnedStr = (string)reader["FBID"];
                }
                conn.Close();
            }
        }

        if (returnedStr == null)
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
        bool duplicate = false;
        duplicate = checkDuplicateFavourite(UID, PID);

        if (!duplicate)
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
    }

    public List<Place> getFavourites(string UID)
    {
        List<Place> favouriteList = new List<Place>();
        Place favouritedPlace = new Place();
        List<String> favouriteID = getFavouriteIDs(UID);
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT * FROM place WHERE PlaceID=?", conn);
        OdbcDataReader reader;

        using (conn)
        {
            using (com)
            {
                conn.Open();
                for (int i = 0; i < favouriteID.Count; i++)
                {
                    com.Parameters.AddWithValue("PID", favouriteID[i]);
                    reader = com.ExecuteReader();
                    while (reader.Read())
                    {
                        favouritedPlace = new Place((string)reader["Name"], (string)reader["Location"], (string)reader["PlaceID"]);
                        favouriteList.Add(favouritedPlace);
                    }
                    com.Parameters.Clear();
                    reader.Close();
                }
                conn.Close();
            }
        }

        return favouriteList;
    }

    public List<String> getFavouriteIDs(string UID)
    {
        List<String> favouriteID = new List<String>();
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT (PlaceID) FROM favourites WHERE FBID=?", conn);

        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@FBID", UID);
                OdbcDataReader reader = com.ExecuteReader();
                while (reader.Read())
                {
                    string placeID = (string)reader["PlaceID"];
                    favouriteID.Add(placeID);
                }
                conn.Close();
            }
        }


        return favouriteID;
    }

    public void deleteFavourite(string UID, string PID)
    {
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("DELETE FROM favourites WHERE FBID=? AND PlaceID=?", conn);
        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@UID", UID);
                com.Parameters.AddWithValue("@PID", PID);
                com.ExecuteReader();
                conn.Close();
            }
        }
    }

    public bool checkRemainingPlaceFavourite(string PID)
    {
        bool duplicate = false;
        string retrievedID= null;
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT PlaceID FROM favourites WHERE PlaceID=?;", conn);
        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@PID", PID);
                OdbcDataReader reader = com.ExecuteReader();
                while (reader.Read())
                {
                    retrievedID = (string)reader["PlaceID"];
                }
                conn.Close();
            }
        }

        if (retrievedID == null)
        {
            duplicate = false;
        }
        else
        {
            duplicate = true;
        }

        return duplicate;
    }

    public void deletePlace(string PID)
    {
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("DELETE FROM place WHERE PlaceID=?;", conn);
        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@PID", PID);
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

    public List<Review> getReviewList(string PID)
    {
        List<Review> reviewList = new List<Review>();
        Review newReview = new Review();
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT * FROM reviews WHERE PlaceID=?;", conn);
        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@PID", PID);
                OdbcDataReader reader = com.ExecuteReader();
                while (reader.Read())
                {
                    newReview = new Review((string)reader["FBID"], (string)reader["PlaceID"], (int)reader["Rating"], (string)reader["Review"]);
                    reviewList.Add(newReview);
                }
                conn.Close();
            }
        }

        return reviewList;
    }

    public Review getSingleReview(string PID, string UID)
    {
        Review requestedReview = new Review();
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("SELECT * FROM reviews WHERE FBID=? AND PlaceID=?;", conn);

        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@UID", UID);
                com.Parameters.AddWithValue("@PID", PID);
                OdbcDataReader reader = com.ExecuteReader();
                while (reader.Read())
                {
                    requestedReview = new Review((string)reader["FBID"], (string)reader["PlaceID"], (int)reader["Rating"], (string)reader["Review"]);
                }
                conn.Close();
            }

        }

        return requestedReview;
    }

    public void postReview(Review postedReview)
    {
        bool duplicate = false;
        duplicate = checkDuplicateReview(postedReview.UID, postedReview.PID);

        if (!duplicate)
        {
            OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
            OdbcCommand com = new OdbcCommand("INSERT INTO reviews (FBID, PlaceID, Rating, Review)" +
                "VALUES (?,?,?,?);", conn);
            using (conn)
            {
                using (com)
                {
                    conn.Open();
                    com.Parameters.AddWithValue("@FBID", postedReview.UID);
                    com.Parameters.AddWithValue("@PlaceID", postedReview.PID);
                    com.Parameters.AddWithValue("@Rating", postedReview.rating);
                    com.Parameters.AddWithValue("@RatingText", postedReview.reviewText);
                    com.ExecuteReader();
                    conn.Close();
                }
            }
        }
        else
        {
            updateReview(postedReview);
        }

    }

    public void updateReview(Review updatedReview)
    {
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("UPDATE reviews SET Rating=?,Review=? WHERE FBID=? AND PlaceID=?;", conn);
        using (conn)
        {
            using(com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@Rating", updatedReview.rating);
                com.Parameters.AddWithValue("@Review", updatedReview.reviewText);
                com.Parameters.AddWithValue("@FBID", updatedReview.UID);
                com.Parameters.AddWithValue("@PlaceID", updatedReview.PID);
                com.ExecuteReader();
                conn.Close();
            }
        }
    }

    public void deleteReview(string PID, string UID)
    {
        OdbcConnection conn = new OdbcConnection(_DatabaseConnection);
        OdbcCommand com = new OdbcCommand("DELETE FROM reviews WHERE FBID=? AND PlaceID=?;", conn);
        using (conn)
        {
            using (com)
            {
                conn.Open();
                com.Parameters.AddWithValue("@UID", UID);
                com.Parameters.AddWithValue("@PID", PID);
                com.ExecuteReader();
                conn.Close();
            }
        }
    }

    public void addNewUser()
    {

    }
}