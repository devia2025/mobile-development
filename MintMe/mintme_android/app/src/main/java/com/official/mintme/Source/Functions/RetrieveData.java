package com.official.mintme.Source.Functions;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import androidx.appcompat.app.AlertDialog;
import com.official.mintme.R;
import com.official.mintme.Source.utils.Config;

public class RetrieveData {

    @SuppressLint("ObsoleteSdkInt")
    public static void saveLocalData(String html, Context context) {
        String extractedCodes = extractContentBetweenDelimiters(html);
        String cleanedContent = removeHtmlTags(extractedCodes);

        cleanedContent = cleanedContent.substring(31, cleanedContent.length() - 1);

        String[] lines = cleanedContent.split(" ");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            result.append(line).append("\n");
        }

        Toast.makeText(context, Config.get(Config.ConfigKeys.DOWNLOAD_TITLE), Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {

            if (context.checkSelfPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, Config.get(Config.ConfigKeys.WRITE_PERMISSION_TOAST), Toast.LENGTH_SHORT).show();
                    return;
            }
        }

        saveToFile(result.toString(), context);
    }

    private static String extractContentBetweenDelimiters(String input) {
        Pattern pattern = Pattern.compile("u003Ccode(.*?)u003C/code", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static String removeHtmlTags(String input) {
        String decodedContent = decodeHtmlEntities(input);
        return decodedContent.replaceAll("<[^>]+>", "").trim();
    }

    private static String decodeHtmlEntities(String input) {
        try {
            String decodedContent = input.replaceAll("\\\\u003C", "<")
                    .replaceAll("\\\\u003E", ">")
                    .replaceAll("&lt;", "<")
                    .replaceAll("&gt;", ">")
                    .replaceAll("&amp;", "&")
                    .replaceAll("&quot;", "\"")
                    .replaceAll("&apos;", "'")
                    .replaceAll("&nbsp;", " ");

            return decodedContent;
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }

    private static void saveToFile(String content, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveToDownloadsScopedStorage(content, context);
        } else {
            saveToDownloadsLegacyStorage(content, context);
        }
    }

    private static void saveToDownloadsScopedStorage(String contents, Context context) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                Config.get(Config.ConfigKeys.BACKUP_CODE_FILENAME));

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(contents.getBytes());
            fos.close();
            Toast.makeText(context, Config.get(Config.ConfigKeys.DOWNLOAD_FINISHED)
               + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, Config.get(Config.ConfigKeys.DOWNLOAD_FAILED), Toast.LENGTH_SHORT).show();
        }
    }

    private static void saveToDownloadsLegacyStorage(String content, Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                     Config.get(Config.ConfigKeys.BACKUP_CODE_FILENAME));

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(context, Config.get(Config.ConfigKeys.DOWNLOAD_FINISHED)
               + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, Config.get(Config.ConfigKeys.SAVE_FILE_ERROR), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public static void retrieveAndShowFileContent(Context context) {
        File file;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                    Config.get(Config.ConfigKeys.BACKUP_CODE_FILENAME));
        } else {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    Config.get(Config.ConfigKeys.BACKUP_CODE_FILENAME));
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, Config.get(Config.ConfigKeys.FILE_REMOVED), Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.alert, null);
        TextView dialogMessage = dialogLayout.findViewById(R.id.dialog_message);

        dialogMessage.setText(Config.get(Config.ConfigKeys.TWO_FACTORY_TITLE)
              + file + " \n\n" + content.toString());
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setView(dialogLayout)
            .setCancelable(false)
            .setPositiveButton(Config.get(Config.ConfigKeys.COPY_DIALOG), (dialogInterface, id) -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(content.toString(), content.toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, Config.get(Config.ConfigKeys.COPIED_TO_CLIPBOARD), Toast.LENGTH_LONG).show();
            })
            .setNegativeButton(Config.get(Config.ConfigKeys.CANEL_DIALOG), null)
            .create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.rounded_border);
        dialog.show();
    }
}
