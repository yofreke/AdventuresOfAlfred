package com.yofreke.alfred.pathing;

import java.awt.Point;
import java.util.ArrayList;

import com.yofreke.alfred.PointF;
import com.yofreke.alfred.entity.Living;
import com.yofreke.alfred.level.Chunk;
import com.yofreke.alfred.level.Level;

public class PathFinder {

	private Point[] MOV_OPT = new Point[]{
			new Point(-1, 0), new Point(1, 0), new Point(0, 1), new Point(0, -1)
	};
	
	private Level level;
	private Living entity;
	private MinHeap openList;
	
	private PathFinderRegion region;
	
	public PathFinder(Level level, Living entity){
		this.level = level;
		this.entity = entity;
	}
	
	public Path findPath(int startX, int startY, int endX, int endY){
		//System.out.println("finding path from ("+startX+","+startY+") to ("+endX+","+endY+")");
		region = new PathFinderRegion(level, entity, startX, startY, endX, endY);
		
		openList = new MinHeap();
		PathNode startNode = new PathNode(region.start, 0, 0, null);
		openList.Add(startNode);
		region.setUsed(startNode.position);
		// start the search
		
		PathNode current;
		PathNode node;
		while(openList.HasNext()){
			 current = openList.ExtractFirst();
			
			if(region.distToEnd(current.position.x, current.position.y) < 1){
				//System.out.println("PATH FOUND");
				ArrayList<PointF> pathPoints = new ArrayList<PointF>();
				PathNode tmpNode = current;
				while(tmpNode != null){
					//System.out.println("  ::"+(tmpNode.position.x + region.ox)+","+(tmpNode.position.y + region.oy));
					pathPoints.add(new PointF((tmpNode.position.x + region.ox) * Chunk.TILE_SIZE + 4, (tmpNode.position.y + region.oy) * Chunk.TILE_SIZE + 4));
					tmpNode = tmpNode.next;
				}
				Path path = new Path(pathPoints.size());
				for(int i = 0; i < pathPoints.size(); i++) {
					path.setPoint(i, pathPoints.get(i));
				}
				return path;
			}
			
			//System.out.println("  cur point: "+current.toString());
			for(Point movPoint : MOV_OPT){
				int newX = current.position.x + movPoint.x;
				int newY = current.position.y + movPoint.y;
				if(region.isValid(newX, newY)) {
					int pathCost = current.pathCost + region.getCost(newX, newY);
					node = new PathNode(new Point(newX, newY), pathCost, current.pathCost + region.distToEnd(newX, newY), current);
					openList.Add(node);
					region.setUsed(node.position);
					//System.out.println("  .  new open list "+node.toString());
				}
			}
		}
		System.out.println("NO PATH");
		return null;
	}
}
