package seniorproject.caretakers.caretakersapp.data.api;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import seniorproject.caretakers.caretakersapp.data.model.Community;
import seniorproject.caretakers.caretakersapp.data.model.DeleteRepeating;
import seniorproject.caretakers.caretakersapp.data.model.Event;
import seniorproject.caretakers.caretakersapp.data.model.GroceryItem;
import seniorproject.caretakers.caretakersapp.data.model.VolunteerRequest;

/**
 * Created by Stephen on 2/18/2015.
 */
public interface CommunitiesApi {

    @GET("/communities")
    public Observable<List<Community>> communities();

    @POST("/communities")
    public Observable<Community> create(@Body Community community);

    @POST("/communities/{id}")
    public Observable<Community> update(@Path("id") String id,
                                        @Body Community community);

    @DELETE("/communties/{id}")
    public void delete(@Path("id") String id);

    @GET("/communities/{id}/events")
    public Observable<List<Event>> events(@Path("id") String id,
                                          @Query("months") int monthsAgo);

    @POST("/communities/{id}/events")
    public Observable<List<Event>> addEvent(@Path("id") String id,
                                            @Body Event event);

    @DELETE("/communities/{id}/events")
    public Observable<List<Event>> deleteEvent(@Path("community_id") String communityId,
                                               @Path("event_id") long eventId,
                                               @Body DeleteRepeating deleteRepeating);


    @POST("/communities/{community_id}/events/{event_id")
    public Observable<Event> updateEvent(@Path("community_id") String communityId,
                                         @Path("event_id") String eventId,
                                         @Body Event event);

    @POST("/communities/{community_id}/events/{event_id}")
    public Observable<Event> volunteer(@Path("community_id") String communityId,
                                       @Path("event_id") String eventId,
                                       @Body VolunteerRequest volunteerRequest);

    @GET("/communities/{community_id}/groceries")
    public Observable<List<GroceryItem>> groceryItems(@Header("Authorization") String auth,
                                                      @Path("community_id") String id);


    @POST("/communities/{community_id}/groceries")
    public Observable<List<GroceryItem>> addGroceryItem(@Header("Authorization") String auth,
                                                        @Path("community_id") String id,
                                                        @Body List<GroceryItem> items);

    @POST("/communities/{community_id}/groceries/{item_id}")
    public Observable<List<GroceryItem>> updateGroceryItem(@Header("Authorization") String auth,
                                                           @Path("community_id") String communityId,
                                                           @Path("item_id") String itemId,
                                                           @Body List<GroceryItem> items);

    @PUT("/communities/{community_id}/groceries/{item_id}")
    public Observable<List<GroceryItem>> volunteerGroceryItem(@Header("Authorization") String auth,
                                                              @Path("community_id") String communityId,
                                                              @Path("item_id") String itemId);

    @POST("/communities/{community_id}/groceries/{item_id}")
    public Observable<List<GroceryItem>> deleteGroceryItem(@Header("Authorization") String auth,
                                                           @Path("community_id") String communityId,
                                                           @Path("item_id") String itemId);

}
