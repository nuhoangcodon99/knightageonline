/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Administrator
 */
package event_daily;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import client.Player;
import core.Service;
import core.Util;
import static javax.swing.UIManager.get;
import map.Map;
import map.Mob_in_map;
import template.Item47;
import template.Medal_Material;
import template.Mob;

public class DailyQuest {
	public static void get_quest(Player p, byte select) throws IOException {
		List<Integer> list_mob = new ArrayList<>();
               for (int i = 0; i < Mob.entrys.size(); i++) {
			if (Math.abs(Mob.entrys.get(i).level - p.level) <= 10 && !Mob.entrys.get(i).is_boss  && Mob.entrys.get(i).mob_id !=167
                                && Mob.entrys.get(i).mob_id !=168&& Mob.entrys.get(i).mob_id !=169&& Mob.entrys.get(i).mob_id !=170
                                && Mob.entrys.get(i).mob_id !=171&& Mob.entrys.get(i).mob_id !=172&& Mob.entrys.get(i).mob_id !=5
                                && Mob.entrys.get(i).mob_id !=11&& Mob.entrys.get(i).mob_id !=23&& Mob.entrys.get(i).mob_id !=91
                                && Mob.entrys.get(i).mob_id !=89
                                && Mob.entrys.get(i).mob_id !=92
                                && Mob.entrys.get(i).mob_id !=155
                                && Mob.entrys.get(i).mob_id !=106
                                && Mob.entrys.get(i).mob_id !=79
                                && Mob.entrys.get(i).mob_id !=149
                                && Mob.entrys.get(i).mob_id !=174
                                && Mob.entrys.get(i).mob_id !=83
                                && Mob.entrys.get(i).mob_id !=105
                                && Mob.entrys.get(i).mob_id !=84
                                && Mob.entrys.get(i).mob_id !=101
                                && Mob.entrys.get(i).mob_id !=104
                                && Mob.entrys.get(i).mob_id !=103
                                
                                ) {
				if  (checkmob(i)) {
					list_mob.add(i);
				}
			}
		}
                 
		if (list_mob.size() < 1) {
			Mob_in_map mob_add = null;
			for (Entry<Integer, Mob_in_map> en : Mob_in_map.ENTRYS.entrySet()) {
				if (mob_add == null) {
					mob_add = en.getValue();
				} else if (mob_add.level < en.getValue().level) {
					mob_add = en.getValue();
				}
			}
                        list_mob.add(mob_add.index);
		}
		int index = Util.random(list_mob.size());
		p.quest_daily[0] = list_mob.get(index);
		p.quest_daily[1] = select;
		p.quest_daily[2] = 0;
		p.quest_daily[4]-=1;
		switch (select) {
			case 0: {
				p.quest_daily[3] = Util.random(25, 50);
				break;
			}
			case 1: {
				p.quest_daily[3] = Util.random(100, 150);
				break;
			}
			case 2: {
				p.quest_daily[3] = Util.random(250, 600);
				break;
			}
			case 3: {
				p.quest_daily[3] = Util.random(800, 1500);
				break;
			}
		}
		Mob mob_info = Mob.entrys.get(p.quest_daily[0]);
		Service.send_notice_box(p.conn, String.format("Nhiệm vụ hiện tại:\nTiêu diệt %s.\nMap : %s\nHôm nay còn %s lượt.",
		      (p.quest_daily[2] + " / " + p.quest_daily[3] + " " + mob_info.name), mob_info.map.name, p.quest_daily[4]));

// else {
                
		// Service.send_box_ThongBao_OK(p, "Hiện tại không có nhiệm vụ phù hợp!");
		// }
	}

	private static boolean checkmob(int i) {
		for (Map[] map : Map.entrys) {
			for (Mob_in_map m_temp : map[0].mobs) {
				if (m_temp.template.mob_id == i) {
					return true;
				}
			}
		}
		return false;
	}

