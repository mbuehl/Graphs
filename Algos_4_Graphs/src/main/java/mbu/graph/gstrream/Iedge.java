package mbu.graph.gstrream;

/**
 * Created by ewa on 5/30/2019.
 */
public interface Iedge
{
    default String getB(){     return "beginning";    }
    default String getE(){     return "ending";      }


    default String S(char c){  return String.valueOf(c);  }
}
