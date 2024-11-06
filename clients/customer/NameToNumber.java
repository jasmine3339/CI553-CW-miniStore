package clients.customer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NameToNumber extends HashMap<String, String>{
	
	
	NameToNumber(){
		put("0001","TV,television,telly");
		put("0002","Radio,stereo,dab");
		put("0003","Toaster");
		put("0004","Watch,clock,wristwatch");
		put("0005","Camera,picture");
		put("0006","Music Player,mp3");
		put("0007","USB driver,usb,usb stick");
	
	}
	public <T, E> T getNumberByName(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			for (String wordoptions:((String) entry.getValue()).split(",")) {
				//System.out.println(wordoptions.toLowerCase());
				//System.out.println(((String) value).toLowerCase());
				if (Objects.equals(((String) value).toLowerCase(), wordoptions.toLowerCase())) {
					return entry.getKey();
				}
			}
		}
		return null;
	}
	
	
	
	
}