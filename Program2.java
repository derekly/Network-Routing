/*
 * Name: Derek Ly
 * EID: DTL398
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Collections;

public class Program2 extends VertexNetwork {
  private ArrayList<DijkstraVertex> newLocation;
  private Double[][] latencyLookup;
  ArrayList<ArrayList<DijkstraVertex>> adjList;

  
    Program2() {
        super();
        newLocation = new ArrayList<DijkstraVertex>();
        latencyLookup = new Double[location.size()][location.size()];
        adjList = new ArrayList<ArrayList<DijkstraVertex>>();
        createLatencyLookup ();
    	newLocation = updateVertex();
        adjList = createAdjList();
    }
    
    Program2(String locationFile) {
        super(locationFile);
        newLocation = new ArrayList<DijkstraVertex>();
        latencyLookup = new Double[location.size()][location.size()];
        adjList = new ArrayList<ArrayList<DijkstraVertex>>();
        createLatencyLookup ();
    	newLocation = updateVertex();
    	adjList = createAdjList();


    }
    
    Program2(String locationFile, double transmissionRange) {
        super(locationFile, transmissionRange);
        newLocation = new ArrayList<DijkstraVertex>();
        latencyLookup = new Double[location.size()][location.size()];
        adjList = new ArrayList<ArrayList<DijkstraVertex>>();
        createLatencyLookup ();
    	newLocation = updateVertex();

        adjList = createAdjList();

    }
    
    Program2(double transmissionRange, String locationFile) {
        super(transmissionRange, locationFile);
        newLocation = new ArrayList<DijkstraVertex>();
        latencyLookup = new Double[location.size()][location.size()];
        adjList = new ArrayList<ArrayList<DijkstraVertex>>();
        createLatencyLookup ();
    	newLocation = updateVertex();
        adjList = createAdjList();

    }

    public ArrayList<Vertex> gpsrPath(int sourceIndex, int sinkIndex) {
    
    	DijkstraVertex sourceVertex = new DijkstraVertex();
    	DijkstraVertex sinkVertex = new DijkstraVertex();
    	DijkstraVertex currentVertex = new DijkstraVertex();
    	DijkstraVertex tempVertex = new DijkstraVertex();
    	ArrayList<Vertex> GPSRPath = new ArrayList<Vertex>();
    	boolean flag = true;
    	sourceVertex = newLocation.get(sourceIndex);
    	sinkVertex = newLocation.get(sinkIndex);
    	currentVertex = sourceVertex;
    	GPSRPath.add(sourceVertex);

    	while(!currentVertex.equals(sinkVertex) && flag == true){
        	tempVertex = currentVertex;
        	int CVIndex = newLocation.indexOf(currentVertex);
    		for(int counter = 0; counter < adjList.get(CVIndex).size(); counter++){
    			if(currentVertex.distance(adjList.get(CVIndex).get(counter)) <= transmissionRange && tempVertex.distance(sinkVertex) > sinkVertex.distance(adjList.get(CVIndex).get(counter))){
   					tempVertex = adjList.get(CVIndex).get(counter);			
    			}
    		}


    		if(currentVertex.equals(tempVertex) && !tempVertex.equals(sinkVertex)){
    	        flag = false;
    		}else{
    			currentVertex = tempVertex;
    			GPSRPath.add(currentVertex);
    		}
    	}
    	
    	if(flag == false){
    		return new ArrayList<Vertex>(0); // empty array list | return if GPSR fails
    	}else{
    		return GPSRPath; // empty array list | return if GPSR fails
    	}
    }
    
    public ArrayList<Vertex> dijkstraPathLatency(int sourceIndex, int sinkIndex) {

    	for(DijkstraVertex v: newLocation){
    		v.setPrevious(null);
    		v.setLatency(Double.POSITIVE_INFINITY);
    	}
  
    	newLocation.get(sourceIndex).setLatency(0);
    	Comparator<DijkstraVertex> comparator = new LatencyComparator();
    	PriorityQueue<DijkstraVertex> latencyQueue = new PriorityQueue<DijkstraVertex>(newLocation.size(),comparator);
    	for(int i = 0;i < newLocation.size(); i++){
    		latencyQueue.add(newLocation.get(i));
    	}

    	while(latencyQueue.size() != 0){
    		DijkstraVertex currentVertex = latencyQueue.poll();
    		if(currentVertex.getLatency() == Double.POSITIVE_INFINITY){
    			break;
    		}
    		if(currentVertex.equals(newLocation.get(sinkIndex))){
    			break;
    		}
    		int CVIndex = newLocation.indexOf(currentVertex);
    		if(adjList.get(CVIndex).size() > 0){
    			for(int i = 0;i < adjList.get(CVIndex).size(); i++){
    				DijkstraVertex adjVertex = adjList.get(CVIndex).get(i);
    				int AVIndex = newLocation.indexOf(adjVertex);
    				double ping = currentVertex.getLatency() + latencyLookup[CVIndex][AVIndex];
    				if(ping < adjVertex.getLatency()){
    					latencyQueue.remove(adjVertex);
    					adjVertex.setLatency(ping);
    					adjVertex.setPrevious(currentVertex);
    					latencyQueue.add(adjVertex);
    				}
    			}
    		}	
    	}

    	ArrayList<Vertex> DijkstraPath = new ArrayList<Vertex>();
    	DijkstraVertex backVertex = newLocation.get(sinkIndex);
    	DijkstraPath.add(location.get(sinkIndex));
    	while(backVertex.getPrevious() != null  ){
    		backVertex = backVertex.getPrevious();
    		DijkstraPath.add(location.get(newLocation.indexOf(backVertex)));
    	}
		Collections.reverse(DijkstraPath);
    	if(DijkstraPath.get(0).equals(location.get(sourceIndex))){
    		return DijkstraPath;
    	}else{
    		return new ArrayList<Vertex>(0);
    	}
    	
    }
    
    public ArrayList<Vertex> dijkstraPathHops(int sourceIndex, int sinkIndex) {
        
    	for(DijkstraVertex v: newLocation){
    		v.setPrevious(null);
    		v.setLatency(Double.POSITIVE_INFINITY);
    	}
    	newLocation.get(sourceIndex).setLatency(0);
    	Comparator<DijkstraVertex> comparator = new LatencyComparator();
    	PriorityQueue<DijkstraVertex> latencyQueue = new PriorityQueue<DijkstraVertex>(newLocation.size(),comparator);
    	for(int i = 0;i < newLocation.size(); i++){
    		latencyQueue.add(newLocation.get(i));
    	}
    
    	while(latencyQueue.size() != 0){
    		DijkstraVertex currentVertex = latencyQueue.poll();
    		if(currentVertex.getLatency() == Double.POSITIVE_INFINITY){
    			break;
    		}
    		if(currentVertex.equals(newLocation.get(sinkIndex))){
    			break;
    		}
    		int CVIndex = newLocation.indexOf(currentVertex);
    		if(adjList.get(CVIndex).size() > 0){
    			for(int i = 0;i < adjList.get(CVIndex).size(); i++){
    				DijkstraVertex adjVertex = adjList.get(CVIndex).get(i);
    				double ping = currentVertex.getLatency() + 1;
    				if(ping < adjVertex.getLatency()){
    					latencyQueue.remove(adjVertex);
    					adjVertex.setLatency(ping);
    					adjVertex.setPrevious(currentVertex);
    					latencyQueue.add(adjVertex);
    				}
    			}
    		}	
    	}
    	ArrayList<Vertex> DijkstraPath = new ArrayList<Vertex>();
    	DijkstraVertex backVertex = newLocation.get(sinkIndex);
    	DijkstraPath.add(location.get(sinkIndex));
    	while(backVertex.getPrevious() != null  ){
    		backVertex = backVertex.getPrevious();
    		DijkstraPath.add(location.get(newLocation.indexOf(backVertex)));
    	}
		Collections.reverse(DijkstraPath);
    	if(DijkstraPath.get(0).equals(location.get(sourceIndex))){
    		return DijkstraPath;
    	}else{
    		return new ArrayList<Vertex>(0);
    	}
    	
    }
  
    
    public ArrayList<DijkstraVertex> updateVertex(){
    	ArrayList<DijkstraVertex> updatedList = new ArrayList<DijkstraVertex>();
    	for(int i = 0;i < location.size(); i++){
    		DijkstraVertex temp = new DijkstraVertex(location.get(i).getX(),location.get(i).getY());
    		updatedList.add(temp);
    	}
    	return updatedList;
    }
    
    public void createLatencyLookup (){
    	for(int i = 0; i < edges.size(); i++){
    		latencyLookup[edges.get(i).getU()][edges.get(i).getV()] = edges.get(i).getW();
			latencyLookup[edges.get(i).getV()][edges.get(i).getU()] = edges.get(i).getW();
    	}
    }
    
    public ArrayList<ArrayList<DijkstraVertex>> createAdjList(){
    	ArrayList<ArrayList<DijkstraVertex>> List = new ArrayList<ArrayList<DijkstraVertex>>();
    	for(int i= 0;i < newLocation.size();i++){			
    		List.add(new ArrayList<DijkstraVertex>());
    	}
    	for(int i = 0;i < newLocation.size();i++){
    		DijkstraVertex currentVertex = newLocation.get(i);
    		for(int j = 0; j < newLocation.size();j++){
    			if(i != j){
    				DijkstraVertex adjVertex = newLocation.get(j);
    				if(currentVertex.distance(adjVertex) <= transmissionRange){
    					List.get(i).add(adjVertex);
    				}
    			}
    		}
    		
    	}
    	return List;
    }
    
    @Override 
    public void setTransmissionRange(double transmissionRange) {
        this.transmissionRange = transmissionRange;
        adjList = createAdjList();									//creates an adjacency list everytime a new transmission range is called
    }
}


