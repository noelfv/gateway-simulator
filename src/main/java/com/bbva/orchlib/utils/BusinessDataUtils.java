package com.bbva.orchlib.utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.bbva.orchlib.configuration.preloaddto.businessdata.Bin;
import com.bbva.orchlib.configuration.preloaddto.businessdata.BusinessData;
import com.bbva.orchlib.configuration.preloaddto.businessdata.Currency;
import com.bbva.orchlib.configuration.preloaddto.businessdata.Fields;

public class BusinessDataUtils {
	private static BusinessData businessData;
	
	private BusinessDataUtils() {}
	
	public static void setBusinessData(BusinessData businessData) {
		BusinessDataUtils.businessData = businessData;
    }
	
	/**
     * Obtiene una lista de campos asociados con la clave especificada para el tipo de Bin.
     *
     * @param key La clave del Bin a recuperar.
     * @return Lista de campos asociados con la clave del Bin o una Lista vacia si no se encuentra.
     */
	public static List<Fields> getBinFields(String key){
    	Optional<Bin> bean = businessData.getBins().stream()
    			.filter(a -> a.getKey().equals(key))
    			.findFirst();
    	
    	if(bean.isEmpty()) {
    		return Collections.emptyList();
    	}
    	
    	return bean.get().getFields();
    }
	
	/**
     * Obtiene la clave asociada con el bin especificado.
     *
     * @param bin Cadena de texto que verificara si existe el bin especificado.

     * @return El Key asociado con el bin especificadp, o el bin mismo si no se encuentra.
     */
	
	public static String getBinKey(String bin) {
		
		for(int i=0 ; i<businessData.getBins().size() ; i++) {
            String key = businessData.getBins().get(i).getKey();
            if(bin.startsWith(key)) {
            	return key;
            }
        }
		return bin;
	}
	
	/**
     * Obtiene el Currency asociado con el id de moneda especificada.
     *
     * @param currencyId El id de moneda cuyo valor se va a recuperar.
     * @return Objeto Currency asociado con el id especificado, o null si no se encuentra.
     */
	
	public static Currency getCurrencyById(String currencyId) {
		
		for(int i=0 ; i<businessData.getCurrency().size() ; i++) {
            Currency currency = businessData.getCurrency().get(i);
            if(currency.getNumericCurrencyId().equals(currencyId)) {
                return currency;
            }
        }
		return null;
	}
	
	/**
     * Obtiene el Currency asociado con el codigo de moneda especificada.
     *
     * @param currencyCode El codigo de moneda cuyo valor se va a recuperar.
     * @return Objeto Currency asociado con el codigo de moneda especificado, o null si no se encuentra.
     */
	
	public static Currency getCurrencyByCode(String currencyCode) {
		
		for(int i=0 ; i<businessData.getCurrency().size() ; i++) {
            Currency currency = businessData.getCurrency().get(i);
            if(currency.getCurrencyCode().equals(currencyCode)) {
                return currency;
            }
        }
		return null;
	}
}
