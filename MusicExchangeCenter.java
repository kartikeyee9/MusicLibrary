import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import javafx.util.Pair;

public class MusicExchangeCenter {

    // an ArrayList of all registered Users
    private ArrayList<User> users;

    // – an ArrayList<Song> containing all of the songs that have been
    // downloaded
    private ArrayList<Song> downloadedSongs;

    // a HashMap with the artist’s name as the keys and the values are floats
    // representing the total amount of royalties for that artist so far
    private HashMap<String, Float> royalties;

    public MusicExchangeCenter() {
	users = new ArrayList<>();
	royalties = new HashMap<>();
	downloadedSongs = new ArrayList<>();
    }

    // returns an ArrayList of all Users that are currently online
    public ArrayList<User> onlineUsers() {
	ArrayList<User> onlineUsers = new ArrayList<>();
	for (User user : users) {
	    if (user.isOnline())
		onlineUsers.add(user);
	}

	return onlineUsers;
    }

    // returns a new ArrayList of all Songs currently available for download
    // (i.e., all songs that are available from all logged-on users).
    public ArrayList<Song> allAvailableSongs() {
	ArrayList<Song> availableSongs = new ArrayList<>();
	ArrayList<User> onlineUsers = onlineUsers();
	for (User user : onlineUsers) {
	    availableSongs.addAll(user.getSongList());
	}

	return availableSongs;
    }

    // returns a string representation of the music center
    public String toString() {
	return String.format("Music Exchange Center (%d users on line, %d songs available)", onlineUsers().size(),
		allAvailableSongs().size());
    }

    // finds and returns the user object with the given name
    public User userWithName(String s) {
	for (User user : users) {
	    if (user.getUserName().equals(s))
		return user;
	}

	return null;
    }

    // adds a given User to the music center’s list of users
    public void registerUser(User x) {
	if (userWithName(x.getUserName()) == null)
	    users.add(x);
    }

    // t returns a new ArrayList of all Songs currently available for download
    // by the specified artist.
    public ArrayList<Song> availableSongsByArtist(String artist) {
	ArrayList<Song> availableSongs = allAvailableSongs();

	ArrayList<Song> songsArtist = new ArrayList<>();
	for (Song song : availableSongs) {
	    if (song.getArtist().equals(artist))
		songsArtist.add(song);
	}

	return songsArtist;
    }

    
    // returns the song with given title and owner
    public Song getSong(String title, String ownerName) {
	User user = userWithName(ownerName);
	if (user != null && user.isOnline()) {
	    Song song = user.songWithTitle(title);
	    if (song != null) {
		downloadedSongs.add(song);
		if (!royalties.containsKey(song.getArtist()))
		    royalties.put(song.getArtist(), 0.0f);

		royalties.put(song.getArtist(), royalties.get(song.getArtist()) + 0.25f);
	    }

	    return song;
	}

	return null;
    }

    
    //  displays the royalties for all artists who have had at least one of their songs downloaded
    public void displayRoyalties() {
	System.out.printf("%-8s %s\n", "Amount", "Artist");
	System.out.println("---------------");
	for (Entry<String, Float> entry : royalties.entrySet()) {
	    System.out.printf("$%-7.2f %s\n", entry.getValue(), entry.getKey());
	}
    }

    public ArrayList<Song> getDownloadedSongs() {
	return downloadedSongs;
    }

    
    // a TreeSet of all downloaded Song objects such that the set is sorted alphabetically by song title
    public TreeSet<Song> uniqueDownloads() {
	TreeSet<Song> set = new TreeSet<>(new Comparator<Song>() {
	    @Override
	    public int compare(Song o1, Song o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	    }
	});

	set.addAll(downloadedSongs);

	return set;
    }

    
    //  an ArrayList of Pair objects where the key of the pair is an Integer and the value is the Song object.
    public ArrayList<Pair<Integer, Song>> songsByPopularity() {
	ArrayList<Pair<Integer, Song>> songsByPopularity = new ArrayList<>();

	for (Song song : uniqueDownloads()) {
	    songsByPopularity.add(new Pair<Integer, Song>(countDownloads(song), song));
	}

	Collections.sort(songsByPopularity, new Comparator<Pair<Integer, Song>>() {
	    public int compare(Pair<Integer, Song> p1, Pair<Integer, Song> p2) {
		return p2.getKey() - p1.getKey();
	    }
	});

	return songsByPopularity;

    }

    
    // cunt number of downloads of a song
    private int countDownloads(Song song) {
	int total = 0;
	for (Song downloaded : downloadedSongs) {
	    if (downloaded == song)
		total++;
	}

	return total;
    }
}
