package stock_analysis;

/**
 * Provides constructor for the Stock class, including attributes price, P/E ratio, etc.
 * @author Gabe Argush
 *
 */
public class Stock {
	private String ticker;
	private double perVolume = 0.00;
	private double peRatio = 0.00;
	private double price = 0.00;
	private double prevClose = 0.00;
	private double perToday = 0.00;
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public double getPerVolume() {
		return perVolume;
	}
	public void setPerVolume(double perVolume) {
		this.perVolume = perVolume;
	}
	public double getPeRatio() {
		return peRatio;
	}
	public void setPeRatio(double peRatio) {
		this.peRatio = peRatio;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPrevClose() {
		return prevClose;
	}
	public void setPrevClose(double prevClose) {
		this.prevClose = prevClose;
	}
	public double getPerToday() {
		return perToday;
	}
	public void setPerToday(double perToday) {
		this.perToday = perToday;
	}
	
	public Stock() {
		super();
	}
	
	public Stock(String ticker, double perVolume, double peRatio, double price, double prevClose, double perToday) {
		this.ticker = ticker;
		this.perVolume = perVolume;
		this.peRatio = peRatio;
		this.price = price;
		this.prevClose = prevClose;
		this.perToday = perToday;
				
	}
	@Override
	public String toString() {
		return "\n" + ticker + ", perVolume=" + perVolume + ", peRatio=" + peRatio + ", price=" + price
				+ ", prevClose=" + prevClose + ", perToday=" + perToday + "]";
	}
}
