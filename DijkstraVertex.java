/*
 * Name: Derek Ly
 * EID: DTL398
 */

public class DijkstraVertex extends Vertex {
    private double x; // x-coordinate of the vertex.
    private double y; // y-coordinate of the vertex.
    private double latency;
    private DijkstraVertex previous;
    
    DijkstraVertex(){
    	super();
    }
    
    DijkstraVertex(double x, double y){
    	super(x,y);
    	latency =  Double.POSITIVE_INFINITY;
    	previous = null;
    }
    
    public double getLatency(){
    	return latency;
    }
    public void setLatency(double latency){
    	this.latency = latency;
    }
    public DijkstraVertex getPrevious(){
    	return previous;
    }
    public void setPrevious(DijkstraVertex previous){
    	this.previous = previous;
    }
}