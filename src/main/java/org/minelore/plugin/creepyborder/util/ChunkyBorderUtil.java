package org.minelore.plugin.creepyborder.util;


import org.bukkit.Location;
import org.popcraft.chunky.platform.util.Vector2;
import org.popcraft.chunky.shape.*;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 09.02.2025
 */
public class ChunkyBorderUtil {

    public static double distToBorder(Location location, Shape shape) {
        Vector2 pos = new Vector2(location.getX(), location.getZ());
        return switch (shape) {
            case AbstractPolygon polygon -> distToBorder(pos, polygon);
            case AbstractEllipse abstractEllipse -> distToBorder(pos, abstractEllipse);
            case null, default -> throw new IllegalArgumentException("Shape " + shape.name() + " no support");
        };
    }

    public static double distToBorder(Vector2 pos, AbstractPolygon polygon) {
        List<Vector2> points = polygon.points();
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); ++i) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i == points.size() - 1 ? 0 : i + 1);
            Vector2 closestPoint = ShapeUtil.closestPointOnLine(pos.getX(), pos.getZ(), p1.getX(), p1.getZ(), p2.getX(), p2.getZ());
            minDistance = Math.min(ShapeUtil.distanceBetweenPoints(pos.getX(), pos.getZ(), closestPoint.getX(), closestPoint.getZ()), minDistance);
        }
        return minDistance;
    }

    public static double distToBorder(Vector2 pos, AbstractEllipse abstractEllipse) {
        if (abstractEllipse instanceof Circle circle) {
            return Math.hypot(circle.center().getX() - pos.getX(), circle.center().getZ() - pos.getZ()) - circle.radii().getX();
        }
//        else if (abstractEllipse instanceof Ellipse ellipse) {
//
//        }
        else throw new IllegalArgumentException("Abstract ellipse " + abstractEllipse.name() + " no support.");
    }
}
