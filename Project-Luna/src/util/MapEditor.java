package util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	static Mission mission; 
	
	public static void main(String[] args) {
		Log.print(CLASS, "- Launching Map Editor -");
		
		// set mission 
		mission = new Mission();
		
		new EditorWindow();
	}
	
	
}

class EditorWindow extends JFrame implements ActionListener{
	
	JButton tile_free = new JButton("Free");
	JButton tile_obstacle = new JButton("Obstacle");
	JButton tile_water = new JButton("Water");
	JButton tile_hazard = new JButton("Hazard");
	JButton deselect = new JButton("Deselect all");
	JButton increaseZoom = new JButton("+");
	JButton decreaseZoom = new JButton("-");
	
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
		
		// add a info panel to the right
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(350,1000));
		
		JPanel converButtons = new JPanel(); 
		converButtons.setPreferredSize(new Dimension(350,150));
		JLabel convertInfo = new JLabel("   Convert selection to:");
		convertInfo.setPreferredSize(new Dimension(350,50));
		converButtons.add(convertInfo);
		converButtons.add(tile_free);
		converButtons.add(tile_obstacle);
		converButtons.add(tile_water);
		converButtons.add(tile_hazard);
		converButtons.add(deselect);
		converButtons.setBorder(BorderFactory.createTitledBorder("Tile Options"));
		
		
		JPanel tileInfoPanel = new JPanel();
		tileInfoPanel.setPreferredSize(new Dimension(350,125));
		tileInfoPanel.setBorder(BorderFactory.createTitledBorder("Tile Information"));
		tileInfoPanel.add(tileInfo);
		
		JPanel mapPanel = new JPanel();
		mapPanel.setPreferredSize(new Dimension(350,75));
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map options"));
		mapPanel.add(decreaseZoom);
		mapPanel.add(increaseZoom);
		
		
		// add to info panel 
		infoPanel.add(converButtons);
		infoPanel.add(tileInfoPanel);
		infoPanel.add(mapPanel);
		
		
		// add the listeners 
		tile_free.addActionListener(this);
		tile_obstacle.addActionListener(this);
		tile_water.addActionListener(this);
		tile_hazard.addActionListener(this);
		deselect.addActionListener(this);
		increaseZoom.addActionListener(this);
		decreaseZoom.addActionListener(this);
		
		
		// Add content and set the frame to visible
		add(scroller, BorderLayout.CENTER);
		add(infoPanel, BorderLayout.EAST);
		setVisible(true);
	}
	
	
	
	// save mission to file
	private static void saveMission(Mission mission){
		
	}
	
	
	
	
	public void setInfo(Tile tile){
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource().equals(tile_free))
			mapview.convertTiles(TileType.FREE);
		if ( e.getSource().equals(tile_obstacle))
			mapview.convertTiles(TileType.OBSTACLE);
		if ( e.getSource().equals(tile_water))
			mapview.convertTiles(TileType.WATER);
		if ( e.getSource().equals(tile_hazard))
			mapview.convertTiles(TileType.HAZARD);
		if ( e.getSource().equals(deselect))
			mapview.deselectTiles();
		if ( e.getSource().equals(increaseZoom))
			mapview.increaseZoomLevel();
		if ( e.getSource().equals(decreaseZoom))
			mapview.decreaseZoomLevel();
	}
	
	
	
	
}

class MapView extends JPanel implements MouseListener, MouseMotionListener{

	Rectangle[][] rect = new Rectangle[200][200];
	boolean[][] selection = new boolean[200][200];
	// shift is held down, so we need to only be able to draw in that row / column
	EditorWindow parent; 
	
	float zoomLevel = 1;
	int rectSize = (int) (10*zoomLevel);
	
	Color obstacle 	= new Color(250,0,0); 
	Color free 		= new Color(0,0,0);
	Color hazard 	= new Color(200,200,0); 
	Color water 	= new Color(0,0,250);
	
	
	
	public MapView(EditorWindow parent){
		this.parent = parent;
		
		// create rectangles from tile map 
		for(int x = 0; x < MapEditor.mission.map.length ; x ++)
			for(int y = 0; y < MapEditor.mission.map[0].length ; y ++)
				rect[x][y] = new Rectangle(x*rectSize, y*rectSize, rectSize, rectSize);
			
	
		// TODO set this to the map size 
		setPreferredSize(new Dimension(2000,2000));
		// add the mouse listener 
		addMouseListener(this);
		addMouseMotionListener(this);
		setDoubleBuffered(true);
			
		
	}
	
	@Override
	   protected void paintComponent(Graphics g) {
	      super.paintComponent(g);

	      // draw map 
	     
	      
	      // draw map tiles 
	      for(int x = 0; x < rect.length ; x ++)
		      for(int y = 0; y < rect.length ; y ++){
		    	  
		    	  switch(MapEditor.mission.map[x][y].type){
			    	  default:
			    	  case FREE: 	 g.setColor(free);		break;
			    	  case OBSTACLE: g.setColor(obstacle); 	break;
			    	  case HAZARD:   g.setColor(hazard); 	break;
			    	  case WATER:    g.setColor(water); 	break;
		    	  }
		    	  if (selection[x][y])
		    		  g.fillRect(rect[x][y].x, rect[x][y].y, rect[x][y].width, rect[x][y].height);
		    	  else 
		    		  g.drawRect(rect[x][y].x, rect[x][y].y, rect[x][y].width, rect[x][y].height);
		      }
	      
	
	}
	
	// convert all the selected tile types to that of the parameter
	// and deselects the lot
	public void convertTiles(TileType type){
      for(int x = 0; x < rect.length ; x ++)
	      for(int y = 0; y < rect.length ; y ++)
	    	  if (selection[x][y]){
	    		  selection[x][y] = false;
	    		  MapEditor.mission.map[x][y].type = type;
	    		  repaint(rect[x][y]);
	    	  }
		    	  
	}
	
	public void deselectTiles(){
	  for(int x = 0; x < rect.length ; x ++)
		  for(int y = 0; y < rect.length ; y ++){
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
		setPreferredSize(new Dimension((int)(2000*zoomLevel),(int)(2000*zoomLevel)));
		for(int x = 0; x < MapEditor.mission.map.length ; x ++)
			for(int y = 0; y < MapEditor.mission.map[0].length ; y ++) 
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
			for(int y = 0; y < rect.length ; y ++)
				if ( rect[x][y].contains(mouseX, mouseY)){
					selection[x][y] = !selection[x][y];
					repaint(rect[x][y]);
				}
		
	}



	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		for(int x = 0; x < rect.length ; x ++)
			for(int y = 0; y < rect.length ; y ++)
				if ( rect[x][y].contains(mouseX, mouseY)){
					selection[x][y] = true;
					repaint(rect[x][y]);
					}
					
	}
	
	

	@Override
	public void mouseMoved(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		
		for(int x = 0; x < rect.length ; x ++)
			for(int y = 0; y < rect.length ; y ++)
				if ( rect[x][y].contains(mouseX, mouseY)){
					parent.tileInfo.setText("<html>"
							+ "Tile Type: " + MapEditor.mission.map[x][y].type  
							+ "<br>Tile Position X: " + x  
							+ "<br>Tile Position Y: " + y 
							+ "<br>Map position X: " + (x*10) // * 10 becuase the map size is 2000x2000 
							+ "<br>Map position Y: " + (y*10) // and the tile[][] is [200][200]
							+ "</html>");
				}
	
	}
	
	
	
	
	
	/* UNUSED METHODS */
	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	
}