
package template;

import BossHDL.BossManager;
import client.Pet;
import client.Player;
import core.Manager;
import core.Service;
import core.Util;
import event_daily.ChiemThanhManager;
import event_daily.ChienTruong;
import io.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Eff_special_skill;
import map.LeaveItemMap;
import map.Map;
import map.MapService;
import map.Mob_in_map;


public class MainObject {

    public String name;
    public int hp, mp;
    protected int hp_max, mp_max;
    public boolean isdie, ishs = true, isATK = true;
    public int index;
    public short x, x_old, y, y_old;
    public short level;
    public byte map_id, zone_id;
    protected int dame, def;
    public boolean isExp = true;
    public byte color_name;
    public byte typepk;
    public Mob template;
    public long exp;
    public byte clazz;
    public Kham_template kham;
    public int hieuchien;

    public int Set_hpMax(int hp_max) {
        return this.hp_max = hp_max;
    }

    public int Set_Dame(int dame) {
        return this.dame = dame;
    }

    protected List<EffTemplate> MainEff;
    protected List<EffTemplate> Eff_me_kham;

    public void updateEff() {
        try {
            if (MainEff != null) {
                synchronized (MainEff) {
                    for (int i = MainEff.size() - 1; i >= 0; i--) {
                        EffTemplate temp = MainEff.get(i);
                        if (temp.time <= System.currentTimeMillis()) {
                            MainEff.remove(i);
                            if (isPlayer()) {
                                if (temp.id == -125) {
                                    ((Player) this).set_x2_xp(0);
                                }
                                if (temp.id == 24 || temp.id == 23 || temp.id == 0 || temp.id == 2 || temp.id == 3 || temp.id == 4) {
                                    if (temp.id == 2 && isPlayer()) {
                                        this.hp += ((Player) this).hp_restore;
                                    }
                                    Service.send_char_main_in4((Player) this);
                                    for (int j = 0; j < ((Player) this).map.players.size(); j++) {
                                        Player p2 = ((Player) this).map.players.get(j);
                                        if (p2 != null && p2.index != this.index) {
                                            MapService.send_in4_other_char(((Player) this).map, p2, (Player) this);
                                        }
                                    }
                                }
                            }
                            continue;
                        }
                        //
                        if (temp.id == 1 && !this.isdie && isPlayer()) {
                            if (((Player) this).time_affect_special_sk < System.currentTimeMillis() && ((Player) this).dame_affect_special_sk > 0) {
                                ((Player) this).time_affect_special_sk = System.currentTimeMillis() + 1000L;
                                this.hp -= ((Player) this).dame_affect_special_sk;
                                Service.usepotion(((Player) this), 0, -((Player) this).dame_affect_special_sk);
                                if (this.hp < 0) {
                                    MapService.Player_Die(((Player) this).map, ((Player) this), ((Player) this), true);
                                }
                            }
                        }
                     
                    }
                }
            }
            if (Eff_me_kham != null) {
                synchronized (Eff_me_kham) {
                    for (int i = Eff_me_kham.size() - 1; i >= 0; i--) {
                        EffTemplate temp = Eff_me_kham.get(i);
                        if (temp.time <= System.currentTimeMillis()) {
                            Eff_me_kham.remove(i);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void add_EffDefault(int id, int param1, long time_end) {
        if (MainEff == null) {
            return;
        }
        synchronized (MainEff) {
            if (param1 == 0) {
                return;
            }
            for (int i = MainEff.size() - 1; i >= 0; i--) {
                EffTemplate temp_test = MainEff.get(i);
                if (temp_test != null && temp_test.id == id) {
                    MainEff.remove(i);
                }
            }
            MainEff.add(new EffTemplate(id, param1, time_end));
        }
//        synchronized (MainEff) {
//            MainEff.add(new EffTemplate(id, param1, time_end));
//        }
    }

    public void add_EffMe_Kham(int id, int param1, long time_end) {
        if (Eff_me_kham == null) {
            return;
        }
        synchronized (Eff_me_kham) {
            Eff_me_kham.add(new EffTemplate(id, param1, time_end));
        }
    }

    public EffTemplate get_EffDefault(int id) {
        if (MainEff == null) {
            return null;
        }
        long time = System.currentTimeMillis();
        synchronized (MainEff) {
            for (EffTemplate e : MainEff) {
                if (e.id == id && e.time > time) {
                    return e;
                }
            }
        }
        return null;
    }

    public EffTemplate get_EffMe_Kham(int id) {
        if (Eff_me_kham == null) {
            return null;
        }
        long time = System.currentTimeMillis();
        synchronized (Eff_me_kham) {
            for (EffTemplate e : Eff_me_kham) {
                if (e.id == id && e.time > time) {
                    return e;
                }
            }
        }
        return null;
    }

    public boolean isStunes(boolean isAtk) {
        if (MainEff == null) {
            return false;
        }
        long time = System.currentTimeMillis();
        synchronized (MainEff) {
            for (EffTemplate e : MainEff) {
                if (e.id >= -124 && e.id <= -121 && (!isAtk || e.id != -123) && e.time > time) {
                    return true;
                }
            }
        }
        return false;
    }

    public int get_TypeObj() {
        return 1;
    }

    public int get_HpMax() {
        return hp_max;
    }

    public int get_MpMax() {
        return mp_max;
    }

    public int get_DameBase() {
        return dame;
    }

    public int get_DameProp(int type) {
        return 0;
    }

    public int get_PercentDameProp(int type) {
        return 0;
    }

    public int get_DefBase() {
        return def;
    }

    public int get_PercentDefBase() {
        return 0;
    }

    public int get_PercentDefProp(int type) {
        return 0;
    }

    public boolean isMob() {
        return false;
    }

    public boolean isMoTaiNguyen() {
        return false;
    }

    public boolean isMobDungeon() {
        return false;
    }

    public boolean isMobDiBuon() {
        return false;
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isNhanBan() {
        return false;
    }

    public boolean isMobCTruong() {
        return false;
    }

    public boolean isMobCTruongHouse() {
        return false;
    }

    public boolean isBoss() {
        return false;
    }

    public int get_Pierce() {//xuyên giáp
        return 0;
    }

    public int get_Crit() {
        return 0;
    }

    public int get_PhanDame() {
        return 0;
    }

    public int get_Miss() {
        return 0;
    }

    public void Obj_Fire(Map map, MainObject objFocus, int skill, LvSkill temp) throws IOException {
        // không có thì đưa vào null;
    }

    public void SetDie(Map map, MainObject mainAtk) throws IOException {
        hp = 0;
        isdie = true;
    }

    public static void MainAttack(Map map, MainObject ObjAtk, MainObject focus, int idxSkill, LvSkill temp, int type) throws IOException {
        // pvp, pve, mob chiến trường, mob chiếm thành, nhân bản boss, (không đánh mob đi buôn)

        //<editor-fold defaultstate="collapsed" desc="... không thể tấn công    ..."> 
        if (ObjAtk == null || focus == null || ObjAtk.equals(focus) || ObjAtk.isdie || ObjAtk.isStunes(true)) {
            return;
        }
        if (ObjAtk.isPlayer() && focus.isPlayer() && !map.isMapChiemThanh() && (map.ismaplang || ObjAtk.level < 11 || focus.level < 11
                || (ObjAtk.typepk != 0 && ObjAtk.typepk == focus.typepk) || ObjAtk.hieuchien > 32_000)) {
            return;
        }
        if (focus.isMob() && focus.template.mob_id == 152 && !ChiemThanhManager.isDameTruChinh(map)) {
            return;
        }
        if (Math.abs(ObjAtk.x - focus.x) > 300 || Math.abs(ObjAtk.y - focus.y) > 300) {
            return;
        }
        if (ObjAtk.isStunes(true)) {
            return;
        }
        if (focus.isPlayer() && focus.get_EffMe_Kham(StrucEff.TangHinh) != null) {
            return;
        }
        if (ObjAtk.isPlayer() && ObjAtk.get_EffMe_Kham(StrucEff.LuLan) != null) {
            return;
        }
        if (focus.isPlayer() && map.ld2 == null && map.zone_id < map.maxzone && focus.get_EffDefault(-126) != null) {
            return;
        }

        if (ObjAtk.isPlayer() && map.ld2 != null && (map.ld2.pl_1 == null || map.ld2.pl_2 == null || (map.ld2.pl_1.index != ObjAtk.index && map.ld2.pl_2.index != ObjAtk.index)
                || (map.ld2.pl_1.index != focus.index && map.ld2.pl_2.index != focus.index))) {
            return;
        }
        if (focus.isdie || focus.hp <= 0 && ObjAtk.isPlayer()) {
            if (focus.isPlayer()) {
                MapService.Player_Die(map, (Player) focus, ObjAtk, false);
            } else {
                MapService.MainObj_Die(map, ((Player) ObjAtk).conn, focus, false);
            }
            return;
        }
        if (ObjAtk.isPlayer() && focus.isPlayer() && focus.typepk == -1)// đồ sát
        {
            if (ObjAtk.hieuchien > 1000) {
                Service.send_notice_box(((Player) ObjAtk).conn, "Không thể đồ sát quá nhiều, cần tẩy điểm trước.");
                return;
            }
            if (((Player) focus).pet_follow != -1 && ((Player) focus).pet_follow == 4708) {
                Service.send_notice_box(((Player) ObjAtk).conn, "Đối phương đang được pet bảo vệ");
                return;
            }
        }
        if (focus.get_Miss() > Util.random(10_000)) {
            if (ObjAtk.isPlayer()) {
                MapService.Fire_Player(map, ((Player) ObjAtk).conn, idxSkill, focus.index, 0, focus.hp, new ArrayList<>());
            }
            return;
        }
        //</editor-fold>

        Player p = ObjAtk.isPlayer() ? (Player) ObjAtk : null;
        EffTemplate ef;
        long dame = ObjAtk.get_DameBase();
        int hutHP = 0;
        float ptCrit = 0;
        float DamePlus = 0;
        float GiamDame = 0;
        boolean xuyengiap = ObjAtk.get_Pierce() > Util.random(10_000);

        //<editor-fold defaultstate="collapsed" desc="Get Dame default...">  
        if (type == 0) {
            int tempDameProp = ObjAtk.get_DameProp(0);
            int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(16));
            dame += dameProp < 0 ? 0 : dameProp;
        } else if (type == 1) {
            switch (ObjAtk.clazz) {
                case 0: {
                    int tempDameProp = ObjAtk.get_DameProp(2);
                    int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(18));
                    dame += dameProp < 0 ? 0 : dameProp;
                    break;
                }
                case 1: {
                    int tempDameProp = ObjAtk.get_DameProp(4);
                    int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(20));
                    dame += dameProp < 0 ? 0 : dameProp;
                    break;
                }
                case 2: {
                    int tempDameProp = ObjAtk.get_DameProp(1);
                    int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(17));
                    dame += dameProp < 0 ? 0 : dameProp;
                    break;
                }
                case 3: {
                    int tempDameProp = ObjAtk.get_DameProp(3);
                    int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(19));
                    dame += dameProp < 0 ? 0 : dameProp;
                    break;
                }
            }
        } else {
            dame += ObjAtk.get_DameProp(0);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Skill...">  
        if (ObjAtk.isPlayer()) {
            if (idxSkill == 19 && ObjAtk.clazz == 1) {
                for (Option op : temp.minfo) {
                    if (op.id == 4) {
                        dame += op.getParam(0);
                    }
                    if (op.id == 11) {
                        dame += dame * (op.getParam(0) / 100) / 100;
                    }
                }
            } else {
                for (int i = temp.minfo.length - 1; i >= 0; i--) {
                    Option op = temp.minfo[i];
                    if (type == 0) {
                        if (op.id == 0) {
                            dame += op.getParam(0);
                        }
                        if (op.id == 7) {
                            dame += dame * (op.getParam(0) / 100) / 100;
                        }
                    } else {
                        if (op.id == 1 || op.id == 2 || op.id == 3 || op.id == 4) {
                            dame += op.getParam(0);
                        }
                        if (op.id == 9 || op.id == 10 || op.id == 11 || op.id == 8) {
                            dame += dame * (op.getParam(0) / 100) / 100;
                        }
                    }
                }
            }
        }

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="ngựa...">   
        if (ObjAtk.isPlayer()) {
            if (p.type_use_mount == 3) {
                DamePlus += 0.2;
            } else if (p.type_use_mount == 5) {
                DamePlus += 0.4;
            } else if (p.type_use_mount == 11 || p.type_use_mount == 12) {
                DamePlus += 0.1;
            } else if ((p.type_use_mount == 20 && p.id_horse == 114)
                    || (p.type_use_mount == 22 && p.id_horse == 117)) {
                DamePlus += 0.15;
            } else if ((p.type_use_mount == 20 && p.id_horse == 116)) {
                DamePlus += 0.35;
            }
        }
        //</editor-fold>
        List<Float> giamdame = new ArrayList<>();
        ef = ObjAtk.get_EffDefault(3);
        if (ef != null) {
            giamdame.add((float) 0.2);
            GiamDame += 0.2;
//            DamePlus -= 0.2;
//            dame = (dame / 10) * 8;
        }
        if (ObjAtk.isPlayer() && p.getlevelpercent() < 0) {
            giamdame.add((float) 0.5);
            GiamDame += 0.5;
        }

        //<editor-fold defaultstate="collapsed" desc="Nộ cánh...">  
        if (ObjAtk.isPlayer()) {
            EffTemplate temp2 = ObjAtk.get_EffDefault(StrucEff.PowerWing);
            if (temp2 == null) {
                Item3 it = p.item.wear[10];
                if (it != null) {
                    int percent = 0;
                    int time = 0;
                    for (Option op : it.op) {
                        if (op.id == 41) {
                            percent = op.getParam(it.tier);
                        } else if (op.id == 42) {
                            time = op.getParam(it.tier);
                        }
                    }
                    if (percent > Util.random(10_000)) {
                        //
                        ObjAtk.add_EffDefault(StrucEff.PowerWing, 1000, time);
                        //
                        Message mw = new Message(40);
                        mw.writer().writeByte(0);
                        mw.writer().writeByte(1);
                        mw.writer().writeShort(ObjAtk.index);
                        mw.writer().writeByte(21);
                        mw.writer().writeInt(time);
                        mw.writer().writeShort(ObjAtk.index);
                        mw.writer().writeByte(0);
                        mw.writer().writeByte(30);
                        byte[] id__ = new byte[]{7, 8, 9, 10, 11, 15, 0, 1, 2, 3, 4, 14};
                        int[] par__ = new int[]{2000, 2000, 2000, 2000, 2000, 2000,
                            2 * (ObjAtk.get_param_view_in4(0) / 10), 2 * (ObjAtk.get_param_view_in4(1) / 10),
                            2 * (ObjAtk.get_param_view_in4(2) / 10), 2 * (ObjAtk.get_param_view_in4(3) / 10),
                            2 * (ObjAtk.get_param_view_in4(4) / 10), 2 * (ObjAtk.get_param_view_in4(14) / 10)};
                        mw.writer().writeByte(id__.length);
                        //
                        for (int i = 0; i < id__.length; i++) {
                            mw.writer().writeByte(id__[i]);
                            mw.writer().writeInt(par__[i]);
                        }
                        //
                        MapService.send_msg_player_inside(p.map, p, mw, true);
                        mw.cleanup();
                    }
                }
            } else {
                DamePlus += 0.2;
            }
        }

        //</editor-fold>
        ef = ObjAtk.get_EffDefault(53);
        int hpmax = ObjAtk.get_HpMax();
        int HoiHP = 0;
        if (ef != null && ObjAtk.hp < hpmax) {
            HoiHP += hpmax / 100;
        }

        //<editor-fold defaultstate="collapsed" desc="Tác dụng mề...">  
        boolean isEffKhaiHoan = focus.isPlayer() && focus.get_EffMe_Kham(StrucEff.NgocKhaiHoan) != null;
        int prMeday = 0;
        if (focus.isPlayer()) {
            giamdame.add((float) (((Player) focus).total_item_param(80) * 0.0001));
        }
//        GiamDame += focus.isPlayer() ? (float) (((Player) focus).total_item_param(80) * 0.0001) : 0;//giáp hắc ám
        if (ObjAtk.isPlayer()) {
            if ((ef = ObjAtk.get_EffMe_Kham(StrucEff.TangHinh)) == null && ObjAtk.total_item_param(82) > Util.random(10_000)) {
                ObjAtk.add_EffMe_Kham(StrucEff.TangHinh, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(81)));
                Eff_special_skill.send_eff_TangHinh(p, 81, prMeday);
            } else if ((ef = ObjAtk.get_EffMe_Kham(StrucEff.KhienMaThuat)) == null && ObjAtk.total_item_param(85) > Util.random(10_000)) {
                ObjAtk.add_EffMe_Kham(StrucEff.KhienMaThuat, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(86)));
                Eff_special_skill.send_eff_Meday(p, 86, prMeday);
            }
        }
        if (focus.isPlayer() && !isEffKhaiHoan) {
            if (focus.get_EffMe_Kham(StrucEff.BongLua) == null && ObjAtk.total_item_param(76) > Util.random(10_000)) {
                focus.add_EffMe_Kham(StrucEff.BongLua, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(77)));
                Eff_special_skill.send_eff_Meday((Player) focus, 77, prMeday);
            } else if (focus.get_EffMe_Kham(StrucEff.BongLanh) == null && ObjAtk.total_item_param(78) > Util.random(10_000)) {
                focus.add_EffMe_Kham(StrucEff.BongLanh, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(79)));
                Eff_special_skill.send_eff_Meday((Player) focus, 79, prMeday);
            } else if (focus.get_EffMe_Kham(StrucEff.LuLan) == null && ObjAtk.total_item_param(87) > Util.random(10_000)) {
                focus.add_EffMe_Kham(StrucEff.LuLan, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(88)));
                Eff_special_skill.send_eff_Meday((Player) focus, 88, prMeday);
            }
            if (focus.get_EffMe_Kham(StrucEff.KhienMaThuat) != null) {
                GiamDame += 0.5;
                giamdame.add((float) 0.5);
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Tác dụng khảm...">  
        int prKham = 0;
        if (focus.isPlayer() && (ObjAtk.isBoss() || ObjAtk.get_TypeObj() == 0)) {
            if (!isEffKhaiHoan && (prKham = focus.total_item_param(101)) > 0) {
                if (focus.kham.idAtk_KH == ObjAtk.index) {
                    focus.kham.CountAtk_KH++;
                } else {
                    focus.kham.idAtk_KH = ObjAtk.index;
                    focus.kham.CountAtk_KH = 1;
                }

                if (focus.kham.CountAtk_KH >= prKham) {
                    focus.kham.idAtk_KH = 0;
                    focus.kham.CountAtk_KH = 0;
                    focus.add_EffMe_Kham(StrucEff.NgocKhaiHoan, 0, System.currentTimeMillis() + 3000);
                    Eff_special_skill.send_eff_kham((Player) focus, StrucEff.NgocKhaiHoan, 3000);
                }
            }

            if ((ef = focus.get_EffMe_Kham(StrucEff.NgocLucBao)) != null) {
                hutHP += (int) (dame * 0.1);
            } else if ((prKham = focus.total_item_param(102)) > Util.random(10000)) {
                focus.add_EffMe_Kham(StrucEff.NgocLucBao, prKham, System.currentTimeMillis() + 3000);
                Eff_special_skill.send_eff_kham((Player) focus, StrucEff.NgocLucBao, 3000);
            }
        }

        if (ObjAtk.isPlayer()) {
            if ((focus.isBoss() || focus.get_TypeObj() == 0)) {
                if (ObjAtk.get_EffMe_Kham(StrucEff.NgocHonNguyen) != null) {
                    DamePlus += 1;
                }
            }

            double ptHP = (ObjAtk.hp / ObjAtk.get_HpMax()) * 100;
            if ((ef = ObjAtk.get_EffMe_Kham(StrucEff.NgocPhongMa)) != null) {
                HoiHP += (int) (hpmax * ef.param * 0.0001);
            } else if (ptHP < ObjAtk.total_item_param(104) / 100 && (prKham = ObjAtk.total_item_param(103)) > Util.random(10_000)) {
                ObjAtk.add_EffMe_Kham(StrucEff.NgocPhongMa, prKham, System.currentTimeMillis() + 5000);
                Eff_special_skill.send_eff_kham(p, StrucEff.NgocPhongMa, 5000);
            }

            if (focus.isBoss() && (ef = ObjAtk.get_EffMe_Kham(StrucEff.NgocSinhMenh)) != null) {
                DamePlus += 0.5;
//                dame += (long)(dame * 0.5);
            } else if (focus.isBoss() && (prKham = ObjAtk.total_item_param(106)) > Util.random(10_000)) {
                ObjAtk.add_EffMe_Kham(StrucEff.NgocSinhMenh, prKham, System.currentTimeMillis() + 3000);
                Eff_special_skill.send_eff_kham(p, StrucEff.NgocSinhMenh, 3000);
            }
            ptCrit += ObjAtk.total_item_param(107) * 0.0001;
        }
        //</editor-fold>

        dame += dame * DamePlus;

        int def = focus.get_DefBase();
//        def += def * focus.get_PercentDefBase() * 0.0001;
//        if (ObjAtk.isPlayer()) {
//            System.out.println("dame:   " + Util.number_format(dame)+"  def:   " +Util.number_format(def) + "   giam: "+GiamDame);
//        }
        if (dame > 2_000_000_000) {
            dame = 2_000_000_000;
        }
        dame -= dame * 0.35;
        dame -= (xuyengiap ? 0 : def);
        if (!giamdame.isEmpty()) {
            for (float f : giamdame) {
                dame -= dame * f;
            }
        }

        if (ObjAtk.isPlayer() && focus.isMob()) {
            boolean check_mob_roi_ngoc_kham = focus.template.mob_id >= 167 && focus.template.mob_id <= 172;
            if (check_mob_roi_ngoc_kham) {
                if (50 > Util.random(100)) {
                    dame = 0;
                } else {
                    dame = 1;
                }
            }
            boolean check = dame < 0
                    || (focus.isBoss() && Math.abs(focus.level - ObjAtk.level) >= 5 && focus.level < 120 && focus.template.mob_id != 174 && !Map.is_map_cant_save_site(focus.map_id))
                    || (focus.isBoss() && focus.template.mob_id == 174 && map.zone_id == 0 && ObjAtk.level > 89)
                    || (focus.isBoss() && focus.template.mob_id == 174 && map.zone_id == 2 && !(ObjAtk.level >= 90 && ObjAtk.level < 110))
                    || (focus.isBoss() && focus.template.mob_id == 174 && map.zone_id == 3 && ObjAtk.level < 110);
            if (check) {
                dame = 0;
            }
        }
        if (focus.isMoTaiNguyen() && ObjAtk.isPlayer()) {
            Mob_MoTaiNguyen mo = (Mob_MoTaiNguyen) focus;
            if (!mo.is_atk) {
                dame = 0;
            } else if (mo.nhanban != null && !mo.nhanban.isdie) {
                mo.nhanban.p_target = (Player) ObjAtk;
                mo.nhanban.is_move = false;
            }
        }

        if (ObjAtk.isPlayer() && HoiHP > 0) {
            Service.usepotion(p, 0, HoiHP);
        }
        if (idxSkill == 17 && ObjAtk.isPlayer() && focus.isPlayer()) {
            MapService.add_eff_skill(map, p, (Player) focus, (byte) idxSkill);
        }

        //<editor-fold defaultstate="collapsed" desc="Hiệu ứng Crit vv       ...">  
        List<Eff_TextFire> ListEf = new ArrayList<>();

        if (hutHP > 0) {
            ListEf.add(new Eff_TextFire(0, (int) dame));
            ListEf.add(new Eff_TextFire(2, hutHP));
            focus.hp += hutHP;
            if (focus.hp > focus.get_HpMax()) {
                focus.hp = focus.get_HpMax();
            }
        }
        if (xuyengiap) {
            ListEf.add(new Eff_TextFire(1, (int) dame));
        } else if (ObjAtk.get_Crit() > Util.random(10_000)) {
            //       dame *= 2;
            dame += dame * (ptCrit + 1);
            if (dame > 2_000_000_000) {
                dame = 2_000_000_000;
            }
            ListEf.add(new Eff_TextFire(4, (int) dame));
        }

        //<editor-fold defaultstate="collapsed" desc="Phản Dame       ...">  
        if (focus.get_PhanDame() > Util.random(10_000)) {
            int DAMEpst = (int) (dame * 0.5);
            DAMEpst -= ObjAtk.get_DefBase();
            if (type == 1) {
                if (ObjAtk.clazz == 0) {
                    DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(18);
                } else if (ObjAtk.clazz == 1) {
                    DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(20);
                } else if (ObjAtk.clazz == 2) {
                    DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(17);
                } else if (ObjAtk.clazz == 3) {
                    DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(19);
                }
            } else {
                DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(16);
            }
            if (DAMEpst <= 0) {
                DAMEpst = 1;
            }

            ListEf.add(new Eff_TextFire(5, DAMEpst));
            ObjAtk.hp -= DAMEpst;
            if (ObjAtk.hp <= 0) {
                ObjAtk.hp = 5;
            }
        }
        //</editor-fold> Phản Dame

        //</editor-fold>    hiệu ứng crit vv
        //<editor-fold defaultstate="collapsed" desc="Set hp       ...">  
        // xả item chiến trường
        long time = System.currentTimeMillis();
        if (ObjAtk.isMobCTruongHouse() && map.Arena != null && map.Arena.timeCnNha > time) {
            dame *= 2;
        } else if (!ObjAtk.isPlayer() && ObjAtk.get_TypeObj() == 0 && map.Arena != null && map.Arena.timeCnLinh > time) {
            dame *= 2;
        }
        if (dame > 2_000_000_000) {
            dame = 2_000_000_000;
        } else if (dame <= 0) {
            dame = 1;
        }
        float ptHP = (focus.hp / focus.get_HpMax()) * 100;
        if (focus.isMobDiBuon()) {
            dame = focus.hp_max * 5 / 100;
        }
        focus.hp -= dame;
        Mob_in_map mob = focus.isMob() ? (Mob_in_map) focus : null;
        if (focus.isBoss() && mob != null && ObjAtk.isPlayer()) {
            if (!mob.top_dame.containsKey(p.name)) {
                mob.top_dame.put(p.name, (long) dame);
            } else {
                long dame_boss = dame + mob.top_dame.get(p.name);
                mob.top_dame.put(p.name, dame_boss);
            }
        }

        if (focus.hp <= 0) {
            if (focus.isPlayer() && ptHP > 70) {
                focus.hp = 5;
            } else {
                if (map.isMapChiemThanh()) {
                    ChiemThanhManager.Obj_Die(map, ObjAtk, focus);
                }
                focus.SetDie(map, ObjAtk);
                if (!focus.isMobDiBuon() && !focus.isPlayer() && focus.template.mob_id >= 89 && focus.template.mob_id <= 92) { // house chien truong
                    p.update_point_arena(20);
                    Manager.gI().chatKTGprocess("@Server : " + p.name + " đã đánh sập " + focus.template.name);
                    ChienTruong.gI().update_house_die(focus.template.mob_id);
                }
                if (focus.isPlayer()) {
                    MapService.Player_Die(map, focus, ObjAtk, true);
                } else {
                    MapService.MainObj_Die(map, null, focus, true);
                }

            }

        }

        if (ObjAtk.isPlayer() && (focus.isPlayer() || focus.get_TypeObj() == 0)) {
            MapService.Fire_Player(map, p.conn, idxSkill, focus.index, (int) dame, focus.hp, ListEf);
        } else if (ObjAtk.isPlayer() && focus.get_TypeObj() == 1) {
            if (map.isMapChienTruong()) {
                switch (focus.template.mob_id) {
                    case 89: {
                        if (p.typepk == 4) {
                            return;
                        }
                        break;
                    }
                    case 90: {
                        if (p.typepk == 2) {
                            return;
                        }
                        break;
                    }
                    case 91: {
                        if (p.typepk == 5) {
                            return;
                        }
                        break;
                    }
                    case 92: {
                        if (p.typepk == 1) {
                            return;
                        }
                        break;
                    }

                }
                MapService.Fire_Mob(map, p.conn, idxSkill, focus.index, (int) dame, focus.hp, ListEf, focus.template.mob_id);

            } else {
                MapService.Fire_Mob(map, p.conn, idxSkill, focus.index, (int) dame, focus.hp, ListEf, 0);

            }
        } else if (ObjAtk.get_TypeObj()
                == 1 && focus.isPlayer()) //            MapService.Mob_Fire(map, (Player)focus, ObjAtk, idxSkill, (int)dame, ListEf);
        {
            MapService.mob_fire(map, (Mob_in_map) ObjAtk, (Player) focus, (int) dame);
        } else if (ObjAtk.get_TypeObj()
                == 0 && focus.isPlayer()) {
            MapService.MainObj_Fire_Player(map, (Player) focus, ObjAtk, idxSkill, (int) dame, ListEf);
        }

//        if (ObjAtk.isPlayer()) {
//
//            System.out.println("dame:   " + Util.number_format(dame)+"def:   " +Util.number_format(def));
//        }
        //</editor-fold>    Set hp
        //<editor-fold defaultstate="collapsed" desc="Tính exp       ...">  
        if (focus.isMobDungeon()
                && ObjAtk.isPlayer()) {
            int expup = 0;
            expup = (int) dame; // tinh exp
            ef = p.get_EffDefault(-125);
            if (ef != null) {
                expup += (expup * (ef.param / 100)) / 100;
            }
            expup = (int) (expup / 3);
            p.update_Exp(expup, true);
            // exp clan
            if (p.myclan != null) {
                int exp_clan = ((int) dame) / 10_000;
                if (exp_clan < 1 || exp_clan > 50) {
                    exp_clan = 1;
                }
                p.myclan.update_exp(exp_clan);
            }
        } else if (focus.isMob()
                && focus.isExp && ObjAtk.isPlayer()) {
            int expup = 0;
            expup = (int) dame; // tinh exp
            if (p.level <= 10) {
                expup = expup * 3;
            }
            if (Math.abs(focus.level - p.level) == 0) {
                expup = expup;
            } else if (Math.abs(focus.level - p.level) > 1) {
                expup = (expup * 11) / 10;
            } else if (Math.abs(focus.level - p.level) > 2) {
                expup = (expup * 12) / 10;
            } else if (Math.abs(focus.level - p.level) > 3) {
                expup = (expup * 13) / 10;
            } else if (Math.abs(focus.level - p.level) > 4) {
                expup = (expup * 14) / 10;
            } else if (Math.abs(focus.level - p.level) > 5) {
                expup = (expup * 15) / 10;
            } else if (Math.abs(p.level - focus.level) > 1) {
                expup = (expup * 9) / 10;
            } else if (Math.abs(p.level - focus.level) > 2) {
                expup = (expup * 8) / 10;
            } else if (Math.abs(p.level - focus.level) > 3) {
                expup = (expup * 7) / 10;
            } else if (Math.abs(p.level - focus.level) > 4) {
                expup = (expup * 6) / 10;
            } else if (Math.abs(p.level - focus.level) > 5) {
                expup = (expup * 5) / 10;
            }
            if (p.hieuchien > 0) {
                expup /= 2;
            }
            if (Math.abs(focus.level - p.level) <= 10 && expup > 0) {
                if (p.party != null) {
                    for (int i = 0; i < p.party.get_mems().size(); i++) {
                        Player pm = p.party.get_mems().get(i);
                        if (pm.index != p.index && (Math.abs(pm.level - p.level) < 10) && pm.map.map_id == p.map.map_id
                                && pm.map.zone_id == p.map.zone_id) {
                            pm.update_Exp((expup / 10), true);
                        }
                    }
                }
                ef = p.get_EffDefault(-125);
                if (ef != null) {
                    expup += (expup * (ef.param / 100)) / 100;
                }
                p.update_Exp(expup, true);
            } else if (expup > 0) {
                p.update_Exp(2, false);
            }
            // exp clan
            if (p.myclan != null) {
                int exp_clan = ((int) dame) / 10_000;
                if (exp_clan < 1 || exp_clan > 50) {
                    exp_clan = 1;
                }
                p.myclan.update_exp(exp_clan);
            }

            if (p.it_wedding != null) {
                if (p.party != null && p.party.get_mems() != null) {
                    for (int i = 0; i < p.party.get_mems().size(); i++) {
                        Player pm = p.party.get_mems().get(i);
                        if (p.it_wedding.equals(pm.it_wedding)) {
                            p.it_wedding.exp += dame / 1_000;
                            break;
                        }
                    }
                }
            }
        }
        //</editor-fold>    Tính exp

        //<editor-fold defaultstate="collapsed" desc="Pet Attack       ...">  
        if (ObjAtk.isPlayer()) {
            if (!focus.isdie && p.pet_follow != -1) {
                Pet my_pet = null;
                for (Pet pett : p.mypet) {
                    if (pett.is_follow) {
                        my_pet = pett;
                        break;
                    }
                }
                if (my_pet != null && my_pet.grown > 0) {
                    int a1 = 0;
                    int a2 = 1;
                    for (Option_pet temp1 : my_pet.op) {
                        if (temp1.maxdam > 0) {
                            a1 = temp1.param;
                            a2 = temp1.maxdam;
                            break;
                        }
                    }
                    int dame_pet = Util.random(a1, Math.max(a2, a1 + 1));
                    if (dame_pet <= 0) {
                        dame_pet = 1;
                    }
                    if (((focus.hp - dame_pet) > 0) && (p.pet_atk_speed < System.currentTimeMillis()) && (a2 > 1)) {
                        if (focus.isMob() && (my_pet.get_id() == 3269 || my_pet.name.equals("Đại Bàng"))) {
                            int vangjoin = Util.random(1666, 2292);
                            p.update_vang(vangjoin);
                            Service.send_notice_nobox_white(p.conn, "+ " + vangjoin + " vàng");
                        }
                        p.pet_atk_speed = System.currentTimeMillis() + 1500L;
                        Message m = new Message(84);
                        m.writer().writeByte(2);
                        m.writer().writeShort(p.index);
                        m.writer().writeByte(focus.get_TypeObj());
                        m.writer().writeByte(1);
                        m.writer().writeShort(focus.index);
                        m.writer().writeInt(dame_pet);
                        focus.hp -= dame_pet;
                        m.writer().writeInt(focus.hp);
                        m.writer().writeInt(p.hp);
                        m.writer().writeInt(p.body.get_HpMax());
                        p.conn.addmsg(m);
                        m.cleanup();
                    }
                }
            }
        }
        //</editor-fold>    Pet Attack

    }

    public int get_param_view_in4(int type) {
        switch (type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4: {
                return get_DameProp(type);
            }
            case 7:
            case 8:
            case 9:
            case 10:
            case 11: {
                return get_PercentDameProp(type);
            }
            case 14: {
                return get_DefBase();
            }
            case 15: {
                return get_PercentDefBase();
            }
            case 33: {
                return get_Crit();
            }
            case 34: {
                return get_Miss();
            }
            case 35: {
                return get_PhanDame();
            }
            case 36: {
                return get_Pierce();
            }
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 27:
            case 28:
            case 22: {
                return (total_item_param(type) + total_skill_param(type));
            }
            default: {
                return total_item_param(type);
            }
        }
    }

    public int total_skill_param(int id) {
        return 0;
    }

    public int total_item_param(int id) {
        return 0;
    }

    public void update(Map map) {

    }
}
