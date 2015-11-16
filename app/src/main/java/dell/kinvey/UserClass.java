package dell.kinvey;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.kinvey.java.model.KinveyMetaData;

/**
 * Created by dell on 10/22/2015.
 */
public class UserClass extends GenericJson{
    @Key("_id")
    private String id;
    @Key
    private String userName;
    @Key
    private String imagePath;
    @Key("_kmd")
    private KinveyMetaData meta; // Kinvey metadata, OPTIONAL
    @Key("_acl")
    private KinveyMetaData.AccessControlList acl; //Kinvey access control, OPTIONAL
    public UserClass(){}

    public void setUserName(String UserName)
    {
        this.userName=UserName;
    }
    public String getUserName()
    {
        return userName;
    }
    public void setImagePath(String ImagePath)
    {
        this.imagePath=ImagePath;
    }
    public String getImagePath()
    {
        return imagePath;
    }



}
