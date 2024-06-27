
package event_daily;

import client.Player;
import core.MenuController;
import core.SQL;
import core.Service;
import core.Util;
import io.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import map.MapService;
import template.Item47;


public class Group_ld {
    //public List<Integer> player_entrys = new ArrayList<>();
    private short minLevel;
    public CopyOnWriteArrayList<LoiDai2> ld_entrys = new CopyOnWriteArrayList<>();
    public final ConcurrentHashMap<Integer, Integer> player_entrys = new ConcurrentHashMap<>();
    public int idTop1 = -1;
    public String NameTop1="";
    
    public byte idxGroup;
    public Group_ld(short setlevel,byte idx){
        idxGroup = idx;
        ld_entrys = new CopyOnWriteArrayList<>();
        ld_entrys.add(new LoiDai2(idxGroup));
        minLevel = setlevel;
    }
    public boolean Containplayer(int id){
        return player_entrys.containsKey(id);
    }
    public void addPlayer(Player p){
        player_entrys.put(p.index, 0);
    }
    public void SetPoint(int idPlayer, int value){
        if(player_entrys.containsKey(idPlayer))
            player_entrys.replace(idPlayer, player_entrys.get(idPlayer)+value);
//            player_entrys.compute(idPlayer, (k, v) -> v == null ? value :  (v + value));
    }
    
    public int GetPoint(int idPlayer){
        return player_entrys.getOrDefault(idPlayer, -1);
    }
    
    public void StartTurn()throws IOException{
        if(player_entrys == null || player_entrys.size() <1)return;
        List<Player> l = new ArrayList<>();
        for(Group_ld g : LoiDaiManager.Group_entrys)
        {
            for(LoiDai2 ld : g.ld_entrys)
            {
                for(Player p : ld.map.players)
                {
                    if(p == null || !Containplayer(p.index) || p.conn == null || !p.conn.connected)continue;
                    if(this.minLevel < 135 && p.level >= this.minLevel && p.level <= this.minLevel +9)
                        l.add(p);
                    else if(this.minLevel >= 135 && p.level >= this.minLevel)
                        l.add(p);
                }
            }
        }
        Collections.shuffle(l);
        for(int i=0;i<l.size();i+=2){
            if(i+1 >= l.size())break;
            Player p1 = l.get(i);
            Player p2 = l.get(i+1);
            if(p1 !=null && p2!=null){
                ld_entrys.add(new LoiDai2(p1,p2,idxGroup));
            }
            else if(p1 == null && p2 != null){
                // cộng thưởng cho p2
                p2.point_active[2] += 50;
                LoiDaiManager.gI().SetPoint(idxGroup, p2.index, 50);
                Service.send_notice_box(p2.conn, "Bạn đã giành chiến thắng vì đối thủ vắng mặt");
            }
            else if(p1 != null && p2 == null){
                // cộng thưởng cho p1
                p1.point_active[2] += 50;
                LoiDaiManager.gI().SetPoint(idxGroup, p1.index, 50);
                Service.send_notice_box(p1.conn, "Bạn đã giành chiến thắng vì đối thủ vắng mặt");
            }
        }
        if(l.size() % 2 ==1){
            //cộng thưởng cho thằng lẻ
            Player p0 = l.get(l.size()-1);
            if(p0 != null)
            {
                p0.point_active[2] += 50;
                LoiDaiManager.gI().SetPoint(idxGroup, p0.index, 50);
                Service.send_notice_box(p0.conn, "Bạn đã giành chiến thắng vì không có đối thủ");
            }
        }
        l.clear();
    }
    
    public void JoinMap(Player p){
        for(LoiDai2 l : ld_entrys){
            if(l.isMapStart){
                p.typepk = -1;
                if(p.isdie)
                {
                    p.hp =1;
                    p.isdie=false;
                }
                MapService.leave(p.map, p);
                p.map = l.map;
                p.x = 180;
                p.y = 230;
                MapService.enter(l.map, p);
                return;
            }
        }
    }
    public void JoinMapPVP(Player p,int idx){
        if(idx<0 || idx >=ld_entrys.size())return;
        p.typepk = -1;
        if(p.isdie)
        {
            p.hp = 1;
            p.isdie=false;
        }
        MapService.leave(p.map, p);
        MapService.enter(ld_entrys.get(idx).map, p);
    }
    public void Remove(LoiDai2 l){
        if(!l.isMapStart)
            ld_entrys.remove(l);
    }
    public void GetMenuViewLD(Session conn)throws IOException{
        String[] menu = new String[ld_entrys.size()+4];
        menu[0] = "Về làng";
        menu[1] = "Thông tin";
        menu[2] = "Thông tin nhóm đấu";
        menu[3] = "Nhận quà top 1";
        for(int i=0; i<ld_entrys.size();i++){
            menu[i+4] = ld_entrys.get(i).Name;
        }
        MenuController.send_menu_select(conn, -82, menu);
    }
    
