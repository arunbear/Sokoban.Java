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
    		case CAISSE:
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
    				case CAISSE:
    					return false;
    				case CAISSE_RANGEE:
    					return false;
    				case WALL:
    					return false;
    				case OUTSIDE:
    					return false;
    				case RANGEMENT:
    					this.setContent(ContenuCase.JOUEUR);
    					this.setContentVoisine(direction, ContenuCase.CAISSE_RANGEE);
    					break;
    				case CASE_VIDE:
    					this.setContent(ContenuCase.JOUEUR);
    					this.setContentVoisine(direction, ContenuCase.CAISSE);
    					break;
    				default:
    					break;
    			}
    			break;
    		case CAISSE_RANGEE:
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
					case CAISSE:
						return false;
					case WALL:
						return false;
					case CAISSE_RANGEE:
						return false;
					case RANGEMENT:
						this.setContent(ContenuCase.JOUEUR_RANGEMENT);
    					this.setContentVoisine(direction, ContenuCase.CAISSE_RANGEE);
    					break;
					case CASE_VIDE:
						this.setContent(ContenuCase.JOUEUR_RANGEMENT);
    					this.setContentVoisine(direction, ContenuCase.CAISSE);
    					break;
					default:
						break;
    			}
    			break;
    		case CASE_VIDE:
    			this.setContent(ContenuCase.JOUEUR);
    			break;
    		case RANGEMENT:
    			this.setContent(ContenuCase.JOUEUR_RANGEMENT);
    			break;
    		default:
    			break;
    	}
    	Direction previous_player = direction.reverse();
    	switch(this.getContentVoisine(previous_player)) {
    		case JOUEUR:
    			this.setContentVoisine(previous_player, ContenuCase.CASE_VIDE);
    			return true;
    		case JOUEUR_RANGEMENT:
    			this.setContentVoisine(previous_player, ContenuCase.RANGEMENT);
    			return true;
    		default:
    			break;
    	}
    	return false;

    }

}
