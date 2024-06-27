package map;

import BossHDL.BossManager;
import java.util.ArrayList;
import java.util.List;
import client.Player;
import core.Manager;
import core.Service;
import core.Util;
import event_daily.ChiemThanhManager;
import event_daily.ChienTruong;
import io.Message;
import java.io.IOException;
import java.util.HashMap;
import template.MainObject;
import template.Mob;

public class Mob_in_map extends MainObject {
public final static HashMap<Integer, Mob_in_map> ENTRYS = new HashMap<>();
    public int time_refresh = 3;
    private boolean is_boss;
    public long time_back;
    public final List<Player> list_fight = new ArrayList<>();
    public long time_fight;
    public boolean is_boss_active;
    public int timeBossRecive = 1000 * 60 * 60 * 8;
    public final HashMap<String, Long> top_dame = new HashMap<>();

    public void Reset() {
        hp = get_HpMax();
        isdie = false;
        synchronized (list_fight) {
            list_fight.clear();
        }
        synchronized (top_dame) {
            top_dame.clear();
        }
    }

    public void Set_isBoss(boolean isBoss) {
        is_boss = isBoss;
    }

    @Override
    public boolean isBoss() {
        return is_boss;
    }

    @Override
    public boolean isMobCTruongHouse() {
        return template.mob_id >= 89 && template.mob_id <= 92;
    }

    @Override
    public boolean isMob() {
        return true;
    }

    @Override
    public int get_DameBase() {
        if (dame <= 0) {
            dame = level * 75;
        }

        int dmob = Util.random((int) (this.dame * 0.95), (int) (this.dame * 1.05));
//            System.out.println("map.MapService.mob_fire()"+dmob);
        if (this.level > 30 && this.level <= 50) {
            dmob = (dmob * 13) / 10;
        } else if (this.level > 50 && this.level <= 70) {
            dmob = (dmob * 16) / 10;
        } else if (this.level > 70 && this.level <= 100) {
            dmob = (dmob * 19) / 10;
        } else if (this.level > 100 && this.level <= 600) {
            dmob = (dmob * 21) / 10;
        }
        if (this.is_boss) {
            dmob = (int) (dmob * this.level * 0.03);
        }
        if (this.color_name != 0 && (this.template.mob_id < 89 || this.template.mob_id > 92)) {
            dmob *= 2;
        }
        return dmob;
    }

    @Override
    public int get_Miss() {
        return 800;
    }

    @Override
    public void SetDie(Map map, MainObject mainAtk) throws IOException {
        hp = 0;
        isdie = true;

        Mob_in_map mob = (Mob_in_map) this;
        if (mainAtk.isPlayer()) {
            if (((Player) mainAtk).hieuchien > 0 && Math.abs(mainAtk.level - mob.level) <= 5) {
                ((Player) mainAtk).hieuchien--;
            }
            if (mob.template.mob_id == 152) {
                ChiemThanhManager.SetOwner((Player) mainAtk);
            }
        }
        boolean check_mob_roi_ngoc_kham = mob.template.mob_id >= 167 && mob.template.mob_id <= 172;
        if (mob.isBoss()) {
            map.BossDie(mob);
            String p_name = "";
            long top_dame = 0;
            for (java.util.Map.Entry<String, Long> en : mob.top_dame.entrySet()) {
                if (en.getValue() > top_dame) {
                    top_dame = en.getValue();
                    p_name = en.getKey();
                }
            }
            mob.is_boss_active = false;
            if (!Map.is_map_cant_save_site(mob.map_id)) {
                Manager.gI().chatKTGprocess("" + mainAtk.name + " Đã Tiêu Diệt " + mob.template.name );
               Manager.gI().chatKTGprocess("" + top_dame + " Đã Nhận Quà 1 Top Sát Thương Đánh " + mob.template.name + "");
               Manager.gI().chatKTGprocess("" + mainAtk.name + " Chỵ Xin Boss Nhé Mấy Cưng !!!" );
            }
            if (mainAtk.isPlayer()) {
                if (mob.template.mob_id == 174 ) {
                    BossManager.DropItemBossEvent(map, mob, (Player) mainAtk);
                } else {
                    LeaveItemMap.leave_item_boss(map, mob, (Player) mainAtk);
                }
            }
        } else {
            mob.time_back = System.currentTimeMillis() + (mob.time_refresh * 1000) - 1000L;
            if (mainAtk.isPlayer()) {
                if (Math.abs(mob.level - mainAtk.level) <= 10 && !check_mob_roi_ngoc_kham) {
                    if (Math.abs(mob.level - mainAtk.level) <= 5 && Manager.gI().event == 3 && Util.random_ratio(10)) {
                        ev_he.Event_3.LeaveItemMap(map, this, mainAtk);
                    } else if (mob.level >= 133) {
                        ev_he.Event_3.LeaveItemMap(map, this, mainAtk);
                    }
                    if (10 > Util.random(0, 300) || mob.color_name != 0) {
                        LeaveItemMap.leave_item_3(map, mob, (Player) mainAtk);
                    }
                    if (20 > Util.random(0, 300)) {
                        LeaveItemMap.leave_item_4(map, mob, (Player) mainAtk);
                    }
                    if (20 > Util.random(0, 300)) {
                        LeaveItemMap.leave_item_7(map, mob, (Player) mainAtk);
                    }
                    if (30 > Util.random(0, 300)) {
                        LeaveItemMap.leave_vang(map, mob, (Player) mainAtk);
                    }
                    if (30 > Util.random(0, 300)) {
                        LeaveItemMap.leave_material(map, mob, (Player) mainAtk);
                    }
                    if (Manager.gI().event != 0 && 30 > Util.random(0, 100) && Math.abs(mob.level - mainAtk.level) <= 5) {
                        LeaveItemMap.leave_item_event(map, mob, (Player) mainAtk);
                    }
                }
                if (check_mob_roi_ngoc_kham) {
                    LeaveItemMap.leave_material_ngockham(map, mob, (Player) mainAtk);
                }
                if (mob.color_name != 0) {
                    map.num_mob_super--;
                }
            }
        }
    }

