package com.thilek.android.qleneagles_quiz.file_manager;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.util.IProgressReceiver;
import com.thilek.android.qleneagles_quiz.util.Logs;
import com.thilek.android.qleneagles_quiz.util.NetworkException;


import java.io.*;
import java.net.ConnectException;

public class FileService {

	private static int BUFFER = 2 * 1024;

	public static String ASSETS = "assets";

	public static String INTERNAL = "internal";

	public static String CACHE = "cache";

	public static String PUBLIC = "public";

	public static String EXTERNAL = "external";

	public static String EXTERNAL_CACHE = "external_cache";

	private final static String TAG = "FileService";

	private static FileService sharedFileService;

	public static FileService getInstance() {
		if (sharedFileService == null)
			sharedFileService = new FileService();
		return sharedFileService;
	}

	public File[] getFilesInDirectory(String path) {
		File directory = new File(path);
		return directory.listFiles();
	}

	public void createNotExistingDirectories(String path) {
		String directories = new File(path).getParent() + File.separator;
		if (!new File(directories).exists())
			new File(directories).mkdirs();
	}

	public InputStream getInputStream(String location, String dataPath) {
		if (dataPath.startsWith("/"))
			throw new IllegalArgumentException();
		if (location.equals(ASSETS))
			return getInputStreamFromAssets(dataPath);

		return getInputStreamFromOutsideAssets(location, dataPath);
	}

	public String getFileContent(String location, String dataPath) {
		if (dataPath.startsWith("/"))
			throw new IllegalArgumentException();

		if (location.equals(ASSETS))
			return getFileContent(getInputStreamFromAssets(dataPath));

		return getFileContentFromOutsideAssets(location, dataPath);
	}

	private InputStream getInputStreamFromAssets(String dataPath) {
		try {
			AssetManager manager = AppContext.getContext().getAssets();
			return manager.open(dataPath);
		} catch (IOException ioe) {
			throw new FileException(FileException.IO_EXCEPTION, ioe.getMessage() + " path:" + dataPath);
		}
	}

	public String getURL(String location, String dataPath) {
		dataPath = checkPrecedingSlash(dataPath);
		
		String packageName = AppContext.getContext().getPackageName();
		File externalPath = Environment.getExternalStorageDirectory();
		File appFiles = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/files");
		
		if (location.equals(INTERNAL))
			return "file://" + AppContext.getContext().getFilesDir().getAbsolutePath() + dataPath;
		if (location.equals(EXTERNAL))
			return "file://" + appFiles + dataPath;
		if (location.equals(ASSETS))
			return "file:///android_asset" + dataPath;
		if (location.equals(CACHE))
			return "file://" + AppContext.getContext().getCacheDir().getAbsolutePath() + dataPath;
		if (location.equals(PUBLIC))
			return "file://" + externalPath.getAbsolutePath() + dataPath;
		throw new FileException(FileException.WRONG_LOCATION_EXCEPTION, "location: " + location);
	}
	
	public Uri getUri(String location, String dataPath) {
		return Uri.fromFile(new File(getPath(location, dataPath)));
	}
	
	public String getPathAndCreateIfNotExists(String location, String dataPath) {
		String path = getPath(location, dataPath);
		createNotExistingDirectories(path);
		return path;
	}

	public String getPath(String location, String dataPath) {
		
		String packageName = AppContext.getContext().getPackageName();
		File externalPath = Environment.getExternalStorageDirectory();
		File appFiles = new File(externalPath.getAbsolutePath() +
                "/Android/data/" + packageName + "/files");
		
		try {
			dataPath = checkPrecedingSlash(dataPath);
			if (location.equals(INTERNAL))
				return AppContext.getContext().getFilesDir().getAbsolutePath() + dataPath;
			if (location.equals(EXTERNAL))
				return appFiles + dataPath;
			if (location.equals(CACHE))
				return AppContext.getContext().getCacheDir().getAbsolutePath() + dataPath;
			if (location.equals(ASSETS))
				return "file:///android_asset/" + dataPath;
			if (location.equals(PUBLIC))
				return "file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + dataPath;
		} catch (NullPointerException e) {
			// possible problem with getExternalFilesDir(null) returns null
			// maybe ask user to reboot device or try to set path manually or
			// with Environment
			Logs.e(TAG, "Android BUG possible problem with getExternalFilesDir(null) returns null");

		}
		throw new FileException(FileException.WRONG_LOCATION_EXCEPTION, "location:" + location);
	}

	public boolean isExternalStorageMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public boolean checkLocationAvailable(String location) {
		if (FileService.EXTERNAL.equals(location) || FileService.PUBLIC.equals(location))
			return isExternalStorageMounted();
		return true;
	}

