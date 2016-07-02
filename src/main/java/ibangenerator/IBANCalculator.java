/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ibangenerator;

import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 *
 * @author ntv
 */
public class IBANCalculator {

    private String IBAN;
    private String countryCode;
    private final HashMap<String, CountryInfo> countryInfoMap;
	// single instance of IBNACalculator
	public static final IBANCalculator ibanInstance = new IBANCalculator(); 
	
    private IBANCalculator() {
        countryInfoMap = new HashMap<>();
        parseXMLFile();
    }
	
	/**
	* Return the single instance of the IBANCalculator.
	* @return instance of IBANCalculator
	*/
	public static IBANCalculator getInstance() {
        return ibanInstance;
    }

    private void parseXMLFile() {
        // Code of parsing XML adapted  from  http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        try {
			InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream("countryinfo.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(xmlStream);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("country");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String constantCheckDigit = eElement.getElementsByTagName("constantCheckDigit").item(0).getTextContent();
                    CountryInfo countryinfo = new CountryInfo(eElement.getAttribute("code"),
                            !"".equals(constantCheckDigit),
                            "".equals(constantCheckDigit) ? -1 : Integer.parseInt(constantCheckDigit),
                            eElement.getElementsByTagName("bbnFormat").item(0).getTextContent(),
                            eElement.getElementsByTagName("bbnPattern").item(0).getTextContent());
                    countryInfoMap.put(eElement.getAttribute("code"), countryinfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the country is supported. 
     * Currently only Germany(DE), Austria(AT), Netherlands(NL) only supported.
     * @param countryCode the code of country
     * @return true if country supported
     */
	private boolean isIBNSupportedCountry(String countryCode) {
        return countryInfoMap.containsKey(countryCode);
	}
	
    /**
     * Calculate the BBAN of the given countries. 
     * Rules of BBAN followed from 
     * https://en.wikipedia.org/wiki/International_Bank_Account_Number#IBAN_formats_by_country
     * @param countryCode code of country
     * @return the BBAN 
     */
    private String generateBBN(String countryCode) {
        String BBN = "";
        if (isIBNSupportedCountry(countryCode)) {
            CountryInfo cInfo = countryInfoMap.get(countryCode);
            String bbnFormat = cInfo.getBbnFormat();
            String bbnPattern = cInfo.getBbnPattern();
            String[] bbnFormatArr = bbnFormat.split(",");
            char[] bbnPatternArr = bbnPattern.toCharArray();
            int count = 0;
            char type = 'n';
            int i = 0;
            for (String item : bbnFormatArr) {
                if (item.contains("a")) {
                    type = 'a';
                    count = Integer.parseInt(item.split("a")[0]);
                } else if (item.contains("n")) {
                    type = 'n';
                    count = Integer.parseInt(item.split("n")[0]);
                } else if (item.contains("c")) {
                    type = 'c';
                    count = Integer.parseInt(item.split("c")[0]);
                }
                BBN = BBN + parseBBNPattern(type, i, i + count, bbnPatternArr);
                i = i + count;
            }
        }
        return BBN;
    }

    private String parseBBNPattern(char type, int parseBegin, int parseEnd, char[] bbnPatternArr) {
        String bbn = "";
        char prevChar = bbnPatternArr[parseBegin];
        int count = 0;
        for (int i = parseBegin; i < parseEnd; i++) {
            count++;
            if (prevChar != bbnPatternArr[i] || i == parseEnd - 1) {
                if (prevChar != bbnPatternArr[i]) {
                    count--;
                }
                switch (prevChar) {
                    case 'a':
                        bbn = bbn + BBNCalculator.generateBICBankCode(type, count);
                        break;
                    case 'b':
                        bbn = bbn + BBNCalculator.generateNationalBankCode(type, count);
                        break;
                    case 'c':
                        bbn = bbn + BBNCalculator.generateAccountNumber(type, count);
                        break;
                    case 'i':
                        bbn = bbn + BBNCalculator.generateNationalIdentificationNumber(type, count);
                        break;
                    case 'm':
                        bbn = bbn + BBNCalculator.generateCurrency(type, count);
                        break;
                    case 'n':
                        bbn = bbn + BBNCalculator.generateBranchNumber(type, count);
                        break;
                    case 's':
                        bbn = bbn + BBNCalculator.generateBranchCode(type, count);
                        break;
                    case 't':
                        bbn = bbn + BBNCalculator.generateAccountType(type, count);
                        break;
                    case 'x':
                        bbn = bbn + BBNCalculator.generateNationalCheckDigit(type, count);
                        break;
                    case '0':
                        bbn = bbn + BBNCalculator.generateZeros(type, count);
                        break;
                }
                prevChar = bbnPatternArr[i];
                count = 1;
            }
        }

        return bbn;
    }

	 /**
     * Calculate the check digit for the IBAN.
     * Followed the algorithm from
     * https://en.wikipedia.org/wiki/International_Bank_Account_Number#Check_digits.
     * @param bbn the BBAN
     * @param countryCode the code of country
     * @return  the check digit
     */
    private String generateCheckDigits(String bbn, String countryCode) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String rotatedInput = bbn + countryCode + "00";
        StringBuilder sb = new StringBuilder();
        for (char c : rotatedInput.toCharArray()) {
            if (c > 57) {
                sb.append(c - 55);
            } else {
                sb.append(c);
            }
        }
        String a = modulo97(sb.toString());
        int val = 98 - Integer.parseInt(a);
        return String.format("%02d", val);
    }

    private static String modulo97(String sb) {
        int begin = 0;
        int last = sb.length();
        String a = "";
        while (begin < last) {
            if (begin == 0 && begin + 9 <= last) {
                a = String.format("%02d", Integer.parseInt(sb.substring(begin, begin + 9)) % 97);
                begin = begin + 9;
            } else if (begin + 7 <= last) {
                a = String.format("%02d", Integer.parseInt(a + sb.substring(begin, begin + 7)) % 97);
                begin = begin + 7;
            } else {
                a = String.format("%02d", Integer.parseInt(a + sb.substring(begin)) % 97);
                begin = last;
            }

        }
        return a;
    }

    /**
     * Check the validity of the IBAN.
     * Algorithm followed from 
     * https://en.wikipedia.org/wiki/International_Bank_Account_Number#Validating_the_IBAN.
     * At present this function is used to validate the IBAN generated by our tool.
     * Therefore assume that pattern of the iban is valid for the given country.
     * Therefore pattern validation is not happening.
     * @param iban the IBAN
     * @param countryCode the code of country
     * @return true if the IBAN is valid
     */
    public boolean validateIBAN(String iban, String countryCode) {
        if (isIBNSupportedCountry(countryCode)) {
            CountryInfo cInfo = countryInfoMap.get(countryCode);
            String bbnFormat = cInfo.getBbnFormat();
            String bbnPattern = cInfo.getBbnPattern();
            String[] bbnFormatArr = bbnFormat.split(",");
            char[] bbnPatternArr = bbnPattern.toCharArray();
            int count = 0;
            char type = 'n';
            int i = 0;
            for (String item : bbnFormatArr) {
                if (item.contains("a")) {
                    count = count + Integer.parseInt(item.split("a")[0]);
                } else if (item.contains("n")) {
                    count = count + Integer.parseInt(item.split("n")[0]);
                } else if (item.contains("c")) {
                    count = count + Integer.parseInt(item.split("c")[0]);
                }
            }
            if (iban.length() != count + 4) {
                return false;
            }
            String changedIban = iban.substring(4) + iban.substring(0, 4);
            StringBuilder sb = new StringBuilder();
            for (char c : changedIban.toCharArray()) {
                if (c > 57) {
                    sb.append(c - 55);
                } else {
                    sb.append(c);
                }
            }
            //System.out.println("chnaged"+sb.toString());
            int remainder = Integer.parseInt(modulo97(sb.toString()));
            return 1 == remainder;
        }
        return false;
    }
	
	 /**
     * Return IBAN of  the  given country. If the given given country does not 
     * support it will return -1.
	 * Currently only Germany(DE), Austria(AT), Netherlands(NL) only supported.
     * Country codes should follow codes given in 
     * https://en.wikipedia.org/wiki/International_Bank_Account_Number#IBAN_formats_by_country.
	 * All the IBAN related algorithm follow above given wikipedia link.
	 * Generated IBAN will not contain any spaces.
     * @param countryCode country code
     * @return the IBAN
     */
    public String generateIBAN(String countryCode) {
        String output = "-1"; //Country " + countryCode + " not supported. Currently only supported DE, AT , NL";
        if (isIBNSupportedCountry(countryCode)) {
            String bbn = generateBBN(countryCode);
            String checkDigits = generateCheckDigits(bbn, countryCode);
            output = countryCode + checkDigits + bbn;
        }
        return output;
    }

	 /**
     * Return the IBAN in printable format.
     * When printed IBAN is expressed in groups of four characters
     * separated by a single space except the the last group.
     * For eg : DE30233828061484772422 will print as follows,
     * DE30 2338 2806 1484 7724 22.
     * @param iban the IBAN
     * @return printable format of IBAN
     */
    public String printIBAN(String iban) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (char c : iban.toCharArray()) {
            if (i != 0 && i % 4 == 0) {
                sb.append(" ");
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }
}
