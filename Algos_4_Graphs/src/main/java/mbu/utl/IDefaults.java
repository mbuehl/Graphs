package mbu.utl;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;


/**
 * Created by ewa on 5/30/2019.
 */
public interface IDefaults
{
    int _10     = 10 ,          _100     = 100 ,
        _1k     = 1000 ,        _10k    = _1k* 10,          _100k   = _1k * 100,
        _1mm    = _1k * _1k,    _10mm   = _10k * _1k,       _14mm   = _1mm * 14;
//
//x                                             ticket sizes per game type
    int p6_sz = 6,
        p5_sz = 5,
        mm_sz = 5;
//x
    int t_1sec = 1000,
        t_1min  = 60 * t_1sec,
        t_1hr   = 60 * t_1min;
//
    int precis = 7;      //Warn: this number reflects non-percentage precision (x -> meaning i.e. 7 gives 5 decm. for %)


    String   tkns[] = { "\"",  "$", "times" },
            vals[] = { "",    "",   "" }  ;

    String   pruneTkns[] = { "[",  "]", " " },
            pruneVals[] = { "",   "",  "" };

    String   toArrTkns[] = { "[",  "]" },
            toArrVals[] = { "{",    "} ," }  ;

    String  arrToColTkns[] = { "{",  ", ", "}" },
            arrToColVals[] = { "",  "\n" , "" }  ;


    String  arrToColTkns2[] = { "[",  ", ", "]" };


//yy--I n t e g e r     numery - pomocnik

/**x  1. znajdz wszystkie numery w stringu i zwroc je jako jeden INT
 * x  2. znajdz pierwszy numer w stringu i zwroc go jako jeden INT
 * x  3. znajdz wszystkie numery w stringu i zwroc je jako INT []
 *
  * @param p_in
 * @return
 */
    default int extrAsSngInt(String p_in)    {
        StringBuilder sb = new StringBuilder();
        for(Character c: p_in.toCharArray())    if(Character.isDigit(c))  sb.append(c);
        return Integer.parseInt(sb.toString());
    }

    default int extrFirstInt(String p_in)    {
        boolean digitIsFound = false;
        StringBuilder sb = new StringBuilder();

        for(Character c: p_in.toCharArray())
        {
            if (Character.isDigit(c))  {
                digitIsFound = true;
                sb.append(c);
            }
            else if (digitIsFound && !Character.isDigit(c))
                break;
        }

        return Integer.parseInt(sb.toString());
    }

    default int[] extrAllIntIntoArr(String p_in)    {
        boolean digitIsFound = false;
        StringBuilder sb = new StringBuilder();
        List<Integer> lst = new ArrayList<>();

        for(Character c: p_in.toCharArray())   {
            if (Character.isDigit(c) )  {
                digitIsFound = true;
                sb.append(c);
            }
            else if (digitIsFound && !Character.isDigit(c))    {
                digitIsFound = false;
                lst.add(Integer.parseInt(sb.toString()));
                sb.delete(0,sb.length());
            }
        }
//on exit
        if(sb.length() > 0)            lst.add(Integer.parseInt(sb.toString()));
//
        int[] retI = new int[lst.size()];
        for(int i=0;i<retI.length;i++)      retI[i] = lst.get(i);

        return retI;
    }

//yy--D o u b l e    numery - pomocnik

