package stock_analysis;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.SwingUtilities;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

/**
 * This program takes a list of stocks and displays each stock's ticker, share price, P/E ratio, etc. 
 * 
 * All information is gathered from Yahoo Finance. 
 * 
 * The program searches for each stock's specific attribute, compiles each attribute for each stock in a Stock element, 
 * and organizes the list based on a certain chosen attribute.
 * @author Gabe Argush
 *
 */
public class stockChecker {
	/**
	 * Sets up list of stocks, going through each entry via URL to that stock, and sorting the entries by certain criteria
	 * @param args
	 * @throws Exception
	 */
	static boolean status = true;
	static String[] stocks = { "NVDA", "BABA", "HUYA", "ATVI", "IQ", "MU", "V", "NFLX", 	// List of stocks
			"AMD", "BAC", "AAPL", "MSFT", "TSLA", "AMZN", "FB", "ROKU"};				
	static String[] titles = { "Ticker", "P/E Ratio", "% Volume Diff",  "Stock Price", "Previous Close", "% Change Today"};
	public static void main(String[] args) throws Exception {
		analysis(stocks);
	}
	public static ArrayList<Stock> analysis(String[] stocks) throws Exception {
		ArrayList<Stock> entries = new ArrayList<Stock>();
		Arrays.sort(stocks);
		
		for(String stock: stocks) {
			URL url = new URL("https://finance.yahoo.com/quote/" + stock + "/key-statistics?p=" + stock);
			URLConnection conn = url.openConnection();
			InputStreamReader input = new InputStreamReader(conn.getInputStream());
			BufferedReader buff = new BufferedReader(input);
			String line = buff.readLine();

			double a, b, c, d = 0;
			double e, f, g, g1 = 0;

			while(line != null) {
				try {
					a = parser("currentPrice\":{\"raw\":", line);
					b = parser("regularMarketChangePercent\":{\"raw\":", line);
					c = parser("regularMarketPreviousClose\":{\"raw\":", line);
					d = parser("trailingPE\":{\"raw\":", line);
					e = parser("regularMarketVolume\":{\"raw\":", line);
					f = parser("averageDailyVolume3Month\":{\"raw\":", line);
					g1 = timer();
					g = (e - f * g1) * 100 / f;
					Stock entry = new Stock(stock, g, d, a, c, b);
					if(a != 0.0) entries.add(entry); 
				
					line = buff.readLine();
				} catch(Exception e1) {
					System.out.println(e1.getMessage());
				}
			}	
			input.close();
			buff.close();
		}	
		//sorter(entries, titles, new PECompare());			// sorts entries by P/E Ratio
		sorter(entries, titles, new ChangeCompare());		// sorts entries by % Change Today
		return entries;
	}
		
	/**
	 * parser() finds the instance of "keyword" in the URL read by "line," and parses the numerical value needed.
	 * @param keyword
	 * @param line
	 * @return
	 */
	private static double parser(String keyword, String line) {
		double value = 0;
		if(line.contains(keyword)) {
			int target = line.indexOf(keyword);		// finds line needed
			int deci = line.indexOf(".", target + 1);	// finds decimal point
			int start = deci;
			while(line.charAt(start) != ':') { start--; }	// includes all numbers before decimal point
			String valueStr = line.substring(start + 1, deci + 4).replaceAll(",","")	// takes entire value, eliminates any
					.replaceAll("\"", "").replaceAll("}", "").replaceAll("M", "").replaceAll("%", "")	// extra characters
					.replaceAll("k", "").replaceAll("B", "").replaceAll(" -", "");	
			value = Double.parseDouble(valueStr);
		}		
		return value;
	}
	
	/**
	 * lister() reads in the ticker values of a list of stocks from a CSV file and enters them into "list"
	 * @param list
	 * 		Reads the CSV file for the individual tickers needed, adds them to the list
	 * @throws Exception
	 */
	private static void lister(String[] list) throws Exception {
		String csvPath = "/Users/Gabe Argush/Downloads/sp500.csv";

		CSVReader reader = null;

		reader = new CSVReader(new FileReader(csvPath), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1);

		String[] record;
		int counter = 0;
		while ((record = reader.readNext()) != null) {
			String ticker = record[0];
			list[counter] = ticker;
			counter++;			
		}	
		reader.close();
	}
	
	/**
	 * timer() returns the fraction of the trading day elapsed, for the "% Volume Diff" values to reflect the
	 * day's volume in relation to the 3 month average volume
	 * @return fraction
	 */
	public static double timer() {
		final double secsInTradingDay = 23400.0;
		LocalDateTime now = LocalDateTime.now();	// current time
		String start = "09:30:00";	// start time of trading day
		int startSecs = Integer.parseInt(start.substring(0, 2)) * 3600  +  Integer.parseInt(start.substring(3,5)) * 60;	
		int nowSecs = Integer.parseInt(now.toString().substring(11, 13)) * 3600  +   // converting each to seconds
				Integer.parseInt(now.toString().substring(14,16)) * 60 + Integer.parseInt(now.toString().substring(17, 19));

		if (((nowSecs - startSecs) / secsInTradingDay) <= 1) {
			return (nowSecs - startSecs) / secsInTradingDay; // fraction of trading day elapsed
		} else {
			return 1;
		}
	}
	
	/**
	 * sorter() lists the entries in "list" from lowest to highest of the attribute given in "class1," the Comparator class
	 * @param list
	 * @param titles
	 * @param class1
	 * @throws MalformedURLException 
	 */
	private static void sorter(ArrayList<Stock> list, String[] titles, Comparator<Stock> class1) throws MalformedURLException {
		int i = 0;
		if(class1.toString().contains("PECompare")) {
			i = 1;											// determine attribute for title
		} else if(class1.toString().contains("ChangeCompare")) {
			i = 5;
		} else {
			throw new MalformedURLException();
		}
			
		Collections.sort(list, class1);		// sort by given Comparator class
		
		for(Stock entry: list) {
			if(entry.getPerToday() > 5) {		
				if(status) {

							try {
								Music.main(1);
								status = false;
							} catch (Exception e) {
								e.printStackTrace();
							}
				} else {
					Toolkit.getDefaultToolkit().beep();		// make noise if stock rises 5%+
				}				
			}
		}
	}
}

class Music  {
	public static void main(int i) throws Exception {
		Clip clip = AudioSystem.getClip();
		AudioInputStream ais = null;
		if(i == 1) {
			ais = AudioSystem.getAudioInputStream(new File("/Users/Gabe Argush/Downloads/money.wav"));
		} 
		clip.open(ais);
		clip.loop(0);
	}

}