import sites.feiras_e_negocios.FeirasENegociosCrawler;
import sites.vagalume.VagalumeCrawler;

import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		//VagalumeCrawler vagalumeCrawler = new VagalumeCrawler();
		//vagalumeCrawler.crawl();

		FeirasENegociosCrawler feirasCrawler = new FeirasENegociosCrawler();
		feirasCrawler.crawl();
	}

}
