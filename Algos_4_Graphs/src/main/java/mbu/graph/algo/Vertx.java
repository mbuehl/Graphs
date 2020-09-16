package mbu.graph.algo;

import mbu.graph.gstrream.Inode;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ewa on 5/30/2019.
 */
public class Vertx implements Inode
{
    private String name;
    private boolean visited;
    private Set<Inode> neighbours;


    public Vertx(char p_name) {
        this(String.valueOf(p_name));
    }

    public Vertx(String p_name) {
        this.name = p_name;
        neighbours = new HashSet<>();
        visited = false;
    }

    @Override
    public String getName()    {      return name;    }
    @Override
    public void setName(String name)   {        this.name = name;    }

    @Override
    public boolean isVisited()   {    return visited;    }
    @Override
    public void setVisited(boolean visited)    {        this.visited = visited;    }

    @Override
    public Set<Inode> getNeighbours()    {        return neighbours;    }

    @Override
    public String getNeighbourNames()
    {
        StringBuffer sb = new StringBuffer("{");
        neighbours.forEach(s -> sb.append(s.getName()).append(" "));
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void setNeighbours(Set<Inode> neighbours)    {        this.neighbours = neighbours;    }

    @Override
    public void addNeighbours(Inode[] p_ngbrs){ for (Inode n: p_ngbrs)   addNeighbour(n);   }
    @Override
    public void addNeighbour(Inode p_ngbr){
        neighbours.add(p_ngbr);
    }

    @Override
    public String toString() {
        return "Vertx{" +
                "name='" + name + '\'' +
                ", visited=" + visited +
//                    ", neighbours=" + neighbours.forEach(( +
                '}';
    }

}
