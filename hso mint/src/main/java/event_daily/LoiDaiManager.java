/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package event_daily;

import client.Player;
import core.Log;
import core.Manager;
import core.SQL;
import core.Service;
import core.Util;
import io.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import map.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;


public class LoiDaiManager {
    private static LoiDaiManager instance;
    public static Group_ld[] Group_entrys = new Group_ld[8];
    public static boolean isRegister;
    public static long timeRegister;
    private static byte turn;
    public static long timeTurn;
    public static long timeSleep;
    public static boolean isSleep;
    public static boolean isATK;
    
    public static LoiDaiManager gI(){
        if (instance == null) {
            instance = new LoiDaiManager();
            Group_entrys[0] = new Group_ld((short)35,(byte)0);
            Group_entrys[1] = new Group_ld((short)75,(byte)1);
            Group_entrys[2] = new Group_ld((short)85,(byte)2);
            Group_entrys[3] = new Group_ld((short)95,(byte)3);
            Group_entrys[4] = new Group_ld((short)105,(byte)4);
            Group_entrys[5] = new Group_ld((short)115,(byte)5);
            Group_entrys[6] = new Group_ld((short)125,(byte)6);
            Group_entrys[7] = new Group_ld((short)135,(byte)7);
            LoadData();
            turn =1;
        }
        return instance;
    }
    
    public Group_ld GetGroup(int idx){
        if(idx <0 || idx >= Group_entrys.length)
            return null;
        return Group_entrys[idx];
    }
    
    public void Info(Session conn)throws IOException{
        long time = System.currentTimeMillis();
        String s = "";
        if(isRegister)
            s+="Thời gian đăng kí lôi sẽ kết thúc sau: "+(int)((timeRegister - time) / 1000 / 60)+"p"+(int)((timeRegister - time) / 1000 % 60)+"s,";
        else if(timeTurn > time)
            s+="\nLoạt đấu "+turn+" sẽ kết thúc sau: "+(int)((timeTurn - time) / 1000 / 60)+"p"+(int)((timeTurn - time) / 1000 % 60)+"s,";
        else if(timeSleep > time)
            s+="\nCòn "+(int)((timeSleep - time) / 1000 / 60)+"p"+(int)((timeSleep - time) / 1000 % 60)+"s nữa trước khi loạt đấu "+turn+" bắt đầu,";
        else if(!isRegister && !isATK)
            s+= "\nKhông trong thời gian diễn ra lôi đài.";
        String DiemLD = "\nBạn chưa tham gia lôi đài.";
        if(Group_entrys[GetIdxGroup(conn.p.level)].Containplayer(conn.p.index))
            DiemLD ="\nĐiểm lôi đài của bạn: "+Group_entrys[GetIdxGroup(conn.p.level)].GetPoint(conn.p.index);
        s+=DiemLD;
        Service.send_notice_box(conn, s);
    }
    
    public int GetDiemLD(int idp){
        for(Group_ld g : Group_entrys){
            try{
                return g.player_entrys.getOrDefault(idp, 0);
            }catch(Exception e){
            }
        }
        return 0;
    }
    
    public void SetPoint(int idx, int idPl, int value){
        if(idx <0 || idx >= Group_entrys.length)return;
        Group_entrys[idx].SetPoint(idPl, value);
    }
    
