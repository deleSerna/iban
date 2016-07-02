/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ibangenerator;

/**
 * IBAN related details of a country is used stored in resources/countryinfo.xml.
 * This Class used to store the information of a country of that file after parsing.
 * All formats and semantics  taken from 
 * https://en.wikipedia.org/wiki/International_Bank_Account_Number.
 * @author ntv
 */
public class CountryInfo {
    private String code;
	/* Some country have constant check code. 
    *  Use this constant code rather than generating check code.
    */
    private boolean isConstantCheckCode;
    private int consantCheckCode;
    private String bbnFormat;
    private String bbnPattern;

    public CountryInfo(String code, boolean isConstantCheckCode, int consantCheckCode, String bbnFormat, String bbnPattern) {
        this.code = code;
        this.isConstantCheckCode = isConstantCheckCode;
        this.consantCheckCode = consantCheckCode;
        this.bbnFormat = bbnFormat;
        this.bbnPattern = bbnPattern;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isIsConstantCheckCode() {
        return isConstantCheckCode;
    }

    public void setIsConstantCheckCode(boolean isConstantCheckCode) {
        this.isConstantCheckCode = isConstantCheckCode;
    }

    public int getConsantCheckCode() {
        return consantCheckCode;
    }

    public void setConsantCheckCode(int consantCheckCode) {
        this.consantCheckCode = consantCheckCode;
    }

    public String getBbnFormat() {
        return bbnFormat;
    }

    public void setBbnFormat(String bbnFormat) {
        this.bbnFormat = bbnFormat;
    }

    public String getBbnPattern() {
        return bbnPattern;
    }

    public void setBbnPattern(String bbnPattern) {
        this.bbnPattern = bbnPattern;
    }
    
    
}
