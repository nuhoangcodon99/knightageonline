
package event_daily;

import client.Player;
import core.Service;
import io.Message;
import io.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Map;
import map.MapService;
import map.Mob_in_map;


public class LoiDai2 {
    public Player pl_1;
    public Player pl_2;
    private byte point1;
    private byte point2;
    
    public byte round;
    //public byte[] WinByRound = new byte[3];
    public Map map;
    public boolean isMapStart;
    public long timeSleep;
    private long timeOut;
    public String Name;
    public byte idxGroup;
    public String namePlayerWin="";
    
    public LoiDai2(byte idx) {
        try
        {
            idxGroup = idx;
            Name = "Map tập kết";
            pl_1 = null;
            pl_2 = null;
            isMapStart =true;
            Map map_temp = Map.get_map_by_id(100)[0];
            map = new Map(100, 0, map_temp.npc_name_data, map_temp.name, map_temp.typemap, map_temp.ismaplang,
                                map_temp.showhs, 100, map_temp.maxzone, map_temp.vgos);
            map.mobs = new Mob_in_map[0];
            map.start_map();
            map.ld2 = this;
        }
        catch (IOException e){
            System.out.println("===========error Loi dai 1==========");
            e.printStackTrace();
        }
        
    }
    public LoiDai2(Player p1,Player p2, byte idx)throws IOException{
        idxGroup = idx;
        Name = p1.name +" vs "+p2.name;
        pl_1 = p1;
        pl_2 = p2;
        
        Map map_temp = Map.get_map_by_id(102)[0];
        map = new Map(102, 0, map_temp.npc_name_data, map_temp.name, map_temp.typemap, map_temp.ismaplang,
                            map_temp.showhs, map_temp.maxplayer, map_temp.maxzone, map_temp.vgos);
        map.mobs = new Mob_in_map[0];
        map.start_map();
        map.ld2 = this;
        
        SetupNewRound();
        
    }
    
    private void SetupNewRound()throws IOException{
        if(isMapStart) return;
        if(pl_1 == null || pl_2 == null)return;
        if(round<3)
            timeSleep = System.currentTimeMillis() + 10_000L;
        pl_1.isdie = false;
        pl_1.hp = pl_1.body.get_HpMax();
        pl_1.mp = pl_1.body.get_MpMax();
        pl_1.x = 280;
        pl_1.y = 228;
        pl_1.typepk = -1;
        
        pl_2.isdie = false;
        pl_2.hp = pl_2.body.get_HpMax();
        pl_2.mp = pl_2.body.get_MpMax();
        pl_2.x = 440;
        pl_2.y = 228;
        pl_2.typepk = -1;
        
        MapService.leave_by_loidai(pl_1.map, pl_1);
        MapService.leave_by_loidai(pl_2.map, pl_2);
        pl_1.map = map;
        pl_2.map = map;
        MapService.enter(map, pl_1);
        MapService.enter(map, pl_2);
//        if(round<3){
//            Service.send_notice_nobox_white(pl_1.conn, "Trận đấu bắt đầu sau 10 Giây nữa");
//            Service.send_notice_nobox_white(pl_2.conn, "Trận đấu bắt đầu sau 10 Giây nữa");
//        }
        //System.out.println("event_daily.LoiDai2.SetupNewRound()"+round);
    }
    
    private void InitATK(){
        if(isMapStart) return;
        if(pl_1 == null || pl_2 == null)return;
        if(pl_1.typepk == 0 || pl_2.typepk == 0) return; 
        pl_1.typepk = 0;
        pl_2.typepk = 0;
        try{
            Message m = new Message(42);
            m.writer().writeShort(pl_1.index);
            m.writer().writeByte(0);
            MapService.send_msg_player_inside(map, pl_1, m, true);
            m.cleanup();

            m = new Message(42);
            m.writer().writeShort(pl_2.index);
            m.writer().writeByte(0);
            MapService.send_msg_player_inside(map, pl_2, m, true);
            m.cleanup();
            
            Service.send_notice_nobox_white(pl_1.conn, "Trận đấu đã bắt đầu");
            Service.send_notice_nobox_white(pl_1.conn, "Trận đấu đã bắt đầu");
        }catch(IOException e){}
    }
    
    public void CloseATK()throws IOException{
        if(isMapStart) return;
        if(pl_1 == null || pl_2 == null)return;
        timeOut = System.currentTimeMillis() + 10_000L;
        if(pl_1.typepk != -1 || pl_2.typepk != -1) {
            pl_1.typepk = -1;
            pl_2.typepk = -1;
            try{
                Message m = new Message(42);
                m.writer().writeShort(pl_1.index);
                m.writer().writeByte(-1);
                MapService.send_msg_player_inside(map, pl_1, m, true);
                m.cleanup();

                m = new Message(42);
                m.writer().writeShort(pl_2.index);
                m.writer().writeByte(-1);
                MapService.send_msg_player_inside(map, pl_2, m, true);
                m.cleanup();
            }catch(IOException e){}
        }
        try{
            Message m = new Message(-104);
            m.writer().writeByte(1);
            m.writer().writeByte(1);
            m.writer().writeShort((int)((timeOut - System.currentTimeMillis())/1000));
            m.writer().writeUTF("Rời map sau");
            send_msg_inside(m);
            m.cleanup();
        }catch(IOException e){}
        SetReward();
    }
    