    public void startRegister(){
        isRegister =true;
        turn = 1;
        timeRegister = System.currentTimeMillis()+ 1000 * 60 * 28;
        System.out.println("StartRegister Loi Dai");
        synchronized(Group_entrys){
            for(Group_ld g : Group_entrys){
                try{
                    synchronized (g.player_entrys) {
                        g.player_entrys.clear();
                    }
                }catch(Exception e){
                    Log.gI().add_Log_Server("loidai", "Clear pointld: "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        try{
            Manager.gI().chatKTGprocess("Đã đến giờ đăng kí lôi đài, các đại hiệp nhanh chân đến kí tên nào!");
        }catch(IOException e){}
    }
    
    public void JoinGroup(Player p) throws IOException{
        if(p == null)return;
        int idx_gr = GetIdxGroup(p.level);
        if(p.get_ngoc()<5){
            Service.send_notice_box(p.conn, "Không đủ 5 ngọc để tham gia!");
            return;
        }
        if(!isRegister){
            Service.send_notice_box(p.conn, "Không trong thời gian đăng kí lôi đài, hãy quay lại sau!");
            return;
        }
        if(p.level <35)
        {
            Service.send_notice_box(p.conn, "Cần tối thiểu level 35 mới có thể tham gia!");
            return;
        }
        if(idx_gr<0 || idx_gr >= Group_entrys.length ){
            Service.send_notice_box(p.conn, "Đã xảy ra lỗi vui lòng thử lại!");
            return;
        }
        if(Group_entrys[idx_gr].Containplayer(p.index)){
            Service.send_notice_box(p.conn, "Đã có tên của bạn trong danh sách tham gia!");
            return;
        }
        p.update_ngoc(-5);
        p.item.char_inventory(5);
        Group_entrys[idx_gr].addPlayer(p);
        Service.send_notice_box(p.conn, "Bạn đã đăng kí tham gia lôi đài thành công, hãy nhớ vào lôi đài trước khi thời gian bắt đầu chiến đấu diễn ra!");
    }
    
    public void JoinMap(Player p, int idx){
        if(idx <0 || idx >= Group_entrys.length)return;
        Group_entrys[idx].JoinMap(p);
    }
    public void JoinMap(Player p)throws IOException{
        int idx_gr = GetIdxGroup(p.level);
        if(Group_entrys[idx_gr].Containplayer(p.index))
            Group_entrys[idx_gr].JoinMap(p);
        else
            Service.send_notice_box(p.conn, "Bạn không có tên trong danh sách tham gia!");  
    }
    public void JoinMapPVP(Player p,int idx_gr, int idxmap)throws IOException{
        if(idx_gr <0 || idx_gr >= Group_entrys.length)return;
        Group_entrys[idx_gr].JoinMapPVP(p,idxmap);
    }
    
    public void StartTurn() throws IOException
    {
        if(turn >= 11)
        {
            isATK = false;
            return;
        }
        System.out.println("Start turn "+turn);
        timeTurn = System.currentTimeMillis() + (1000 * 60 * 3);
        isSleep =false;
        for(Group_ld g : Group_entrys){
            try{
                g.StartTurn();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Manager.gI().chatKTGprocess("Cuộc chiến lôi đài đợt "+turn+" đã bắt đầu, hãy mau đến xem");
    }
    
    public void EndTurn() throws IOException{
        timeSleep = System.currentTimeMillis() + (1000 * 60 * 1);
        isSleep =true;
        turn++;
        if(turn>=11)
        {
            System.out.println("End Loi Dai");
            Manager.gI().chatKTGprocess("Lôi dài đã kết thúc, các đại hiệp hãy nghỉ ngơi chờ đợt tới chúng ta lại chiến tiếp");
            //isATK=false;
            return;
        }
        else
        {
            Manager.gI().chatKTGprocess("Loạt đấu tiếp theo sẽ bắt đầu sau 2p nữa.");
        }
        for(Group_ld g : Group_entrys){
            for(LoiDai2 l : g.ld_entrys)
            {
                if(l.isMapStart)continue;
                l.CloseATK();
            }
        }
        SetTop();
    }
    
    public Player GetPlayerByID(int id){
        for(Group_ld g : Group_entrys){
            for(LoiDai2 l : g.ld_entrys)
            {
                if(l.pl_1!=null && l.pl_1.index == id)
                    return l.pl_1;
                else if(l.pl_2!=null && l.pl_2.index == id)
                    return l.pl_2;
                for(Player p0 : l.map.players)
                    if(p0!=null && p0.index == id)
                        return p0;
            }
        }
        return null;
    }
    
    public void RemoveLD(LoiDai2 l, int idx){
        if(idx <0 || idx >= Group_entrys.length)return;
        Group_entrys[idx].Remove(l);
    }
    
    
    
    
    private int GetIdxGroup(short lv_pl){
        if(lv_pl >=35 && lv_pl<=74)
            return 0;
        else if(lv_pl<= 84)
            return 1;
        else if(lv_pl<= 94)
            return 2;
        else if(lv_pl<= 104)
            return 3;
        else if(lv_pl<= 114)
            return 4;
        else if(lv_pl<= 124)
            return 5;
        else if(lv_pl<= 134)
            return 6;
        else if(lv_pl > 134)
            return 7;
        else return -1;
    }
    
    public void Update(){
        long _time = System.currentTimeMillis();
        if(isRegister && timeRegister < _time){
            isRegister = false;
            try{
                System.out.println("EndRegister Loi Dai");
                isSleep = true;
                timeSleep = System.currentTimeMillis() + 1000 * 60 * 2;
                isATK= true;
                Manager.gI().chatKTGprocess("Đã kết thúc đăng kí lôi đài, các đại hiệp hãy tập trung vào lôi đài để bắt đầu giao đấu");
            }catch(Exception e){}
        }
        if(!isATK)return;
        try{
            
            if(timeTurn ==0 && timeSleep ==0){//turn đầu tiên
                StartTurn();
            }else if(timeTurn < _time && isSleep && timeSleep < _time){
                StartTurn();
            }
            else if(timeTurn < _time && !isSleep){
                EndTurn();
            }
            
            for(Group_ld g : Group_entrys){
                List<LoiDai2> lds = g.ld_entrys;
                for(LoiDai2 l : lds)
                {
                    if(l.isMapStart)continue;
                    l.Update();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void GetMenuViews(Session conn, int idx)throws IOException{
        if(idx <0 || idx >= Group_entrys.length)return;
        Group_entrys[idx].GetMenuViewLD(conn);
    }
    
    public void SaveData(Connection conn){
        try{
            //Connection conn = DriverManager.getConnection(SQL.gI().url, Manager.gI().mysql_user, Manager.gI().mysql_pass);
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE `loi_dai` SET `data` = ? WHERE `idx_group` = ?;");
            synchronized(Group_entrys){
                for(Group_ld g : Group_entrys){
                    ps.clearParameters();
                    String data = "[";
                    for(java.util.Map.Entry<Integer, Integer> entry : g.player_entrys.entrySet())
                    {
                        data += "["+entry.getKey()+","+entry.getValue()+"],";
                    }
                    if(data.endsWith(","))
                        data = data.substring(0, data.length()-1);
                    data +="]";
                    ps.setNString(1, data );
                    ps.setByte(2, g.idxGroup);
                    ps.executeUpdate();
                }
            }
            ps.close();
            //conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[" + Util.get_now_by_time() + "] save ld fail!");
        }
        catch(Exception e){
            Log.gI().add_Log_Server("SaveLD", e.getMessage());
        }
    }
    
    public static void LoadData(){
        try
        {
            try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `loi_dai` ;")) {
                while (rs.next()) {
                    byte idx = rs.getByte("idx_group");
                    JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("data"));
                    if(idx >=0 && idx < Group_entrys.length && jsar!= null && !jsar.isEmpty())
                    {
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Group_entrys[idx].player_entrys.clear();
                            Group_entrys[idx].player_entrys.put(Integer.valueOf(jsar2.get(0).toString()), Integer.valueOf(jsar2.get(1).toString()));
                            jsar2.clear();
                        }
                    }
                    if(jsar!=null)
                        jsar.clear();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            gI().SetTop();
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void SetTop(){
        for(Group_ld g: Group_entrys)
            g.SetTop();
    }
}
