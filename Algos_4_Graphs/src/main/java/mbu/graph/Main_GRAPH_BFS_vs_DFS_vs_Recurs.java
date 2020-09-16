package mbu.graph;

import mbu.graph.gstrream.IGraphStream;
import mbu.utl.IDefaults;

import org.graphstream.ui.view.Viewer;
import mbu.graph.gstrream.*;
import mbu.graph.algo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/*edges - balanced tree
                                                  0

                    1                              2                         3

            11      14         17             21     24          31        32          34

        111 112   114 115  117 118 119    121 122   124 125     131     132 133    134 135 136
      ..........................................................................................
*/


/** 2020/08/15
 x                                                                                                                      *
 Title:     This is to traverse graph using queued or stacked node elements or applying recursive approach
*/

public class Main_GRAPH_BFS_vs_DFS_vs_Recurs implements IDefaults, IGraphStream
{
    int[][] edges_BTree = { {0,1},{0,2},{0,3},{1,11},{1,14},{1,17},{2,21},{2,24},{3,31},{3,32},{3,34},
                            {11,111},{11,112},{14,114},{14,115},{17,117},{17,118},{17,119},{21,121},{21,122},{24,124},{24,125},{31,131},{32,132},{32,133},{34,134} ,{34,135},{34,136}
                    ,   {124, 1124},{124,1125},{124,1126},{124,1127}
                    ,   {1125,3}

    };

    int[][] edges_article = { {0,1},{1,7},{7,2},{2,3},{3,0},{3,4},{4,8},{0,8},{2,5},{5,6} };


//x  which edges definition to use?

    int[][] edges = edges_BTree;            // or   = edges_article;

//x  Check for the following paths /nFROM - node from;  nTO - node to /...
    int[] nFROM = { 0 };
    int[] nTO = { 99 };



//x--Swing related - graphstream ---------------------------------------------------------------------------------------

    Viewer gView = null;
    boolean autoLayout = true;

    public Main_GRAPH_BFS_vs_DFS_vs_Recurs()   {   }

    public static void main(String[] s)
    {
        Main_GRAPH_BFS_vs_DFS_vs_Recurs z = null;

        try {
            new Main_GRAPH_BFS_vs_DFS_vs_Recurs().doTheWork();
        } catch (Exception e) {
            e.printStackTrace();
        }

//x-- press key to E X I T
        if(z != null)
            z.PressCR2Exit();
    }
/**
 */
     void doTheWork() throws Exception {

        long st = System.currentTimeMillis(), stA = st;

//======================================================================================================================
//x-- G R A P H                                                                                    III

         AGrph<Iedge>  aGph = new AGrph<Iedge>();         //x create edges and nodes

         final _Zyrafa  zyrafa = new _Zyrafa();
         SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 zyrafa.makeIt();
             }
         });

//x     1.build edges
         for(int[] edge: edges)
             aGph.edges.add(new Edge(""+edge[0], ""+edge[1]));

//x     2.build verticies {nodes} map
         aGph.buildNodeMap();

//x     3.draw graph
         SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 zyrafa.drawGraph(aGph.edges,aGph.nodeM);
             }
         });

//x--4. run paths
//         int[] FROM = { ... };
//         int[] TO   = { ... };

         String from    = "";
         String to      = "";

         p0("Recursive search...\n\n");
//x--Recursive ... using Sets of node references under each node (definition)
         for(int _from : nFROM)
         {
             from = ""+_from;
             for (int _to : nTO)
             {
                 to = "" + _to;
                 p0("\nPath search for node: FROM: " + from + " - TO: " + to + "  Result is: " +
                         aGph.do_Recursn(aGph.nodeM.get(from), to, "//" + from + "/") + "\n\n\n");

                 aGph.resetVisited();
             }
         }

         p0(elapsed( st,4,"Recursion  GRAPH  Elapsed"));     st = System.currentTimeMillis();
         p(elapsed(stA, "E l a p s e d"));

 //x--DFS ... depth first
         p0("DFS search...\n");
         from    = "";
         to      = "";


         for(int _from : nFROM)
         {
             from = ""+_from;
             for (int _to : nTO)
             {
                 to = "" + _to;
                 p0("\nPath search for node: FROM: " + from + " - TO: " + to + "  Result is: " +
                         aGph.do_DFS_stack(aGph.nodeM.get(from), to, "//" + from + "/") + "\n\n\n");

                 aGph.resetVisited();
             }
         }

         p0(elapsed( st,4,"DFS stack  GRAPH  Elapsed"));     st = System.currentTimeMillis();
         p(elapsed(stA, "E l a p s e d"));

//x--BFS ... breath first
         p0("BFS search...\n");
         from    = "";
         to      = "";

         for(int _from : nFROM)
         {
             from = ""+_from;
             for (int _to : nTO)
             {
                 to = "" + _to;
                 p0("\nPath search for node: FROM: " + from + " - TO: " + to + "  Result is: " +
                         aGph.do_BFS_queue(aGph.nodeM.get(from), to, "//" + from + "/") + "\n\n\n");

                 aGph.resetVisited();
             }
         }

