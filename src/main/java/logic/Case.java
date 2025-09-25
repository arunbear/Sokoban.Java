package logic;



public class Case {
    private int ligne;
    private int colonne;
    private TileType content;
    public Warehouse warehouse;

    public Case (int ligne, int colonne, TileType content, Warehouse warehouse) {
    	this.ligne = ligne;
    	this.colonne = colonne;
    	this.content = content;
    	this.warehouse = warehouse;
    }

    public TileType getContent() {
    	return this.content;
    }

    public void setContent (TileType content) {
    	this.content = content;
    }

    public void setContentVoisine(Direction direction, TileType content) {
        switch (direction) {
            case UP    -> warehouse.getCase(ligne - 1, colonne).setContent(content);
            case DOWN  -> warehouse.getCase(ligne + 1, colonne).setContent(content);
            case LEFT  -> warehouse.getCase(ligne, colonne - 1).setContent(content);
            case RIGHT -> warehouse.getCase(ligne, colonne + 1).setContent(content);
        }
    }

    public TileType getContentVoisine(Direction direction) {
        return switch (direction) {
            case UP    -> warehouse.getCase(ligne - 1, colonne).getContent();
            case DOWN  -> warehouse.getCase(ligne + 1, colonne).getContent();
            case LEFT  -> warehouse.getCase(ligne, colonne - 1).getContent();
            case RIGHT -> warehouse.getCase(ligne, colonne + 1).getContent();
        };
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
    					this.setContent(TileType.WORKER_ON_FLOOR);
    					this.setContentVoisine(direction, TileType.STORED_BOX);
    					break;
    				case FLOOR:
    					this.setContent(TileType.WORKER_ON_FLOOR);
    					this.setContentVoisine(direction, TileType.UNSTORED_BOX);
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
						this.setContent(TileType.WORKER_IN_STORAGE_AREA);
    					this.setContentVoisine(direction, TileType.STORED_BOX);
    					break;
					case FLOOR:
						this.setContent(TileType.WORKER_IN_STORAGE_AREA);
    					this.setContentVoisine(direction, TileType.UNSTORED_BOX);
    					break;
					default:
						break;
    			}
    			break;
    		case FLOOR:
    			this.setContent(TileType.WORKER_ON_FLOOR);
    			break;
    		case STORAGE_AREA:
    			this.setContent(TileType.WORKER_IN_STORAGE_AREA);
    			break;
    		default:
    			break;
    	}
    	Direction previous_player = direction.reverse();
    	switch(this.getContentVoisine(previous_player)) {
    		case WORKER_ON_FLOOR:
    			this.setContentVoisine(previous_player, TileType.FLOOR);
    			return true;
    		case WORKER_IN_STORAGE_AREA:
    			this.setContentVoisine(previous_player, TileType.STORAGE_AREA);
    			return true;
    		default:
    			break;
    	}
    	return false;

    }

}
