/*
 * Name: Derek Ly
 * EID: DTL398
 */

import java.util.Comparator;


public class LatencyComparator implements Comparator<DijkstraVertex> {

	public int compare(DijkstraVertex x, DijkstraVertex y){
		if(x.getLatency() < y.getLatency()){
			return -1;
		}
		if(x.getLatency() > y.getLatency()){
			return 1;
		}
		return 0;
	}
	
}
