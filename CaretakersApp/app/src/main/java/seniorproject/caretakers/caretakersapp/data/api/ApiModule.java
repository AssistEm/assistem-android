package seniorproject.caretakers.caretakersapp.data.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import seniorproject.caretakers.caretakersapp.ui.fragments.LoginFragment;

/**
 * Created by Stephen on 2/18/2015.
 */
@Module(complete = false,
        library = true,
        injects = {
                LoginFragment.class
        })
public class ApiModule {

    // TODO: What is the endpoint?
    private static final String API_URL = "http://sca.jasonmsu.com/api";

    @Provides @Singleton
    Client provideClient() {
        return new OkClient();
    }

    @Provides @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(API_URL);
    }

    @Provides @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    @Provides @Singleton
    CommunitiesApi provideCommunitiesApi(RestAdapter restAdapter) {
        return restAdapter.create(CommunitiesApi.class);
    }

    @Provides @Singleton
    UserApi provideUserApi(RestAdapter restAdapter) {
        return restAdapter.create(UserApi.class);
    }

}
