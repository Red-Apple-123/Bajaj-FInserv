package HashGenerator;

import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONTokener;

public class HashGenerator {

	public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar test.jar <PRN Number> <JSON File Path>");
            return;
        }

        String prnNumber = args[0].toLowerCase().replaceAll("\\s+", ""); // Ensure PRN is lowercase and has no spaces
        String jsonFilePath = args[1];
        
        try {
            // Parse JSON file and find destination value
            String destinationValue = getDestinationValueFromJson(jsonFilePath);
            if (destinationValue == null) {
                System.out.println("Key 'destination' not found in the JSON file.");
                return;
            }

            // Generate random string
            String randomString = generateRandomString(8);

            // Generate MD5 hash
            String combinedString = prnNumber + destinationValue + randomString;
            String md5Hash = generateMD5Hash(combinedString);

            // Print the result
            System.out.println(md5Hash + ";" + randomString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getDestinationValueFromJson(String filePath) throws IOException {
        FileReader reader = new FileReader(filePath);
        JSONTokener tokener = new JSONTokener(reader);
        JSONObject jsonObject = new JSONObject(tokener);
        reader.close();
        return findKey(jsonObject, "destination");
    }

    private static String findKey(JSONObject jsonObject, String key) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String currentKey = keys.next();
            Object value = jsonObject.get(currentKey);
            if (currentKey.equals(key)) {
                return value.toString();
            } else if (value instanceof JSONObject) {
                String result = findKey((JSONObject) value, key);
                if (result != null) return result;
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

	}


