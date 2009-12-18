package org.flashmonkey.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {

	public static String getFileContents(String fname) {
		try {
			File src = new File(fname);
			InputStream in = new FileInputStream(src);
			StringBuffer contents = new StringBuffer("");

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				contents.append(new String(buf, "utf-8"));
			}
			in.close();
			return contents.toString();
		} catch (Exception e) {
			return "";
		}
	}

	public static void writeFileContents(String file, String content) {
		System.out.println("Writing content to " + file);
		writeFileContents(new File(file), content);
	}

	public static void writeFileContents(File file, String content) {
		try {
			System.out.println("file " + file.getAbsolutePath());
			Writer output = new BufferedWriter(new FileWriter(file));
			try {
				// FileWriter always assumes default encoding is OK!
				output.write(content);
			} finally {
				System.out.println("File written");
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean deleteDirectory(String path) {
		return deleteDirectory(new File(path));
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					try {
					boolean success = files[i].delete();
					System.out.println("Deleting file " + files[i].getName() + " " + success);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (path.delete());
	}

	public static void copy(String src, String dst) {
		try {
			copy(new File(src), new File(dst));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Copies src file to dst file.
	// If the dst file does not exist, it is created
	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		in.close();
		out.close();
	}
	
	public static void copyDirectory(File srcPath, File dstPath) throws IOException {

		if (srcPath.isDirectory()) {

			if (!dstPath.exists()) {

				dstPath.mkdir();
			}

			String files[] = srcPath.list();

			for(int i = 0; i < files.length; i++) {
				copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
			}
		} else {

			if(!srcPath.exists()){

				System.out.println("File or directory does not exist.");

			} else {

				InputStream in = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath); 
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];

				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			}
		}

		System.out.println("Directory copied.");
	}
	
	/**
	 * Create a new temporary directory. Use something like
	 * {@link #recursiveDelete(File)} to clean this directory up since it isn't
	 * deleted automatically
	 * @return  the new directory
	 * @throws IOException if there is an error creating the temporary directory
	 */
	public static File createTempDir() throws IOException
	{
	    final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
	    File newTempDir;
	    final int maxAttempts = 9;
	    int attemptCount = 0;
	    do
	    {
	        attemptCount++;
	        if(attemptCount > maxAttempts)
	        {
	            throw new IOException(
	                    "The highly improbable has occurred! Failed to " +
	                    "create a unique temporary directory after " +
	                    maxAttempts + " attempts.");
	        }
	        String dirName = UUID.randomUUID().toString();
	        newTempDir = new File(sysTempDir, dirName);
	    } while(newTempDir.exists());

	    if(newTempDir.mkdirs())
	    {
	        return newTempDir;
	    }
	    else
	    {
	        throw new IOException(
	                "Failed to create temp dir named " +
	                newTempDir.getAbsolutePath());
	    }
	}

	/**
	 * Recursively delete file or directory
	 * @param fileOrDir
	 *          the file or dir to delete
	 * @return
	 *          true iff all files are successfully deleted
	 */
	public static boolean recursiveDelete(File fileOrDir)
	{
	    if(fileOrDir.isDirectory())
	    {
	        // recursively delete contents
	        for(File innerFile: fileOrDir.listFiles())
	        {
	            if(!recursiveDelete(innerFile))
	            {
	                return false;
	            }
	        }
	    }

	    return fileOrDir.delete();
	}

	public static void extractZip(String src, File dest) {
		byte[] buffer = new byte[1024];
		// Copy the contents of the jar into the experiment's temporary directory.
		try {
			// extracts just sizes only.
			ZipFile zipFile = new ZipFile(src);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.isDirectory()) {
					File dir = new File(dest.getPath() + "/" + entry.getName());
					dir.mkdir();
				} else {
					// Extract a file
					System.out.println("Extracting File... " + entry.getName());

					InputStream in = zipFile.getInputStream(entry);
					OutputStream out = new BufferedOutputStream(
							new FileOutputStream(dest.getPath() + "/"
									+ entry.getName()));

					int len;

					while ((len = in.read(buffer)) >= 0) {
						out.write(buffer, 0, len);
					}

					in.close();
					out.close();
				}
			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