    @Override
    public void update(Map map) {
        try {
            if (this.isdie && this.ishs && this.time_back < System.currentTimeMillis()) {
                this.isdie = false;
                this.Reset();
                this.hp = this.get_HpMax();
                if (this.isBoss()) {
                    this.color_name = 3;
                } else if (5 > Util.random(200) && map.num_mob_super < 2 && this.level > 50) {
                    this.color_name = (new byte[]{1, 2, 4, 5})[Util.random(4)];
                    map.num_mob_super++;
                } else {
                    this.color_name = 0;
                }
                for (int j = 0; j < map.players.size(); j++) {
                    Player pp = map.players.get(j);
                    if ((Math.abs(pp.x - this.x) < 200) && (Math.abs(pp.y - this.y) < 200)) {
                        if (!pp.other_mob_inside.containsKey(this.index)) {
                            pp.other_mob_inside.put(this.index, true);
                        }
                        if (pp.other_mob_inside.get(this.index)) {
                            Message mm = new Message(4);
                            mm.writer().writeByte(1);
                            mm.writer().writeShort(this.template.mob_id);
                            mm.writer().writeShort(this.index);
                            mm.writer().writeShort(this.x);
                            mm.writer().writeShort(this.y);
                            mm.writer().writeByte(-1);
                            pp.conn.addmsg(mm);
                            mm.cleanup();
                            pp.other_mob_inside.replace(this.index, true, false);
                        } else {
                            Service.mob_in4(pp, this.index);
                        }
                    }
                }
            } else if (!this.isdie && this.isATK && this.time_fight < System.currentTimeMillis()) {
                if ((this.template.mob_id == 151 || this.template.mob_id == 152 || this.template.mob_id == 154)) {
                    for (Player p0 : this.list_fight) {
                        if (p0 != null && !p0.isdie && p0.map.map_id == this.map_id && p0.map.zone_id == this.zone_id
                                && Math.abs(this.x - p0.x) < 200 && Math.abs(this.y - p0.y) < 200) {
                            MainObject.MainAttack(map, this, p0, 0, null, 2);
                        }
//                                MapService.mob_fire(this, mob, p0);
                    }
                    this.time_fight = System.currentTimeMillis() + 3500L;
                } else if (this.list_fight.size() > 0) {
                    Player p0 = this.list_fight.get(Util.random(this.list_fight.size()));
                    if (p0 != null && !p0.isdie && p0.map.map_id == this.map_id && p0.map.zone_id == this.zone_id) {
                        if (Math.abs(this.x - p0.x) < 200 && Math.abs(this.y - p0.y) < 200) {
                            if (this.time_fight < System.currentTimeMillis()) {
                                this.time_fight = System.currentTimeMillis() + 1200L;
                                MainObject.MainAttack(map, this, p0, 0, null, 2);
//                                    MapService.mob_fire(this, mob, p0);
                            }
                        } else {
                            this.list_fight.remove(p0);
                            //
                            Message m = new Message(10);
                            m.writer().writeByte(0);
                            m.writer().writeShort(this.index);
                            MapService.send_msg_player_inside(map, p0, m, true);
                            m.cleanup();
                        }
                    }
                    if (p0.isdie) {
                        this.list_fight.remove(p0);
                        //
                        Message m = new Message(10);
                        m.writer().writeByte(0);
                        m.writer().writeShort(this.index);
                        MapService.send_msg_player_inside(map, p0, m, true);
                        m.cleanup();
                    }
                    if (this.list_fight.contains(p0) && !(p0.map.map_id == this.map_id && p0.map.zone_id == this.zone_id)) {
                        this.list_fight.remove(p0);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
