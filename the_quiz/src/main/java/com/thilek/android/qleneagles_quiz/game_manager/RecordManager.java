package com.thilek.android.qleneagles_quiz.game_manager;

import com.thilek.android.qleneagles_quiz.file_manager.FileService;
import com.thilek.android.qleneagles_quiz.util.DateUtil;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class RecordManager {

    private static final String FILE_EXTENSION = ".dat";
    private static final String FILE_ROOT = "records/";

    private static final String sdf = "HH_mm_dd_MM_yyyy";

    public RecordManager() {

    }


    public void saveGame(JSONObject jsonObject) {

        File file = new File(FileService.getInstance().getPathAndCreateIfNotExists(FileService.EXTERNAL, generateFileName()));

        try {

            InputStream inStream = new ByteArrayInputStream(jsonObject.toString().getBytes());

            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            int readByte = 0;
            // read the bytes one-by-one from the inputstream to the buffer.
            while (true) {
                readByte = inStream.read();
                if (readByte == -1) {
                    break;
                }
                byteOutStream.write(readByte);
            }
            byteOutStream.flush();
            inStream.close();
            byteOutStream.close();
            byte[] response = byteOutStream.toByteArray();
            // now response byte array is the complete json in the biary
            // form. We will save this stuff to file.
            FileOutputStream outStream = new FileOutputStream(file);
            // write the whole data into the file
            for (int i = 0; i < response.length; i++) {
                outStream.write(response[i]);
            }

            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String generateFileName() {

        String filename = DateUtil.getCurrentDateTime(sdf);

        return FILE_ROOT + filename + FILE_EXTENSION;
    }
}
