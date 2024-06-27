
package BossHDL;

import client.Player;
import core.Manager;
import core.Util;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import map.LeaveItemMap;
import map.Mob_in_map;
import template.Mob;
import map.Map;


public class BossManager {
    public static final CopyOnWriteArrayList<Mob_in_map> entrys = new CopyOnWriteArrayList<>();
    
    private static byte GetIdMap(int idboss){
        switch (idboss) {
            //case = mod - Return = map
            
            case 103: return  7;
            case 104: return 15;
            case 101: return 25;
            case 84: return 37;
            case 105: return 45;
            
            case 83: return  51;
            case 106: return 62;
            case 149: return 76;
            case 155: return 79;
            case 174: return 26;
            case 173: return 113;
            case 195: return 112;
            case 196: return 115;
            case 197: return 114;
            case 186: return 109;
            case 187: return 110;
            case 188: return 111;
            default:
                throw new AssertionError();
        }
    }
    private static short[] GetSite(int idboss){
        switch (idboss) {
            //mod + vị trí 
            
            case 103: return  new short[]{432,512};
            case 104: return new short[]{ 530,213};
            case 101: return new short[]{ 204,284};
            case 84: return new short[]{ 160,224};
            case 105: return new short[]{ 816,1064};
            
            case 83: return  new short[]{ 320,1520};
            case 106: return new short[]{ 468,498};
            case 149: return new short[]{ 204,762};
            case 155: return new short[]{ 534,732};
            case 174: return new short[]{ 550,250};
            case 173: return new short[]{ 450,432};
            case 195: return new short[]{ 450,432};
            case 196: return new short[]{ 450,432};
            case 197: return new short[]{ 450,432};
            case 186: return new short[]{ 450,432};
            case 187: return new short[]{ 450,432};
            case 188: return new short[]{ 450,432};
            default:
                throw new AssertionError();
        }
    }
    public static void init(){
        int idx = 10_000;
        int[] ids = new int[]{101 , 84 , 83 ,103 ,104 ,105 , 106, 149 , 155, 174, 173, 195, 196, 197, 186, 187, 188};
        for(int id : ids){
            for(int i=0; i<5;i++){
                if(id == 174){
                    if(i == 1 || i == 4 || Manager.gI().event != 2) continue;
                }
                Mob m = Mob.entrys.get(id);
                Mob_in_map temp = new Mob_in_map();
                temp.template = m;
                temp.x = GetSite(id)[0];
                temp.y = GetSite(id)[1];
                temp.level = id == 174? 150: m.level;
                temp.Set_isBoss(true);
                temp.hp = temp.Set_hpMax(id == 174? (500_000_000 * (i+1)) : m.hpmax );
                if(id == 174)
                    temp.timeBossRecive = 1000 * 60 * 60 * 4;
                else
                    temp.timeBossRecive = 1000 * 60 * 60 * 6;
                temp.map_id = GetIdMap(id);
                temp.zone_id = (byte)i;
                temp.index = idx++;
                entrys.add(temp);
                //map[i].Boss_entrys.add(temp);
            }
        }
    }
    
    public static String GetInfoBoss(int idMob){
        String s = null;
        //long currentTimeMillis = System.currentTimeMillis();
        
        long time = System.currentTimeMillis();
        for(Mob_in_map mob: entrys){
            if(mob.template.mob_id == idMob){
                if(s == null)
                {
                    Map[] m = Map.get_map_by_id(mob.map_id);
                    if(m == null)continue;
                    s = "Map: "+m[0].name;
                }
                s+="\nkhu: "+ (mob.zone_id+1) + (mob.time_back > time ? (" Hồi sinh vào lúc: "+Helps._Time.ConvertTime(mob.time_back) +""):" Còn sống");
            }
        }
        return s;
    }
    
    public static void Update(){
        try{
            long time = System.currentTimeMillis();
            for(Mob_in_map mob : entrys){
                if((!mob.is_boss_active || mob.isdie ) && time > mob.time_back){
                    Map[] map = Map.get_map_by_id(mob.map_id);
                    if(map == null || map.length <= mob.zone_id)continue;
                    mob.isdie = false;
                    mob.is_boss_active =true;
                    mob.hp = mob.get_HpMax();
                    if(map[mob.zone_id].Boss_entrys.contains(mob))
                        map[mob.zone_id].Boss_entrys.remove(mob);
                    map[mob.zone_id].Boss_entrys.add(mob);
                    Manager.gI().chatKTGprocess(""+mob.template.name+" Đã Xuất Hiện Tại "+map[mob.zone_id].name);
                    //System.out.println("Boss "+mob.template.name+" Acctive "+map[mob.zone_id].name+ " khu "+(mob.zone_id+1));
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    
    
    public static void DropItemBossEvent(Map map, Mob_in_map mob, Player p)throws IOException{
        if(Manager.gI().event == 2){
            int[] it4 = new int[]{48,49,50,51,52,5,26,131,132,24,10};
            int[] it7 = new int[]{346,349,33,34,12,13};
            
            for(int i=0; i< 15; i++){
                LeaveItemMap.leave_vang(map,mob, -1);
            }
            for(int i=0; i<40; i++){
                int ran = Util.random(100);
                if(ran < 10)
                    LeaveItemMap.leave_item_by_type7(map, (short)Util.random(it7),p, mob.index, p.index);
                if(ran < 40)
                    LeaveItemMap.leave_item_by_type4(map, (short)Util.random(it4),p, mob.index, -1);
                else
                    LeaveItemMap.leave_item_by_type7(map, (short)Util.random(417,464),p, mob.index, -1);
            }
        }
    }
    
    
    
}
