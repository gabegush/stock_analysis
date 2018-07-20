package stock_analysis;

import java.util.Comparator;

/**
 * Compares each entry by % change today
 * @author Gabe Argush
 *
 */
public class ChangeCompare implements Comparator<Stock>{
	public int compare(Stock a, Stock b) {
		if(a.getPerToday() == b.getPerToday()) {
			if(a.getPeRatio() > b.getPeRatio()) {
				return 1;
			} else if (b.getPeRatio() > a.getPeRatio()){
				return -1;
			} else return 0; 
		} 
		else if (a.getPerToday() > b.getPerToday()) {
			return 1;
		}
		else if (b.getPerToday() > a.getPerToday()) {
			return -1;
		}
		else return 0;
	}
}
