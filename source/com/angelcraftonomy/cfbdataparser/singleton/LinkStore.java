package com.angelcraftonomy.cfbdataparser.singleton;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class LinkStore {
	private HashMap<String, String> links;
	private ArrayList<String> files;

	// Singleton initiation ensures one object creation
	private static LinkStore INSTANCE = null;

	public static LinkStore getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LinkStore();
		}
		return INSTANCE;
	}

	private LinkStore() {
		links = new HashMap<String, String>();
		files = new ArrayList<String>();
	}
	// End Singleton

	public void saveLink(String link) {
		// Emit white spaces before and after the string
		link = link.trim();
		// Do not attempt to connect to empty string urls
		if (link.length() == 0)
			return;
		// no duplicate links will be saved
		try {
			URL url = new URL(link);
			String extension = FilenameUtils.getExtension(url.getPath());
			if (extension.length() > 0 && !extension.contains("html") && !extension.contains("aspx")) {
				// save as file
				this.saveFile(link);
			} else {
				// save as link
				links.put(link, link);
			}
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + link);
		}

	}

	private void saveFile(String file) {
		files.add(file);

	}

	public ArrayList<String> getLinks() {
		ArrayList<String> retVal = new ArrayList<String>();
		// returns all links in an array list format
		retVal.addAll(links.values());
		Collections.sort(retVal);
		return retVal;
	}

	public ArrayList<String> getFiles() {
		Collections.sort(files);
		return files;
	}

	public void downloadFiles() {
		System.out.println("\nDownloading " + files.size() + " files:");
		String dirName = "files/";
		int fileNumber = 0;
		for (String file : files) {
			try {
				URL url = new URL(file);
				File dir = new File(dirName);
				if (!dir.exists())
					dir.mkdirs();
				File fileName = new File(dirName + "(" + fileNumber + ")" + FilenameUtils.getName(url.getPath()));
				// System.out.println(fileName.toString());
				// Open a URL Stream
				Response resultResponse = Jsoup.connect(file).ignoreContentType(true).execute();

				// output here
				FileOutputStream out = (new FileOutputStream(fileName));
				out.write(resultResponse.bodyAsBytes());
				out.close();
				fileNumber++;
			} catch (Exception e) {
				System.out.println("Could not download file: " + file);
			}
		}
		System.out.println("Download Completed!");
	}

	@Override
	public String toString() {
		return "links = " + links.size() + "\nfiles = " + files.size();
	}

}
