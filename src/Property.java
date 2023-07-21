public enum Property {
	ACCEPTED, CONFLICT_FREE, ADMISSIBLE, COMPLETE, STABLE, UNDEFINED;
	
	public String toString(){
        switch(this){
        case ACCEPTED :
            return "accepted";
        case CONFLICT_FREE :
            return "a conflict-free extension";
        case ADMISSIBLE :
            return "an admissible extension";
        case COMPLETE :
        	return "a complete extension";
        case STABLE :
        	return "a stable extension";
        case UNDEFINED :
        	return "";
        }
        return null;
    }
}
