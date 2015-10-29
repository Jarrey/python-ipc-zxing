
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;
import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class zxingHost {

    public static void main(String[] args) {        
        Scanner scan = new Scanner(System.in);
        while (true){
            try{
                String[] params = parseArgs(scan.nextLine());
                executeCore(params);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    private static String[] parseArgs(String args){
        List<String> tokens = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        boolean insideQuote = false;
        for (char c : args.toCharArray()) {
            if (c == '"' || c == '\'')
                insideQuote = !insideQuote;

            if (c == ' ' && !insideQuote) {// when space is not inside quote split..
                tokens.add(Trim(Trim(sb.toString(), "\""), "'")); // token is ready, lets add it to list
                sb.delete(0, sb.length()); // and reset StringBuilder`s content
            } else {
                sb.append(c); // else add character to token
            }
        }
        
        tokens.add(Trim(Trim(sb.toString(), "\""), "'"));
        return tokens.toArray(new String[0]);    
    }
    
    private static void executeCore(String[] args) throws WriterException, IOException, NotFoundException {
        String charset = "UTF-8"; 
        String command = null, data = null, file = "code.png";
        String result;
        if (args.length > 0) {
            command = args[0];
            if (args.length > 1) data = args[1];
            if (args.length > 2) file = args[2];
        }

        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        
        switch (command.toLowerCase()) {
            case "createqrcode":
            case "create":
            case "qr":
                createQRCode(data, file, charset, hintMap, 300, 300);
                System.out.println("Created QRCode Image " + file + " OK");
                break;
            case "readfilecode":
            case "readfile":
            case "file":
                result = readCodeFromFile(data, charset, hintMap);
                System.out.println(result);
                break;
            case "readbase64code":
            case "readbase64":
            case "base64":
                result = readCodeFromBase64(data, charset, hintMap);
                System.out.println(result);
                break;
            case "exit":
            case "quit":
            case "q":
                System.out.println("Bye");
                System.exit(0);
                break;
        }
    }
    
    private static void createQRCode(
        String qrCodeData, 
        String filePath,
        String charset, 
        Map hintMap, 
        int qrCodeheight, 
        int qrCodewidth)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath));
    }

    private static String readCodeFromFile(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        return readCode(new FileInputStream(filePath), charset, hintMap);
    }
        
    private static String readCodeFromBase64(String base64, String charset, Map hintMap)
            throws IOException, NotFoundException {
        InputStream stream = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(base64));
        return readCode(stream, charset, hintMap);
    }
    
    private static String readCode(InputStream stream, String charset, Map hintMap)
            throws IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(
            new HybridBinarizer(
                new BufferedImageLuminanceSource(ImageIO.read(stream))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
        return qrCodeResult.getText();
    }

    public static String TrimEnd(String input, String charsToTrim)
    {
        return input.replaceAll("[" + charsToTrim + "]+$", "");		
    }	

    public static String TrimStart(String input, String charsToTrim)
    {
        return input.replaceAll("^[" + charsToTrim + "]+", "");	
    }

    public static String Trim(String input, String charsToTrim)
    {
        return input.replaceAll("^[" + charsToTrim + "]+|[" + charsToTrim + "]+$", "");	
    }
}
