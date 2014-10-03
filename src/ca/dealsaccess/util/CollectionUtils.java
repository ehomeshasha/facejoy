package ca.dealsaccess.util;

import java.util.List;

public class CollectionUtils {

	public static void removeElementByPosition(List<String> targetList, List<Integer> positionDel) {
		for(int i=0;i<positionDel.size();i++) {
			targetList.remove(positionDel.get(i).intValue());
		}
		
	}

}
