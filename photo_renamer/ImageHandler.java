package photo_renamer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Handles images
 * 
 * Implements Serializable that allows a list of all edited images to be serialized in 
 * a file
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public class ImageHandler implements Serializable{
	
	/** List of all images that have had tags added to them */
	private ArrayList<Image> editedImages;
	
	/** 
	 * Create a new ImageHandler that initializes and empty ArrayList of edited images 
	 */
	public ImageHandler() {
		this.editedImages = new ArrayList<Image>();
	}
	
	/**
	 * Returns all images within a directory and its sub-directories.
	 * 
	 * @param dir
	 * 			  the directory that is searched
	 * @return
	 * 			  an ArrayList<File> that has all images within a directory
	 */
	public ArrayList<File> getAllImages(File dir) {
		File[] filesAndDir = dir.listFiles();
		ArrayList<File> images = new ArrayList<File>(); 
		for (File file: filesAndDir) {		
			if (file.isDirectory()) {
				images.addAll(getAllImages(file));
			} 
			else{
				if (isImage(file)){
					images.add(file);
				}
			}
		}
		return images;
	}
	
	/**
	 * Returns the names of all files passed from an array list of files
	 * 
	 * @param files
	 * 			  an ArrayList<file> of files
	 * @return
	 * 			  an ArrayList<String> of each file's name
	 */
	public ArrayList<String> changeToName(ArrayList<File> files) {
		ArrayList<String> names = new ArrayList<String>(); 
		for (File file: files) {		
			names.add(file.getName());
			}
		return names;
	}
	
	/**
	 * Finds all images within a directory and its sub-directories.
	 * 
	 * @param dir
	 * 			  the directory that is searched
	 * @param s
	 *            the string that contains all name of all images
	 */
	public String findImages(File dir, String s) {
		File[] filesAndDir = dir.listFiles();
		for (File file: filesAndDir) {		
			if (file.isDirectory()) {
				s = findImages(file, s);
			} 
			else {
				if (isImage(file)){
					s += "\n" + file.getName();
				}
			}
		}
		return s;
	}
	
	/**
	 * Returns true if a file is an image.
	 * 
	 * @param file
	 * 			  file being checked
	 * @return 
	 *            true if the file is an image
	 */
	public boolean isImage(File file) {
		String extension = "";
		int i = file.getName().lastIndexOf(".");
		if (i > 0) {
			extension = file.getName().substring(i+1);
		}
		if (extension.equalsIgnoreCase("jpg")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns ImageHandler object by deserializing if there is an ImagList.txt file
	 * 
	 * @return 		A ImageHandler with previously edited images
	 */
	public static ImageHandler open() {
		ImageHandler handler = null;
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream("ImageList.txt"));
			handler = (ImageHandler) is.readObject();
			is.close();
		} catch (FileNotFoundException e) {
			return handler = new ImageHandler();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return handler;
	}
	
	/**
	 * Serializes ImageHandler object, this.
	 */
	public void close() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("ImageList.txt"));
			os.writeObject(this);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the ImageHandler with an Image editedIm 
	 * 
	 * @param editedIm	
	 * 				The edited Image to record into the ImageHandler, this.
	 */
	public void updateImageList(Image editedIm) {
		ImageHandler.open();
		int i = -1;
		for (Image imInHandler : this.editedImages) {
			if(imInHandler.getCurrName().equals(editedIm.getCurrName())) {
				i = this.editedImages.indexOf(imInHandler);
				this.editedImages.set(i, editedIm);
			}
		}
		if(i == -1){
			this.editedImages.add(editedIm);
		}
		this.close();
	}
	
	/**
	 * Gets the image according to the current name
	 * 
	 * @param name	
	 * 				current name of the image
	 * @return 
	 * 				return the image that matches the current name
	 */
	public Image getImage(String name) {
		Image im = null;
		for(Image imInList : this.editedImages) {
			if(imInList.getCurrName().equals(name)) {
				return imInList;
			}
		}
		return im;
	}
	
	/**
	 * Returns the current Images in editedImages list
	 * 
	 * @return 
	 * 				returns the images that have been edited
	 */
	@Override
	public String toString() {
		String s = "The current Images in editedImages list:";
		System.out.println(this.editedImages.size());
		for (Image im: this.editedImages) {
			s += "\n" + im.toString();
		}
		return s;
	}
	
	//Main Method
	public static void main(String[] args) {
		
//		ImageHandler h = new ImageHandler();
//		String images = h.findImages(new File("C:/Users/pravir/Pictures/Saved Pictures"),"");
//		System.out.println(images);
//		
//		File f1 = new File("C:\\Users\\pravir\\Pictures\\Saved Pictures\\Intro to Evol Antro.jpg");
//		Image im1 = new Image(f1);
//		System.out.println(f1.exists());
//		File f2 = new File("/Users/pravir/Pictures/Saved Pictures/MAT224 - Linear Algebra.jpg");
//		Image im2 = new Image(f2);
//		
//		Image.getTagsCatalog().addTag("Yolo");
//		Image.getTagsCatalog().saveTagsCatalog();
//		
//		im1.addTag("Pravir");
//		im1.addTag("chad");
//		System.out.println(im1.toString());
//		im1.changeName();
//		System.out.println(im1.toString());
//		im2.addTag("Pravir");
//		im2.changeName();
//		ImageHandler ih = ImageHandler.open();
//		System.out.println(ih.toString());
		
//		Image im4 = ih.getImage("Intro to Evol Antro @chad");
//		System.out.println(im4.getCurrName());
//		
//		File file = new File("C:\\Users\\pravir\\Pictures\\Saved Pictures\\Intro to Evol Antro @chad.jpg");
//		Image im3 = new Image(file);
//		System.out.println(im3.getOriginalName());

		
	}
}


