/**
 * 
 */
package adam.test.urlencode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.URLEncoder;

import org.springframework.adam.common.utils.encode.AdamURLEncoder;
import org.springframework.adam.common.utils.encode.impl.AdamURLEncoderImpl;

import adam.test.rule.AdsCache;

/**
 * @author USER
 *
 */
public class AdamURLEncodeTest {

	public static void main(String[] args) throws Exception {
		File file = new File("test_urlencode");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String text = "";
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			// 显示行号
			text = text + tempString;
		}
		reader.close();
		String a = AdamURLEncoder.encode(text, false);
		String b = AdamURLEncoder.encode(text, true);
		System.out.println(a.equals(b));
		System.out.println(a);
		System.out.println(b);
		System.out.println(URLEncoder.encode(text, "utf-8"));
		System.out.println(AdamURLEncoder.encode(" ", true));
		System.out.println(URLEncoder.encode(" ", "utf-8"));
		
		AdsCache cache = new AdsCache();
		cache.refresh("30");
		AdamURLEncoder.setCache(cache);
		System.out.println(AdamURLEncoder.encode("http://www.baidu.com", true));
		System.out.println(AdamURLEncoder.encode("http://www.baidu.com", true));
		String str = "http://www.baidu.com/张晓将test.jpg?a=张晓将";
		System.out.println(URI.create(str).toASCIIString());
		System.out.println(URI.create(URI.create(str).toASCIIString()).toASCIIString());
	}

}
