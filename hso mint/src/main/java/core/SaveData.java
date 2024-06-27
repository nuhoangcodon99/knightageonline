package core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import client.Clan;
import client.Friend;
import client.Player;
import core.BXH.Memin4;
import map.Map;
import template.EffTemplate;
import template.Item3;
import template.Item47;
import template.Level;
import template.Part_player;
import event.Event_1;
import event_daily.ChiemMo;
import event_daily.ChiemThanhManager;
import event_daily.Group_ld;
import event_daily.LoiDai2;
import event_daily.LoiDaiManager;
import event_daily.Wedding;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map.Entry;
import map.MapService;
import org.json.simple.JSONObject;

public class SaveData {

    @SuppressWarnings("unchecked")
    public synchronized static void process() {
        if(Manager.isServerTest)
            return;
        long time_check = System.currentTimeMillis();
        try {
//            Connection conn = SQL.gI().getConnection();
            Connection conn = DriverManager.getConnection(SQL.gI().url, Manager.gI().mysql_user, Manager.gI().mysql_pass);
            LoiDaiManager.gI().SaveData(conn);
            Manager.gI().chiem_mo.SaveData(conn);
            // clan
            BXH.BXH_clan.clear();
            for (Clan clan : Clan.get_all_clan()) {
                BXH.BXH_clan.add(clan);
            }
            Collections.sort(BXH.BXH_clan, new Comparator<Clan>() {
                @Override
                public int compare(Clan o1, Clan o2) {
                    int com1 = (o1.level == o2.level) ? 0 : (o1.level > o2.level) ? -1 : 1;
                    if (com1 != 0) {
                        return com1;
                    }
                    return (o1.exp >= o2.exp) ? -1 : 1;
                }
            });
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE `clan` SET `level` = ?, `exp` = ?, `slogan` = ?, `rule` = ?, `mems` = ?, `item` = ?, `notice` = ?, `vang` = ?, `kimcuong` = ?, `icon` = ?, `max_mem` = ? WHERE `name` = ?;");
            // clan
            List<Clan> list_to_remove = new ArrayList<>();
            for (int i = 0; i < Clan.entrys.size(); i++) {
                Clan clan = Clan.entrys.get(i);
                if (clan.mems.size() < 1) {
                    list_to_remove.add(clan);
                    Clan.entrys.remove(clan);
                    i--;
                } else {
                    ps.clearParameters();
                    ps.setInt(1, clan.level);
                    ps.setLong(2, clan.exp);
                    ps.setNString(3, clan.slogan );
                    ps.setNString(4, clan.rule);
                    ps.setNString(5, Clan.flush_mem_json(clan.mems));
                    ps.setNString(6, Clan.flush_item_json(clan.item_clan));
                    ps.setNString(7, clan.notice);
                    ps.setLong(8, clan.get_vang());
                    ps.setInt(9, clan.get_ngoc());
                    ps.setInt(10, clan.icon);
                    ps.setInt(11, clan.max_mem);
                    ps.setNString(12, clan.name_clan);
                    ps.executeUpdate();
//                    ps.addBatch();
//                    if (i % 50 == 0) {
//                        ps.executeBatch();
//                    }
                }
            }
//            ps.executeBatch();
            //
            // save chiến trường
            ps.close();
            ps = conn.prepareStatement("UPDATE `config_server` SET `data1` = ?,`data2` = ? WHERE `name` = ?;");
            ChiemThanhManager.SaveData(ps);
            
            ps.close();
            ps = conn.prepareStatement("DELETE FROM `clan` WHERE `name` = ?;");
            for (int i = 0; i < list_to_remove.size(); i++) {
                Clan clan = list_to_remove.get(i);
                ps.clearParameters();
                ps.setNString(1, clan.name_clan);
                ps.executeUpdate();
//                ps.addBatch();
//                if (i % 50 == 0) {
//                    ps.executeBatch();
//                }
            }
            ps.close();
            ps = conn.prepareStatement("UPDATE `wedding` SET `item` = ? WHERE `name` = ?;");
            for (int i = 0; i < Wedding.list.size(); i++) {
                Wedding temp = Wedding.list.get(i);
                ps.clearParameters();
                JSONArray js2 = new JSONArray();
                js2.add(temp.exp);
                js2.add(temp.it.color);
                js2.add(temp.it.tier);
                JSONArray js22 = new JSONArray();
                for (int i2 = 0; i2 < temp.it.op.size(); i2++) {
                    JSONArray js23 = new JSONArray();
                    js23.add(temp.it.op.get(i2).id);
                    js23.add(temp.it.op.get(i2).getParam(0));
                    js22.add(js23);
                }
                js2.add(js22);
                ps.setNString(1, js2.toJSONString());
                JSONArray js = new JSONArray();
                js.add(temp.name_1);
                js.add(temp.name_2);
                ps.setNString(2, js.toJSONString());
                ps.execute();
            }
            ps.close();
            // flush player
            String query
                    = "UPDATE `player` SET `level` = ?, `exp` = ?, `site` = ?, `body` = ?, `eff` = ?, `friend` = ?, `skill` = ?, `item4` = ?, "
                    + "`item7` = ?, `item3` = ?, `itemwear` = ?, `giftcode` = ?, `enemies` = ?, `rms_save` = ?, `itembox4` = ?, "
                    + "`itembox7` = ?, `itembox3` = ?, `pet` = ?, `medal_create_material` = ?, `point_active` = ?, `vang` = ?, "
                    + "`kimcuong` = ?, `tiemnang` = ?, `kynang` = ?, `diemdanh` = ?, `chucphuc` = ?, `hieuchien` = ?, `typeexp` = ?, "
                    + "`date` = ?, `point1` = ?, `point2` = ?, `point3` = ?, `point4` = ?  WHERE `id` = ?;";
            ps = conn.prepareStatement(query);
            try{
                for(Group_ld g:LoiDaiManager.gI().Group_entrys){
                    for(LoiDai2 l : g.ld_entrys){
                        for(Player p0:l.map.players){
                            if(p0!=null && p0.conn != null && p0.conn.connected && p0.conn.socket != null && !p0.conn.socket.isClosed())
                                p0.flush();
                        }
                    }
                }
            }catch(Exception ee){
                Log.gI().add_Log_Server("save_data", ee.getMessage());
                ee.printStackTrace();
            }
            for (Map[] map : Map.entrys) {
                if(map == null)continue;
                for (Map map0 : map) {
                    if(map0 == null || map0.players == null )continue;
                    for (int i1 = 0; i1 < map0.players.size(); i1++) {
                        // for (int i1 = 0; i1 < ServerManager.gI().t1.list_p.size(); i1++) {
                        try
                        {
                            ps.clearParameters();
                            Player p0 = map0.players.get(i1);
                            if(p0.conn == null || p0.conn.socket == null || p0.conn.socket.isClosed() || !p0.conn.connected)
                            {
                                MapService.leave(map0, p0);
                                continue;
                            }
                            p0.flush();
                        }catch(Exception ee){
                            Log.gI().add_Log_Server("save_data", ee.getMessage());
                            ee.printStackTrace();
                        }
                    }
                }
            }
            
            
//            try
//            {
//                SessionManager.checkBugAccount();
//            }catch(Exception eee){}
            // }
//            ps.executeBatch();
            ps.close();
            // event
            if (Manager.gI().event == 1) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, Event_1.SaveData().toJSONString());
                ps.setInt(2, 0);
                ps.executeUpdate();
                ps.close();
            }
            else if (Manager.gI().event == 2) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, ev_he.Event_2.SaveData().toJSONString());
                ps.setInt(2, 1);
                ps.executeUpdate();
                ps.close();
            }
            else if (Manager.gI().event == 3) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, ev_he.Event_3.SaveData().toJSONString());
                ps.setInt(2, 2);
                ps.executeUpdate();
                ps.close();
            }
            // bxh
            BXH.BXH_level.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `exp`, `name`, `body`, `itemwear` FROM `player` WHERE `level` > 10 ORDER BY `level` DESC, exp DESC LIMIT 20;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                
                Memin4 temp = new Memin4();
                temp.level = rs.getShort("level");
                temp.exp = rs.getLong("exp");
                temp.name = rs.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percent
                        = String.format("%.1f", (((float) temp.exp * 1000) / Level.entrys.get(temp.level - 1).exp) / 10f);
                temp.info = "Level : " + (temp.level) + "\t-\t" + percent + "%";
                BXH.BXH_level.add(temp);
            }
            rs.close();
            //

            Map.head = -1;
            Map.eye = -1;
            Map.hair = -1;
            Map.weapon = -1;
            Map.body = -1;
            Map.leg = -1;
            Map.hat = -1;
            Map.wing = -1;
            Map.name_mo = "";
             rs = ps.executeQuery("SELECT * FROM `player` ORDER BY `level` DESC, `id` LIMIT 1");
   //         rs = ps.executeQuery("SELECT * FROM `player` ORDER BY `hieuchien` DESC, `id` LIMIT 1");
  //          rs = ps.executeQuery("SELECT * FROM `player` WHERE `name` = '"+Manager.VuaChienTruong+"' LIMIT 1");
            if (rs.next()) {
                Map.name_mo = rs.getString("name");
                JSONArray js = (JSONArray) JSONValue.parse(rs.getString("body"));
                Map.head = Short.parseShort(js.get(0).toString());
                Map.eye = Short.parseShort(js.get(1).toString());
                Map.hair = Short.parseShort(js.get(2).toString());
                js.clear();
                js = (JSONArray) JSONValue.parse(rs.getString("itemwear"));
                for (int i3 = 0; i3 < js.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(js.get(i3).toString());
                    if (jsar2 == null) {
                        return;
                    }
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 2 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }

                    Part_player temp = new Part_player();
                    temp.type = Byte.parseByte(jsar2.get(2).toString());
                    temp.part = Byte.parseByte(jsar2.get(6).toString());
                    if (temp.type == 2) {
                        Map.hat = temp.part;
                    }
                    if (temp.type == 0) {
                        Map.body = temp.part;
                    }
                    if (temp.type == 1) {
                        Map.leg = temp.part;
                    }
                    if (temp.type == 7) {
                        Map.wing = temp.part;
                    }
                    if (temp.type == 10) {
                        Map.weapon = temp.part;
                    }
                }

            }
            
            //
//            conn.commit();
            rs.close();
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[" + Util.get_now_by_time() + "] save data fail!");
            return;
        }
        System.out.println("[" + Util.get_now_by_time() + "] save data ok " + (System.currentTimeMillis() - time_check));
        ServerManager.gI().time_l = System.currentTimeMillis()+60_000L;
    }
}
