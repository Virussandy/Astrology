package astrology.in;

import com.google.firebase.database.Exclude;

public class Data {

    public String  title,discription;
    private String mKey;

    public Data() {
    }

    public Data(String Title, String Discription) {
        this.title = Title;
        this.discription = Discription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }


}
