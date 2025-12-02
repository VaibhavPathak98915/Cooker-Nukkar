package resources;

public class ListItem{
    private int id=-1;
    private String title;

    public ListItem(String title){
        this.title=title;
    }
    public ListItem(int id, String title){
        this.id=id;
        this.title=title;
    }
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    @Override
    public String toString(){
        return title;
    }
}