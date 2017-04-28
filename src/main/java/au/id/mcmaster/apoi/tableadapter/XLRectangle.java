package au.id.mcmaster.apoi.tableadapter;

public class XLRectangle {
	private int startX = -1;
	private int startY = -1;
	private int endX = -1;
	private int endY = -1;
	
	public String toString() {
		return String.format("[x1=%d, y1=%d, x2=%d, y2=%d]", startX, startY, endX, endY);
	}
	public int getHeight() {
		return endY-startY+1;
	}
	public int getWidth() {
		return endX-startX+1;
	}
	public int getStartX() {
		return startX;
	}
	public void setStartX(int startX) {
		this.startX = startX;
	}
	public int getStartY() {
		return startY;
	}
	public void setStartY(int startY) {
		this.startY = startY;
	}
	public int getEndX() {
		return endX;
	}
	public void setEndX(int endX) {
		this.endX = endX;
	}
	public int getEndY() {
		return endY;
	}
	public void setEndY(int endY) {
		this.endY = endY;
	}
}
