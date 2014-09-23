package util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.opengl.Texture;

import util.MapView.SelectionType;
import model.Mission;
import model.Tile;
import model.TileType;


/**
 * @version 1.00
 * <br><br>
 * This class is only used by the developers to create the map tiles for every mission.
 * But it is planned to be added to the main game as a level creator. 
 * 
 */
public class MapEditor {

	private static final String CLASS = "MAP_EDITOR";
	
	public static void main(String[] args) {
		Log.print(CLASS, "- Launching Map Editor -");
		new EditorWindow();
	}
	
	
}

class EditorWindow extends JFrame implements ActionListener{
	
	JButton button_tile_free = new JButton("Free");
	JButton button_tile_obstacle = new JButton("Obstacle");
	JButton button_tile_water = new JButton("Water");
	JButton button_tile_hazard = new JButton("Hazard");
	JButton button_deselect = new JButton("Deselect all");
	JButton button_increaseZoom = new JButton("+");
	JButton button_decreaseZoom = new JButton("-");
	JButton button_save = new JButton("Save Map");
	JButton button_load = new JButton("Load Map or Create New Map");
	
	JRadioButton rb_drag = new JRadioButton("Click & Drag");
	JRadioButton rb_click = new JRadioButton("Single Click");
	JRadioButton rb_draw_grid = new JRadioButton("Draw Grid");
	JRadioButton rb_draw_solid = new JRadioButton("Draw Solid");
	JRadioButton rb_draw_map = new JRadioButton("Draw Map");
	
	JTextField textField_fileName = new JTextField("");
	
	MapView mapview ; 
	
	JLabel label_tileInfo = new JLabel("Hover over a tile for information.");
	JLabel label_mapInfo = new JLabel("<html>-> Create a new map by loading a map texture (.png)"
										+ "<br>-> Or load a tile map file (.map)"
										+ "<br>-> You can also create a tile map from a heightmap file (.hm.png)"
										+ "<br><br>*Note* all files belonging to one map "
										+ "<br>must have the same name (but different extensions)"
										+ "</html>");
			
	
	
	public EditorWindow(){
		// create window 
		setTitle("Project-Luna Map Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1375, 1000);
		
		final int infoPanelWidth = 375;
		// init map view 
		mapview = new MapView(this);
		
		// add the map view (the grid)
		JScrollPane scroller = new JScrollPane(mapview);
		scroller.setPreferredSize(new Dimension(1000,1000));
		scroller.getVerticalScrollBar().setUnitIncrement(16);
		
		// add a info panel to the right
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(infoPanelWidth,1000));

		JPanel selectionModePanel = new JPanel();
		selectionModePanel.setPreferredSize(new Dimension(infoPanelWidth,75));
		ButtonGroup rbg = new ButtonGroup();
		rbg.add(rb_click);
		rbg.add(rb_drag);
		rb_click.setSelected(true);
		selectionModePanel.add(rb_drag);
		selectionModePanel.add(rb_click);
		selectionModePanel.setBorder(BorderFactory.createTitledBorder("Selection Mode"));
		
		
		JPanel convertButtons = new JPanel(); 
		convertButtons.setPreferredSize(new Dimension(infoPanelWidth,150));
		JLabel convertInfo = new JLabel("   Convert selection to:");
		convertInfo.setPreferredSize(new Dimension(infoPanelWidth,50));
		
		button_tile_free.setBackground(Color.BLACK);
		button_tile_free.setForeground(Color.LIGHT_GRAY);
		button_tile_obstacle.setBackground(Color.RED);
		button_tile_water.setBackground(Color.BLUE);
		button_tile_water.setForeground(Color.LIGHT_GRAY);
		button_tile_hazard.setBackground(Color.ORANGE);
		convertButtons.add(convertInfo);
		convertButtons.add(button_tile_free);
		convertButtons.add(button_tile_obstacle);
		convertButtons.add(button_tile_water);
		convertButtons.add(button_tile_hazard);
		convertButtons.add(button_deselect);
		convertButtons.setBorder(BorderFactory.createTitledBorder("Tile Options"));
		
		
		JPanel tileInfoPanel = new JPanel();
		tileInfoPanel.setPreferredSize(new Dimension(infoPanelWidth,125));
		tileInfoPanel.setBorder(BorderFactory.createTitledBorder("Tile Information"));
		tileInfoPanel.add(label_tileInfo);
		
		// Map Options
		JPanel mapPanel = new JPanel();
		mapPanel.setPreferredSize(new Dimension(infoPanelWidth,125));
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map Options"));
		mapPanel.add(button_decreaseZoom);
		mapPanel.add(button_increaseZoom);
		JPanel drawOptionsPanel = new JPanel();
		rb_draw_solid.setSelected(true);
		rb_draw_map.setSelected(true);
		drawOptionsPanel.add(rb_draw_grid);
		drawOptionsPanel.add(rb_draw_solid);
		drawOptionsPanel.add(rb_draw_map);
		mapPanel.add(drawOptionsPanel);
		
		// file options panel 
		JPanel fileOptions = new JPanel();
		fileOptions.setPreferredSize(new Dimension(infoPanelWidth,125));
		fileOptions.setBorder(BorderFactory.createTitledBorder("File Options"));
		textField_fileName.setPreferredSize(new Dimension(150,25));
		textField_fileName.setEditable(false);
		button_save.setEnabled(false);
		button_load.setPreferredSize(new Dimension(243,25));
		fileOptions.add(button_load);
		fileOptions.add(textField_fileName);
		fileOptions.add(button_save);
		
		
		// Map information 
		JPanel mapInfo = new JPanel();
		mapInfo.setPreferredSize(new Dimension(infoPanelWidth,150));
		mapInfo.setBorder(BorderFactory.createTitledBorder("Map Information"));
		mapInfo.add(label_mapInfo);
		
		
		// add to info panel 
		infoPanel.add(selectionModePanel);
		infoPanel.add(convertButtons);
		infoPanel.add(mapPanel);
		infoPanel.add(tileInfoPanel);
		infoPanel.add(mapInfo);
		infoPanel.add(fileOptions);
		
		
		// add the listeners 
		button_tile_free.addActionListener(this);
		button_tile_obstacle.addActionListener(this);
		button_tile_water.addActionListener(this);
		button_tile_hazard.addActionListener(this);
		button_deselect.addActionListener(this);
		button_increaseZoom.addActionListener(this);
		button_decreaseZoom.addActionListener(this);
		button_save.addActionListener(this);
		button_load.addActionListener(this);
		
		rb_draw_grid.addActionListener(this);
		rb_draw_solid.addActionListener(this);
		rb_draw_map.addActionListener(this);
		rb_drag.addActionListener(this);
		rb_click.addActionListener(this);
		// Add content and set the frame to visible
		add(scroller, BorderLayout.CENTER);
		add(infoPanel, BorderLayout.EAST);
		setVisible(true);
	}
	
