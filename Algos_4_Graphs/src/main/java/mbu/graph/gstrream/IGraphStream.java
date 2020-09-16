package mbu.graph.gstrream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ewa on 6/8/2019.
 */
public interface IGraphStream
{
    boolean isBFS = true,
            isDFS = false;

    String _AutLyOuTxt      = "Auto Layout";
    String _ExplrBFSTxt     = "BFS explr";
    String _ExplrDFSTxt     = "DFS explr";


    int             //x         sizes
        szNode = 34,
        szTxt = 31
    ;

//--Grph
    Graph graph = new SingleGraph("IsConnected_w_Paths_GraphStream");     //x 2019/06/08      ln: http://graphstream-project.org
//    SpriteManager sman = new SpriteManager(graph);
//--
    default void drawGraph(List<Iedge> edges, Map<String,Inode> nodeM)
    {
        nodeM.forEach((x,y) -> graph.addNode(x));

        edges.forEach(v -> graph.addEdge(v.getB() + v.getE()
                , v.getB()
                , v.getE()));
//When Printing
//        graph.getNodeSet().forEach(x -> p(x.getId()));

//Apply styles
        graph.getNodeSet().forEach(x -> style2Node(x));
    }

    default void style2Node(Node p_nd)
    {
        p_nd.addAttribute("ui.style", "fill-color: green; size: " + szNode + "px; text-size:" + szTxt +  "px;");
        p_nd.addAttribute("ui.label", p_nd.getId());
        p_nd.removeAttribute("ui.hide");
    }

    default Viewer initialGSVals()
    {
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setStrict(false);
        graph.setAutoCreate(true);

//note: quality but slower
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");

        return graph.display();
    }

//Button controls for Auto Layout
    default void initGrphControls(Viewer p_vw, ActionListener p_actListn)
    {
        JButton jb1 = new JButton(_AutLyOuTxt);
        jb1.addActionListener(p_actListn);
        p_vw.getDefaultView().add(jb1);
    }


    default void exploreBFS()   {
        Node srcStartFrom = (Node)graph.getNodeSet().toArray()[0];
        for (Node nd :graph.getNodeSet())
            if (nd.getId().equals("0")){
                srcStartFrom = nd;
                break;
            }
        exploreBFSorDFS(srcStartFrom, true);
    }

    default void exploreDFS()   {
        Node srcStartFrom = (Node)graph.getNodeSet().toArray()[0];
        for (Node nd :graph.getNodeSet())
            if (nd.getId().equals("0")){
                srcStartFrom = nd;
                break;
            }
        exploreBFSorDFS(srcStartFrom, false);
    }

    default void exploreBFSorDFS(Node source, boolean isBFSalgo)
    {
        Iterator<? extends Node> k = isBFSalgo ?
                source.getBreadthFirstIterator():
                source.getDepthFirstIterator() ;

        new Thread(new Runnable()   {           //Warn:     release the button 'Explore...'
            @Override
            public void run()            {
                Node prevNode = null;              //For:  edge color change logic

                while (k.hasNext())               {
                    Node nextNode = k.next();
                    nextNode.setAttribute("ui.class",
                            nextNode.getAttribute("ui.class")!=null && nextNode.getAttribute("ui.class").equals("marked")?"marked2":"marked");
//x edge color change logic
                    if(prevNode != null)   {
                        if(nextNode.hasEdgeFrom(prevNode))
                            nextNode.getEdgeBetween(prevNode).setAttribute("ui.class",
                                    nextNode.getEdgeBetween(prevNode).getAttribute("ui.class")!= null &&
                                    nextNode.getEdgeBetween(prevNode).getAttribute("ui.class").equals("marked")?"marked2":"marked");
                    }
                    sleep();
                    prevNode = nextNode;
                }
            }
        }).start();

    }
    default void sleep() { try { Thread.sleep(800); } catch (Exception e) {}   }

//Note:     -----------------------------------------------------------------------------------------------------------
//Note:     to get Vertx.marked raised - need to call   someNode.setAttribute("ui.class", "marked");                  -
//Note:
    String styleSheet =
           "node {	fill-color: blue;                           }" +
           "edge {  fill-color:green;                           }" +

           "node.marked {	fill-color: red; text-color: black;   }" +
           "edge.marked {   fill-color:red;                     }"+

           "node.marked2 {	fill-color: black; text-color: black;   }" +
           "edge.marked2 {  fill-color:blue;                    }"
            ;

    default String S(char c)        {   return String.valueOf(c);  }
}
