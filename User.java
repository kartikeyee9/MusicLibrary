import java.util.ArrayList;

public class User {
    private String userName;
    private boolean online;
    // list of songs
    private ArrayList<Song> songList;

    public User() {
	this("");
    }

    public User(String u) {
	userName = u;
	online = false;
	// create empty list
	songList = new ArrayList<>();
    }

    public String getUserName() {
	return userName;
    }

    public boolean isOnline() {
	return online;
    }

    public String toString() {
	String s = "" + userName + ": " + songList.size() + " songs (";
	if (!online)
	    s += "not ";
	return s + "online)";
    }

    // add a song and set owner
    public void addSong(Song s) {
	songList.add(s);
	s.setOwner(this);
    }

    // total song time
    public int totalSongTime() {
	int total = 0;
	for (Song song : songList) {
	    total += song.getDuration();
	}
	return total;
    }

    public ArrayList<Song> getSongList() {
	return songList;
    }

    public void register(MusicExchangeCenter m) {
	m.registerUser(this);
    }

    // log on if not already logged and registered
    public void logon(MusicExchangeCenter m) {
	if (!this.online) {
	    User user = m.userWithName(userName);
	    if (user != null)
		user.online = true;
	}
    }
    // log on if logged in and registered
    public void logoff(MusicExchangeCenter m) {
	if (this.online) {
	    User user = m.userWithName(userName);
	    if (user != null)
		user.online = false;
	}
    }

    // This method should gather the list of all available songs from all users
    // that are online at the given music exchange center
    public ArrayList<String> requestCompleteSonglist(MusicExchangeCenter m) {
	ArrayList<Song> allAvailableSongs = m.allAvailableSongs();
	return songsToStrings(allAvailableSongs);
    }

    // This method should gather the list of all available songs by the given
    // artist from all users that are online at the given music exchange cente
    public ArrayList<String> requestSonglistByArtist(MusicExchangeCenter m, String artist) {
	ArrayList<Song> allAvailableSongs = m.availableSongsByArtist(artist);
	return songsToStrings(allAvailableSongs);
    }

    // helper method to convert songs to strings
    private ArrayList<String> songsToStrings(ArrayList<Song> list) {
	ArrayList<String> completeSongList = new ArrayList<>();

	completeSongList.add(String.format("%4s %-30s %-20s %-10s %s", "", "TITLE", "ARTIST", "TIME", "OWNER"));

	int i = 1;
	for (Song song : list) {
	    completeSongList.add(String.format("%3d. %-30s %-20s %-10s %s", i++, song.getTitle(), song.getArtist(),
		    String.format("%d:%02d", song.getMinutes(), song.getSeconds()), song.getOwner().getUserName()));
	}

	return completeSongList;
    }

    
    // returns song with given title from list
    public Song songWithTitle(String title) {
	for (Song song : songList) {
	    if (song.getTitle().equals(title))
		return song;
	}

	return null;
    }

    
    //  simulates the downloading of one of the songs in the catalog
    public void downloadSong(MusicExchangeCenter m, String title, String ownerName) {
	Song song = m.getSong(title, ownerName);
	if (song != null)
	    songList.add(song);
    }

    // Various Users for test purposes
    public static User DiscoStew() {
	User discoStew = new User("Disco Stew");
	discoStew.addSong(new Song("Hey Jude", "The Beatles", 4, 35));
	discoStew.addSong(new Song("Barbie Girl", "Aqua", 3, 54));
	discoStew.addSong(new Song("Only You Can Rock Me", "UFO", 4, 59));
	discoStew.addSong(new Song("Paper Soup Cats", "Jaw", 4, 18));
	return discoStew;
    }

    public static User SleepingSam() {
	User sleepingSam = new User("Sleeping Sam");
	sleepingSam.addSong(new Song("Meadows", "Sleepfest", 7, 15));
	sleepingSam.addSong(new Song("Calm is Good", "Waterfall", 6, 22));
	return sleepingSam;
    }

    public static User RonnieRocker() {
	User ronnieRocker = new User("Ronnie Rocker");
	ronnieRocker.addSong(new Song("Rock is Cool", "Yeah", 4, 17));
	ronnieRocker.addSong(new Song("My Girl is Mean to Me", "Can't Stand Up", 3, 29));
	ronnieRocker.addSong(new Song("Only You Can Rock Me", "UFO", 4, 52));
	ronnieRocker.addSong(new Song("We're Not Gonna Take It", "Twisted Sister", 3, 9));
	return ronnieRocker;
    }

    public static User CountryCandy() {
	User countryCandy = new User("Country Candy");
	countryCandy.addSong(new Song("If I Had a Hammer", "Long Road", 4, 15));
	countryCandy.addSong(new Song("My Man is a 4x4 Driver", "Ms. Lonely", 3, 7));
	countryCandy.addSong(new Song("This Song is for Johnny", "Lone Wolf", 4, 22));
	return countryCandy;
    }

    public static User PeterPunk() {
	User peterPunk = new User("Peter Punk");
	peterPunk.addSong(new Song("Bite My Arms Off", "Jaw", 4, 12));
	peterPunk.addSong(new Song("Where's My Sweater", "The Knitters", 3, 41));
	peterPunk.addSong(new Song("Is that My Toenail ?", "Clip", 4, 47));
	peterPunk.addSong(new Song("Anvil Headache", "Clip", 4, 34));
	peterPunk.addSong(new Song("My Hair is on Fire", "Jaw", 3, 55));
	return peterPunk;
    }
}
