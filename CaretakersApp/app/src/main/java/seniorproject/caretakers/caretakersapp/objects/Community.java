package seniorproject.caretakers.caretakersapp.objects;

public class Community {

    long mCommunityId;
    String mCommunityName;
    Patient mPatient;

    public Community(long communityId, String communityName, Patient patient) {
        mCommunityId = communityId;
        mCommunityName = communityName;
        mPatient = patient;
    }
}