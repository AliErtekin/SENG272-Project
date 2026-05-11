//kullanıcı profil bilgileri burda
//1. adımda (Profile ekranında) girilen veriler burada
public class Profile {

    private String username;
    private String school;
    private String sessionName;

    //boş profil oluşturma
    public Profile() {
        this.username    = "";
        this.school      = "";
        this.sessionName = "";
    }


    public Profile(String username, String school, String sessionName) {
        this.username    = username;
        this.school      = school;
        this.sessionName = sessionName;
    }

    //get'ler
    public String getUsername()    { return username; }
    public String getSchool()      { return school; }
    public String getSessionName() { return sessionName; }

    //set'ler
    public void setUsername(String u)    { this.username    = u; }
    public void setSchool(String s)      { this.school      = s; }
    public void setSessionName(String s) { this.sessionName = s; }

    // stringe donusturme
    @Override
    public String toString() {
        return "Profile{username='" + username
             + "', school='" + school
             + "', session='" + sessionName + "'}";
    }
}
