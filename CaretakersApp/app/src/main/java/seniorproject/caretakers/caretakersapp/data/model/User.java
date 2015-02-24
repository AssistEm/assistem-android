package seniorproject.caretakers.caretakersapp.data.model;

import android.util.JsonReader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import dagger.Provides;

/**
 * Created by Stephen on 2/18/2015.
 */
public class User extends ApiItem {

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    @SerializedName("phone")
    String phone;

    @SerializedName("type")
    String type;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

}
