package cf.netdex.raytracer.primitive;

import java.awt.Color;

public abstract class Primitive3D implements Intersectable {

	private Color color;
	protected Box bounds;
	
	public Primitive3D(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
}
