package service.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceUtils {
    public static Integer priceExtractor(String price) {
        Pattern MY_PATTERN = Pattern.compile("(\\d[\\d.]*)(([.,]-)|(Ft))");
        try {
            String urlDecodedPrice = java.net.URLDecoder.decode(price, "UTF-8");
            urlDecodedPrice = urlDecodedPrice
                    .replaceAll(String.valueOf((char) 160), "") // Non-breakable spaces
                    .replaceAll("\\s+", "") // All (other) whitespaces
                    .replace(".", "");
            Matcher matcher = MY_PATTERN.matcher(urlDecodedPrice);
            while (matcher.find()) {
                String netPrice = matcher.group(1);
                //System.out.println(netPrice);
                return Integer.parseInt(
                        netPrice
                );
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