//x--

        p0(elapsed( st,4,"BFS queue  GRAPH  Elapsed"));     st = System.currentTimeMillis();
        p(elapsed(stA, "E l a p s e d"));

    }//x--end-of-main



//    /**x     All the connections that come from querying Duos for X number of draws
// *
// * @param ag
// * @param tupl
// * @param freq
// */
//    void buildStraightEdgeList(AGrph<Iedge>  ag, Tuple tupl, int freq)
//    {
//        if(freq <= 1)
//            ag.edges.add(new Edge(tupl.a+"",tupl.b+""));
//    }


/**note ------   V i s u a l i z a t i o n  ----------------------------------------------------------------------------
   note                                                                                                                -
   note      S W I N G     UI    RELATED                                                                               -
   note                                                                                                                -
   note      can create multiple instances  - each display() call opens new UI                                         -
   note ----------------------------------------------------------------------------------------------------------------
 */

    public  class _Zyrafa implements IGraphStream, ActionListener, FocusListener
    {
        Viewer gView;
        boolean toggleAutoLayout = false;
        String[] butNms = { _AutLyOuTxt, _ExplrBFSTxt, _ExplrDFSTxt};
        JButton[] jbtn = new JButton[butNms.length];

        public _Zyrafa()    {   }
        public void makeIt()
        {
//Define frame
            JFrame f = new JFrame("Algos for Graphs 2020/08/17");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new BorderLayout());
//x--Calculate graph
            gView = initialGSVals();
//x--Add toggle button for AUTO layout
            JPanel pane = gView.getDefaultView();
            pane.setLayout(null);                                   //x     USE: absolute layout
            makebtn(butNms[0],pane,1 , 1, 45);
//
//ADD   a seperate button panel
            f.getContentPane().add(addCtrlBtns(),BorderLayout.WEST);

//ADD   graph view
            Component grphC = gView.getDefaultView().getTopLevelAncestor().getComponent(0);

            grphC.setPreferredSize(new Dimension(1290, 950));
            f.getContentPane().add(grphC,BorderLayout.CENTER);
//
            f.pack();
            f.revalidate();
            f.repaint();
            f.setVisible(true);
            toggleAutoLayout(_AutLyOuTxt);
        }

        public JPanel addCtrlBtns()
        {
            int i=0;
            JPanel bPan = new JPanel(); //absolute
            bPan.setLayout(null);

            for(String bNm: butNms)
                if(i++ == 0)
                    ;//makebtn(bNm,bPan,10 , 0, 0);

                else
                    makebtn(bNm,bPan, 1, 0+(i-1)*43);

            bPan.setPreferredSize(new Dimension(157, 800));
            return  bPan;
        }

        void makebtn(String nm, final Container cntnrPane, int ... coord) //x1,y1, dx,dy
        {
            int x1=coord.length>0?coord[0]:0,         y1=coord.length>1?coord[1]:0,
                dx=coord.length>2?coord[2]:0,         dy=coord.length>3?coord[3]:0;

            JButton btn = new JButton(nm);
            btn.setFont(new Font("Calabri", Font.PLAIN,22));
            btn.setName("source-is-"+nm);

            btn.addFocusListener(this);

//Note      COOL feature:   internal content is not painted but button focus changes
//Note                      with cursor move ... when set to FALSE
            btn.setContentAreaFilled(false);
//
            Insets insets = cntnrPane.getInsets();
            Dimension size = btn.getPreferredSize();
            btn.setBounds(x1 + insets.left, y1 + insets.top,  size.width+dx, size.height+dy);
            btn.addActionListener(this);

            cntnrPane.add(btn);
        }



        public boolean toggleAutoLayout(String p_butName)   {
            if(p_butName.startsWith(_AutLyOuTxt)) {
                if (toggleAutoLayout) gView.disableAutoLayout();
                else gView.enableAutoLayout();
                toggleAutoLayout = !toggleAutoLayout;
            }
            return toggleAutoLayout;
        }

        @Override
        public void actionPerformed(ActionEvent e)    {
            if (((JButton) e.getSource()).getText().startsWith(_AutLyOuTxt))
                ((JButton) e.getSource()).setText(_AutLyOuTxt + (toggleAutoLayout(((JButton) e.getSource()).getText()) ? " ON" : " OFF"));
            if (((JButton) e.getSource()).getText().startsWith(_ExplrBFSTxt))
                exploreBFS();
            if (((JButton) e.getSource()).getText().startsWith(_ExplrDFSTxt))
                exploreDFS();

        }


        public Viewer getgView() { return gView; }

        @Override
        public void focusGained(FocusEvent e)
        {
            p("focus");
            ((JButton)e.getSource()).setContentAreaFilled(true);
        }

        @Override
        public void focusLost(FocusEvent e)
        {
            p("oof");
            ((JButton)e.getSource()).setContentAreaFilled(false);
        }
    }//end-of_Zyrafa class

}//end-of class

