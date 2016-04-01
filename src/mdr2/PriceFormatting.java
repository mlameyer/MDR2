/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdr2;

import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class PriceFormatting {

    private BigDecimal PriceRaw;
    private float PriceFormated;
    
    public float formatPrice(long mantissa, byte exponent) {
        
        PriceRaw = new BigDecimal(mantissa);
        PriceFormated = PriceRaw.movePointRight(exponent).floatValue();
        
        return PriceFormated;
    }
    
}
