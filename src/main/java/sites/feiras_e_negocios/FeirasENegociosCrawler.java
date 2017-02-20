package sites.feiras_e_negocios;

import model.Artist;
import model.Local;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Tuple;

import javax.print.Doc;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by inafalcao on 17/02/17.
 */
public class FeirasENegociosCrawler {

    public static ArrayList<Artist> artistList;
    public static int count=0;

    static File dir = new File(".");
    static String locArtistis;

    static FileWriter fstreamArtists;
    static BufferedWriter outArtists;

    public void crawl() throws IOException {
        File dir = new File(".");
        String loc = dir.getCanonicalPath() + File.separator + "record.txt";

        openArtistJson();

        File file = new File(loc);
        file.delete();

        FileWriter fstream = new FileWriter(loc, true);
        BufferedWriter out = new BufferedWriter(fstream);
        out.newLine();
        out.close();

        artistList = new ArrayList<Artist>();

        processListPage("http://feirasenegocios.com.br/eventos/", 1);

        closeArtistJson();
    }

    // givn a String, and a File
    // return if the String is contained in the File
    public static boolean checkExist(String s, File fin) throws IOException {

        FileInputStream fis = new FileInputStream(fin);
        // //Construct the BufferedReader object
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));

        String aLine = null;
        while ((aLine = in.readLine()) != null) {
            // //Process each line
            if (aLine.trim().contains(s)) {
                //System.out.println("contains " + s);
                in.close();
                fis.close();
                return true;
            }
        }

        // do not forget to close the buffer reader
        in.close();
        fis.close();

        return false;
    }

    public void processListPage(String URL, int round) throws IOException {

        String pageURL = URL + "?page=" + round;

        // Get list of event pages
        Document doc = null;
        try {
            doc = Jsoup.connect(pageURL).userAgent("Mozilla").timeout(120*1000).get();
        } catch (IOException e1) {
            return;
        }

        Elements linksElements = doc.getElementsByClass("read-more-button");
        List<String> links = new ArrayList<String>();
        for(Element link : linksElements) {
            String mLink = link.attr("abs:href");
            processEventPage(mLink);
        }

        processListPage(URL, round + 1);
    }

    public void processEventPage(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla").timeout(120*1000).get();
        } catch (IOException e1) {
            return;
        }

        String eventName = doc.getElementsByClass("title").text();
        String eventDescription = getEventDesctiption(doc);
        String eventPicture = getEventPicture(doc);
        Tuple<String, String> eventDateTime = getEventDateTime(doc);
        String dateBegin = eventDateTime.getLeft();
        String dateEnd = eventDateTime.getRight();
        Local eventLocal = getEventLocal(doc);
    }

    private String getEventDesctiption(Document doc) {
        Element article = doc.select("article").first();
        Elements paragraphs = article.select("p");
        String paragraphStrings = "";

        for(Element p : paragraphs) {
            paragraphStrings += p.toString();
        }

        return paragraphStrings;
    }

    private String getEventPicture(Document doc) {
        Element img = doc.select("aside").first()
                         .select("picture").first()
                         .select("img").first();
        String picture = img.attr("src");
        return picture;
    }

    /**
     * Como faltam informações de ingresso, cadastrar sessão única.
     */
    private Tuple<String, String> getEventDateTime(Document doc) {
        // Das 10h às 19h: 10 de Março a 19 de Março de 2017

        // Left = beginTime; Right = endTime
        Tuple<String, String> tuple = new Tuple<String, String>("", "");


        return tuple;
    }

    private Local getEventLocal(Document doc) {
        Element location = doc.select("article").first();
        String locationName = location.select("strong").first().html();
        location.select("strong").first().remove();

        String theRest = location.html();
        // parse somehow
        return new Local();
    }

    public static Artist getArtist(ArrayList<String> list) {
        ArrayList<Artist> artistList = new ArrayList<Artist>();
        Artist artist = new Artist();

        artist.setNome(list.get(0));
        artist.setNomeShow(list.get(1));
        artist.setCidade(list.get(2));
        artist.setEstado(list.get(3));
        artist.setDataShow(list.get(4));
        artist.setPicture(list.get(5));
        artist.setHoraShow(list.get(6));
        artist.setLocal(list.get(7));
        artist.setIngressos(list.get(8));
        artist.setClassificacao(list.get(9));
        String date = getDate();
        artist.setDataExtracao(date.substring(0, 10));
        artist.setHoraExtracao(date.substring(11));

		/*try {
			String rank = processPageVagalume("http://www.vagalume.com.br/"+ artist.getNome().replace(" ", "-").replace("&", "e").toLowerCase() +"/popularidade/");
			artist.setRank(rank);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        return artist;

    }

    public static String getPicture(Document doc) {
        Element pic = doc.body().getElementsByClass("borda-arredondada").first();
        return pic.attr("src");
    }

    public static String getHourShow(String text) {
        return text.substring(text.length()-6, text.length()).trim();
    }

    public static String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        return date;
    }

    public static void openArtistJson(){

        try {
            locArtistis = dir.getCanonicalPath() + File.separator + "eventList.txt";
            new File(locArtistis).delete();
            fstreamArtists = new FileWriter(locArtistis, true);
            outArtists = new BufferedWriter(fstreamArtists);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void closeArtistJson(){


        try {
            outArtists.close();
            fstreamArtists.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//		File dir = new File(".");
//		String loc;
//
//		FileWriter fstream;
//		try {
//			loc = dir.getCanonicalPath() + File.separator + "artistList.txt";
//
//			fstream = new FileWriter(loc, true);
//			BufferedWriter out = new BufferedWriter(fstream);
//			out.write("{} ]");
//			//out.newLine();
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

    }

    public static void writeArtist(Artist artist, int count){
        File dir = new File(".");
        String loc;

        System.out.println("start writting:");
        System.out.println("");

        try {
            loc = dir.getCanonicalPath() + File.separator + "artistList.txt";

            FileWriter fstream = new FileWriter(loc, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(artist.toString());
            out.newLine();
            out.flush();
            fstream.flush();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


    }

    public static void writeArtistList(){
        File dir = new File(".");
        String loc;

        System.out.println("start writting:");
        System.out.println("");

        try {
            loc = dir.getCanonicalPath() + File.separator + "artistList.txt";
            File file = new File(loc);
            file.delete();

            for(int i=0; i<artistList.size(); i++){
                String toWrite;
                if(i==0)toWrite = "[" + artistList.get(i).toString() + ",";
                else if(i<artistList.size()-1)toWrite = artistList.get(i).toString() + ",";
                else toWrite = artistList.get(i).toString();

                FileWriter fstream = new FileWriter(loc, true);
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(toWrite);
                out.newLine();
                out.close();

                System.out.println(toWrite);

            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


    }

    public static String processPageVagalume(String URL) throws IOException {
        String returnStr = "";

        boolean foundAgenda = false;
        boolean foundMusicas = false;
        boolean foundPopularidade = false;

		/*File dir = new File(".");
		String loc = dir.getCanonicalPath() + File.separator + "record.txt";*/

        // invalid link
        if (URL.contains(".pdf") || URL.contains("@")
                || URL.contains("adfad") || URL.contains(":80")
                || URL.contains("fdafd") || URL.contains(".jpg")
                || URL.contains(".pdf") || URL.contains(".jpg")
                || URL.contains(".exe"))
            return "";

        // process the url first
        if(URL.contains("agenda/")) foundAgenda = true;
        if(URL.contains("popularidade/"))foundPopularidade = true;
        if (URL.contains("www.sites.vagalume.com.br") && !URL.endsWith("/")) {

        } else if(URL.contains("http://www.sites.vagalume.com.br") && URL.endsWith("/")){
            URL = URL.substring(0, URL.length()-1);
        }else{
            // url of other site -> do nothing
            return "";
        }


        Document doc = null;
        try {
            doc = Jsoup.connect(URL).timeout(120*1000).get();
        } catch (IOException e1) {
            e1.printStackTrace();
            return "";
        }

        if (doc.text().contains("Agenda de Shows")) {
        }
        if(foundPopularidade){
            System.out.println(URL);
            if(doc.text().contains("popularidade em seu site.")){
                String[] splitSite = doc.text().split("popularidade em seu site.");
                String rank = splitSite[1].split("ranking")[0];
                returnStr = rank;
                System.out.println(rank);
            }


        }

        return returnStr.trim();

    }


    public static String getArtistName(String url){
        String aux = url.substring(url.indexOf("www"));
        String name = aux.split("/")[1];
        System.out.println("nome"+name);

        return name;

    }
}
