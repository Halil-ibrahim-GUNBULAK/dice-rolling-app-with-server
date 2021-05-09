package game;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class RoundButton extends JButton 
{
  
	private static final long serialVersionUID = 1L;
	Shape shape;
	
	public RoundButton(String label)
	{
		super(label);
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width,size.height);
		setPreferredSize(size);
		setContentAreaFilled(false);
	}
	
	
	
	
	@Override
	public void setEnabled(boolean flag)
	{
		super.setEnabled(flag);
	}
	
	
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		if (getModel().isArmed()) {
			g.setColor(Color.lightGray);
		} 
		
		else 
		{
			g.setColor(getBackground());
		}
    
		g.fillOval(0, 0, getSize().width-1,getSize().height-1);

		super.paintComponent(g);
	}
	
	
	
	
	@Override
	protected void paintBorder(Graphics g)
	{
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width-1,getSize().height-1);
	}
	
	
	
	
	@Override
	public boolean contains(int x, int y)
	{
		if (shape == null || shape.getBounds().equals(getBounds())) 
		{
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
    
		return shape.contains(x, y);
	}

  
}