	public static void remove_quest(Player p) throws IOException {
		if (p.quest_daily[0] == -1) {
			Service.send_notice_box(p.conn, "Hiện tại không nhận nhiệm vụ nào!");
		} else {
			p.quest_daily[0] = -1;
			p.quest_daily[1] = -1;
			p.quest_daily[2] = 0;
			p.quest_daily[3] = 0;
			Service.send_notice_box(p.conn, "Hủy nhiệm vụ thành công, nào rảnh quay lại nhận tiếp nhá!");
		}
	}

	public static String info_quest(Player p) {
		if (p.quest_daily[0] == -1) {
			return String.format("Bạn chưa nhận nhiệm vụ.\nĐiểm Chuyên cần : "+p.chuyencan+"\nHôm nay còn %s lượt.", p.quest_daily[4]);
		} else {
			Mob mob_info = Mob.entrys.get(p.quest_daily[0]);
			return String.format("Nhiệm vụ hiện tại:\nTiêu diệt %s.\nMap : %s\nHôm nay còn %s lượt.",
			      (p.quest_daily[2] + " / " + p.quest_daily[3] + " " + mob_info.name), mob_info.map.name,
			      p.quest_daily[4]);
		}
	}

	public static void finish_quest(Player p) throws IOException {
		if (p.quest_daily[0] == -1) {
			Service.send_notice_box(p.conn, "Hiện tại không nhận nhiệm vụ nào!");
		} else if (p.quest_daily[2] == p.quest_daily[3]) {
			 short id_blue = Medal_Material.m_blue[Util.random(0, 10)];
                         short id_yellow = Medal_Material.m_yellow[Util.random(0, 10)];
                         short id_violet = Medal_Material.m_violet[Util.random(0, 10)];
                         int vang = Util.random(50, 100) * (p.quest_daily[1] + 1) * p.quest_daily[2];
			int ngoc = p.quest_daily[1] == 3 ? Util.random(200, 400)
			      : (p.quest_daily[1] == 2 ? Util.random(100, 200)
			            : (p.quest_daily[1] == 1 ? Util.random(50, 100) : Util.random(5, 10)));
			int exp = Util.random(50, 100) * (p.quest_daily[1] + 1) * p.quest_daily[2];
                        
			p.update_vang(vang);
			p.update_ngoc(ngoc);
			p.update_Exp(exp, true);
                        p.chuyencan ++;
                        if (p.quest_daily[1] == 1) {
                        if (((p.item.get_bag_able() > 0) || (p.item.total_item_by_id(7, id_blue) > 0))) {
                        Item47 itbag = new Item47();
                        itbag.id = id_blue;
                        itbag.quantity = (short) Util.random(0, 2);
                        itbag.category = 7;
                        p.item.add_item_bag47(7, itbag);
                        p.item.char_inventory(7); 
                        }
                    }else if (p.quest_daily[1] == 2) {
                        if (((p.item.get_bag_able() > 0) || (p.item.total_item_by_id(7, id_yellow) > 0))) {
                        Item47 itbag = new Item47();
                        itbag.id = id_yellow;
                        itbag.quantity = (short) Util.random(0, 2);
                        itbag.category = 7;
                        p.item.add_item_bag47(7, itbag);
                        p.item.char_inventory(7); 
                        }
                    }else if (p.quest_daily[1] == 3) {
                        if (((p.item.get_bag_able() > 0) || (p.item.total_item_by_id(7, id_violet) > 0))) {
                        Item47 itbag = new Item47();
                        itbag.id = id_violet;
                        itbag.quantity = (short) Util.random(0, 2);
                        itbag.category = 7;
                        p.item.add_item_bag47(7, itbag);
                        p.item.char_inventory(7); 
                        }
                    }
			p.item.char_inventory(5);
                        
			Service.send_notice_box(p.conn,
			      "Trả thành công, nhận được " + vang + " vàng, " + ngoc + " ngọc và " + exp + " kinh nghiệm!");
			p.quest_daily[0] = -1;
			p.quest_daily[1] = -1;
			p.quest_daily[2] = 0;
			p.quest_daily[3] = 0;
		} else {
			Service.send_notice_box(p.conn, "Chưa hoàn thành được nhiệm vụ!");
		}
	}
}

