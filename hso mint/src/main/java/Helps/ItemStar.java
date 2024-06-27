
package Helps;

import core.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import template.Item3;
import template.ItemTemplate3;
import template.Option;


public class ItemStar {
    public static short GetLevelItemStar(int tier2)
    {
        short level=45;
        if(tier2>0 && tier2 <10)
            level += (short)(tier2 * 10);
        return level;
    }
    public static List<Option> GetOpsItemStar(byte clazz, byte typeItem, int tier2)
    {
        //System.out.println("Helps.ItemStar.GetOpsItemStar()"+typeItem);
        short level= GetLevelItemStar(tier2);
        
        List<Option> re = new ArrayList<>();
        List<ItemTemplate3> t = new ArrayList<>();
        for(ItemTemplate3 tem : ItemTemplate3.item)
        {
            if(tem == null)continue;
            if((tem.getClazz() != clazz && tem.getClazz() != 4) || tem.getType() != typeItem || Math.abs(tem.getLevel() - level) > 9 || tem.getColor() != 4)continue;
            t.add(tem);
        }
        if(t==null || t.size()<1)return null;
        for(Option o: t.get(Util.random(t.size())).getOp()){
            if(o.id == 71)continue;
            re.add(o);
        }
//        re.addAll(t.get(Util.random(t.size())).getOp());
        return re;
    }
    
    public static List<Option> GetOpsItemStarUpgrade(byte clazz, byte typeItem,short idItem, int tier2, List<Option> LastOps)
    {
        
        //System.out.println("Helps.ItemStar.GetOpsItemStar()"+typeItem);
        short level= GetLevelItemStar(tier2);
        
        
        List<Option> re = new ArrayList<>();
        List<ItemTemplate3> t = new ArrayList<>();
        for(ItemTemplate3 tem : ItemTemplate3.item)
        {
            if(tem == null)continue;
            if((tem.getClazz() != clazz && tem.getClazz() != 4) || tem.getType() != typeItem || Math.abs(tem.getLevel() - level) > 9 || tem.getColor() != 4)continue;
            t.add(tem);
        }
        Integer[] names = {-126, -127, -111, -110, -109, -108, -107, -102, -101, -113, -87, -86, -84, -82, -80, -105, -89, -104, -103, -91};
        List<Integer> li = Arrays.asList(names);
        if (t == null || t.size() < 1) {
            return null;
        }
        for (ItemTemplate3 tem : t) {
            List<Option> kham = new ArrayList<>();
            boolean next = false;
            byte id_op_change = -1;
            byte countkham = 0;
            for (Option o : LastOps) {
                if ((li.contains((int) o.id))) {
                    kham.add(o);
                    ++countkham;
                    continue;
                }
                if((o.id >= 58 && o.id <=60) || (o.id >= 100 && o.id <=107) ){
                    kham.add(o);
                    ++countkham;
                    continue;
                }
                if(o.id == 71)continue;
                boolean next2 = false;
                for(Option o2 : tem.getOp())
                {
                    if(o.id == o2.id)
                    {
                        next2 =true;
                        break;
                    }
                }
                if(next2)
                    continue;
                if(id_op_change == -1)
                    id_op_change = o.id;
                else if(id_op_change > -1)
                {
                    
                    next =true;
                    break;
                }
            }
            if  (!next && Math.abs((LastOps.size()-countkham) - tem.getOp().size()) <2)
            {
                for(int i=0;i<tem.getOp().size();i++)
                {
                    Option oo = tem.getOp().get(i);
                    if(oo.id == 71)continue;
                    re.add(new Option(oo.id, oo.getParam(0), idItem));
                }
                //re.addAll(tem.getOp());
                if(kham != null && kham.size()>0)
                {
                    for(int i=0;i<kham.size();i++)
                    {
                        Option oo = kham.get(i);
//                        System.out.println("khảm: "+oo.id + "   "+oo.getParam(0));
                        re.add(new Option(oo.id, oo.getParam(0), idItem));
                    }
                    //re.addAll(kham);
                }
                if((id_op_change >= 0 && id_op_change <= 6) || (id_op_change >= 7 && id_op_change <= 13) || (id_op_change >= 16 && id_op_change <= 22))
                {
                    boolean clear=false;
                    for(Option o : re)
                    {
                        if  (   (id_op_change >= 0 && id_op_change <= 6 && o.id >= 0 && o.id <= 6 ) || 
                                (id_op_change >= 7 && id_op_change <= 13 && o.id >= 7 && o.id <= 13) || 
                                (id_op_change >= 16 && id_op_change <= 22 && o.id >= 16 && o.id <= 22)  )
                        {
                            o.id = id_op_change;
                            break;
                        }
                        else
                            clear =true;
                    }
                    if(clear)
                        re.clear();
                }
                if(re != null && re.size()>0)
                    break;
            }
        }
        if(re.size() <1)
            return null;
        return re;
    }
    
    public static byte ConvertType(byte typeItem)
    {
        if(typeItem >= 8 && typeItem<=11)
            return 6;     
        switch (typeItem) {
            case 2:
                return 0;
            case 0:
                return 1;
            case 1:
                return 2;
            case 6:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;    
            case 5:
                return 7;     
        }
        return -1;
    }
    
    public static short ConvertType(byte typeActions, byte clazz)
    {
        short re =-1;
        switch (typeActions) {
            case 0:
                re = 2;
                break;
            case 1:
                re = 0;
                break;
            case 2:
                re = 1;
                break;
            case 3:
                re = 6;
                break;
            case 4:
                re = 3;
                break;
            case 5:
                re = 4;
                break;
            case 6:
                if(clazz == 0)
                    re = 8;
                else if(clazz == 1)
                    re = 9;
                else if(clazz == 2)
                    re = 11;
                else if(clazz == 3)
                    re = 10;
                break;
            case 7:
                re = 5;
                break;
        }
        return re;
    }
    
    public static short GetIDItem(byte typeActions, byte clazz)
    {
        short re =-1;
        switch (typeActions) {
            case 0://nón
                re = (short)(4664+clazz);
                break;
            case 1://áo
                re = (short)(4656+clazz);
                break;
            case 2://quần
                re = (short)(4660+clazz);
                break;
            case 3://giày
                re = 4671;
                break;
            case 4://găng tay
                re = 4668;
                break;
            case 5://nhẫn
                re = 4669;
                break;
            case 6:
                if(clazz == 3)
                    re = 4674;
                else if(clazz == 2)
                    re = 4675;
                else
                    re = (short)(4672+clazz);
                break;
            case 7: //chuyền
                re = 4670;
                break;
        }
        return re;
    }
    
}
