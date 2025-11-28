package com.bbva.orchlib.utils;

import com.bbva.orchlib.featuretoggle.businessdata.Bin;
import com.bbva.orchlib.featuretoggle.businessdata.BusinessData;
import com.bbva.orchlib.featuretoggle.businessdata.Fields;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BusinessDataUtils {
	private static BusinessData businessData;
	
	private BusinessDataUtils() {}
	
	public static void setBusinessData(BusinessData businessData) {
		BusinessDataUtils.businessData = businessData;
    }
	
	public static List<Fields> getBinFields(String key){
    	Optional<Bin> bean = businessData.getBins().stream()
    			.filter(a -> a.getKey().equals(key))
    			.findFirst();
    	
    	if(bean.isEmpty()) {
    		return Collections.emptyList();
    	}
    	
    	return bean.get().getFields();
    }
	
	public static String getBinKey(String bin) {
		
		for(int i=0 ; i<businessData.getBins().size() ; i++) {
            String key = businessData.getBins().get(i).getKey();
            if(bin.startsWith(key)) {
            	return key;
            }
        }
		return bin;
	}
}
