public class MovieDB {
	private MyLinkedList<Genre> db;
	
    public MovieDB() {db = new MyLinkedList<Genre>();}

    public void insert(MovieDBItem item) {
    	String inputGenre = item.getGenre();
    	boolean existingGenre = false;
    	
    	for(Genre genre : db) {
    		if(inputGenre.equals(genre.getGenre())) {
    			existingGenre = true;
    			genre.insert(item.getTitle());
    			break;
    		}
    	}
    	
    	if(!existingGenre) {
    		Genre newGenre = new Genre(item.getGenre());
    		newGenre.insert(item.getTitle());
    		db.insert(newGenre);
    	}
    }

    public void delete(MovieDBItem item) {
        String inputGenre = item.getGenre();
    	String inputTitle = item.getTitle();
        
        for(Genre genre : db) {
        	if(inputGenre.equals(genre.getGenre())) {
        		genre.delete(inputTitle);
        		
        		if(genre.isEmpty()) {db.delete(genre);}
        		break;
        	}
        }
    }

    public MyLinkedList<MovieDBItem> search(String term) { 
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        
        for(Genre genre : db) {
        	MyLinkedList<MovieDBItem> found = genre.search(term);
        	for(MovieDBItem movie : found) {results.insert(movie);}
        }

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        
        for(Genre genre : db) {
        	MyLinkedList<MovieDBItem> movieList = genre.getMovieList();
        	for(MovieDBItem movie : movieList) {
        		results.insert(movie);
        	}
        }
        
    	return results;
    }
}

class Genre implements Comparable<Genre> {
    private MyLinkedList<MovieDBItem> movieList;
    private String name;
    
    public Genre(String name) {
        this.name = name;
        movieList = new MyLinkedList<MovieDBItem>();
    }
    
    public String getGenre() {return this.name;}
    
    public MyLinkedList<MovieDBItem> getMovieList() {return this.movieList;}
    
    public void insert(String title) {  	
    	for(MovieDBItem movie : movieList) {
    		if(movie.getTitle().compareTo(title) == 0) return;
    	}
    	movieList.insert(new MovieDBItem(name, title));
    }	
    
    public void delete(String title) {
    	MovieDBItem m = new MovieDBItem(name, title);

    	for(MovieDBItem movie : movieList) {
    		if(movie.getTitle().compareTo(m.getTitle()) == 0) {
    			movieList.delete(m);
    			return;
    		}
    	}
    }
    
    public MyLinkedList<MovieDBItem> search(String term) {
    	MyLinkedList<MovieDBItem> found = new MyLinkedList<MovieDBItem>();
    	
    	for(MovieDBItem movie : movieList) {
    		if(movie.getTitle().indexOf(term) != -1) {found.insert(movie);}
    	}
    	return found;
    }
    
    public boolean isEmpty() {return movieList.isEmpty();}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (this.getClass() != obj.getClass()) {return false;}
        
        Genre other = (Genre) obj;
        
        if (this.name == null) {
            if (other.name != null) {return false;}
        } else if (!this.name.equals(other.name)) {return false;}
        
        return true;
    }
    
    @Override
    public int compareTo(Genre o) {return this.getGenre().compareTo(o.getGenre());}
}
