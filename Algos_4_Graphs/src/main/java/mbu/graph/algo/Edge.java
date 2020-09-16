package mbu.graph.algo;


import mbu.graph.gstrream.Iedge;

/**
 * Created by ewa on 6/2/2019.
 */
public class Edge implements Iedge
{
    String B,E;

    public Edge(String b, String e)
    {
        B = b;
        E = e;
    }

    @Override
    public String getB()
    {
        return B;
    }

    @Override
    public String getE()
    {
        return E;
    }
}
