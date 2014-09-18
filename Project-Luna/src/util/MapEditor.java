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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;

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
	JButton button_load = new JButton("Load Map");
	
	JRadioButton rb_drag = new JRadioButton("Click & Drag");
	JRadioButton rb_click = new JRadioButton("Single Click");
	JRadioButton rb_draw_grid = new JRadioButton("Draw Grid");
	JRadioButton rb_draw_solid = new JRadioButton("Draw Solid");
	JRadioButton rb_draw_map = new JRadioButton("Draw Map");
	
	static JTextField textField_fileName = new JTextField("");
	
	MapView mapview ; 
	
	JLabel tileInfo = new JLabel("Hover over a tile for information.");
	
	
	public EditorWindow(){
		// create window 
		setTitle("Project-Luna Map Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1350, 1000);
		
		// init map view 
		mapview = new MapView(this);
		
		// add the map view (the grid)
		JScrollPane scroller = new JScrollPane(mapview);
		scroller.setPreferredSize(new Dimension(1000,1000));
		scroller.getVerticalScrollBar().setUnitIncrement(16);
		
		// add a info panel to the right
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(350,1000));

		JPanel selectionModePanel = new JPanel();
		selectionModePanel.setPreferredSize(new Dimension(350,75));
		ButtonGroup rbg = new ButtonGroup();
		rbg.add(rb_click);
		rbg.add(rb_drag);
		rb_click.setSelected(true);
		selectionModePanel.add(rb_drag);
		selectionModePanel.add(rb_click);
		selectionModePanel.setBorder(BorderFactory.createTitledBorder("Selection Mode"));
		
		
		JPanel convertButtons = new JPanel(); 
		convertButtons.setPreferredSize(new Dimension(350,150));
		JLabel convertInfo = new JLabel("   Convert selection to:");
		convertInfo.setPreferredSize(new Dimension(350,50));
		convertButtons.add(convertInfo);
		convertButtons.add(button_tile_free);
		convertButtons.add(button_tile_obstacle);
		convertButtons.add(button_tile_water);
		convertButtons.add(button_tile_hazard);
		convertButtons.add(button_deselect);
		convertButtons.setBorder(BorderFactory.createTitledBorder("Tile Options"));
		
		
		JPanel tileInfoPanel = new JPanel();
		tileInfoPanel.setPreferredSize(new Dimension(350,125));
		tileInfoPanel.setBorder(BorderFactory.createTitledBorder("Tile Information"));
		tileInfoPanel.add(tileInfo);
		
		// Map Options
		JPanel mapPanel = new JPanel();
		mapPanel.setPreferredSize(new Dimension(350,125));
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map Options"));
		mapPanel.add(button_decreaseZoom);
		mapPanel.add(button_increaseZoom);
		JPanel drawOptionsPanel = new JPanel();
		rbg = new ButtonGroup();
		rbg.add(rb_draw_grid);
		rbg.add(rb_draw_solid);
		rb_draw_solid.setSelected(true);
		rb_draw_map.setSelected(true);
		drawOptionsPanel.add(rb_draw_grid);
		drawOptionsPanel.add(rb_draw_solid);
		drawOptionsPanel.add(rb_draw_map);
		mapPanel.add(drawOptionsPanel);
		
		// file options panel 
		JPanel fileOptions = new JPanel();
		fileOptions.setPreferredSize(new Dimension(350,125));
		fileOptions.setBorder(BorderFactory.createTitledBorder("File Options"));
		textField_fileName.setPreferredSize(new Dimension(150,25));
		fileOptions.add(textField_fileName);
		fileOptions.add(button_save);
		fileOptions.add(button_load);

		
		
		
		// add to info panel 
		infoPanel.add(selectionModePanel);
		infoPanel.add(convertButtons);
		infoPanel.add(tileInfoPanel);
		infoPanel.add(mapPanel);
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
	private static void saveMap(Tile[][] tiles){
		try {
			
			String fileName =  textField_fileName.getText();
			if ( fileName.trim().isEmpty() || fileName == null )
				fileName = "New Map";
			
			File file = new File("missions/custom/" + fileName + ".map");
			if( !file.exists()){
				file.createNewFile();
			}
			
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream objout = new ObjectOutputStream(out);
			objout.writeObject(tiles);
			objout.close();
			out.close();
			
			Log.print("MAP_EDITOR", "Saved map '" + fileName + ".map'");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	
	
	private static Tile[][] loadMap(){
		
		String fileName =""; 
		
		JFileChooser chooser = new JFileChooser("missions/custom");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Map files", "map");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
			fileName = chooser.getSelectedFile().getName();

		
		
		
		try {
			File file = new File("missions/custom/" + fileName);
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream objin = new ObjectInputStream(in);
			Tile[][] temp =	(Tile[][]) objin.readObject();
			objin.close();
			in.close();
			
			
			textField_fileName.setText(fileName.replace(".map", ""));
			Log.print("MAP_EDITOR", "Loaded map '" + fileName +"'");	

			return temp;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		
		
		return null;
		
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
			mapview.missionMap = loadMap();
			mapview.repaint();
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
	Color drag  	= new Color(250,0,0);
	
	Image texMap; 
	
	Tile[][] missionMap;
	
	
	
	public MapView(EditorWindow parent){
		this.parent = parent;
		
		// load the map texture
		texMap = TextureManager.loadImage("missions/abstract/map.png");
	
		

		// set the dimensions of the map
		texHeight = texMap.getHeight(null);
		texWidth = texMap.getWidth(null);
		tileMapHeight = (int)(texHeight/10);
		tileMapWidth = (int)(texWidth/10);
		
		// set the tile map 
		missionMap = new Tile[tileMapWidth][tileMapHeight];

		// set the drawing rectangles
		rect = new Rectangle[tileMapWidth][tileMapHeight];
		selection = new boolean[tileMapWidth][tileMapHeight];
		
		// create rectangles from tile map 
		for(int x = 0; x < missionMap.length ; x ++)
			for(int y = 0; y < missionMap[x].length ; y ++){
				missionMap[x][y] = new Tile(TileType.FREE, x, y);
				rect[x][y] = new Rectangle(x*rectSize, y*rectSize, rectSize, rectSize);
			}
			
	
		
		setPreferredSize(new Dimension(texWidth, texHeight));
		// add the mouse listener 
		addMouseListener(this);
		addMouseMotionListener(this);
		setDoubleBuffered(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	      super.paintComponent(g);

	      // draw map 
	      if( draw_map)
	    	  g.drawImage(texMap, 0, 0, (int)(texWidth*zoomLevel), (int)(texHeight*zoomLevel), null);
	     
	      
	      
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
		    		  
		    	  else{
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
					parent.tileInfo.setText("<html>"
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
			repaint(drag_selection);
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