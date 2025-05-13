package logic;



public class Case {
    private int ligne;
    private int colonne;
    private ContenuCase content;
    public Warehouse warehouse;
    
    public Case (int ligne, int colonne, ContenuCase content, Warehouse warehouse) {
    	this.ligne = ligne;
    	this.colonne = colonne;
    	this.content = content;
    	this.warehouse = warehouse;
    }
    
    public ContenuCase getContent() {
    	return this.content;
    }
    
    public void setContent (ContenuCase content) {
    	this.content = content;
    }
    
    public void setContentVoisine(Direction direction, ContenuCase content) {
    	switch (direction) {
		case UP:
			warehouse.getCase(ligne - 1, colonne).setContent(content);
			break;
		case DOWN:
			warehouse.getCase(ligne + 1, colonne).setContent(content);
			break;
		case LEFT:
			warehouse.getCase(ligne, colonne - 1).setContent(content);
			break;
		case RIGHT:
			warehouse.getCase(ligne, colonne + 1).setContent(content);
			break;
	}
    }

    

    public ContenuCase getContentVoisine(Direction direction) {
    	switch (direction) {
			case UP:
				return warehouse.getCase(ligne - 1, colonne).getContent();
			case DOWN:
				return warehouse.getCase(ligne + 1, colonne).getContent();
			case LEFT:
				return warehouse.getCase(ligne, colonne - 1).getContent();
			case RIGHT:
				return warehouse.getCase(ligne, colonne + 1).getContent();
    	}
		return content;
    }

    public boolean acceptGardian(Direction direction) {
    	switch (this.content) {
    		case WALL:
    			return false;
    		case OUTSIDE:
    			return false;
    		case UNSTORED_BOX:
    			switch(direction) {
    				case UP:
    					if (this.ligne == 0) {
    						return false;
    					}
    					break;
    				case DOWN:
    					if (this.ligne == warehouse.getLines() - 1) {
    						return false;
    					}
    					break;
    				case LEFT:
    					if (this.colonne == 0) {
    						return false;
    					}
    					break;
    				case RIGHT:
    					if (this.colonne == warehouse.getColumns() - 1) {
    						return false;
    					}
    					break;

    			}
    			switch(this.getContentVoisine(direction)) {
    				case UNSTORED_BOX:
    					return false;
    				case STORED_BOX:
    					return false;
    				case WALL:
    					return false;
    				case OUTSIDE:
    					return false;
    				case STORAGE_AREA:
    					this.setContent(ContenuCase.WORKER_ON_FLOOR);
    					this.setContentVoisine(direction, ContenuCase.STORED_BOX);
    					break;
    				case FLOOR:
    					this.setContent(ContenuCase.WORKER_ON_FLOOR);
    					this.setContentVoisine(direction, ContenuCase.UNSTORED_BOX);
    					break;
    				default:
    					break;
    			}
    			break;
    		case STORED_BOX:
    			switch(direction) {
				case UP:
					if (this.ligne == 0) {
						return false;
					}
					break;
				case DOWN:
					if (this.ligne == warehouse.getLines() - 1) {
						return false;
					}
					break;
				case LEFT:
					if (this.colonne == 0) {
						return false;
					}
					break;
				case RIGHT:
					if (this.colonne == warehouse.getColumns() - 1) {
						return false;
					}
					break;

    			}
    			switch(this.getContentVoisine(direction)) {
					case UNSTORED_BOX:
						return false;
					case WALL:
						return false;
					case STORED_BOX:
						return false;
					case STORAGE_AREA:
						this.setContent(ContenuCase.WORKER_IN_STORAGE_AREA);
    					this.setContentVoisine(direction, ContenuCase.STORED_BOX);
    					break;
					case FLOOR:
						this.setContent(ContenuCase.WORKER_IN_STORAGE_AREA);
    					this.setContentVoisine(direction, ContenuCase.UNSTORED_BOX);
    					break;
					default:
						break;
    			}
    			break;
    		case FLOOR:
    			this.setContent(ContenuCase.WORKER_ON_FLOOR);
    			break;
    		case STORAGE_AREA:
    			this.setContent(ContenuCase.WORKER_IN_STORAGE_AREA);
    			break;
    		default:
    			break;
    	}
    	Direction previous_player = direction.reverse();
    	switch(this.getContentVoisine(previous_player)) {
    		case WORKER_ON_FLOOR:
    			this.setContentVoisine(previous_player, ContenuCase.FLOOR);
    			return true;
    		case WORKER_IN_STORAGE_AREA:
    			this.setContentVoisine(previous_player, ContenuCase.STORAGE_AREA);
    			return true;
    		default:
    			break;
    	}
    	return false;

    }

}
