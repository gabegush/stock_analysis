package stock_analysis;

import java.util.Comparator;

/**
 * Compares each entry by P/E Ratio (trailing)
 * @author Gabe Argush
 *
 */
public class PECompare implements Comparator<Stock>{
	public int compare(Stock a, Stock b) {
		if(a.getPeRatio() == b.getPeRatio()) {
			if(a.getPerToday() > b.getPerToday()) {
				return 1;
			} else if (b.getPerToday() > a.getPerToday()){
				return -1;
			} else return 0; 
		} 
		else if (a.getPeRatio() > b.getPeRatio()) {
			return 1;
		}
		else if (b.getPeRatio() > a.getPeRatio()) {
			return -1;
		}
		else return 0;
	}
}
