public class MovieDBItem implements Comparable<MovieDBItem> {

    private final String genre;
    private final String title;

    public MovieDBItem(String genre, String title) {
        if (genre == null) throw new NullPointerException("genre");
        if (title == null) throw new NullPointerException("title");

        this.genre = genre;
        this.title = title;
    }

    public String getGenre() {return genre;}

    public String getTitle() {return title;}

    @Override
    public int compareTo(MovieDBItem other) {
      if (this.genre == null || this.title == null) {throw new NullPointerException();}
      if (other == null || other.genre == null || other.title == null) {throw new NullPointerException();}

      int compared = this.genre.compareTo(other.genre);

      if (compared == 0) {compared = this.title.compareTo(other.title);}

      return compared;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;}
        
        MovieDBItem other = (MovieDBItem) obj;
        
        if (genre == null) {
            if (other.genre != null) {return false;}
        } else if (!genre.equals(other.genre)) {return false;}
        
        if (title == null) {
            if (other.title != null) {return false;}
        } else if (!title.equals(other.title)) {return false;}
        
        return true;
    }
}
