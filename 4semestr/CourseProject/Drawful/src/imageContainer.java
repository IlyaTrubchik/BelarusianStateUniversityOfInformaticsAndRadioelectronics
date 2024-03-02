public class imageContainer {
    byte[] image;
    String playerName = "UNKNOWN";

    String imageMeaning="UNKNOWN";
   public imageContainer(byte[] image,String name,String word)
   {
       this.image = image;
       this.imageMeaning = word;
       this.playerName = name;
   }
}