    default double clcPrct(BigDecimal p_big, BigDecimal p_small)    {
        return p_small.divide(p_big, precis,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


//------------------------------------------------------------------------------------------

    static int calcTop(int[][] tmplt)
    {
        int max = 0;
        for(int[] line: tmplt)
            for (int no: line)
                if(max < no)
                    max = no;
        return max;
    }

//x--C u s t o m   default implementations

    default String retBseNm()  {
        for(Class ifc: getClass().getInterfaces())
            if(ifc.getSimpleName().contains("IWh"))
                return ifc.getSimpleName();
        return null;
    }



//x----------------------------------------------------------------------------------------------------------------------
//x                                                                                                                     -
//yy        D e f a u l t    i m p l e m e n t a t i o n s                                                              -
//x                                                                                                                     -
    default void PressCR2Exit(int ... p_expireSec)
    {
        if(p_expireSec.length > 0)
        {
            final int expireExit = p_expireSec[0];
            new Thread(() ->
            {
                p("E X I T I N G  on  expire " + expireExit + " sec term...\n");
                try { Thread.sleep(1000*expireExit);  } catch (InterruptedException e)  { }
                System.exit(-0);
            }).start();
        }

        p("\n\nPress enter to EXIT...\n\n");
        try { System.in.read(); } catch (IOException ignore)  {  }
        p("E X I T I N G...\n");
        System.exit(0);
    }


/** pokaz pare dni wstecz - jakie rzadki byly ostatnio
 *
 * @param p_entirePastL
 * @param p_iloscWstecz
 */
    default void pokazPareLosowanWstecz(List<List<Integer>> p_entirePastL, int p_zacznijOd, int p_iloscWstecz)
    {
        for(int dniWstecz = p_zacznijOd; dniWstecz < p_zacznijOd+p_iloscWstecz && dniWstecz < p_entirePastL.size();dniWstecz++ )
        {
            ap("\n -" + dniWstecz + "  =>  " );
            for(int i= 0; i< p_entirePastL.get(dniWstecz).toString().split(",").length;i++ )
            {
                if (i >= 9 ) continue;
                if (i == 3 ) ap("      ");
                ap(p_entirePastL.get(dniWstecz).toString().split(",")[i]);
            }
        }
    }



//x--do nothing
    default void nop(){}

//x--P R I N Ts
    default void p(String ... p_ss) {     p(System.out, p_ss);    }
    default void p0(String ... p_ss) {     p(System.out, "\n"); p(System.out, p_ss);    }
    default void err(String ... p_ss) {   p(System.err, p_ss);    }
    default void p(PrintStream p_ps, String ... p_ss)  {
        for(String s: p_ss)
            p_ps.print(s + " ");
    }

    //x--same as above but with with logging
    default void ap(String ... p_ss) {     p(System.out, p_ss);    }

//x--   Padding (right side) to len
    default String pad(String p_in, int p_len)   {
        return String.format("%-" +p_len+"."+p_len+"s",p_in);
    }

//
    default String showUntil(int p_howMany,String p_in){
        if(p_in == null)
            return "";
        if(p_in.length() > p_howMany)
            return p_in.substring(0, p_howMany)+"...";
        else
            return p_in;
    }

//
//x--P R I N Ts - specialized

    default void p(final PrintWriter p_pw, String ... p_ss)  {
        for(String s: p_ss)
            p_pw.print(s+" ");
    }

//x--RANDOM NUMBER GENERATOR
    default int getRandomNumberInRange(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

/**x     Simplified generator                                                               2020/01/22
 *
 */
    Random rnd = new Random();
    default int getRandomNumberInRangeSimplified(int min, int max) {
        return rnd.nextInt((max - min) + 1) + min;
    }



//x--STOPER STRINGs
    default String elapsed(long start,  String ... msgs){ return elapsed(start, 2, msgs); }
    default String elapsed3(long start,  String ... msgs){ return elapsed(start, 3, msgs); }
    default String elapsed(long start, int precision, String ... msgs)
    {
        String precisStr = "%6." + precision + "f";

        if(msgs.length == 1 && "".equals(msgs[0]))
            return "\n\n>> "  + String.format(precisStr,(double)(System.currentTimeMillis() - start)/1000L) + " sec\n";
        else if(msgs.length == 1 && !"".equals(msgs[0]))
            return msgs[0] +": " + String.format(precisStr,(double)(System.currentTimeMillis() - start)/1000L) + " sec\n";
        else if(msgs.length == 2 )
            return msgs[0] +": " + String.format(precisStr,(double)(System.currentTimeMillis() - start)/1000L) + (msgs.length > 1?msgs[1]:" sec\n");
        else
            return String.format(precisStr,(double)(System.currentTimeMillis() - start)/1000L);
    }

//--FORMATTERs  FOR  DOUBLE
    default String fmt2Str(double p_dbl){  return fmt2Str(p_dbl,3,3);    }
    default String f0(double p_dbl){  return fmt2Str(p_dbl,0,0);    }
    default String fmt2Str(double p_dbl, int p_bse, int p_fract){
        if(p_bse < 1) p_bse = 9;
        return String.format(" %,"+p_bse + "." + p_fract + "f ", p_dbl);    }

    default double fmt2dbl(double p_dbl, int p_bse, int p_fract){
        if(p_bse < 1) p_bse = 9;
        return new Double(String.format(" %"+p_bse + "." + p_fract + "f", p_dbl));    }

//--

    default double cPrctge(int p_big,  int p_small, int p_scale)
    {
        BigDecimal allPopulation = new BigDecimal(p_big);
        BigDecimal smallPopulation = new BigDecimal(p_small);
        double res = smallPopulation.divide(allPopulation,p_scale,BigDecimal.ROUND_HALF_UP).doubleValue();
        return res;
    }

    default Map<Integer, Integer> addToIntMap(int p_2add, final Map<Integer, Integer> p_mp){
        if(p_mp.get(p_2add) != null)
            p_mp.put(p_2add,1+p_mp.get(p_2add));
        else
            p_mp.put(p_2add,1);

        return p_mp;
    }


 /**Note:    Assume: all elements are unque
 * x         Small collection has to have all its elems in BIG
 *
 * @param big
 * @param sm
 * @return  bool
 */
     default boolean collcIncollcOfUniq(Collection<Integer> big, Collection<Integer> sm)
    {
        for(int smElm: sm)
            if(!big.contains(smElm))
                return false;

        return true;
    }

    default void dly(long dly){  try { Thread.sleep(dly); } catch (InterruptedException e) { e.printStackTrace(); } }


}