	// save mission to file
	private void saveMap(Tile[][] tiles){
		// tileMap to be saved 
		try {
			
			// get the filename
			String fileName =  textField_fileName.getText();
			if ( fileName.trim().isEmpty() || fileName == null )
				fileName = "New Map";
			
			// save the file with the '.MAP' Extension
			// if the file exists, we just overwrite it for now
			File file = new File("missions/" + fileName + ".map");
			if( !file.exists()){
				file.createNewFile();
			}
			
			// save the tileMap as a binary java object 
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream objout = new ObjectOutputStream(out);
			objout.writeObject(tiles);
			objout.close();
			out.close();
			Log.print("MAP_EDITOR", "Saved map '" + fileName + ".map'");

			// TODO notify the user of anyhting that didn't happen according to plan
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Loads a map from either a .map or a .png file 
	 * <br><br> 
	 * If the user chooses to load from a .map, then the map.png is 
	 * also loaded. 
	 * <br><br>
	 * If the user chooses to load from a .png file, then an empty 
	 * tileMap is created and can be saved. 
	 * <br><br>
	 * If the users chooses a .hm.png (heightmap file) then create 
	 * a tileMap using the pixel data, also save and show the the 
	 * tileMap afterwards.
	 * 
	 */
	private void loadMap(){
		
		String fileName =""; 
		
		// The use may either load a normal PNG image 
		// or a map file 
		
		JFileChooser chooser = new JFileChooser("missions");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Maps, maps and heightmaps", "map", "png");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
			fileName = chooser.getSelectedFile().getName();
		else
			return; 

		
		if ( fileName.endsWith(".map"))
			loadFromMAP(fileName);

		else if (fileName.endsWith(".hm.png"))
			loadFromHeightmap(fileName);

		else if (fileName.endsWith(".png"))
			loadFromPNG(fileName);
			
		
		// enable the save button now
		mapview.invalidate();
		button_save.setEnabled(true);
		return;
	}
	
	private void loadFromMAP(String fileName){
		try{
			File file = new File("missions/" + fileName);
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream objin = new ObjectInputStream(in);
			Tile[][] temp =	(Tile[][]) objin.readObject();
			objin.close();
			in.close();
			// create the mission map and load the map from the mapview createMap method
			mapview.createMap("missions/" + fileName.replace(".map", ".png"), temp);
			// set the mission name
			textField_fileName.setText(fileName.replace(".map", ""));
			Log.print("MAP_EDITOR", "Loaded map '" + fileName +"'");
		}
		catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	private void loadFromPNG(String fileName){
		// load the map texture, but tell the mapview to create a new missionMap
		mapview.createMap("missions/" + fileName, null);
		textField_fileName.setText(fileName.replace(".png", ""));
	}
	
	private void loadFromHeightmap(String fileName) {
		
		// we need a bufferedImage to call getRGB()
		BufferedImage heightmap = (BufferedImage) TextureManager.loadImage("missions/" + fileName);
		
		// get the dimensions and create a new tile map
		int width = heightmap.getWidth();
		int height = heightmap.getHeight();
		int tileMapHeight = (int)(width/10);
		int tileMapWidth = (int)(height/10);
		Tile[][] missionMap = new Tile[tileMapHeight][tileMapWidth];

		// PREdefine the colors used in the heightmap
		final int blockType_free 	 = -1;				// WHITE
		final int blockType_obstacle = -16777216;		// BLACK
		final int blockType_water 	 = -16776961;		// BLUE
		final int blockType_hazard 	 = -39424;			// ORANGE

		
		for(int x = 0; x < missionMap.length ; x ++){
			for(int y = 0; y < missionMap[x].length ; y ++){
				// get the pixel colors every 10 pixels 
				switch(heightmap.getRGB(x*10, y*10)){
					default:
					case blockType_free: 	 missionMap[x][y] = new Tile(TileType.FREE, x, y); break;
					case blockType_obstacle: missionMap[x][y] = new Tile(TileType.OBSTACLE, x, y); break;
					case blockType_water: 	 missionMap[x][y] = new Tile(TileType.WATER, x, y); break;
					case blockType_hazard:	 missionMap[x][y] = new Tile(TileType.HAZARD, x, y); break;
				}
			}
		}
		
		
	    
		// set the filename
		textField_fileName.setText(fileName.replace(".hm.png", ""));
		// save the newly created tilemap
		//saveMap(missionMap);
		// finally, tell the mapview to show the newly created map and its tile map
		mapview.createMap("missions/" + fileName.replace(".hm.png", ".png"), missionMap);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource().equals(button_tile_free))
			mapview.convertTiles(TileType.FREE);
		if ( e.getSource().equals(button_tile_obstacle))
			mapview.convertTiles(TileType.OBSTACLE);
		if ( e.getSource().equals(button_tile_water))
			mapview.convertTiles(TileType.WATER);
		if ( e.getSource().equals(button_tile_hazard))
			mapview.convertTiles(TileType.HAZARD);
		if ( e.getSource().equals(button_deselect))
			mapview.deselectTiles();
		if ( e.getSource().equals(button_increaseZoom))
			mapview.increaseZoomLevel();
		if ( e.getSource().equals(button_decreaseZoom))
			mapview.decreaseZoomLevel();
		if ( e.getSource().equals(button_save))
			saveMap(mapview.missionMap);
		if ( e.getSource().equals(button_load)){
			String fileName =  textField_fileName.getText();
			loadMap();
		}
		
		// set selection type mode 
		if ( e.getSource().equals(rb_click) || e.getSource().equals(rb_drag)){
			if ( rb_click.isSelected()){
				mapview.selectionType = SelectionType.CLICK;
			}else{
				mapview.selectionType = SelectionType.DRAG;
			}
			mapview.deselectTiles();
		}
		
		if( e.getSource().equals(rb_draw_grid) || e.getSource().equals(rb_draw_solid)){
			mapview.draw_grid = rb_draw_grid.isSelected();
			mapview.draw_fill = rb_draw_solid.isSelected();
			mapview.repaint();
		}
		if ( e.getSource().equals(rb_draw_map)){
			mapview.draw_map = rb_draw_map.isSelected();
			mapview.repaint();
		}
		
		
		
		
		
	}
	
}

class MapView extends JPanel implements MouseListener, MouseMotionListener{

	
	public enum SelectionType{DRAG, CLICK}
	
	// current selection type 
	SelectionType selectionType = SelectionType.CLICK;
	
	// Drag selections 
	Rectangle drag_selection = new Rectangle();
	// when the selection mode DRAG is set as active
	int mouseXorigin;
	int mouseYorigin;
	
	boolean draw_map = true; 
	boolean draw_grid = false;
	boolean draw_fill = false;
	
	//
	Rectangle[][] rect;
	boolean[][] selection;
	// shift is held down, so we need to only be able to draw in that row / column
	EditorWindow parent; 
	
	// texture dimensions (default values for when the map fails to load) 
	int texHeight = 1;
	int texWidth  = 1;
	// map tile dimensions 
	// -> 10th of the texture dimensions
	int tileMapHeight;
	int tileMapWidth;
	
	float zoomLevel = 1;
	int rectSize = 10;
	
	Color obstacle 	= new Color(250,0,0,100); 
	Color free 		= new Color(0,0,0,100);
	Color hazard 	= new Color(200,200,0,100); 
	Color water 	= new Color(0,0,250,100);
	Color drag  	= new Color(0,250,0,10);
	
	// current map files
	// the map texture must have the same name as it's .map file
	// but in PNG format 
	Image texMap, texMap_foreground;
	
	Tile[][] missionMap;
	
	
	public MapView(EditorWindow parent){
		this.parent = parent;
	}
	
	
	public void createMap(String missionName, Tile[][] missionMap){
		
		// ALWAYS load the map texture
		texMap = TextureManager.loadImage(missionName);
		texMap_foreground = TextureManager.loadImage(missionName.replace(".png", "_foreground.png"));
		
		if ( texMap == null ) {
			Log.printErr("MAP_EDITOR", "Error loading map (texMap==null)");
			return;
		}
		
		// set the dimensions of the map
		texHeight = texMap.getHeight(null);
		texWidth = texMap.getWidth(null);
		tileMapHeight = (int)(texHeight/10);
		tileMapWidth = (int)(texWidth/10);
		
		// create a new mission map 
		// set the tile map 
		if ( missionMap == null ){
			// create new mission map
			this.missionMap = new Tile[tileMapWidth][tileMapHeight];
			// create every tile 
			for(int x = 0; x < this.missionMap.length ; x ++)
				for(int y = 0; y < this.missionMap[x].length ; y ++)
					this.missionMap[x][y] = new Tile(TileType.FREE, x, y);
		}
		else 
			this.missionMap = missionMap;

		// set the drawing rectangles
		rect = new Rectangle[tileMapWidth][tileMapHeight];
		selection = new boolean[tileMapWidth][tileMapHeight];
		
		// create rectangles from tile map 
		for(int x = 0; x < this.missionMap.length ; x ++)
			for(int y = 0; y < this.missionMap[x].length ; y ++)
				rect[x][y] = new Rectangle(x*rectSize, y*rectSize, rectSize, rectSize);
			
		// set the size of this panel to that of the map
		setPreferredSize(new Dimension(texWidth, texHeight));
		repaint();
		
		// add the mouse listener 
		addMouseListener(this);
		addMouseMotionListener(this);
		setDoubleBuffered(true);
		
		// set the map info in the parent view 
		parent.label_mapInfo.setText("<html>"
				+ "Map Texture: " + missionName  
				+ "<br>tile Map: " + missionName.replace(".png", ".map")  
				+ "<br>Map Width: " + texWidth  
				+ "<br>Map Height: " + texHeight  
				+ "<br>Tile Map Width: " + tileMapWidth
				+ "<br>Tile Map Height: " + tileMapHeight  
				+ "<br>Tile Size: " + rectSize  
				+ "</html>");
		
	}
	
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
	      super.paintComponent(g);

	      if ( texMap == null)
	    	  return; 
	      
	      // draw map 
	      if( draw_map && texMap != null ){
	    	  g.drawImage(texMap, 0, 0, (int)(texWidth*zoomLevel), (int)(texHeight*zoomLevel), null);
	    	  // draw the foreground as well, if there is one 
	    	  if ( texMap_foreground != null )
	    		  g.drawImage(texMap_foreground, 0, 0, (int)(texWidth*zoomLevel), (int)(texHeight*zoomLevel), null);
	    		  
	      }
	      
	      // draw map tiles 
	      for(int x = 0; x < rect.length ; x ++)
		      for(int y = 0; y < rect[0].length ; y ++){
		    	  
		    	  switch(missionMap[x][y].type){
			    	  default:
			    	  case FREE: 	 g.setColor(free);		break;
			    	  case OBSTACLE: g.setColor(obstacle); 	break;
			    	  case HAZARD:   g.setColor(hazard); 	break;
			    	  case WATER:    g.setColor(water); 	break;
		    	  }
		    	  
		    	  // Draw mode solid or grid
		    	  if ( draw_grid ){
		    		  g.drawRect(rect[x][y].x, rect[x][y].y, rect[x][y].width, rect[x][y].height);
		    		  if (selection[x][y])
		    			  g.fillRect(rect[x][y].x, rect[x][y].y, rect[x][y].width, rect[x][y].height);
		    	  }
		    	  
		    	  if( draw_fill ){
		    		  g.fillRect(rect[x][y].x, rect[x][y].y, rect[x][y].width, rect[x][y].height);
		    		  // if the tile is selected, draw it again to appear selected (because of the alpha value
		    		  // it will appear darker)
		    		  if (selection[x][y])
		    			  g.fillRect(rect[x][y].x, rect[x][y].y, rect[x][y].width, rect[x][y].height);
		    	  }
		    		  
		    	  // draw drag selection
		    	  if (selectionType == SelectionType.DRAG){
		    		  g.setColor(drag);
		    		  g.drawRect(drag_selection.x, drag_selection.y, drag_selection.width, drag_selection.height);
		    	  }
		      
		      
		      }
	}
	
	// convert all the selected tile types to that of the parameter
	// and deselects the lot
	public void convertTiles(TileType type){
      for(int x = 0; x < rect.length ; x ++)
	      for(int y = 0; y < rect[0].length ; y ++)
	    	  if (selection[x][y]){
	    		  selection[x][y] = false;
	    		  missionMap[x][y].type = type;
	    		  repaint(rect[x][y]);
	    	  }
		    	  
	}
	
	public void deselectTiles(){
	  for(int x = 0; x < rect.length ; x ++)
		  for(int y = 0; y < rect[0].length ; y ++){
	    	  selection[x][y] = false;
	    	  repaint(rect[x][y]);
	    }
	}
	
	public void increaseZoomLevel(){
		if ( zoomLevel < 4.5 ) 
			zoomLevel += 0.5;
	
		setZoom();
	}
	
	public void decreaseZoomLevel(){
		if ( zoomLevel > 0.5) 
			zoomLevel -= 0.5;
		
		setZoom();
	}
	
	private void setZoom(){
		rectSize = (int) (10*zoomLevel);
		setPreferredSize(new Dimension((int)(texWidth*zoomLevel),(int)(texHeight*zoomLevel)));
		for(int x = 0; x < missionMap.length ; x ++)
			for(int y = 0; y < missionMap[0].length ; y ++) 
				rect[x][y].setBounds(x*rectSize, y*rectSize, rectSize, rectSize);
					
		
		repaint();
		invalidate();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// get mouse position 
		int mouseX = e.getX();
		int mouseY = e.getY();
		// find the clicked tile
		for(int x = 0; x < rect.length ; x ++)
			for(int y = 0; y < rect[0].length ; y ++)
				if ( rect[x][y].contains(mouseX, mouseY)){
					selection[x][y] = !selection[x][y];
					repaint(rect[x][y]);
				}
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		
		if (selectionType == selectionType.CLICK){
			for(int x = 0; x < rect.length ; x ++)
				for(int y = 0; y < rect[0].length ; y ++)
					if ( rect[x][y].contains(mouseX, mouseY)){
						selection[x][y] = true;
						repaint(rect[x][y]);
					}
		}
		
		else {
			drag_selection.setBounds(
					Math.min(mouseXorigin, mouseX),
					Math.min(mouseYorigin, mouseY), 
					Math.abs(mouseX-mouseXorigin), 
					Math.abs(mouseY-mouseYorigin));
			
			repaint();
		}
		
		
					
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		for(int x = 0; x < rect.length ; x ++)
			for(int y = 0; y < rect[0].length ; y ++)
				if ( rect[x][y].contains(mouseX, mouseY)){
					parent.label_tileInfo.setText("<html>"
							+ "Tile Type: " + missionMap[x][y].type  
							+ "<br>Tile Position X: " + x  
							+ "<br>Tile Position Y: " + y 
							+ "<br>Map position X: " + (x*10) // * 10 because the map size is 2000x2000 
							+ "<br>Map position Y: " + (y*10) // and the tile[][] is [200][200]
							+ "</html>");
				}
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouseXorigin = (int) e.getX(); 
		mouseYorigin = (int) e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseYorigin = 0; 
		mouseXorigin = 0;
		
		// if the user dragged a rectangle to select rects 
		if (selectionType == SelectionType.DRAG){
			for(int x = 0; x < rect.length ; x ++)
				for(int y = 0; y < rect[0].length ; y ++)
					if ( drag_selection.contains(rect[x][y])){
						selection[x][y] = true; 
					}
			repaint();
		}
		
		drag_selection.setBounds(0,0,0,0);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	
}