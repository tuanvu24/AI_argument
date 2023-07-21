public enum Semantic {
	CONFLICT_FREE, ADMISSIBLE, COMPLETE, STABLE, UNDEFINED;
	
	public String toString(){
        switch(this){
        case CONFLICT_FREE :
            return "conflict-free";
        case ADMISSIBLE :
            return "admissible";
        case COMPLETE :
        	return "complete";
        case STABLE :
        	return "stable";
        case UNDEFINED :
        	return "";
        }
        return null;
    }
}
