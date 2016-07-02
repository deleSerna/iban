This tool is used to create IBAN (International Bank Account Number) for different countries. 
Please refer  https://en.wikipedia.org/wiki/International_Bank_Account_Number for further IBAN details.
Other than IBAN generation the tool provide a utility  to express IBAN in printable format and limited 
amount of validation functionality. All the IBAN related rules are followed from the above wikipedia link.
Countries IBAN related details are taken from resources/countryinfo.xml. 
At present it supports only German, Austria and Netherlands. To support more countries add
details in that xml file. Assumes that data provided in the xml file is valid. There is no further validation for that details.
The tool is written in java 8 as maven project. It does not depend on any other third party libraries other than junit which used for 
unit testing. The project is uploaded in https://github.com/deleSerna/.

How to use it ? 
Follow rules of a normal maven project for building and executing (https://maven.apache.org/guides/getting-started/
1. mvn package
2. Use the generated iban-1.0-SNAPSHOT.jar as any other executable jar.

Please find a sample code 

 
public class ex {
    public static void main(String[] args) {
        ibangenerator.IBANCalculator ibanInstance = ibangenerator.IBANCalculator.getInstance();
        String iban = ibanInstance.generateIBAN("DE");
        System.out.println(ibanInstance.printIBAN(iban));
        System.out.println(ibanInstance.validateIBAN(iban, "DE"));
    }
    
}