	public boolean delete(String location, String dataPath) {
		String path = getPath(location, dataPath);
		return this.delete(path);
	}

	public boolean delete(String absolutePath) {
		File target = new File(absolutePath);
		if (target.isDirectory()) {
			String[] children = target.list();
			for (int i = 0; i < children.length; i++) {
				File file = new File(target, children[i]);
				boolean success = this.delete(file.getAbsolutePath());
				if (!success)
					return false;
			}
		}
		return target.delete();
	}

	private String checkPrecedingSlash(String dataPath) {
		if (!dataPath.startsWith("/"))
			dataPath = "/" + dataPath;
		return dataPath;
	}

	private String getFileContentFromOutsideAssets(String location, String dataPath) {
		InputStream inputStream = getInputStreamFromOutsideAssets(location, dataPath);
		return getFileContent(inputStream);
	}

	private InputStream getInputStreamFromOutsideAssets(String location, String dataPath) {
		try {
			String absolutePath = getPath(location, dataPath);
			File file = new File(absolutePath);
			FileInputStream inputStream = new FileInputStream(file);
			return inputStream;
		} catch (FileNotFoundException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			// fall back to assets
			throw new FileException(FileException.FILE_OUTSIDE_ASSETS_NOT_FOUND_EXCEPTION, e.getMessage());
		}
	}

	private String getFileContent(InputStream inputStream) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(inputStream), BUFFER);
			StringBuilder content = new StringBuilder();

			String row;
			while ((row = in.readLine()) != null) {
				content.append(row);
			}
			return content.toString();
		} catch (IOException e) {
			throw new FileException(FileException.IO_EXCEPTION, e.getMessage());
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	public boolean saveFile(String location, String dataPath, InputStream inputStream, IProgressReceiver progressReceiver) {
		if (!checkLocationAvailable(location))
			throw new FileException(FileException.EXTERNAL_STORAGE_NOT_MOUNTED_EXCEPTION, "location:" + location);
		return saveFile(getPath(location, dataPath), inputStream, progressReceiver);
	}

	private boolean saveFile(String absolutePath, InputStream inputStream, IProgressReceiver progressReceiver) {
		FileOutputStream outputStream = null;
		try {
			createNotExistingDirectories(absolutePath);
			outputStream = new FileOutputStream(absolutePath);
			Logs.d(this.getClass().getName(), "download to path:" + absolutePath);
			return streamOut(inputStream, progressReceiver, outputStream);

		} catch (ConnectException e) {
			Log.e(this.getClass().getName(), "ConnectException:" + e.getMessage());
			throw new NetworkException(NetworkException.IO_EXCEPTION, e.getMessage() + " absolutePath:" + absolutePath);
		} catch (IOException e) {
			Log.e(this.getClass().getName(), "FileException: " + e.getMessage());
			throw new FileException(FileException.IO_EXCEPTION, e.getMessage() + " absolutePath:" + absolutePath);
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (Exception e) {
			}
		}
	}

	private boolean streamOut(InputStream inputStream, IProgressReceiver progressReceiver, FileOutputStream outputStream) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			byte buf[] = new byte[BUFFER];
			bis = new BufferedInputStream(inputStream, buf.length);
			bos = new BufferedOutputStream(outputStream, buf.length);
			int len;
			long progressIntervall = getProgressIntervall(progressReceiver);
			long currentProgress = progressIntervall;
			long overallSize = 0;
			int progressDisplay = 0;
			while ((len = bis.read(buf, 0, buf.length)) > 0) {
				bos.write(buf, 0, len);
				if (progressReceiver != null) {
					overallSize += len;
					if (overallSize > currentProgress) {
						// if (progressReceiver.isTaskToBeCanceled()) {
						// throw new UpdateCanceledException();
						// }
						currentProgress += progressIntervall;
						progressReceiver.setProgress(null, ++progressDisplay, true, getFormattedFileSize(overallSize));
					}
				}
			}
			return true;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}

	private long getProgressIntervall(IProgressReceiver progressReceiver) {
		if (progressReceiver != null && progressReceiver.getMax() > 0)
			return Math.round(progressReceiver.getMax() / 100);
		return 0;
	}

	public long getFreeSpace(String absolutePath) {
		StatFs stats = new StatFs(absolutePath);
		long availableBlocks = stats.getAvailableBlocks();
		long blockSizeInBytes = stats.getBlockSize();
		long freeSpace = availableBlocks * blockSizeInBytes;
		return freeSpace;
	}

	public long getFreeSpace(String location, String dataPath) {
		String absolutePath = getPath(location, dataPath);
		return getFreeSpace(absolutePath);

	}

	public static String getFormattedFileSize(long size) {
		return Formatter.formatFileSize(AppContext.getContext(), size);
	}

}
