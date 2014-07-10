package com.thilek.android.qleneagles_quiz.util;

import android.content.Context;
import android.content.res.AssetManager;
import com.thilek.android.qleneagles_quiz.file_manager.FileException;
import com.thilek.android.qleneagles_quiz.file_manager.FileService;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipService {

	private static final String TAG = "ZipService";

	IProgressReceiver progressReceiver;

	private final static int BUFFER = 1 * 1024;

	public ZipService(IProgressReceiver progressReceiver) {
		this.progressReceiver = progressReceiver;
	}

	public ZipService() {

	}

	public long extractInputStream(String targetLocation, String subDirectory, InputStream inputStream) {
		try {
			if (inputStream == null)
				return 0;
			FileService.getInstance().checkLocationAvailable(targetLocation);
			String absolutePath = FileService.getInstance().getPath(targetLocation, subDirectory);
			long totalBytesRead = extract(inputStream, absolutePath);
			return totalBytesRead;

		} catch (Exception e) {
			throw new FileException(FileException.IO_EXCEPTION, e.getMessage() + " targetLocation:" + targetLocation + " subDirectory:" + subDirectory);
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {

			}
		}
	}

	private long extract(InputStream inputStream, String outputPath) throws IOException, FileNotFoundException {

		long totalBytesRead = 0;
		try {
			ZipEntry zipEntry = null;

			ZipInputStream zipInputStream = new ZipInputStream(inputStream);
			long overallSize = 0;
			final int step = 1024 * 1024;
			int currentStep = step;
			int progressDisplay = 0;
			long start = System.currentTimeMillis();
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {
					dirChecker(zipEntry.getName(), outputPath);
				} else {
					int size;
					byte[] buffer = new byte[BUFFER];

					String path = outputPath + "/" + zipEntry.getName();
					FileService.getInstance().createNotExistingDirectories(path);
					FileOutputStream fout = new FileOutputStream(path);
					BufferedOutputStream bos = new BufferedOutputStream(fout, buffer.length);

					while ((size = zipInputStream.read(buffer, 0, buffer.length)) != -1) {


						bos.write(buffer, 0, size);
						if (progressReceiver != null) {
							overallSize += size;
							if (overallSize > currentStep) {
								currentStep += step;
								progressReceiver.setProgress(null, ++progressDisplay, false, "");
							}
						}
					}
					bos.flush();
					bos.close();

					zipInputStream.closeEntry();

				}
			}
			Logs.d(TAG, "extract overall:" + (System.currentTimeMillis() - start) + " ms > ");
		} finally {

			if (inputStream != null)
				inputStream.close();
		}
		return totalBytesRead;
	}

	private void dirChecker(String dir, String dataPath) {
		File f = new File(dataPath + dir);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}

	public InputStream getInputStreamFromAssets(Context context, String fileName) {
		try {
			AssetManager manager = context.getAssets();
			InputStream inputStream = manager.open(fileName);
			return inputStream;
		} catch (FileNotFoundException e) {
			Logs.e(this.getClass().getName(), e.getMessage());
			throw new FileException(FileException.FILE_NOT_FOUND_EXCEPTION, e.getMessage() + "from assets file:" + fileName);
		} catch (IOException e) {
			Logs.e(this.getClass().getName(), e.getMessage());
			throw new FileException(FileException.IO_EXCEPTION, e.getMessage() + "from assets file:" + fileName);
		}
	}

	public ZipInputStream getZipInputStreamFromInternalFile(Context context, String dataPath) throws FileNotFoundException {
		InputStream in = null;
		try {
			FileInputStream inputStream = context.openFileInput(dataPath);
			in = new BufferedInputStream(inputStream);
			ZipInputStream zipInputStream = new ZipInputStream(in);
			return zipInputStream;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Logs.e(this.getClass().getName(), e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

}
