/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.junit.Assert;
import org.junit.Test;

import ibangenerator.IBANCalculator;

/**
 *
 * @author ntv
 */
public class IBANCalculatorTest {

    IBANCalculator ibnCa = IBANCalculator.getInstance();
	
    @Test
    public void positiveCases() {
        String[] arr = {"DE", "AT", "NL"};
        String iban = "";
        for (String arr1 : arr) {
		    iban = ibnCa.generateIBAN(arr1);
			Assert.assertTrue(ibnCa.validateIBAN(iban, arr1));
        }
    }

	@Test
    public void negativeCases() {
	    Assert.assertEquals("-1", ibnCa.generateIBAN("MP"));  // unsupported country
        Assert.assertFalse(ibnCa.validateIBAN("DE12134444", "DE"));  // invalid length
		Assert.assertFalse(ibnCa.validateIBAN("NL12ABCD1213444456", "DE")); // invalid IBAN for DE
        Assert.assertFalse(ibnCa.validateIBAN("MS12ABCD12134444", "MS")); // unsupported country
    }
}
