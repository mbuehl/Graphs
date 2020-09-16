package mbu.graph.gstrream;

import java.util.Set;

/**
 * Created by ewa on 6/16/2019.
 */
public interface Inode
{

    String getName();

    void setName(String name);

    boolean isVisited();

    void setVisited(boolean visited);

    Set<Inode> getNeighbours();

    String getNeighbourNames();

    void setNeighbours(Set<Inode> neighbours);

    void addNeighbours(Inode[] p_ngbrs);

    void addNeighbour(Inode p_ngbr);

    @Override
    String toString();
}