    private void SetReward() throws IOException{
        if(isMapStart || (namePlayerWin != null && !namePlayerWin.isEmpty())) return;
        int p1=point1;
        int p2 =point2;
        if(p1 > p2){
            namePlayerWin = pl_1.name;
//            pl_1.point_active[2] += 50;
            LoiDaiManager.gI().SetPoint(idxGroup, pl_1.index, 50);
            Service.send_notice_box(pl_1.conn, "Bạn đã dành chiến thắng chung cuộc và dành được 50đ lôi đài");
            Send_Notice_Map_Insider(namePlayerWin+" đã dành chiến thắng chung cuộc và có được 50đ lôi đài");
        }
        else if(p2 > p1){
//            pl_2.point_active[2] += 50;
            namePlayerWin = pl_2.name;
            LoiDaiManager.gI().SetPoint(idxGroup, pl_2.index, 50);
            Service.send_notice_box(pl_2.conn, "Bạn đã dành chiến thắng chung cuộc và dành được 50đ lôi đài");
            Send_Notice_Map_Insider(namePlayerWin+" đã dành chiến thắng chung cuộc và có được 50đ lôi đài");
        }
        else 
        {
//            pl_1.point_active[2] += 25;
//            pl_2.point_active[2] += 25;
            LoiDaiManager.gI().SetPoint(idxGroup, pl_1.index, 25);
            LoiDaiManager.gI().SetPoint(idxGroup, pl_2.index, 25);
            Send_Notice_Map_Insider("Kết quả chung cuộc: Hòa. mỗi hiệp sĩ sẽ nhận về 25đ lôi đài");
        }
    }
    
    public void SetWinRound(Player p)throws IOException{
        if(isMapStart || timeOut > System.currentTimeMillis()) return;
        if(round<3){
            if(p.index == pl_1.index)
                point1++;
            else if(p.index == pl_2.index)
                point2++;
        }
        round++;
        SetupNewRound();
        if(round>=3 || point1 >=2 || point2>=2)
            CloseATK();
            
    }
    
    
    public void Update(){
        if(isMapStart)return;
        try{
            long time = System.currentTimeMillis();
            if(timeSleep < time && pl_1 != null && pl_2 != null)
                InitATK();
            if( timeOut!= 0 && timeOut < time)//đưa player về map chờ
                CloseMap();
            if(timeSleep < time)
                CheckBug();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void CloseMap(){
        if(isMapStart) return;
        if(pl_1 != null)
        {
            LoiDaiManager.gI().JoinMap(pl_1, idxGroup);
        }
        if(pl_2 != null)
        {
            LoiDaiManager.gI().JoinMap(pl_2, idxGroup);
        }
        for(int i = map.players.size() -1; i>=0; i--){
            Player p = map.players.get(i);
            if(p!=null){
                LoiDaiManager.gI().JoinMap(p, idxGroup);
            }
        }
        map.stop_map();
        LoiDaiManager.gI().RemoveLD(this, idxGroup);
    }
    
    public void PlayerLeaveMap(Player p){
        if(namePlayerWin != null && !namePlayerWin.isEmpty())return;
        if(pl_1!=null && pl_2!=null){
            if(p.index == pl_1.index)
            {
//                pl_2.point_active[2]+=50;
                LoiDaiManager.gI().SetPoint(idxGroup, pl_2.index, 50);
                namePlayerWin = pl_2.name;
                timeOut = System.currentTimeMillis() + 10_000L;
                pl_1 = null;
            }
            else if(p.index == pl_2.index){
//                pl_1.point_active[2]+=50;
                LoiDaiManager.gI().SetPoint(idxGroup, pl_1.index, 50);
                namePlayerWin = pl_1.name;
                timeOut = System.currentTimeMillis() + 10_000L;
                pl_2 = null;
            }
            try{
                Send_Notice_Map_Insider(namePlayerWin+" đã dành chiến thắng chung cuộc và có được 50đ lôi đài");
                Message m = new Message(-104);
                m.writer().writeByte(1);
                m.writer().writeByte(1);
                m.writer().writeShort((int)((LoiDaiManager.timeSleep - System.currentTimeMillis())/1000));
                m.writer().writeUTF("Rời map sau");
                send_msg_inside(m);
                m.cleanup();
            }catch(IOException e){}
        }
    }
    
    public void Send_Notice_Map_Insider(String msg)throws IOException{
        Message m = new Message(53);
        m.writer().writeUTF(msg);
        m.writer().writeByte(0);
        for(Player p: map.players){
            if(p!=null && p.conn != null && p.conn.connected)
                p.conn.addmsg(m);
        }
        m.cleanup();
    }
    public void send_msg_inside( Message m) {
        for (int i = 0; i < map.players.size(); i++) {
            Player p0 = map.players.get(i);
            if(p0!=null && p0.conn != null && p0.conn.connected)
                p0.conn.addmsg(m);
        }
    }
    
    private void CheckBug()throws IOException{
        if(isMapStart)return;
        if(namePlayerWin != null && !namePlayerWin.isEmpty())return;
        if(pl_1 == null && pl_2 == null)return;
        
        if(pl_1 != null && pl_2 == null){
            //p1 win
            SetWinRound(pl_1);
        }
        else if(pl_2 != null && pl_1 == null ){
            //p2 win
            SetWinRound(pl_2);
        }
        else if(pl_2.hp <= 0 || pl_2.isdie){
            //p1 win
            SetWinRound(pl_1);
        }
        else if(pl_1.hp <= 0 || pl_1.isdie){
            //p2 win
            SetWinRound(pl_2);
        }
        else if(!(pl_2.x >= 120 && pl_2.x <=600) || !(pl_2.y >= 120 && pl_2.y <=360))
            SetWinRound(pl_1);
        else if(!(pl_1.x >= 120 && pl_1.x <=600) || !(pl_1.y >= 120 && pl_1.y <=360))
            SetWinRound(pl_2);
    }
}
