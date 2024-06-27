
package client;

import core.Manager;
import core.Service;
import core.Util;
import event_daily.ChiemThanhManager;
import io.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Eff_special_skill;
import map.Map;
import map.MapService;
import map.Mob_in_map;
import template.EffTemplate;
import template.Eff_TextFire;
import template.Item3;
import template.Kham_template;
import template.LvSkill;
import template.MainObject;
import template.Option;
import template.Option_pet;
import template.StrucEff;


public class Body2 extends MainObject{
    private Player p;

    protected void SetPlayer(Player p) {
        if(this.p != null)return;
        this.p = p;
        kham = new Kham_template();
        MainEff = new ArrayList<>();
        Eff_me_kham = new ArrayList<>();
    }
    @Override
    public boolean isPlayer(){
        return true;
    }

    private int get_point(int i) {
        int point = 0;
        switch (i) {
            case 1: {
                point += p.point1 + get_plus_point(23);
                break;
            }
            case 2: {
                point += p.point2 + get_plus_point(24);
                break;
            }
            case 3: {
                point += p.point3 + get_plus_point(25);
                break;
            }
            case 4: {
                point += p.point4 + get_plus_point(26);
                break;
            }
        }
        return point;
    }

    public int get_plus_point(int i) {
        int param = 0;
        switch (i) {
            case 23: {
                param += total_item_param(i);
                EffTemplate ef = p.get_EffDefault(23);
                if (ef != null) {
                    param += (p.point1 * (ef.param / 100)) / 100;
                }
                for (Pet temp : p.mypet) {
                    if (temp.is_follow) {
                        for (Option_pet op : temp.op) {
                            if (op.id == 23) {
                                param += op.param;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 24: {
                param += total_item_param(i);
                for (Pet temp : p.mypet) {
                    if (temp.is_follow) {
                        for (Option_pet op : temp.op) {
                            if (op.id == 24) {
                                param += op.param;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 25: {
                param += total_item_param(i);
                for (Pet temp : p.mypet) {
                    if (temp.is_follow) {
                        for (Option_pet op : temp.op) {
                            if (op.id == 25) {
                                param += op.param;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 26: {
                param += total_item_param(i);
                for (Pet temp : p.mypet) {
                    if (temp.is_follow) {
                        for (Option_pet op : temp.op) {
                            if (op.id == 26) {
                                param += op.param;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return param;
    }

    @Override
    public int total_item_param(int id) {
        int param = 0;
        for (int i = 0; i < p.item.wear.length; i++) {
            Item3 temp = p.item.wear[i];
            if (temp != null ) {
                if(p.level < temp.level)continue;
                for (Option op : temp.op) {
                    if (op.id == id) {
                        param += op.getParam(temp.tier);
                    }
                }
            }
        }
        return param;
    }

    @Override
    public int get_HpMax() {
        long hpm = (int)(2500 * Manager.ratio_hp);
        switch (p.clazz) {
            case 0: {
                hpm += (550 + get_point(3) * 320) ;
                break;
            }
            case 1: {
                hpm += (get_point(3) * 300) ;
                break;
            }
            case 2: {
                hpm += (50 + get_point(3) * 310) ;
                break;
            }
            case 3: {
                hpm += (120 + get_point(3) * 300) ;
                break;
            }
        }
        int percent = total_item_param(27);
        if (p.skill_point[9] > 0) {
            for (Option op : p.skills[9].mLvSkill[p.skill_point[9] - 1].minfo) {
                if (op.id == 27) {
                    percent += op.getParam(0);
                    break;
                }
            }
        }
        hpm += ((hpm * (percent / 100)) / 100);
        if (p.type_use_mount == 11 || p.type_use_mount == 12 || p.type_use_mount == 13
                || (p.type_use_mount == 20 && (p.id_horse == 114 || p.id_horse == 121))) {
            hpm += (hpm / 10) ;
        }
        EffTemplate ef = p.get_EffDefault(2);
        if (ef != null) {
            hpm = (hpm * 8) / 10;
        }
        if (hpm > 2_000_000_000) {
            hpm = 2_000_000_000;
        }
        return (int) (hpm * Manager.ratio_hp);
    }

    @Override
    public int get_MpMax() {
        long mpm = 250;
        switch (p.clazz) {
            case 0:
            case 1: {
                mpm += get_point(4) * 10;
                break;
            }
            case 2: {
                mpm += 10 + get_point(3) + get_point(4) * 11;
                break;
            }
            case 3: {
                mpm += 5 + get_point(3) + get_point(4) * 11;
                break;
            }
        }
        int percent = total_item_param(28);
        if (p.skill_point[10] > 0) {
            for (Option op : p.skills[10].mLvSkill[p.skill_point[10] - 1].minfo) {
                if (op.id == 28) {
                    percent += op.getParam(0);
                    break;
                }
            }
        }
        mpm += ((mpm * (percent / 100)) / 100);
        EffTemplate ef = p.get_EffDefault(2);
        if (ef != null) {
            mpm = (mpm * 8) / 10;
        }
        if (mpm > 2_000_000_000) {
            mpm = 2_000_000_000;
        }
        return (int) mpm;
    }

    
    @Override
    public int total_skill_param(int id) {
        int param = 0;
        for (int i = 0; i < p.skill_point.length; i++) {
            if (p.skill_point[i] > 0) {
                Option[] temp = p.skills[i].mLvSkill[get_skill_point(i) - 1].minfo;
                for (Option op : temp) {
                    if (op.id == id) {
                        param += op.getParam(0);
                    }
                }
            }
        }
        return param;
    }

    @Override
    public int get_Pierce() {
        int pie = total_item_param(36) + total_skill_param(36);
        pie += get_point(4) * 2;
        EffTemplate ef = get_EffDefault(36);
        if (ef != null) {
            pie += ef.param;
        }
        return (int)(pie / 2.1);
    }

    @Override
    public int get_PhanDame() {
        int param = 2 * get_point(3);
        param += total_item_param(35);
        EffTemplate ef = get_EffDefault(35);
        if (ef != null) {
            param += ef.param;
        }
        return (int)(param / 2.1);
    }

    @Override
    public int get_Miss() {
        int param = 2 * get_point(2);
        param += total_item_param(34);
        EffTemplate ef = get_EffDefault(34);
        if (ef != null) {
            param += ef.param;
        }
        return (int)(param / 1.8);
    }

    @Override
    public int get_Crit() {
        int crit = total_item_param(33) + total_skill_param(33);
        crit += get_point(1) * 2;
        EffTemplate ef= get_EffDefault(33);
        if(ef!= null)
            crit += ef.param;
        return (int)(crit / 2.1);
    }

    public int get_skill_point(int i) {
        if (p.skill_point[i] > 0) {
            int par_ = p.skill_point[i] + get_skill_point_plus(i);
            return (par_ > 15 ? 15 : par_);
        }
        return 0;
    }
    
    @Override
    public int get_PercentDefBase() {
        int def = total_item_param(15);
        def += get_point(2) * 10;
        if (get_skill_point(15) > 0) {
            for (Option op : p.skills[15].mLvSkill[get_skill_point(15) - 1].minfo) {
                if (op.id == 15) {
                    def += op.getParam(0);
                    break;
                }
            }
        }
        EffTemplate ef = p.get_EffDefault(24);
        if (ef != null) {
            def += ef.param;
        }
        return def;
    }

    @Override
    public int get_DefBase() {
        int def = total_item_param(14);
        switch (p.clazz) {
            case 0: {
                def += get_point(2) * 20;
                break;
            }
            case 1: {
                def += get_point(2) * 22;
                break;
            }
            case 2: {
                def += get_point(2) * 20;
                break;
            }
            case 3: {
                def += get_point(2) * 22;
                break;
            }
        }
        def += ((def * (get_PercentDefBase() / 100)) / 100);
        if (p.type_use_mount == 2 || (p.type_use_mount == 15 && p.id_horse == 106)) {
            def += ((def * 2) / 10);
        } else if (p.type_use_mount == 3 || p.type_use_mount == 5 || (p.type_use_mount == 17 && p.id_horse == 111)
                || (p.type_use_mount == 20 && p.id_horse == 115)) {
            def += ((def * 1) / 10);
        } else if ((p.type_use_mount == 22 && p.id_horse == 117)
                || (p.type_use_mount == 20 && (p.id_horse == 114 || p.id_horse == 116))) {
            def += ((def * 15) / 100);
        }
        EffTemplate ef = p.get_EffDefault(0);
        if (ef != null) {
            def = (def * 8) / 10;
        }
        ef = p.get_EffDefault(15);
        if (ef != null) {
            def += (def * (ef.param / 100)) / 100;
        }
        return (int)(def*0.8);
    }

    @Override
    public int get_PercentDameProp(int type) {
        if(type ==0){
            int percent = total_item_param(7);
            switch (p.clazz) {
                case 0:
                case 1: {
                    percent += get_point(1) * 20;
                    break;
                }
                case 2:
                case 3: {
                    percent += get_point(1) * 20 + get_point(4) * 18;
                    break;
                }
            }
            if (get_skill_point(11) > 0) {
                for (Option op : p.skills[11].mLvSkill[get_skill_point(11) - 1].minfo) {
                    if (op.id == 7) {
                        percent += op.getParam(0);
                        break;
                    }
                }
            }
            EffTemplate eff = get_EffDefault(StrucEff.BuffSTVL);
            if(eff!=null)
                percent += eff.param;
            return percent;
        }
        int perct = 0;
        switch (p.clazz) {
            case 0: {
                if (type == 9 || type == 2) {
                    perct += get_point(1) * 20;
                    perct += total_item_param(9);
                    if (get_skill_point(12) > 0) {
                        for (Option op : p.skills[12].mLvSkill[get_skill_point(12) - 1].minfo) {
                            if (op.id == 9) {
                                perct += op.getParam(0);
                                break;
                            }
                        }
                    }
                }
                EffTemplate eff = get_EffDefault(StrucEff.BuffSTLua);
                if(eff!=null)
                    perct += eff.param;
                break;
            }
            case 1: {
                if (type == 11 || type == 4) {
                    perct += get_point(1) * 20;
                    perct += total_item_param(11);
                    if (get_skill_point(12) > 0) {
                        for (Option op : p.skills[12].mLvSkill[get_skill_point(12) - 1].minfo) {
                            if (op.id == 11) {
                                perct += op.getParam(0);
                                break;
                            }
                        }
                    }
                }
                EffTemplate eff = get_EffDefault(StrucEff.BuffSTDoc);
                if(eff!=null)
                    perct += eff.param;
                break;
            }
            case 2: {
                if (type == 8 || type == 1) {
                    perct += get_point(1) * 20 + get_point(4) * 18;
                    perct += total_item_param(8);
                    if (get_skill_point(12) > 0) {
                        for (Option op : p.skills[12].mLvSkill[get_skill_point(12) - 1].minfo) {
                            if (op.id == 8) {
                                perct += op.getParam(0);
                                break;
                            }
                        }
                    }
                }
                EffTemplate eff = get_EffDefault(StrucEff.BuffSTBang);
                if(eff!=null)
                    perct += eff.param;
                break;
            }
            case 3: {
                if (type == 10 || type == 3) {
                    perct += get_point(1) * 20 + get_point(4) * 18;
                    perct += total_item_param(10);
                    if (get_skill_point(12) > 0) {
                        for (Option op : p.skills[12].mLvSkill[get_skill_point(12) - 1].minfo) {
                            if (op.id == 10) {
                                perct += op.getParam(0);
                                break;
                            }
                        }
                    }
                }
                EffTemplate eff = get_EffDefault(StrucEff.BuffSTDien);
                if(eff!=null)
                    perct += eff.param;
                break;
            }
        }
        return perct;
    }

    
    @Override
    public int get_DameBase(){
        return get_param_view_in4(40);
    }
    @Override
    public int get_DameProp(int type) {
        if(type == 0){
            long dame = total_item_param(0);
            switch (p.clazz) {
                case 0: {
                    dame += get_point(1) * 4;
                    break;
                }
                case 1: {
                    dame += get_point(1) * 4;
                    break;
                }
                case 2: {
                    dame += get_point(4) * 4;
                    break;
                }
                case 3: {
                    dame += get_point(4) * 4;
                    break;
                }
            }
            dame += ((dame * (get_PercentDameProp(0) / 100)) / 100);
            if (dame > 2_000_000_000) {
                dame = 2_000_000_000;
            }
            return (int) dame;
        }
        long dprop = 0;
        switch (p.clazz) {
            case 0: {
                if (type == 2) {
                    dprop += get_point(1) * 4;
                    dprop += total_item_param(2);
                }
                break;
            }
            case 1: {
                if (type == 4) {
                    dprop += get_point(1) * 4;
                    dprop += total_item_param(4);
                }
                break;
            }
            case 2: {
                if (type == 1) {
                    dprop += get_point(4) * 4;
                    dprop += total_item_param(1);
                }
                break;
            }
            case 3: {
                if (type == 3) {
                    dprop += get_point(4) * 4;
                    dprop += total_item_param(3);
                }
                break;
            }
        }
        dprop += ((dprop * (get_PercentDameProp(type) / 100)) / 100);
        if (dprop > 2_000_000_000) {
            dprop = 2_000_000_000;
        }
        return (int) dprop;
    }

    

    public int get_skill_point_plus(int i) {
        int par = 0;
        if (i >= 1 && i <= 8 || i == 19 || i == 20 || i == 17) {
            par = total_item_param(37);
        }
        if ((i >= 9 && i <= 16) || i == 18) {
            par = total_item_param(38);
        }
        return (par > 5) ? 5 : par;
    }

    @Override
    public int get_PercentDefProp(int type) {
        int param = total_item_param(type) + total_skill_param(type);
        EffTemplate ef = p.get_EffDefault(4);
        if (ef != null) {
            param -= 1000;
        }
        if (param < 0) {
            param = 0;
        }
        return param;
    }
    
    
    
    @Override
    public void SetDie(Map map, MainObject mainAtk)throws IOException{
        if(map.map_id == 87)
            ChiemThanhManager.PlayerDie(p);
        p.dame_affect_special_sk = 0;
        p.hp = 0;
        p.isdie = true;
        p.type_use_mount = -1;
        Player pATK = mainAtk.isPlayer() ? (Player)mainAtk : null;
        if(pATK != null){
            if (pATK.list_enemies.contains(this.name)) {
                pATK.list_enemies.remove(this.name);
                MapService.SendChat(map, pATK, "Này Thì Cắn :v", true);
            }
            else if (p.typepk == -1) {
                if (!p.list_enemies.contains(pATK.name)) {
                    p.list_enemies.add(pATK.name);
                    if (p.list_enemies.size() > 20) {
                        p.list_enemies.remove(0);
                    }
                }
                MapService.SendChat(map, p, "Thằng ranh con này tí bố online bố sút cho không trượt phát nào ><", true);
            }
        }
    }
    
    
    @Override
    public int get_TypeObj(){
        return 0;
    }
    
    @Override
    public void update(Map map){
        try{
            if(isdie)return;
            //<editor-fold defaultstate="collapsed" desc="auto +hp,mp       ...">  
            int percent = p.body.total_skill_param(29) + p.body.total_item_param(29);
            if (p.time_buff_hp < System.currentTimeMillis()) {
                p.time_buff_hp = System.currentTimeMillis() + 5000L;
                if (percent > 0 && p.hp < p.body.get_HpMax()) {
                    long param = (((long) p.body.get_HpMax()) * (percent / 100)) / 100;
                    Service.usepotion(p, 0, param);
                }
            }
            percent = p.body.total_skill_param(30) + p.body.total_item_param(30);
            if (p.time_buff_mp < System.currentTimeMillis()) {
                p.time_buff_mp = System.currentTimeMillis() + 5000L;
                if (percent > 0 && p.mp < p.body.get_MpMax()) {
                    long param = (((long) p.body.get_MpMax()) * (percent / 100)) / 100;
                    Service.usepotion(p, 1, param);
                }
            }
            //</editor-fold>    auto +hp,mp
            
            //<editor-fold defaultstate="collapsed" desc="eff Player       ...">  
            long _time = System.currentTimeMillis();
            if (get_EffMe_Kham(StrucEff.BongLua) != null && !p.isdie) {
                Service.usepotion(p, 0, (int) -(p.hp * Util.random(5, 10) * 0.01));
//                p.hp -= (int) (p.hp * Util.random(5, 10) * 0.01);
                if(p.hp <= 0)
                    p.hp =1;
//                Service.send_char_main_in4(p);
            }
            if (get_EffMe_Kham(StrucEff.BongLanh) != null) {
                Service.usepotion(p, 1, (int) -(p.mp * Util.random(5, 10) * 0.01));
//                p.mp -= (int) (p.mp * Util.random(5, 10) * 0.01);
//                Service.send_char_main_in4(p);
            }
            if (p.hp <= 0 && !p.isdie) {
                p.hp = 0;
                p.isdie = true;
            }
            //</editor-fold>    eff Player
        }catch(Exception e){}
    }
}
