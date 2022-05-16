package rs.lunarshop.map;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GougedRoomNode extends MapRoomNode {
    public GougedRoomNode(int x, int y) {
        super(x, y);
        color = Color.BLUE.cpy();
    }
    
    @Override
    public void addEdge(MapEdge edge) {
        try {
            Field edges = MapRoomNode.class.getDeclaredField("edges");
            edges.setAccessible(true);
            List<MapEdge> list = (ArrayList<MapEdge>) edges.get(this);
            list.add(edge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}