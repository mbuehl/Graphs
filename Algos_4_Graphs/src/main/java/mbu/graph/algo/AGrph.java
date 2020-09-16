package mbu.graph.algo;


import mbu.graph.gstrream.*;
import mbu.utl.IDefaults;

import java.util.*;

/**
 x Created by ewa on 5/30/2019.   note:      Strictly graph object related -  N O   connection with Swing UI
 x
 x           PREP
 x           -----
 x edges     1. create Edge list - all pairs of letters
 x vertex    2. build Vertx map - iterate thru Edge list - create Vertx if not in map
 x
 */
public class AGrph<Elm extends Iedge> implements Iedge, IDefaults
{
    public List<Elm> edges = new ArrayList<>();
    public Map<String,Inode> nodeM  = new HashMap<>();

    public void prep(Elm[] elms)
    {
        for(Elm edge: elms)
            edges.add(edge);

        buildNodeMap();
        dsplNodeMap();
    }

    public void buildNodeMap()
    {
//1. create nodes  and  build map of vertices
        for (Elm edge : edges)   {
            nodeM.putIfAbsent(edge.getB(), new Vertx(edge.getB()));
            nodeM.putIfAbsent(edge.getE(), new Vertx(edge.getE()));
        }

//2. validate whether FROM and TO are in map


//3. populate neighbours - vertices must be existing --- no need to check for that
        for (Elm edge : edges)        {
            nodeM.get(edge.getB()).addNeighbour(nodeM.get(edge.getE()));
            nodeM.get(edge.getE()).addNeighbour(nodeM.get(edge.getB()));
        }
    }


    public void buildNodeMap4startingPointOnly()
    {
//1. create nodes  and  build map of vertices
        for (Elm edge : edges)
            nodeM.putIfAbsent(edge.getB(), new Vertx(edge.getB()));

//2. validate whether FROM and TO are in map
        nop();

//3. populate neighbours - vertices must be existing --- no need to check for that
        for (Elm edge : edges)
            if(nodeM.get(edge.getE()) != null)                              //Warn:  End Vertx might not have any presence in Duos
                nodeM.get(edge.getB()).addNeighbour(nodeM.get(edge.getE()));

// TODO: 6/6/2019   czy nie chcesz tylko tych ktore nie maja swoich Duos ?? to by znaczylo, ze nie chcemy zadnych
// TODO: 6/6/2019   nodow ktore maja juz jakies polaczenia
//        for (Elm Edge : edges)
//            if(nodeM.get(Edge.getE()) == null)                              //Warn: End Vertx do not have any presence in Duos
//            {
//                nodeM.put(Edge.getE(), new Vertx(Edge.getE()));
//                nodeM.get(Edge.getB()).addNeighbour(nodeM.get(Edge.getE()));
//            }


    }



/*x  Recursive calls:               should be equivalent to BFS (cause of linked list of refs to neighbours)
 x   -----------------
 x  1. if Vertx.visited return false
 x  2. if Vertx.name == searchNm return true
 x  3. if any Vertx.name in neigbour Set is == searchNm return true
 *
 * @param p_n
 * @param p_searchNm
 * @param p_pth
 * @return
 */
    public boolean do_Recursn(Inode p_n, String p_searchNm, String p_pth)
    {
        String pth;

        if(p_n == null)
        {
            err("\n\n\nERR:  AGraph:","\nERR:  Null Vertx for searchPath",p_searchNm,"Path:",p_pth,"\nERR:  \nERR\nERR\n\n");
            nop();
        }

        if(p_n == null)         return false;
        if(p_n.isVisited())     return false;

        p_n.setVisited(true);

        if(p_n.getName().equals(p_searchNm))
            return true;
        else
        {
            for(Inode n: p_n.getNeighbours())
            {
//              if(n == null)                    continue;

                if(n.isVisited())                    continue;

// TODO: 7/18/2019
// TODO: 7/18/2019  path may be the answer - link the last 2 nodes while building graph
//
                pth = p_pth + n.getName() + "/";
                System.out.println(pth);

                if(do_Recursn(n, p_searchNm, pth))
                    return true;
            }
//--
        }
        return false;
    }

/**  Stack approach - DFS  - no recursion
 *
 * @param p_root
 * @param p_searchNm
 * @param p_pth
 * @return
 */

    public boolean do_DFS_stack(Inode p_root, String p_searchNm, String p_pth)
    {
        String pth;

        if(p_root == null)
        {
            err("\n\n\nERR:  AGraph:","\nERR:  Null Vertx for searchPath",p_searchNm,"Path:",p_pth,"\nERR:  \nERR\nERR\n\n");
            nop();
        }

        Stack<Inode> s = new Stack<Inode>();  // Create a stack

        s.push(p_root);            // Start the "to visit" at node 0

/* ===========================================
x    Loop as long as there are "active" node
 =========================================== */
        while( !s.isEmpty() )
        {
            Inode nextNode;                      // Next node to visit

            nextNode = s.pop();
//--EXIT success
            if(nextNode.getName().equals(p_searchNm))
                return true;

            if ( ! nextNode.isVisited() )
            {
                nextNode.setVisited(true);      // Mark node as visited
                p0("nextNode = " + nextNode );

//          for ( i = 0; i < NNodes; i++ )
                for (Inode i : nextNode.getNeighbours() )
                    if ( !i.isVisited())
                        s.push(i);
            }
        }
        return false;
    }


    /**  Stack approach - DFS  - no recursion
     *
     * @param p_root
     * @param p_searchNm
     * @param p_pth
     * @return
     */

    public boolean do_BFS_queue(Inode p_root, String p_searchNm, String p_pth)
    {
        String pth;

        if(p_root == null)
        {
            err("\n\n\nERR:  AGraph:","\nERR:  Null Vertx for searchPath",p_searchNm,"Path:",p_pth,"\nERR:  \nERR\nERR\n\n");
            nop();
        }

//        Stack<Inode> s = new Stack<Inode>();  // Create a stack
        Queue<Inode> q = new LinkedList<Inode>();

//        s.push(p_root);            // Start the "to visit" at node 0
        q.add(p_root);

/* ===========================================
x    Loop as long as there are "active" node
 =========================================== */
//        while( !s.isEmpty() )
        while( !q.isEmpty() )
        {
            Inode nextNode;                     // Next node to visit

//            nextNode = s.pop();
            nextNode = q.remove();              // Delete and GET
//--EXIT success
            if(nextNode.getName().equals(p_searchNm))
                return true;


            if ( ! nextNode.isVisited() )
            {
                nextNode.setVisited(true);      // Mark node as visited
                p0("nextNode = " + nextNode );

//          for ( i = 0; i < NNodes; i++ )
                for (Inode i : nextNode.getNeighbours() )
                    if ( !i.isVisited())
//                        s.push(i);
                        q.add(i);
            }
        }
        return false;
    }



    public void resetVisited(){
        for(String key: nodeM.keySet())
            nodeM.get(key).setVisited(false);
    }

    public void dsplNodeMap()
    {
//4. display
        StringBuffer sb = new StringBuffer();

        (new TreeMap<String,Inode>(nodeM)).forEach((x, y ) -> sb.append(x).append(" => ").
                append( y.getNeighbourNames() ).append(",\n"));

//        nodeM.forEach((x, y ) -> sb.append(x).append(" => {").append(y.neighbours).append("},\n"));

        System.out.printf("Check point - all nodes...." +
                "\nnodeM size = " +  nodeM.size()+
                "\nnodeM keys = " +  nodeM.keySet()+
                "\n\nnodes content  = \n" +  sb.toString()+
                "\n\n"

        );
    }
}
