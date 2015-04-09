package seniorproject.caretakers.caretakersapp.data.api;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import seniorproject.caretakers.caretakersapp.data.model.Community;
import seniorproject.caretakers.caretakersapp.data.model.Event;
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

    @POST("/communities/{community_id}/events/{event_id")
    public Observable<Event> updateEvent(@Path("community_id") String communityId,
                                         @Path("event_id") String eventId,
                                         @Body Event event);

    @POST("/communities/{community_id}/events/{event_id}")
    public Observable<Event> volunteer(@Path("community_id") String communityId,
                                       @Path("event_id") String eventId,
                                       @Body VolunteerRequest volunteerRequest);




}
