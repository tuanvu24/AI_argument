public enum Context {
	EXTENSION_S, EXTENSION_S2, CONFLICT_FREE, ADMISSIBLE, COMPLETE, STABLE, AF, AF2, UNDEFINED;
	
	public String toString(){
	    switch(this){
	    case EXTENSION_S :
	        return "in the extenstion S";
	    case EXTENSION_S2 :
	        return "in the extenstion S'";
	    case CONFLICT_FREE :
	        return "for the conflict-free semantic";
	    case ADMISSIBLE :
	        return "for the admissible semantic";
	    case COMPLETE :
	    	return "for the complete semantic";
	    case STABLE :
	    	return "for the stable semantic";
	    case AF :
	    	return "in the AF(A,R)";
	    case AF2 :
	    	return "in the AF(A',R')";
	    }
	    return null;
	}
}


