package dell.kinvey;

/**
 * Created by dell on 10/21/2015.
 */
public class GridViewItemData
{

    String imagePath;
    String userName;
    String id;
    GridViewItemData(String id,String imagePath,String UserName)

    {
        this.id=id;
        this.imagePath=imagePath;
        this.userName=UserName;
    }

    GridViewItemData(String imagePath)
    {

        this.imagePath
                =imagePath;

    }
    GridViewItemData()
    {

        this.imagePath=null;
        this.id=null;

    }
    public String getImagePath()
    {
        return imagePath;
    }
    public void setImagePath(String imagePath)
    {
        this.imagePath=imagePath;
    }

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id=id;
    }
    public String getUserName()

    {
        return userName;
    }
    public void setUserName(String userName)
    {
        this.userName=userName;
    }
}