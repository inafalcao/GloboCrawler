package sites.feiras_e_negocios;

import com.fasterxml.jackson.databind.ObjectMapper;
import enumeration.Mes;
import model.Artist;
import model.Event;
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
import java.util.Arrays;
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

        processListPage("http://feirasenegocios.com.br/eventos/", 1, null);

        closeArtistJson();
    }

    public void processListPage(String URL, int round, List<Event> events) throws IOException {
        if(events == null) {
            events = new ArrayList<Event>();
        }

        String pageURL = URL + "?page=" + round;

        // Get list of event pages
        Document doc = null;
        try {
            doc = Jsoup.connect(pageURL).userAgent("Mozilla").timeout(120*1000).get();
        } catch (IOException e1) {
            // páginas acabaram, então salvar no arquivo
            writeEvents(events);
            return;
        }

        //String test = Mes.getZeroedMonthByName("Fevereiro");

        Elements linksElements = doc.getElementsByClass("read-more-button");
        List<String> links = new ArrayList<String>();
        for(Element link : linksElements) {
            String mLink = link.attr("abs:href");
            events.add(processEventPage(mLink));
        }

        processListPage(URL, round + 1, events);
    }

    public Event processEventPage(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla").timeout(120*1000).get();
        } catch (IOException e1) {
            return null;
        }

        String eventName = doc.getElementsByClass("title").text();
        String eventDescription = getEventDesctiption(doc);
        String eventPicture = getEventPicture(doc);
        Tuple<String, String> eventDateTime = getEventDateTime(doc);
        String dateBegin = eventDateTime.getLeft();
        String dateEnd = eventDateTime.getRight();
        Local eventLocal = getEventLocal(doc);

        Event event = new Event();
        event.setName(eventName);
        event.setRelease(eventDescription);
        event.setBengin(dateBegin);
        event.setEnd(dateEnd);
        event.setBeginSales(dateBegin);
        event.setEndSales(dateEnd);
        event.setCensorship("livre");
        event.setOnlyExhibition(true);
        // todo: categorias


        // Os 'transientes'
        event.setBanner(eventPicture);
        event.setLocal(eventLocal);

        return event;
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
        Elements times = doc.select("time");

        // Left = from, Rigth = to
        Tuple<String, String> date = parseDate(times.first().html());
        Tuple<String, String> time = parseTime(times);

        String beginDate = date.getLeft() + "T" + time.getLeft();
        String endDate = date.getRight() + "T" + time.getRight();

        return new Tuple<String, String>(beginDate, endDate);
    }

    private Local getEventLocal(Document doc) {
        Element location = doc.select("address").first();
        String locationName = location.select("b").first().html();
        location.select("b").first().remove();

        String rawAddress = location.text();
        String[] rawAddressArray = rawAddress.split("CEP:");
        String address = rawAddressArray[0];
        String cep = rawAddressArray[1].replace("-", "").trim();

        // Assumindo que tudo está em SP (e tá mesmo) porque não tem padrão nessa string do endereço
        String city = "São Paulo";

        // E aí?
        Local local = new Local();
        local.name = locationName;
        local.address = address;
        local.state = "SP"; // só porque é sp, né.
        local.city = city;
        local.cityName = city;
        local.cityUF = "SP";
        local.postalCode = Long.parseLong(cep);
        local.totalCapacity = 1000; // qualquer valor.
        local.lat = 1000;
        local.lng = 1000;

        return local;
    }

    /**
     * Converte uma data no seguinte formato '07 de Março a 10 de Março de 2017'
     * para duas datas no formato 'yyyy-MM-dd'
     * @param date
     * @return
     */
    public Tuple<String, String> parseDate(String date) {
        String[] tokens = date.split(" ");

        String year = tokens[8];
        // yyyy-MM-dd
        String from = year + "-" + Mes.getZeroedMonthByName(tokens[2]) + "-" + tokens[0];
        String to  = year  + "-" + Mes.getZeroedMonthByName(tokens[6]) + "-" + tokens[4];

        return new Tuple<String, String>(from, to);
    }

    public Tuple<String, String> parseTime(Elements doc) {
        String firstTime = "";
        String lastTime = "";

        // Das 10h às 20h: 07 de Março a 9 de Março de 2017
        String first = doc.get(1).html();

        // Pega o primeiro pedaço até os dois pontos. ex: Das 10h às 20h
        String rawFirst = first.split(":")[0];
        firstTime = getZeroedTime(rawFirst.split(" ")[1]);

        if(doc.size() > 2) { // quer dizer que tem mais de uma sessão
            String last = doc.get(doc.size() - 1).html();
            String rawLast = first.split(":")[0]; // ex: Das 10h às 20h
            lastTime = getZeroedTime(rawFirst.split(" ")[3]); // ex: 20:00

        } else { // quer dizer que só tem uma sessão, daí o horário final pode ser o da primeira.
            lastTime = getZeroedTime(rawFirst.split(" ")[3]);
        }

        return new Tuple<String, String>(firstTime, lastTime);
    }

    public String getZeroedTime(String time) {
        // Tenho 8h e vira só 8
        String rawTime = time.replace("h", "");
        return (rawTime.length() == 1 ? "0" + rawTime : rawTime) + ":00";
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

    public static void closeArtistJson() {
        try {
            outArtists.close();
            fstreamArtists.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeEvents(List<Event> events) throws IOException {
        File dir = new File(".");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(dir.getCanonicalPath() + File.separator + "events.json"), events.toArray());
    }

}
