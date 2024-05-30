//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import org.jsoup.* ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ; 
import org.w3c.dom.Element ; 
import javax.xml.parsers.DocumentBuilder ; 
import javax.xml.parsers.DocumentBuilderFactory ;
import java.io.*;
import java.net.* ;
import java.lang.Exception ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;



//// NOTATION  :  MOST WEBSITES which I USED , DO NOT OPEN WITHOUT VPN ( THE CODE WILL RUN BUT WON'T SHOW THEM )
public class RSS {
    public static void main(String[] args) throws FileNotFoundException {



        try {
            System.out.println("Welcome to RSS Reader!") ;
            System.out.println("Type a valid number for your desired action:") ;
            System.out.println("[1] Show updates ") ;
            System.out.println("[2] Add URL") ;
            System.out.println("[3] Remove URL") ;
            System.out.println("[4] Exit") ;

            int n ;
            Scanner input = new Scanner(System.in) ;
            n = input.nextInt() ;

            File file = new File("Data.txt") ;
            Scanner scanner = new Scanner(file) ; // scanner will read our file

            ArrayList<String> dataOfFile = new ArrayList<String>() ;
            int i = 0;
            while (scanner.hasNextLine()) {
                dataOfFile.add(i, scanner.nextLine());
                ++i;
            }

             while( n != 4) {
                 if (n == 1) {

                     int numberOfElements = dataOfFile.size();
                     System.out.println("Show updates for: ");
                     System.out.println("[" + 0 + "]" + "All websites ");
                     for (int j = 0; j < numberOfElements; ++j) {
                         String eachWebInfo = dataOfFile.get(j) ;
                         String[] eachWebURL = eachWebInfo.split(";") ;
                         System.out.println("[" + (j + 1) + "]" +eachWebURL[0]);
                     }
                     System.out.println("Enter -1 to return.");

                     Scanner inputSecondStep = new Scanner(System.in);
                     int selectLine;
                     selectLine = inputSecondStep.nextInt();

                     if (selectLine == -1) {
                         System.out.println("Type a valid number for your desired action:");
                         System.out.println("[1] Show updates ");
                         System.out.println("[2] Add URL");
                         System.out.println("[3] Remove URL");
                         System.out.println("[4] Exit");
                         n = input.nextInt() ;
                     }
                     else if(selectLine == 0 ){
                         int numberOfArrayElement = dataOfFile.size() ;
                         for(int j = 0 ; j< numberOfArrayElement ; ++j){
                             String webLine = dataOfFile.get(j) ;
                             String[] onlyWebURL = webLine.split(";") ;
                             System.out.println(onlyWebURL[0]);
                             retrieveRssContent(onlyWebURL[2]) ;
                             System.out.println();
                         }
                         System.out.println("Type a valid number for your desired action:");
                         System.out.println("[1] Show updates ");
                         System.out.println("[2] Add URL");
                         System.out.println("[3] Remove URL");
                         System.out.println("[4] Exit");
                         n = input.nextInt() ;
                     }
                     else {
                         String consideredLine = dataOfFile.get( (selectLine - 1) ); // I wrote -1 because previously I had shift "selectedLine " index .
                         String[] separateLine = consideredLine.split(";");
                         System.out.println(separateLine[0]);
                         retrieveRssContent(separateLine[2]);
                         System.out.println("Type a valid number for your desired action:");
                         System.out.println("[1] Show updates ");
                         System.out.println("[2] Add URL");
                         System.out.println("[3] Remove URL");
                         System.out.println("[4] Exit");
                         n = input.nextInt() ;
                     }
                 }

                 else if (n == 2) {
                    while( n == 2) {
                         System.out.println("Please enter website URL to add: ");
                         Scanner inputURL = new Scanner(System.in);
                         String siteURL = inputURL.next();

                         // First we check that the entered website
                         //doesn't exist in current file

                        int numberOfElements = dataOfFile.size() ;
                        int flag = 0 ;
                        for(int j = 0 ;  j < numberOfElements ; ++j){
                            String websInfo = dataOfFile.get(j) ;
                            String[] onlyURL = websInfo.split(";") ;
                            if(onlyURL[1].equals(siteURL)){
                                flag = 1 ;
                            }
                        }
                        if(flag==1){
                            System.out.println(siteURL + " already exists. ");
                            System.out.println("Type a valid number for your desired action:");
                            System.out.println("[1] Show updates ");
                            System.out.println("[2] Add URL");
                            System.out.println("[3] Remove URL");
                            System.out.println("[4] Exit");
                            n = input.nextInt() ;
                        }
                        else {

                            //now we have URL address
                            //we need to have page title and rss address
                            String htmlSource = fetchPageSource(siteURL); // to get html source
                            String pageTitle = extractPageTitle(htmlSource); // now we have page title
                            String rssAddress = extractRssUrl(siteURL); // to extract rss address of website

                            if(rssAddress.equals("")){
                                System.out.println("Couldn't find RSS address for " + siteURL);
                                System.out.println("Type a valid number for your desired action:");
                                System.out.println("[1] Show updates ");
                                System.out.println("[2] Add URL");
                                System.out.println("[3] Remove URL");
                                System.out.println("[4] Exit");
                                n = input.nextInt();

                            }
                            else {
                                int currentElements = dataOfFile.size();
                                dataOfFile.add(currentElements, pageTitle + ";" + siteURL + ";" + rssAddress);
                                System.out.println("Added " + siteURL + " successfully");
                                System.out.println("Type a valid number for your desired action:");
                                System.out.println("[1] Show updates ");
                                System.out.println("[2] Add URL");
                                System.out.println("[3] Remove URL");
                                System.out.println("[4] Exit");
                                n = input.nextInt();
                            }
                        }
                     }
                 }
                 else if (n == 3) {
                     while (n == 3) {

                         System.out.println("Please enter website URL to remove: ");
                         String removeURL;
                         Scanner inputURLRemove = new Scanner(System.in);
                         removeURL = inputURLRemove.next();

                         //checking that does it exist or no
                         int numberOfElements = dataOfFile.size();
                         int flag = 0;
                         int indexOfRemovingURL;
                         for (int j = 0 ; j < numberOfElements ; ++j) {
                             String eachLineOfFile = dataOfFile.get(j);
                             String[] webURL = eachLineOfFile.split(";");
                             if (webURL[1].equals(removeURL)) {
                                 flag = 1 ;
                                 dataOfFile.remove(j);
                                 System.out.println("Removed " + removeURL + " successfully.");
                                 System.out.println("Type a valid number for your desired action:");
                                 System.out.println("[1] Show updates ");
                                 System.out.println("[2] Add URL");
                                 System.out.println("[3] Remove URL");
                                 System.out.println("[4] Exit");
                                 n = input.nextInt();
                                 break;
                             }
                         }
                         if (flag == 0) {
                             System.out.println("Couldn't find " + removeURL);
                             System.out.println("Type a valid number for your desired action:");
                             System.out.println("[1] Show updates ");
                             System.out.println("[2] Add URL");
                             System.out.println("[3] Remove URL");
                             System.out.println("[4] Exit");
                             n = input.nextInt();
                         }
                     }
                 }
             }

             if(n==4){
                 // if user enter 4 , the arraylist should add to the main file .
                 try {
                     PrintStream printStream = new PrintStream(file) ;

                     int numberOfWebInfo = dataOfFile.size() ;
                     for(int j = 0 ; j<numberOfWebInfo ; ++j) {
                         String string = dataOfFile.get(j) ;
                         printStream.println(string);
                     }
                     printStream.flush();
                     printStream.close();
                 }
                 catch (Exception e){
                     e.printStackTrace();
                 }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

        String html ;

        public static String extractPageTitle(String html)
         {
             try
             {
                 org.jsoup.nodes.Document doc = Jsoup.parse(html);
                 return doc.select("title").first().text();
                 }
             catch (Exception e)
             {
                 return "Error: no title tag found in page source!";
                 }
             }


    public static void retrieveRssContent(String rssUrl) {
         try {
             String rssXml = fetchPageSource(rssUrl);
             DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
             DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
             StringBuilder xmlStringBuilder = new StringBuilder();
             xmlStringBuilder.append(rssXml);
             ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
             org.w3c.dom.Document doc = documentBuilder.parse(input);
             NodeList itemNodes = doc.getElementsByTagName("item");

             for (int i = 0; i < 5 ; ++i){
                 Node itemNode = itemNodes.item(i);
                 if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                     Element element = (Element) itemNode;
                     System.out.println("Title: " + element.getElementsByTagName("title").item(0).getTextContent());
                     System.out.println("Link: " + element.getElementsByTagName("link").item(0).getTextContent());
                     System.out.println("Description: " + element.getElementsByTagName("description").item(0).getTextContent());
                     }
                 }
             }
         catch (Exception e)
         {
             System.out.println("Error in retrieving RSS content for " + rssUrl + ": " + e.getMessage());
             }
         }

    public static String extractRssUrl(String url)
            throws IOException {
         org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
         return doc.select("[type='application/rss+xml']").attr("abs:href");
         }

    public static String fetchPageSource(String urlString)
            throws Exception{
         URI uri = new URI(urlString);
         URL url = uri.toURL();
         URLConnection urlConnection = url.openConnection();
         urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML , like Gecko) Chrome/108.0.0.0 Safari/537.36");
         return toString(urlConnection.getInputStream());
         }

    private static String toString(InputStream inputStream)
            throws IOException{
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"));
         String inputLine;
         StringBuilder stringBuilder = new StringBuilder();
         while ((inputLine = bufferedReader.readLine()) != null)
             stringBuilder.append(inputLine);

         return stringBuilder.toString();
         }

}