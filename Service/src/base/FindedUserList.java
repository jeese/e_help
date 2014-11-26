package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindedUserList  implements Serializable{
	public ArrayList<Map<String, Object>> userList; 
	
	public FindedUserList()
	{
		userList = new ArrayList<Map<String, Object>>();
	}
}
