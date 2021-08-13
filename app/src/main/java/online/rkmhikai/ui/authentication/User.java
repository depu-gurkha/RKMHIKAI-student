package online.rkmhikai.ui.authentication;
//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
    public class User {


        private String email,session_id,name,userType,stream,gender,dateOfBirth,profilePicture,accessToken,refreshToken,tokenExpiry,standard, participantId;

    public User(String email, String session_id, String name, String userType, String stream, String gender, String dateOfBirth, String profilePicture, String accessToken, String refreshToken, String tokenExpiry, String standard,String participantId) {
        this.email = email;
        this.session_id = session_id;
        this.name = name;
        this.userType = userType;
        this.stream = stream;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.profilePicture = profilePicture;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenExpiry = tokenExpiry;
        this.standard=standard;
        this.participantId=participantId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public String getEmail() {
        return email;
    }

    public String getSession_id() {
        return session_id;
    }

    public String getName() {
        return name;
    }

    public String getUserType() {
        return userType;
    }

    public String getStream() {
        return stream;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getStandard() {
        return standard;
    }

    public String getTokenExpiry() {
        return tokenExpiry;
    }
}