    public void InfoGroup(Session conn)throws IOException{
        String s = "Bạn đang ở nhóm "+(65 + 10 * idxGroup)+" - "+(idxGroup == 7? "trở lên":(65 + 10 * idxGroup + 9));
        s += "\nNhóm đấu đang có "+player_entrys.size()+" đăng kí.";
        if(NameTop1 != null && !NameTop1.isEmpty())
            s+="\nTop 1 là: "+NameTop1+" với số điểm: "+player_entrys.getOrDefault(idTop1, 0);
        else 
            s+="\nChưa có thông tin của top 1.";
        Service.send_notice_box(conn, s);
    }
    
    public void SetTop(){
        try
        {
            int id = -1;
            int val = -1;
            for(java.util.Map.Entry<Integer, Integer> entry : player_entrys.entrySet())
            {
                if(id == -1 || val < entry.getValue()){
                    id = entry.getKey();
                    val = entry.getValue();
                }
            }
            if(id > -1){
                try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT `id`,`name` FROM `player` WHERE `id` = '" + id + "' ;")) {

                    if (!rs.next()) {
                        System.out.println("=======không tìm thấy player có id = "+id);
                        return;
                    }
                    String name = rs.getString("name");
                    this.idTop1 = id;
                    this.NameTop1 = name;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void GetRewarded(Session conn)throws IOException{
        if(conn.p.index != idTop1){
            Service.send_notice_box(conn, "Bạn không phải là người dành top 1");
            return;
        }
        LocalTime now = LocalTime.now();
        LocalTime startTime = LocalTime.of(20, 0); // 20:00
        LocalTime endTime = LocalTime.of(23, 59, 59); // 23:59:59
//        LocalTime startTime = LocalTime.of(0, 0); // 20:00
//        LocalTime endTime = LocalTime.of(1, 0, 59); // 23:59:59
        if (now.isAfter(startTime) && now.isBefore(endTime)){
            List<Short> IDs = new ArrayList<>();
            List<Integer> Quants = new ArrayList<>();
            List<Short> Types = new ArrayList<>();
            try{
                if(idxGroup < 4){
                    if(conn.p.item.get_bag_able()<8){
                        Service.send_notice_box(conn, "Bạn cần tối thiểu 8 ô hành trang");
                        return;
                    }
                    Item47 it = new Item47();
                    it.id = 14;
                    it.quantity = 10;
                    IDs.add(it.id);
                    Quants.add((int)it.quantity);
                    Types.add((short)7);
                    conn.p.item.add_item_bag47(7, it);

                    it.id = 351;
                    it.quantity = 2;
                    IDs.add(it.id);
                    Quants.add((int)it.quantity);
                    Types.add((short)7);
                    conn.p.item.add_item_bag47(7, it);

                    it.id = 471;
                    it.quantity = 2;
                    IDs.add(it.id);
                    Quants.add((int)it.quantity);
                    Types.add((short)7);
                    conn.p.item.add_item_bag47(7, it);

                    for(int i=0; i<5;i++){
                        it.id = (short)Util.random(335,346);
                        it.quantity =1;
                        IDs.add(it.id);
                        Quants.add((int)it.quantity);
                        Types.add((short)7);
                        conn.p.item.add_item_bag47(7, it);
                    }
                    IDs.add((short)-1);
                    Quants.add((int)(30_000_000));
                    Types.add((short)4);
                    conn.p.update_vang(30_000_000);
                }else{
                    if(conn.p.item.get_bag_able()<13){
                        Service.send_notice_box(conn, "Bạn cần tối thiểu 13 ô hành trang");
                        return;
                    }
                    Item47 it = new Item47();
                    it.id = 14;
                    it.quantity = 20;
                    IDs.add(it.id);
                    Quants.add((int)it.quantity);
                    Types.add((short)7);
                    conn.p.item.add_item_bag47(7, it);

                    it.id = 351;
                    it.quantity = 3;
                    IDs.add(it.id);
                    Quants.add((int)it.quantity);
                    Types.add((short)7);
                    conn.p.item.add_item_bag47(7, it);

                    it.id = 471;
                    it.quantity = 3;
                    IDs.add(it.id);
                    Quants.add((int)it.quantity);
                    Types.add((short)7);
                    conn.p.item.add_item_bag47(7, it);

                    for(int i=0; i<10;i++){
                        it.id = (short)Util.random(335,346);
                        it.quantity =1;
                        IDs.add(it.id);
                        Quants.add((int)it.quantity);
                        Types.add((short)7);
                        conn.p.item.add_item_bag47(7, it);
                    }
                    IDs.add((short)-1);
                    Quants.add((int)(50_000_000));
                    Types.add((short)4);
                    conn.p.update_vang(50_000_000);
                }
                short[] ar_id = new short[IDs.size()];
                int[] ar_quant = new int[Quants.size()];
                short[] ar_type = new short[Types.size()];
                for(int i=0;i<ar_id.length;i++)
                {
                    ar_id[i] = IDs.get(i);
                    ar_quant[i] = Quants.get(i);
                    ar_type[i] = Types.get(i);
                }
                Service.Show_open_box_notice_item(conn.p,"Bạn nhận được",ar_id,ar_quant,ar_type);
            }catch(Exception e){}
            
            synchronized (player_entrys) {
                player_entrys.clear();
            }
            idTop1 = -1;
            NameTop1 ="";
        }else
            Service.send_notice_box(conn, "Chỉ có thể nhận thưởng từ 20h đến 23h59");
    }
}
